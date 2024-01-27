package com.farmingdale.stockscreener.model.local

/**
 * Local data class for Global Quotes
 * @param symbol the stock symbol
 * @param open the open price of the day
 * @param high the high price of the day
 * @param low the low price of the day
 * @param price the current price
 * @param volume the current volume
 * @param latestTradingDay the latest trading day
 * @param previousClose the previous close price
 * @param change the change in price
 * @param changePercent the change in percent
 */
data class QuoteData(
    val symbol: String,
    val open: String,
    val high: String,
    val low: String,
    val price: String,
    val volume: Int,
    val latestTradingDay: String,
    val previousClose: String,
    val change: String,
    val changePercent: String
)
