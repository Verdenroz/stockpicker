package com.farmingdale.stockscreener.repos.base

import com.farmingdale.stockscreener.model.local.Exchange
import com.farmingdale.stockscreener.model.local.FullQuoteData
import com.farmingdale.stockscreener.model.local.GeneralSearchData
import com.farmingdale.stockscreener.model.local.WatchList
import kotlinx.coroutines.flow.Flow

abstract class FinancialModelPrepRepository {
    /**
     * Get search results for a given query
     */
    abstract suspend fun generalSearch(query: String, exchange: Exchange? = null): Flow<GeneralSearchData>

    /**
     * Get full quote data for a given symbol
     */
    abstract suspend fun getFullQuote(symbol: String): Flow<FullQuoteData>

    /**
     * Get the user's [WatchList]
     */
    abstract fun getWatchList(): Flow<WatchList>

    /**
     * Add a symbol to the user's [WatchList]
     */
    abstract suspend fun addToWatchList(symbol: String)

    /**
     * Delete a symbol from the user's [WatchList]
     */
    abstract suspend fun deleteFromWatchList(symbol: String)

    /**
     * Deletes all data from the user's [WatchList]
     */
    abstract suspend fun clearWatchList()

    /**
     * Update the user's [WatchList] with new stock data
     */
    abstract suspend fun updateWatchList()

    companion object
}