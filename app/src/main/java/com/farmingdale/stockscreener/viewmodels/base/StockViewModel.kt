package com.farmingdale.stockscreener.viewmodels.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.farmingdale.stockscreener.model.local.Analysis
import com.farmingdale.stockscreener.model.local.FullQuoteData
import com.farmingdale.stockscreener.model.local.HistoricalData
import com.farmingdale.stockscreener.model.local.Interval
import com.farmingdale.stockscreener.model.local.MarketSector
import com.farmingdale.stockscreener.model.local.News
import com.farmingdale.stockscreener.model.local.SimpleQuoteData
import com.farmingdale.stockscreener.model.local.TimePeriod
import com.farmingdale.stockscreener.model.local.indicators.AnalysisIndicators
import kotlinx.coroutines.flow.StateFlow

abstract class StockViewModel(application: Application) : AndroidViewModel(application) {

    abstract val quote: StateFlow<FullQuoteData?>

    abstract val timeSeries: StateFlow<Map<String, HistoricalData>>

    abstract val similarStocks: StateFlow<List<SimpleQuoteData>>

    abstract val sectorPerformance: StateFlow<MarketSector?>

    abstract val news: StateFlow<List<News>>

    abstract val analysis: StateFlow<Analysis?>

    abstract val signals: StateFlow<Map<AnalysisIndicators, String>>

    abstract val movingAveragesSummary: StateFlow<Double>
    abstract val oscillatorsSummary: StateFlow<Double>
    abstract val trendsSummary: StateFlow<Double>
    abstract val overallSummary : StateFlow<Double>

    abstract val watchList: StateFlow<List<SimpleQuoteData>>


    abstract fun getTimeSeries(symbol: String, timePeriod: TimePeriod, interval: Interval)

    abstract fun getAnalysis(symbol: String, interval: Interval)

    abstract fun addToWatchList(symbol: String)

    abstract fun deleteFromWatchList(symbol: String)
}