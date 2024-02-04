package com.farmingdale.stockscreener.model.local

/**
 * Local data class for individual stock information from FinancialModelPrep
 * @param symbol the stock symbol
 * @param name the stock name
 * @param price the stock price
 * @param exchange exchange the stock is listed on
 * @param exchangeShortName the stock exchange short name
 * @param type the equity type
 */
data class SymbolData(
    val symbol: String?,
    val name: String?,
    val price: Double?,
    val exchange: String?,
    val exchangeShortName: String?,
    val type: String?
)

/**
 * Local data class for a list of all 25,000+ stocks from FinancialModelPrep
 * @param symbols a list of all stock information in the form of [SymbolData]
 */
data class SymbolList(val symbols: List<SymbolData>)