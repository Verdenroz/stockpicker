package com.farmingdale.stockscreener.model.remote.googlefinanceResponses

import kotlinx.serialization.Serializable

/**
 * Data response for Google Finance news
 * @param headline News headline
 * @param image News image
 * @param source News source
 * @param url News URL
 */
@Serializable
data class GoogleFinanceNewsResponse(
    val headline: String,
    val image: String,
    val source: String,
    val url: String
)
