package com.farmingdale.stockscreener.model.remote.googlefinanceResponses

import kotlinx.serialization.Serializable

/**
 * Data response for Google Finance stock information
 * @param symbol Stock symbol
 * @param name Stock name
 * @param current Current stock price
 * @param change Change in stock price
 * @param percentChange Percent change in stock price

 */
@Serializable
data class GoogleFinanceStockResponse(
    val symbol: String,
    val name: String,
    val current: String,
    val change: String,
    val percentChange: String
)
