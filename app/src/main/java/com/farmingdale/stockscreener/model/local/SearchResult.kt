package com.farmingdale.stockscreener.model.local

import kotlinx.serialization.Serializable

/**
 * Data class for Algolia search results
 * @param symbol the symbol of the security
 * @param name the name of the security
 * @param exchangeShortName the short name of the exchange
 * @param type the type of security
 */
@Serializable
data class SearchResult(
    val symbol: String,
    val name: String,
    val exchangeShortName: String,
    val type: String,
)
