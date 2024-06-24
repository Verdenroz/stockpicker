package com.farmingdale.stockscreener.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.viewModelScope
import com.farmingdale.stockscreener.R
import com.farmingdale.stockscreener.model.local.Analysis
import com.farmingdale.stockscreener.model.local.FullQuoteData
import com.farmingdale.stockscreener.model.local.HistoricalData
import com.farmingdale.stockscreener.model.local.Interval
import com.farmingdale.stockscreener.model.local.MarketSector
import com.farmingdale.stockscreener.model.local.News
import com.farmingdale.stockscreener.model.local.SimpleQuoteData
import com.farmingdale.stockscreener.model.local.TimePeriod
import com.farmingdale.stockscreener.model.local.indicators.AnalysisIndicators
import com.farmingdale.stockscreener.repos.ImplFinanceQueryRepository.Companion.get
import com.farmingdale.stockscreener.repos.ImplWatchlistRepository.Companion.get
import com.farmingdale.stockscreener.repos.base.FinanceQueryRepository
import com.farmingdale.stockscreener.repos.base.WatchlistRepository
import com.farmingdale.stockscreener.viewmodels.base.StockViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ImplStockViewModel(symbol: String, application: Application) : StockViewModel(application) {
    private val financeQueryRepo = FinanceQueryRepository.get()
    private val watchlistRepo = WatchlistRepository.get(application)

    private val _quote = MutableStateFlow<FullQuoteData?>(null)
    override val quote: StateFlow<FullQuoteData?> = _quote.asStateFlow()

    private val _timeSeries = MutableStateFlow<Map<String, HistoricalData>>(emptyMap())
    override val timeSeries: StateFlow<Map<String, HistoricalData>> = _timeSeries.asStateFlow()

    override val similarStocks: StateFlow<List<SimpleQuoteData>> =
        financeQueryRepo.getSimilarStocks(symbol)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    override val sectorPerformance: StateFlow<MarketSector?> =
        combine(
            financeQueryRepo.sectors,
            quote
        ) { sectors, fullQuoteData ->
            sectors.firstOrNull { it.sector == fullQuoteData?.sector }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), null)

    override val news: StateFlow<List<News>> =
        financeQueryRepo.getNewsForSymbol(symbol)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    private val _analysis = MutableStateFlow<Analysis?>(null)
    override val analysis: StateFlow<Analysis?> = _analysis.asStateFlow()

    override val signals: StateFlow<Map<AnalysisIndicators, String>> = combine(
        quote,
        analysis
    ) { quote, analysis ->
        val context = getApplication<Application>().applicationContext
        val currentPrice = quote?.price ?: 0.0
        val signals = mutableMapOf<AnalysisIndicators, String>()

        val indicators = AnalysisIndicators.entries
        if (analysis != null) {
            for (indicator in indicators) {
                val value = when (indicator) {
                    AnalysisIndicators.SMA10 -> analysis.sma10
                    AnalysisIndicators.SMA20 -> analysis.sma20
                    AnalysisIndicators.SMA50 -> analysis.sma50
                    AnalysisIndicators.SMA100 -> analysis.sma100
                    AnalysisIndicators.SMA200 -> analysis.sma200
                    AnalysisIndicators.EMA10 -> analysis.ema10
                    AnalysisIndicators.EMA20 -> analysis.ema20
                    AnalysisIndicators.EMA50 -> analysis.ema50
                    AnalysisIndicators.EMA100 -> analysis.ema100
                    AnalysisIndicators.EMA200 -> analysis.ema200
                    AnalysisIndicators.WMA10 -> analysis.wma10
                    AnalysisIndicators.WMA20 -> analysis.wma20
                    AnalysisIndicators.WMA50 -> analysis.wma50
                    AnalysisIndicators.WMA100 -> analysis.wma100
                    AnalysisIndicators.WMA200 -> analysis.wma200
                    AnalysisIndicators.VWMA20 -> analysis.vwma20
                    AnalysisIndicators.RSI -> analysis.rsi14
                    AnalysisIndicators.SRSI -> analysis.srsi14
                    AnalysisIndicators.CCI -> analysis.cci20
                    AnalysisIndicators.ADX -> analysis.adx14
                    AnalysisIndicators.MACD -> analysis.macd.macd
                    AnalysisIndicators.STOCH -> analysis.stoch
                    AnalysisIndicators.AROON -> analysis.aroon.aroonUp
                    AnalysisIndicators.BBANDS -> analysis.bBands.upperBand
                    AnalysisIndicators.SUPERTREND -> analysis.superTrend.superTrend
                    AnalysisIndicators.ICHIMOKUCLOUD -> analysis.ichimokuCloud.leadingSpanA
                }
                val base = when (indicator) {
                    AnalysisIndicators.MACD -> analysis.macd.signal
                    AnalysisIndicators.AROON -> analysis.aroon.aroonDown
                    AnalysisIndicators.BBANDS -> analysis.bBands.lowerBand
                    AnalysisIndicators.ICHIMOKUCLOUD -> analysis.ichimokuCloud.leadingSpanB
                    else -> null
                }
                signals[indicator] = createSignal(
                    context = context,
                    value = value!!,
                    base = base ?: 0.0,
                    type = indicator,
                    currentPrice = currentPrice,
                )
            }
        }
        signals
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyMap())

    override val movingAveragesSummary: StateFlow<Double> = signals.map { signalMap ->
        val movingAverageSignals = signalMap.filterKeys { it in AnalysisIndicators.MOVING_AVERAGES }
        val value = { signal: String ->
            when (signal) {
                getApplication<Application>().getString(R.string.buy) -> 1
                getApplication<Application>().getString(R.string.sell) -> -1
                else -> 0
            }
        }
        val sum = movingAverageSignals.values.sumOf(value)
        if (movingAverageSignals.isNotEmpty()) sum.toDouble() / movingAverageSignals.size else 0.0
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), 0.0)

    override val oscillatorsSummary: StateFlow<Double> = signals.map { signalMap ->
        val movingAverageSignals = signalMap.filterKeys { it in AnalysisIndicators.OSCILLATORS }
        val value = { signal: String ->
            when (signal) {
                getApplication<Application>().getString(R.string.buy) -> 1
                getApplication<Application>().getString(R.string.sell) -> -1
                else -> 0
            }
        }
        val sum = movingAverageSignals.values.sumOf(value)
        if (movingAverageSignals.isNotEmpty()) sum.toDouble() / movingAverageSignals.size else 0.0
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), 0.0)

    override val trendsSummary: StateFlow<Double> = signals.map { signalMap ->
        val movingAverageSignals = signalMap.filterKeys { it in AnalysisIndicators.MOVING_AVERAGES }
        val value = { signal: String ->
            when (signal) {
                getApplication<Application>().getString(R.string.strong_trend) -> 1
                getApplication<Application>().getString(R.string.very_strong_trend) -> 1
                getApplication<Application>().getString(R.string.extreme_trend) -> 1
                getApplication<Application>().getString(R.string.buy) -> 1
                getApplication<Application>().getString(R.string.weak_trend) -> -1
                getApplication<Application>().getString(R.string.sell) -> -1
                else -> 0
            }
        }
        val sum = movingAverageSignals.values.sumOf(value)
        if (movingAverageSignals.isNotEmpty()) sum.toDouble() / movingAverageSignals.size else 0.0
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), 0.0)

    override val overallSummary: StateFlow<Double> = combine(
        movingAveragesSummary,
        oscillatorsSummary,
        trendsSummary
    ) { movingAverages, oscillators, trends ->
        (movingAverages + oscillators + trends) / 3
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), 0.0)

    override val watchList: StateFlow<List<SimpleQuoteData>> =
        watchlistRepo.watchlist.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )

    private val timeSeriesMap = mutableMapOf<TimePeriod, Map<String, HistoricalData>>()

    private val analysisMap = mutableMapOf<Interval, Analysis>()

    init {
        if (symbol.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                // Start loading the quote immediately
                financeQueryRepo.getFullQuote(symbol).collect {
                    _quote.value = it
                }
                // Load all time series data in parallel
                val deferredTimeSeriesYTD =
                    async { loadTimeSeries(symbol, TimePeriod.YEAR_TO_DATE, Interval.DAILY) }
                val deferredTimeSeries1D =
                    async { loadTimeSeries(symbol, TimePeriod.ONE_DAY, Interval.FIFTEEN_MINUTE) }
                val deferredTimeSeries5D =
                    async { loadTimeSeries(symbol, TimePeriod.FIVE_DAY, Interval.FIFTEEN_MINUTE) }
                val deferredTimeSeries1M =
                    async { loadTimeSeries(symbol, TimePeriod.ONE_MONTH, Interval.DAILY) }
                val deferredTimeSeries6M =
                    async { loadTimeSeries(symbol, TimePeriod.SIX_MONTH, Interval.DAILY) }
                val deferredTimeSeries1Y =
                    async { loadTimeSeries(symbol, TimePeriod.ONE_YEAR, Interval.DAILY) }
                val deferredTimeSeries5Y =
                    async { loadTimeSeries(symbol, TimePeriod.FIVE_YEAR, Interval.DAILY) }

                val deferredAnalysis15M = async { loadAnalysis(symbol, Interval.FIFTEEN_MINUTE) }
                val deferredAnalysis30M = async { loadAnalysis(symbol, Interval.THIRTY_MINUTE) }
                val deferredAnalysis1H = async { loadAnalysis(symbol, Interval.ONE_HOUR) }
                val deferredAnalysis1D = async { loadAnalysis(symbol, Interval.DAILY) }
                val deferredAnalysis1W = async { loadAnalysis(symbol, Interval.WEEKLY) }
                val deferredAnalysis1M = async { loadAnalysis(symbol, Interval.MONTHLY) }

                // Set the default time series data (data should already be loaded)
                val deferredTimeSeries =
                    async { getTimeSeries(symbol, TimePeriod.YEAR_TO_DATE, Interval.DAILY) }
                //Set the default analysis data (data should already be loaded)
                val deferredAnalysis = async { getAnalysis(symbol, Interval.DAILY) }

                deferredTimeSeries1D.await()
                deferredTimeSeries5D.await()
                deferredTimeSeries1M.await()
                deferredTimeSeries6M.await()
                deferredTimeSeries1Y.await()
                deferredTimeSeriesYTD.await()
                deferredTimeSeries5Y.await()
                deferredTimeSeries.await()

                deferredAnalysis15M.await()
                deferredAnalysis30M.await()
                deferredAnalysis1H.await()
                deferredAnalysis1D.await()
                deferredAnalysis1W.await()
                deferredAnalysis1M.await()
                deferredAnalysis.await()

            }
        }
    }

    private fun loadTimeSeries(symbol: String, timePeriod: TimePeriod, interval: Interval) {
        viewModelScope.launch(Dispatchers.IO) {
            financeQueryRepo.getTimeSeries(symbol, timePeriod, interval).collect {
                timeSeriesMap[timePeriod] = it
            }
        }
    }

    private fun loadAnalysis(symbol: String, interval: Interval) {
        viewModelScope.launch(Dispatchers.IO) {
            financeQueryRepo.getAnalysis(symbol, interval).collect {
                analysisMap[interval] = it
            }
        }
    }

    override fun getTimeSeries(symbol: String, timePeriod: TimePeriod, interval: Interval) {
        // Check if the time series data has already been loaded
        viewModelScope.launch(Dispatchers.IO) {
            if (timeSeriesMap.containsKey(timePeriod)) {
                _timeSeries.value = timeSeriesMap[timePeriod]!!
                return@launch
            }
            // If not, get the data from the repository
            financeQueryRepo.getTimeSeries(symbol, timePeriod, interval).collect {
                _timeSeries.value = it
            }
        }
    }

    override fun getAnalysis(symbol: String, interval: Interval) {
        viewModelScope.launch(Dispatchers.IO) {
            // Check if the analysis data has already been loaded
            if (analysisMap.containsKey(interval)) {
                _analysis.value = analysisMap[interval]
                return@launch
            }
            // If not, get the data from the repository
            financeQueryRepo.getAnalysis(symbol, interval).collect {
                _analysis.value = it
            }
        }
    }

    private fun createSignal(
        context: Context,
        value: Double,
        type: AnalysisIndicators,
        currentPrice: Double = 0.0,
        base: Double = 0.0,
    ): String {
        return when (type) {
            in AnalysisIndicators.MOVING_AVERAGES -> {
                when {
                    value < currentPrice -> context.getString(R.string.buy)
                    value > currentPrice -> context.getString(R.string.sell)
                    else -> context.getString(R.string.neutral)
                }
            }
            in AnalysisIndicators.OSCILLATORS -> {
                when (type) {
                    AnalysisIndicators.RSI -> {
                        when {
                            value < 30 -> context.getString(R.string.buy)
                            value > 70 -> context.getString(R.string.sell)
                            else -> context.getString(R.string.neutral)
                        }
                    }
                    AnalysisIndicators.SRSI, AnalysisIndicators.STOCH -> {
                        when {
                            value < 20 -> context.getString(R.string.buy)
                            value > 80 -> context.getString(R.string.sell)
                            else -> context.getString(R.string.neutral)
                        }
                    }
                    AnalysisIndicators.CCI -> {
                        when {
                            value < -100 -> context.getString(R.string.buy)
                            value > 100 -> context.getString(R.string.sell)
                            else -> context.getString(R.string.neutral)
                        }
                    }
                    AnalysisIndicators.ADX -> {
                        when {
                            value <= 25 -> context.getString(R.string.weak_trend)
                            value > 75 -> context.getString(R.string.extreme_trend)
                            value > 50 -> context.getString(R.string.very_strong_trend)
                            value > 25 -> context.getString(R.string.strong_trend)
                            else -> context.getString(R.string.neutral)
                        }
                    }
                    AnalysisIndicators.MACD -> {
                        when {
                            value > base -> context.getString(R.string.buy)
                            else -> context.getString(R.string.sell)
                        }
                    }
                    else -> context.getString(R.string.neutral)
                }
            }
            in AnalysisIndicators.TRENDS -> {
                when (type) {
                    AnalysisIndicators.AROON, AnalysisIndicators.ICHIMOKUCLOUD -> {
                        when {
                            value > base -> context.getString(R.string.buy)
                            else -> context.getString(R.string.sell)
                        }
                    }
                    AnalysisIndicators.BBANDS -> {
                        when {
                            currentPrice > value -> context.getString(R.string.buy)
                            currentPrice < base -> context.getString(R.string.sell)
                            else -> context.getString(R.string.neutral)
                        }
                    }
                    AnalysisIndicators.SUPERTREND -> {
                        when {
                            currentPrice > value -> context.getString(R.string.buy)
                            else -> context.getString(R.string.sell)
                        }
                    }
                    else -> context.getString(R.string.neutral)
                }
            }
            else -> context.getString(R.string.neutral)
        }
    }
}