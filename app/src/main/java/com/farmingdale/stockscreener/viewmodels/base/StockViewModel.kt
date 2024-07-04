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
import com.farmingdale.stockscreener.utils.DataError
import com.farmingdale.stockscreener.utils.Resource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.coroutines.flow.StateFlow

abstract class StockViewModel(application: Application) : AndroidViewModel(application) {

    abstract val quote: StateFlow<Resource<FullQuoteData, DataError.Network>>

    abstract val timeSeries: StateFlow<Resource<ImmutableMap<String, HistoricalData>, DataError.Network>>

    abstract val similarStocks: StateFlow<Resource<ImmutableList<SimpleQuoteData>, DataError.Network>>

    abstract val sectorPerformance: StateFlow<Resource<MarketSector?, DataError.Network>>

    abstract val news: StateFlow<Resource<ImmutableList<News>, DataError.Network>>

    abstract val analysis: StateFlow<Resource<Analysis?, DataError.Network>>

    abstract val signals: StateFlow<ImmutableMap<AnalysisIndicators, String>>

    abstract val movingAveragesSummary: StateFlow<Double>
    abstract val oscillatorsSummary: StateFlow<Double>
    abstract val trendsSummary: StateFlow<Double>
    abstract val overallSummary : StateFlow<Double>

    abstract val watchList: StateFlow<ImmutableList<SimpleQuoteData>>


    abstract fun getTimeSeries(symbol: String, timePeriod: TimePeriod, interval: Interval)

    abstract fun getAnalysis(symbol: String, interval: Interval)

}