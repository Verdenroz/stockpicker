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
data class GoogleFinanceIndexResponse(
    val name: String,
    val score: String,
    val change: String,
    val percentChange: String
)
