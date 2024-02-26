package com.farmingdale.stockscreener.providers.base

import com.farmingdale.stockscreener.model.local.googlefinance.MarketIndex

/**
 * Interface for Google Finance API
 */
interface GoogleFinanceAPI {
    /**
     * Get current market indices in the US as a list of [MarketIndex]
     */
    suspend fun getIndices(): List<MarketIndex>
}