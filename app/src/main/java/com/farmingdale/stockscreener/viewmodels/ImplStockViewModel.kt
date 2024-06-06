package com.farmingdale.stockscreener.viewmodels

import androidx.lifecycle.viewModelScope
import com.farmingdale.stockscreener.model.local.HistoricalData
import com.farmingdale.stockscreener.model.local.Interval
import com.farmingdale.stockscreener.model.local.News
import com.farmingdale.stockscreener.model.local.TimePeriod
import com.farmingdale.stockscreener.repos.ImplFinanceQueryRepository.Companion.get
import com.farmingdale.stockscreener.repos.base.FinanceQueryRepository
import com.farmingdale.stockscreener.viewmodels.base.StockViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ImplStockViewModel(symbol: String) : StockViewModel() {
    private val financeQueryRepo = FinanceQueryRepository.get()

    private val _timeSeries = MutableStateFlow<Map<String, HistoricalData>>(emptyMap())
    override val timeSeries: StateFlow<Map<String, HistoricalData>> = _timeSeries.asStateFlow()

    private val _news = MutableStateFlow<List<News>>(emptyList())
    override val news: StateFlow<List<News>> = _news.asStateFlow()

    private val timeSeriesMap = mutableMapOf<TimePeriod, Map<String, HistoricalData>>()

    init {
        if (symbol.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                // Load all time series data in parallel
                val deferredTimeSeriesYTD = async { loadTimeSeries(symbol, TimePeriod.YEAR_TO_DATE, Interval.DAILY) }
                val deferredTimeSeries1D = async { loadTimeSeries(symbol, TimePeriod.ONE_DAY, Interval.FIFTEEN_MINUTE) }
                val deferredTimeSeries5D = async { loadTimeSeries(symbol, TimePeriod.FIVE_DAY, Interval.FIFTEEN_MINUTE) }
                val deferredTimeSeries1M = async { loadTimeSeries(symbol, TimePeriod.ONE_MONTH, Interval.DAILY) }
                val deferredTimeSeries6M = async { loadTimeSeries(symbol, TimePeriod.SIX_MONTH, Interval.DAILY) }
                val deferredTimeSeries1Y = async { loadTimeSeries(symbol, TimePeriod.ONE_YEAR, Interval.DAILY) }
                val deferredTimeSeries5Y = async { loadTimeSeries(symbol, TimePeriod.FIVE_YEAR, Interval.DAILY) }

                val deferredNews = async { getNews(symbol) }
                // Set the default time series data (data should already be loaded)
                val deferredTimeSeries = async{getTimeSeries(symbol, TimePeriod.YEAR_TO_DATE, Interval.DAILY)}

                deferredTimeSeries1D.await()
                deferredTimeSeries5D.await()
                deferredTimeSeries1M.await()
                deferredTimeSeries6M.await()
                deferredTimeSeries1Y.await()
                deferredTimeSeriesYTD.await()
                deferredTimeSeries5Y.await()
                deferredTimeSeries.await()

                deferredNews.await()
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

    override fun getNews(symbol: String) {
        viewModelScope.launch(Dispatchers.IO) {
            financeQueryRepo.getNewsForSymbol(symbol).collect {
                _news.value = it
            }
        }
    }

}