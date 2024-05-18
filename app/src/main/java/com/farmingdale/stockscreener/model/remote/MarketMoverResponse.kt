package com.farmingdale.stockscreener.model.remote

import kotlinx.serialization.Serializable

/**
 * Data response for market movers
 * @param symbol Stock symbol
 * @param name Stock name
 * @param price Current stock price
 * @param change Change in stock price
 * @param percent_change Percent change in stock price

 */
@Serializable
data class MarketMoverResponse(
    val symbol: String,
    val name: String,
    val price: String,
    val change: String,
    val percent_change: String
)
