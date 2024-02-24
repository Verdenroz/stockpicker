package com.farmingdale.stockscreener.providers.base

import com.farmingdale.stockscreener.model.local.googlefinance.StockIndex

/**
 * Interface for Google Finance API
 */
interface GoogleFinanceAPI {
    /**
     * Get current market indices in the US as a list of [StockIndex]
     */
    suspend fun getIndices(): List<StockIndex>
}