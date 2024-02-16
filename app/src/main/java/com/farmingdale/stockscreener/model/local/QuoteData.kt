package com.farmingdale.stockscreener.model.local

/**
 * Local data class for Global Quotes used by Alpha Vantage
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
@Deprecated("Use FullQuoteData instead")
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

/**
 * Local data class for individual stock data given by FinancialModelPrep
 * @param symbol the stock symbol
 * @param name the stock name
 * @param price the current price
 * @param changesPercentage the change in price in percent
 * @param change the change in price
 * @param dayLow the lowest price of the day
 * @param dayHigh the highest price of the day
 * @param yearHigh the highest price of the year
 * @param yearLow the lowest price of the year
 * @param marketCap the market capitalization
 * @param priceAvg50 the average price of the last 50 days
 * @param priceAvg200 the average price of the last 200 days
 * @param exchange the exchange the stock is traded on
 * @param volume the number of shares traded in a day
 * @param avgVolume the average volume traded in a day
 * @param open the opening price of the day
 * @param previousClose the previous closing price
 * @param eps the earnings per share
 * @param pe the price to earnings ratio
 * @param earningsAnnouncement the date of the earnings announcement
 * @param sharesOutstanding the number of shares outstanding
 * @param timestamp the timestamp of the data
 */
data class FullQuoteData(
    val symbol: String,
    val name: String,
    val price: Double,
    val changesPercentage: Double,
    val change: Double,
    val dayLow: Double,
    val dayHigh: Double,
    val yearHigh: Double,
    val yearLow: Double,
    val marketCap: Long,
    val priceAvg50: Double,
    val priceAvg200: Double,
    val exchange: String,
    val volume: Long,
    val avgVolume: Long,
    val open: Double,
    val previousClose: Double,
    val eps: Double,
    val pe: Double?,
    val earningsAnnouncement: String?,
    val sharesOutstanding: Long,
    val timestamp: Long
)

/**
 * Local data class for a list of watchlisted stock data given by FinancialModelPrep
 * @param quotes a list [FullQuoteData] for each stock in the watchlist
 */
data class WatchList(val quotes: List<FullQuoteData>)
