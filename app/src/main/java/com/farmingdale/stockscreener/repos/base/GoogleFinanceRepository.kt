package com.farmingdale.stockscreener.repos.base

import com.farmingdale.stockscreener.model.local.googlefinance.MarketIndex
import kotlinx.coroutines.flow.Flow

/**
 * Repository for fetching information from Google Finance
 */
abstract class GoogleFinanceRepository {
    abstract suspend fun getIndices(): Flow<List<MarketIndex>>

    companion object
}