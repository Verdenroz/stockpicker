package com.farmingdale.stockscreener.model.local

import java.util.Date

/**
 * Local data class for individual stock information
 * @param symbol the stock symbol
 * @param price the stock price
 * @param date the date of the stock price
 * @param analysisType the type of analysis performed on the stock if any
 */
data class StockData(
    val symbol: String,
    val price: Float,
    val date: Date,
    val analysisType: AnalysisType?,
)
