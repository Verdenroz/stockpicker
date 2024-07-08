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
import com.farmingdale.stockscreener.utils.DataError
import com.farmingdale.stockscreener.utils.Resource
import com.farmingdale.stockscreener.utils.isMarketOpen
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.coroutines.flow.Flow

abstract class FinanceQueryRepository {
    /**
     * The refresh interval for the user's watch list depending on whether the market is open or not
     * 15 seconds when the market is open, 60 minutes when the market is closed
     */
    var refreshInterval = if (isMarketOpen()) 15000L else 3600000L
        private set

    fun updateRefreshInterval(interval: Long) {
        refreshInterval = interval
    }

    /**
     *  Market indices as list of [MarketIndex]
     */
    abstract val indices: Flow<Resource<ImmutableList<MarketIndex>, DataError.Network>>

    /**
     * Active stocks as list of [MarketMover]
     */
    abstract val actives: Flow<Resource<ImmutableList<MarketMover>, DataError.Network>>

    /**
     * Losers as list of [MarketMover]
     */
    abstract val losers: Flow<Resource<ImmutableList<MarketMover>, DataError.Network>>

    /**
     * Gainers as list of [MarketMover]
     */
    abstract val gainers: Flow<Resource<ImmutableList<MarketMover>, DataError.Network>>

    /**
     * Latest news headlines as list of [News]
     */
    abstract val headlines: Flow<Resource<ImmutableList<News>, DataError.Network>>

    /**
     * Sectors as list of [MarketSector]
     */
    abstract val sectors: Flow<Resource<ImmutableList<MarketSector>, DataError.Network>>

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
    abstract fun getFullQuote(symbol: String): Flow<Resource<FullQuoteData, DataError.Network>>

    /**
     * Get simple quote data for a stock with basic information as [SimpleQuoteData]
     */
    abstract suspend fun getSimpleQuote(symbol: String): Flow<Resource<SimpleQuoteData, DataError.Network>>

    /**
     * Get simple quote data for a list of symbols as a list of [SimpleQuoteData]
     */
    abstract suspend fun getBulkQuote(symbols: List<String>): Flow<Resource<ImmutableList<SimpleQuoteData>, DataError.Network>>

    /**
     * Get news for a symbol as a list of [News]
     */
    abstract fun getNewsForSymbol(symbol: String): Flow<Resource<ImmutableList<News>, DataError.Network>>

    /**
     * Find similar stocks for a symbol as a list of [SimpleQuoteData]
     */
    abstract fun getSimilarStocks(symbol: String): Flow<Resource<ImmutableList<SimpleQuoteData>, DataError.Network>>

    /**
     * Gets the [MarketSector] performance of the symbol if available
     */
    abstract fun getSectorBySymbol(symbol: String): Flow<Resource<MarketSector?, DataError.Network>>

    /**
     * Get historical data for a symbol as a map of dates to [HistoricalData]
     */
    abstract suspend fun getTimeSeries(
        symbol: String,
        timePeriod: TimePeriod,
        interval: Interval
    ): Flow<Resource<ImmutableMap<String, HistoricalData>, DataError.Network>>

    /**
     * Get summary analysis for a symbol as [Analysis] given an [Interval]
     */
    abstract fun getAnalysis(symbol: String, interval: Interval): Flow<Resource<Analysis?, DataError.Network>>

    companion object {
        const val NEWS_SECTORS_REFRESH_INTERVAL = 3600000L
    }

}