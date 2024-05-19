package com.farmingdale.stockscreener.model.remote

import kotlinx.serialization.Serializable

/**
 * This class is used to parse the response from the FinanceQuery API when requesting a full quote for a stock.
 * @param symbol the stock symbol
 * @param name the stock name
 * @param price the current price
 * @param after_hours_price the price after market close
 * @param change the change in price
 * @param percent_change the percentage change in price
 * @param open the opening price of the day
 * @param high the highest price of the day
 * @param low the lowest price of the day
 * @param yearHigh the highest price of the year
 * @param yearLow the lowest price of the year
 * @param volume the number of shares traded in a day
 * @param avgVolume the average volume traded in a day
 * @param marketCap the market capitalization
 * @param beta the beta value
 * @param eps the earnings per share
 * @param pe the price to earnings ratio
 * @param dividend the dividend amount
 * @param exDividend the last date before which the stock must be bought to receive the dividend
 * @param earningsDate the earnings announcement date
 * @param sector the sector the stock belongs to
 * @param industry the industry the stock belongs to
 * @param about a brief description of the company
 */
@Serializable
data class FullQuoteResponse(
    val symbol: String,
    val name: String,
    val price: Double,
    val after_hours_price: Double,
    val change: String,
    val percent_change: String,
    val open: Double,
    val high: Double,
    val low: Double,
    val year_high: Double,
    val year_low: Double,
    val volume: Long,
    val avg_volume: Long,
    val market_cap: String,
    val beta: Double?,
    val pe: Double?,
    val eps: Double?,
    val dividend: Double?,
    val ex_dividend: Double?,
    val earnings_date: String?,
    val sector: String?,
    val industry: String?,
    val about: String?
)
