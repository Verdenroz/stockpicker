package com.farmingdale.stockscreener.model.local

/**
 *  Data class for search results
 *  @param matches a list of search matches
 */
data class SearchData(
    val matches: List<SearchMatch>
)