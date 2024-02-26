package com.farmingdale.stockscreener.model.local.googlefinance

/**
 * Local data class for stock market indices
 * @param name Name of the index
 * @param score Current value of the index
 * @param change Change in value the index
 * @param percentChange Percent change in value of the index
 */
data class MarketIndex(
    val name: String,
    val score: Double,
    val change: Double,
    val percentChange: String
)