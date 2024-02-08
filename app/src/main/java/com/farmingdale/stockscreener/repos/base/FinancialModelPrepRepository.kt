package com.farmingdale.stockscreener.repos.base

import com.farmingdale.stockscreener.model.local.FullQuoteData
import com.farmingdale.stockscreener.model.local.GeneralSearchData
import com.farmingdale.stockscreener.model.local.WatchList
import kotlinx.coroutines.flow.Flow

abstract class FinancialModelPrepRepository {
    abstract suspend fun generalSearch(query: String): Flow<GeneralSearchData>

    abstract suspend fun getFullQuote(symbol: String): Flow<FullQuoteData>

    abstract suspend fun getWatchList(): Flow<WatchList>

    abstract suspend fun addToWatchList(symbol: String)

    abstract suspend fun deleteFromWatchList(symbol: String)

    abstract suspend fun updateWatchList()

    companion object
}