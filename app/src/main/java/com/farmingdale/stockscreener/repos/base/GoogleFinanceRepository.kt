package com.farmingdale.stockscreener.repos.base

import com.farmingdale.stockscreener.model.local.UnitedStatesExchanges
import com.farmingdale.stockscreener.model.local.googlefinance.GoogleFinanceNews
import com.farmingdale.stockscreener.model.local.googlefinance.GoogleFinanceStock
import com.farmingdale.stockscreener.model.local.googlefinance.MarketIndex
import kotlinx.coroutines.flow.Flow

/**
 * Repository for fetching information from Google Finance
 */
abstract class GoogleFinanceRepository {
    /**
     * Get the indices from Google Finance
     * @return [Flow] of [List] of [MarketIndex]
     */
    abstract suspend fun getIndices(): Flow<List<MarketIndex>>

    /**
     * Get the active stocks from Google Finance
     * @return [Flow] of [List] of [GoogleFinanceStock]
     */
    abstract suspend fun getActives(): Flow<List<GoogleFinanceStock>>

    /**
     * Get the losers from Google Finance
     * @return [Flow] of [List] of [GoogleFinanceStock]
     */
    abstract suspend fun getLosers(): Flow<List<GoogleFinanceStock>>

    /**
     * Get the gainers from Google Finance
     * @return [Flow] of [List] of [GoogleFinanceStock]
     */
    abstract suspend fun getGainers(): Flow<List<GoogleFinanceStock>>

    /**
     * Get the news for a stock from Google Finance
     * @param symbol the stock symbol
     * @param exchange the stock exchange the stock is listed on
     * @return [Flow] of [List] of [GoogleFinanceNews]
     */
    abstract suspend fun getNews(symbol: String, exchange: UnitedStatesExchanges): Flow<List<GoogleFinanceNews>>

    companion object
}