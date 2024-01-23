package com.farmingdale.stockscreener.providers.base

import com.farmingdale.stockscreener.model.local.AnalysisType
import com.farmingdale.stockscreener.model.local.Interval
import com.farmingdale.stockscreener.model.local.SeriesType
import com.farmingdale.stockscreener.model.remote.QuoteData
import com.farmingdale.stockscreener.model.remote.TechnicalAnalysis

interface AlphaVantageAPI {

    suspend fun getQuote(symbol: String): QuoteData
    suspend fun getTechnicalAnalysis(
        function: AnalysisType,
        symbol: String,
        interval: String,
        timePeriod: Int,
        seriesType: String
    ): TechnicalAnalysis
}