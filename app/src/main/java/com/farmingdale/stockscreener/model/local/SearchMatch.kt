package com.farmingdale.stockscreener.model.local
/**
 * Data class for search results used by Alpha Vantage
 * @param symbol the symbol of the security
 * @param name the name of the security
 * @param type the type of the security
 * @param region the region of the security
 * @param marketOpen the market open time of the security
 * @param marketClose the market close time of the security
 * @param timezone the timezone of the security
 * @param currency the currency of the security
 * @param matchScore how well the search result matches the keywords
 */
@Deprecated("Use GeneralSearchMatch instead")
data class SearchMatch(
    val symbol: String,
    val name: String,
    val type: String,
    val region: String,
    val marketOpen: String,
    val marketClose: String,
    val timezone: String,
    val currency: String,
    val matchScore: String
)

/**
 * Data class for FinacialModelPrep general search results
 * @param symbol the symbol of the security
 * @param name the name of the security
 * @param currency the currency of the security
 * @param stockExchange the exchange the security is listed on
 * @param exchangeShortName the short name of the exchange
 */
data class GeneralSearchMatch(
    val symbol: String,
    val name: String,
    val currency: String?,
    val stockExchange: String?,
    val exchangeShortName: String?
)
