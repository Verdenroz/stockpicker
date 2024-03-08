package com.farmingdale.stockscreener.providers.base

import com.farmingdale.stockscreener.model.local.UnitedStatesExchanges
import com.farmingdale.stockscreener.model.local.googlefinance.GoogleFinanceNews
import com.farmingdale.stockscreener.model.local.googlefinance.GoogleFinanceStock
import com.farmingdale.stockscreener.model.local.googlefinance.MarketIndex

/**
 * Interface for Google Finance API
 * @see <a href="https://github.com/Verdenroz/GoogleFinanceAPI">Google Finance API</a>
 */
interface GoogleFinanceAPI {
    /**
     * Get current market indices in the US as a list of [MarketIndex]
     */
    suspend fun getIndices(): List<MarketIndex>

    /**
     * Get active stocks in the US as a list of [GoogleFinanceStock]
     */
    suspend fun getActiveStocks(): List<GoogleFinanceStock>

    /**
     * Get stocks with the highest percentage gain in the US as a list of [GoogleFinanceStock]
     */
    suspend fun getGainers(): List<GoogleFinanceStock>

    /**
     * Get stocks with the highest percentage loss in the US as a list of [GoogleFinanceStock]
     */
    suspend fun getLosers(): List<GoogleFinanceStock>

    /**
     * Get related news to a stock as a list of [GoogleFinanceNews]
     * @param symbol the stock symbol
     * @param exchange the stock exchange
     */
    suspend fun getRelatedNews(symbol: String, exchange: UnitedStatesExchanges): List<GoogleFinanceNews>
}