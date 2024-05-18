package com.farmingdale.stockscreener.providers.base

import com.farmingdale.stockscreener.model.local.FullQuoteData
import com.farmingdale.stockscreener.model.local.Interval
import com.farmingdale.stockscreener.model.local.MarketSector
import com.farmingdale.stockscreener.model.local.News
import com.farmingdale.stockscreener.model.local.SimpleQuoteData
import com.farmingdale.stockscreener.model.local.TimePeriod
import com.farmingdale.stockscreener.model.local.TimeSeriesData
import com.farmingdale.stockscreener.model.local.googlefinance.MarketMover
import com.farmingdale.stockscreener.model.local.googlefinance.MarketIndex

interface FinanceQueryAPI {

    /**
     * Get full quote data for a stock with all available information
     * @param symbol identifies the quote is for
     * @return [FullQuoteData] containing all available information such as price, volume, market cap, etc.
     */
    suspend fun getQuote(symbol: String): FullQuoteData

    /**
     * Get simple quote data for a stock with basic information
     * @param symbol identifies the quote is for
     * @return [SimpleQuoteData] containing basic information such as price, change, percent change, etc.
     */
    suspend fun getSimpleQuote(symbol: String): SimpleQuoteData

    /**
     * Get simple quote data for a list of stocks
     * @param symbols a variable number of strings to identify the requested quote
     * @return a list of [SimpleQuoteData]
     */
    suspend fun getBulkQuote(symbols: List<String>): List<SimpleQuoteData>

    /**
     * Get historical prices (OHLCV) for a stock
     * @param symbol symbol of the stock
     * @param time the time period to get data for (1d, 5d, 1m, 3m, 6m, 1y, 2y, 5y, 10y, ytd, max)
     * @param interval the interval between data points (15m, 30m, 1h, 1d, 1wk, 1mo, 3mo)
     * @return [TimeSeriesData] containing the historical data
     * @see Interval
     * @see TimePeriod
     */
    suspend fun getHistoricalData(symbol: String, time: TimePeriod, interval: Interval): TimeSeriesData

    suspend fun search(query: String)

    /**
     * Get current market indices in the US
     * @return a list of [MarketIndex]
     */
    suspend fun getIndices(): List<MarketIndex>


    /**
     * Get market sectors in the US
     * @return a list of [MarketSector]
     */
    suspend fun getSectors(): List<MarketSector>

    /**
     * Get active stocks in the US
     * @return a list of [MarketMover]
     */
    suspend fun getActives(): List<MarketMover>

    /**
     * Get stocks with the highest percentage gain in the US
     * @return a list of [MarketMover]
     */
    suspend fun getGainers(): List<MarketMover>

    /**
     * Get stocks with the highest percentage loss in the US
     * @return a list of [MarketMover]
     */
    suspend fun getLosers(): List<MarketMover>

    /**
     * Get the latest general financial news
     * @return a list of [News]
     */
    suspend fun getNews(): List<News>

    /**
     * Get the latest financial news for a stock
     * @param symbol the stock to get news for
     * @return a list of [News]
     */
    suspend fun getNewsForSymbol(symbol: String): List<News>

    /**
     * Find similar stocks to a given symbol
     * @param symbol the stock to find similar stocks for
     * @return a list of [SimpleQuoteData]
     */
    suspend fun getSimilarSymbols(symbol: String): List<SimpleQuoteData>

    suspend fun getTechnicalIndicator(symbol: String)

    suspend fun getSummaryAnalysis(symbol: String)
}