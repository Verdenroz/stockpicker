package com.farmingdale.stockscreener.model.remote.financialmodelprepResponses

import kotlinx.serialization.Serializable

/**
 * This class is used to parse the response from the FinancialModelPrep API when requesting a full quote for a stock.
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
@Serializable
data class FullQuoteResponse(
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
    val pe: Double,
    val earningsAnnouncement: String,
    val sharesOutstanding: Long,
    val timestamp: Long
)