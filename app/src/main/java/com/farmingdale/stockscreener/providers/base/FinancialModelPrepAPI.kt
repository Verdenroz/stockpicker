package com.farmingdale.stockscreener.providers.base

import com.farmingdale.stockscreener.model.local.FullQuoteData
import com.farmingdale.stockscreener.model.local.GeneralSearchData
import com.farmingdale.stockscreener.model.local.SymbolList
import com.farmingdale.stockscreener.model.local.WatchList

/**
 * Base interface for the FinancialModelPrepAPI
 * @see <a href="https://financialmodelingprep.com/developer/docs/">FinancialModelPrep API</a>
 */
interface FinancialModelPrepAPI {

    /**
     * Search for a stock by name or symbol
     * @param query the search query
     * @return a list of 5 [GeneralSearchData] sorted by relevance
     */
    suspend fun generalSearch(query: String): GeneralSearchData

    /**
     * Find symbols for traded and non-traded stocks
     * @return [SymbolList] a list of SymbolData with basic information
     */
    suspend fun getSymbolList(): SymbolList

    /**
     * Get full quote data for a stock
     * @param symbol identifies the quote is for
     * @return [FullQuoteData] containing all available information such as price, volume, market cap, etc.
     */
    suspend fun getFullQuote(symbol: String): FullQuoteData

    /**
     * Get full quote data for a list of stocks
     * @param symbols a variable number of strings to identify the requested quote
     * @return [WatchList]
     */
    suspend fun getBulkQuote(symbols: List<String>): WatchList


}