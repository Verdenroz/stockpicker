package com.farmingdale.stockscreener.model.remote

import kotlinx.serialization.Serializable

/**
 * Remote data class for individual stock information given by FinancialModelPrep
 * @param symbol the stock symbol
 * @param name the stock name
 * @param price the stock price
 * @param change the price change
 * @param percentChange the percentage change
 */
@Serializable
data class SimpleQuoteResponse(
    val symbol: String,
    val name: String,
    val price: Double,
    val change: String,
    val percentChange: String,
)
