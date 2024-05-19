package com.farmingdale.stockscreener.repos.base

import com.farmingdale.stockscreener.model.local.SimpleQuoteData
import kotlinx.coroutines.flow.Flow

abstract class WatchlistRepository {
    /**
     * The user's watch list as a list of [SimpleQuoteData]
     */
    abstract val watchlist: Flow<List<SimpleQuoteData>>

    /**
     * Refresh the user's watch list with new stock data
     */
    abstract suspend fun refreshWatchList()

    /**
     * Add a symbol to the user's watch list
     */
    abstract suspend fun addToWatchList(symbol: String)

    /**
     * Delete a symbol from the user's watch list
     */
    abstract suspend fun deleteFromWatchList(symbol: String)

    /**
     * Deletes all data from the user's watch list
     */
    abstract suspend fun clearWatchList()

    companion object {
        const val REFRESH_INTERVAL = 5000L
    }
}