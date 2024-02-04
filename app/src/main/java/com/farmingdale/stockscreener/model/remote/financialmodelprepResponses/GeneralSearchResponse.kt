package com.farmingdale.stockscreener.model.remote.financialmodelprepResponses

import kotlinx.serialization.Serializable

/**
 * Remote data class for general search results given by FinancialModelPrep
 * @param symbol the symbol of the security
 * @param name the name of the security
 * @param currency the currency of the security
 * @param stockExchange the exchange the security is listed on
 * @param exchangeShortName the short name of the exchange
 */
@Serializable
data class GeneralSearchResponse(
    val symbol: String,
    val name: String,
    val currency: String?,
    val stockExchange: String,
    val exchangeShortName: String
)
