package com.farmingdale.stockscreener.repos.base

import com.farmingdale.stockscreener.model.local.Analysis
import com.farmingdale.stockscreener.model.local.FullQuoteData
import com.farmingdale.stockscreener.model.local.HistoricalData
import com.farmingdale.stockscreener.model.local.Interval
import com.farmingdale.stockscreener.model.local.MarketIndex
import com.farmingdale.stockscreener.model.local.MarketMover
import com.farmingdale.stockscreener.model.local.MarketSector
import com.farmingdale.stockscreener.model.local.News
import com.farmingdale.stockscreener.model.local.SimpleQuoteData
import com.farmingdale.stockscreener.model.local.TimePeriod
import com.farmingdale.stockscreener.utils.isMarketOpen
import kotlinx.coroutines.flow.Flow

abstract class FinanceQueryRepository {
    /**
     * The refresh interval for the user's watch list depending on whether the market is open or not
     * 15 seconds when the market is open, 30 minutes when the market is closed
     */
    var refreshInterval = if (isMarketOpen()) 15000L else 1800000L
        private set

    fun updateRefreshInterval(interval: Long) {
        refreshInterval = interval
    }

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
     * Sectors as list of [MarketSector]
     */
    abstract val sectors: Flow<List<MarketSector>>

    /**
     * Refresh [indices], [actives], [losers], [gainers]
     */
    abstract suspend fun refreshMarketData()

    /**
     * Refresh [headlines]
     */
    abstract suspend fun refreshNews()

    /**
     * Refresh [sectors]
     */
    abstract suspend fun refreshSectors()

    /**
     * Get full quote data for a stock with all available information as [FullQuoteData]
     */
    abstract fun getFullQuote(symbol: String): Flow<FullQuoteData>

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
    abstract fun getNewsForSymbol(symbol: String): Flow<List<News>>

    /**
     * Find similar stocks for a symbol as a list of [SimpleQuoteData]
     */
    abstract fun getSimilarStocks(symbol: String): Flow<List<SimpleQuoteData>>

    /**
     * Get historical data for a symbol as a map of dates to [HistoricalData]
     */
    abstract suspend fun getTimeSeries(
        symbol: String,
        timePeriod: TimePeriod,
        interval: Interval
    ): Flow<Map<String, HistoricalData>>

    /**
     * Get summary analysis for a symbol as [Analysis] given an [Interval]
     */
    abstract fun getAnalysis(symbol: String, interval: Interval): Flow<Analysis>

    /**
     * Get a specific sector's performance as [MarketSector]
     */
    abstract fun getSectorPerformance(sector: String): Flow<MarketSector>

    companion object {
        const val NEWS_REFRESH_INTERVAL = 60000L
        const val SECTOR_REFRESH_INTERVAL = 3600000L
    }

}