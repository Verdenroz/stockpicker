package com.farmingdale.stockscreener.repos.base

import com.farmingdale.stockscreener.model.local.SimpleQuoteData
import com.farmingdale.stockscreener.utils.DataError
import com.farmingdale.stockscreener.utils.Resource
import com.farmingdale.stockscreener.utils.isMarketOpen
import kotlinx.coroutines.flow.Flow

abstract class WatchlistRepository {
    /**
     * The refresh interval for the user's watch list depending on whether the market is open or not
     * 30 seconds when the market is open, 10 minutes when the market is closed
     */
    var refreshInterval: Long = if (isMarketOpen()) 30000L else 600000L
        private set

    fun updateRefreshInterval(interval: Long) {
        refreshInterval = interval
    }

    /**
     * The user's watch list as a list of [SimpleQuoteData]
     */
    abstract val watchlist: Flow<List<SimpleQuoteData>>

    /**
     * Refresh the user's watch list with new stock data
     */
    abstract suspend fun refreshWatchList(): Resource<Unit, DataError.Network>

    /**
     * Add a symbol to the user's watch list
     */
    abstract suspend fun addToWatchList(symbol: String): Resource<Unit, DataError.Network>

    /**
     * Delete a symbol from the user's watch list
     */
    abstract suspend fun deleteFromWatchList(symbol: String)

    /**
     * Deletes all data from the user's watch list
     */
    abstract suspend fun clearWatchList()

    companion object
}