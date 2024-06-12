package com.farmingdale.stockscreener.viewmodels

import androidx.lifecycle.viewModelScope
import com.farmingdale.stockscreener.model.local.Analysis
import com.farmingdale.stockscreener.model.local.FullQuoteData
import com.farmingdale.stockscreener.model.local.HistoricalData
import com.farmingdale.stockscreener.model.local.Interval
import com.farmingdale.stockscreener.model.local.MarketSector
import com.farmingdale.stockscreener.model.local.News
import com.farmingdale.stockscreener.model.local.SimpleQuoteData
import com.farmingdale.stockscreener.model.local.TimePeriod
import com.farmingdale.stockscreener.repos.ImplFinanceQueryRepository.Companion.get
import com.farmingdale.stockscreener.repos.base.FinanceQueryRepository
import com.farmingdale.stockscreener.viewmodels.base.StockViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ImplStockViewModel(symbol: String) : StockViewModel() {
    private val financeQueryRepo = FinanceQueryRepository.get()

    override val quote: StateFlow<FullQuoteData?> =
        financeQueryRepo.getFullQuote(symbol).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), null)

    private val _timeSeries = MutableStateFlow<Map<String, HistoricalData>>(emptyMap())
    override val timeSeries: StateFlow<Map<String, HistoricalData>> = _timeSeries.asStateFlow()

    override val similarStocks: StateFlow<List<SimpleQuoteData>> =
        financeQueryRepo.getSimilarStocks(symbol).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    override val sectorPerformance: StateFlow<MarketSector?> =
        combine(
            financeQueryRepo.sectors,
            quote
        ) { sectors, fullQuoteData ->
            sectors.firstOrNull { it.sector == fullQuoteData?.sector }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), null)

    override val news: StateFlow<List<News>>  =
        financeQueryRepo.getNewsForSymbol(symbol).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    private val _analysis = MutableStateFlow<Analysis?>(null)
    override val analysis: StateFlow<Analysis?> = _analysis.asStateFlow()

    private val timeSeriesMap = mutableMapOf<TimePeriod, Map<String, HistoricalData>>()

    private val analysisMap = mutableMapOf<Interval, Analysis>()

    init {
        if (symbol.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
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
                val deferredTimeSeries = async { getTimeSeries(symbol, TimePeriod.YEAR_TO_DATE, Interval.DAILY) }
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

}