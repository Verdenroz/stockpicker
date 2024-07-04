package com.farmingdale.stockscreener.model.local

import androidx.compose.runtime.Immutable

/**
 * Local data class for basic stock data used in the watchlist
 * @param symbol the stock symbol
 * @param name the stock name
 * @param price the stock price
 * @param change the price change
 * @param percentChange the percentage change
 */
@Immutable
data class SimpleQuoteData(
    val symbol: String,
    val name: String,
    val price: Double,
    val change: String,
    val percentChange: String,
)

/**
 * Local data class for comprehensive stock data
 * @param symbol the stock symbol
 * @param name the stock name
 * @param price the current price
 * @param postMarketPrice the price after market close
 * @param change the change in price
 * @param percentChange the percentage change in price
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
 * @param yield the dividend yield
 * @param netAssets the net assets
 * @param nav the net asset value
 * @param expenseRatio the expense ratio
 * @param category the category the fund belongs to
 * @param lastCapitalGain the last capital gain of the fund
 * @param morningstarRating the morningstar rating of the fund
 * @param morningstarRisk the morningstar risk of the fund
 * @param holdingsTurnover the holdings turnover of the fund
 * @param lastDividend the last dividend date
 * @param inceptionDate the inception date of the fund
 * @param exDividend the last date before which the stock must be bought to receive the dividend
 * @param earningsDate the earnings announcement date
 * @param sector the sector the stock belongs to
 * @param industry the industry the stock belongs to
 * @param about a brief description of the company
 * @param ytdReturn the year to date return
 * @param yearReturn the one year return
 * @param threeYearReturn the three year return
 * @param fiveYearReturn the five year return
 * @param logo the URL of the company logo
 */
@Immutable
data class FullQuoteData(
    val symbol: String,
    val name: String,
    val price: Double,
    val postMarketPrice: Double?,
    val change: String,
    val percentChange: String,
    val open: Double?,
    val high: Double?,
    val low: Double?,
    val yearHigh: Double?,
    val yearLow: Double?,
    val volume: String?,
    val avgVolume: String?,
    val marketCap: String?,
    val beta: Double?,
    val pe: Double?,
    val eps: Double?,
    val dividend: String?,
    val yield: String?,
    val netAssets: String?,
    val nav: Double?,
    val expenseRatio: String?,
    val category: String?,
    val lastCapitalGain: String?,
    val morningstarRating: String?,
    val morningstarRisk: String?,
    val holdingsTurnover: String?,
    val lastDividend: String?,
    val inceptionDate: String?,
    val exDividend: String?,
    val earningsDate: String?,
    val sector: String?,
    val industry: String?,
    val about: String?,
    val ytdReturn: String?,
    val yearReturn: String?,
    val threeYearReturn: String?,
    val fiveYearReturn: String?,
    val logo: String?
)
