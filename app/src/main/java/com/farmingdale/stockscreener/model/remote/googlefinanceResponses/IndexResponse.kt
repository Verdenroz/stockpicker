package com.farmingdale.stockscreener.model.remote.googlefinanceResponses

import kotlinx.serialization.Serializable

/**
 * Data response for stock market indices
 * @param name Name of the index
 * @param score Current value of the index
 * @param change Change in value the index
 * @param percentChange Percent change in value of the index
 */
@Serializable
data class StockIndex(
    val name: String,
    val score: Double,
    val change: Double,
    val percentChange: String
)

/**
 * Wrapper for [StockIndex]
 */
@Serializable
data class StockIndexWrapper(
    val stockIndex: StockIndex
)
