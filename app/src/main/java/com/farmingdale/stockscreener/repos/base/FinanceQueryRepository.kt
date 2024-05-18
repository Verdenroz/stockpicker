package com.farmingdale.stockscreener.repos.base

import com.farmingdale.stockscreener.model.local.FullQuoteData
import com.farmingdale.stockscreener.model.local.News
import com.farmingdale.stockscreener.model.local.SimpleQuoteData
import com.farmingdale.stockscreener.model.local.googlefinance.MarketIndex
import com.farmingdale.stockscreener.model.local.googlefinance.MarketMover
import kotlinx.coroutines.flow.Flow

abstract class FinanceQueryRepository {

    /**
     *  Market indices as list of [MarketIndex]
     */
    abstract val indices: Flow<List<MarketIndex>?>

    /**
     * Active stocks as list of [MarketMover]
     */
    abstract val actives: Flow<List<MarketMover>?>

    /**
     * Losers as list of [MarketMover]
     */
    abstract val losers: Flow<List<MarketMover>?>

    /**
     * Gainers as list of [MarketMover]
     */
    abstract val gainers: Flow<List<MarketMover>?>

    /**
     * Latest news headlines as list of [News]
     */
    abstract val headlines: Flow<List<News>?>

    /**
     * Refresh indices and market movers data
     */
    abstract suspend fun refreshMarketData()

    /**
     * Refresh news headlines
     */
    abstract suspend fun refreshNews()

    /**
     * Get full quote data for a stock with all available information as [FullQuoteData]
     */
    abstract suspend fun getFullQuote(symbol: String): Flow<FullQuoteData>

    /**
     * Get simple quote data for a stock with basic information as [SimpleQuoteData]
     */
    abstract suspend fun getSimpleQuote(symbol: String): Flow<SimpleQuoteData>

    /**
     * Get simple quote data for a list of symbols as a list of [SimpleQuoteData]
     */
    abstract suspend fun getBulkQuote(symbols: List<String>): Flow<List<SimpleQuoteData>>

    /**
     * Get news for a symbol as a list of [News]
     */
    abstract suspend fun getNewsForSymbol(symbol: String): Flow<List<News>>

    /**
     * Find similar stocks for a symbol as a list of [SimpleQuoteData]
     */
    abstract suspend fun getSimilarStocks(symbol: String): Flow<List<SimpleQuoteData>>

    companion object {
        const val REFRESH_INTERVAL = 5000L
        const val NEWS_REFRESH_INTERVAL = 60000L
    }

}