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
     *  Market indices as list of [MarketIndex]
     */
    abstract val indices: Flow<List<MarketIndex>?>

    /**
     * Active stocks as list of [GoogleFinanceStock]
     */
    abstract val actives: Flow<List<GoogleFinanceStock>?>

    /**
     * Losers as list of [GoogleFinanceStock]
     */
    abstract val losers: Flow<List<GoogleFinanceStock>?>

    /**
     * Gainers as list of [GoogleFinanceStock]
     */
    abstract val gainers: Flow<List<GoogleFinanceStock>?>

    /**
     * Refresh the [actives], [losers], [gainers] and [indices] values
     */
    abstract suspend fun refreshValues()

    /**
     * Get the news for a stock from Google Finance
     * @param symbol the stock symbol
     * @param exchange the stock exchange the stock is listed on
     * @return [Flow] of [List] of [GoogleFinanceNews]
     */
    abstract suspend fun getNews(symbol: String, exchange: UnitedStatesExchanges): Flow<List<GoogleFinanceNews>>

    companion object
}