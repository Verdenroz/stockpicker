package com.farmingdale.stockscreener.viewmodels.base

import androidx.lifecycle.ViewModel
import com.farmingdale.stockscreener.model.local.Analysis
import com.farmingdale.stockscreener.model.local.HistoricalData
import com.farmingdale.stockscreener.model.local.Interval
import com.farmingdale.stockscreener.model.local.News
import com.farmingdale.stockscreener.model.local.SimpleQuoteData
import com.farmingdale.stockscreener.model.local.TimePeriod
import kotlinx.coroutines.flow.StateFlow

abstract class StockViewModel : ViewModel() {

    abstract val timeSeries: StateFlow<Map<String, HistoricalData>>

    abstract val similarStocks: StateFlow<List<SimpleQuoteData>>

    abstract val news: StateFlow<List<News>>

    abstract val analysis: StateFlow<Analysis?>

    abstract fun getTimeSeries(symbol: String, timePeriod: TimePeriod, interval: Interval)

    abstract fun getSimilarStocks(symbol: String)

    abstract fun getNews(symbol: String)

    abstract fun getAnalysis(symbol: String, interval: Interval)

}