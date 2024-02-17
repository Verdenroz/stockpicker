package com.farmingdale.stockscreener.model.remote.newsResponses

import kotlinx.serialization.Serializable

/**
 * Data response for the news API representing the source of the article
 * @param name Name of the source. Results with null are not filtered out
 */
@Serializable
class SourceResponse(
    val name: String?
)