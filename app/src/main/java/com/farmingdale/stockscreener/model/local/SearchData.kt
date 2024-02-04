package com.farmingdale.stockscreener.model.local

/**
 *  Data class for search results from AlphaVantage
 *  @param matches a list of search matches
 */
@Deprecated("Use GeneralSearchData instead")
data class SearchData(
    val matches: List<SearchMatch>
)

/**
 * Local data class for search results from FinancialModelPrep
 * @param matches a list of search matches sorted by relevance
 */
data class GeneralSearchData(
    val matches: List<GeneralSearchMatch>
)