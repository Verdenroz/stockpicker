package com.farmingdale.stockscreener.model.local

import com.farmingdale.stockscreener.model.remote.AnalysisData
import java.util.Date

data class StockData(
    val symbol: String,
    val price: Float,
    val date: Date,
    val analysisType: AnalysisType?,
)
