package com.farmingdale.stockscreener.model.remote.financialmodelprepResponses

import kotlinx.serialization.Serializable

/**
 * Remote data class for individual stock information given by FinancialModelPrep
 * @param symbol the stock symbol
 * @param name the stock name
 * @param price the stock price
 * @param exchange exchange the stock is listed on
 * @param exchangeShortName the stock exchange short name
 * @param type the equity type
 */
@Serializable
data class SymbolDataResponse(
    val symbol: String?,
    val name: String?,
    val price: Double?,
    val exchange: String?,
    val exchangeShortName: String?,
    val type: String?
)
