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

class ImplStockViewModel(symbol: String): StockViewModel() {
    private val financeQueryRepo = FinanceQueryRepository.get()

    private val _timeSeries = MutableStateFlow<Map<String, HistoricalData>>(emptyMap())
    override val timeSeries: StateFlow<Map<String, HistoricalData>> = _timeSeries.asStateFlow()

    private val _news = MutableStateFlow<List<News>>(emptyList())
    override val news: StateFlow<List<News>> = _news.asStateFlow()

    init {
        if (symbol.isNotEmpty()){
            viewModelScope.launch(Dispatchers.IO) {
                val deferredNews = async{getNews(symbol) }
                val deferredTimeSeries = async{getTimeSeries(symbol, TimePeriod.YEAR_TO_DATE, Interval.DAILY)}

                deferredNews.await()
                deferredTimeSeries.await()
            }
        }
    }

    override fun getTimeSeries(symbol: String, timePeriod: TimePeriod, interval: Interval) {
        viewModelScope.launch(Dispatchers.IO) {
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