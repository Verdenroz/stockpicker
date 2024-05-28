package com.farmingdale.stockscreener.repos

import com.farmingdale.stockscreener.model.local.FullQuoteData
import com.farmingdale.stockscreener.model.local.HistoricalData
import com.farmingdale.stockscreener.model.local.Interval
import com.farmingdale.stockscreener.model.local.News
import com.farmingdale.stockscreener.model.local.SimpleQuoteData
import com.farmingdale.stockscreener.model.local.MarketIndex
import com.farmingdale.stockscreener.model.local.MarketMover
import com.farmingdale.stockscreener.model.local.TimePeriod
import com.farmingdale.stockscreener.providers.ImplFinanceQueryAPI
import com.farmingdale.stockscreener.providers.okHttpClient
import com.farmingdale.stockscreener.repos.base.FinanceQueryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.withContext

class ImplFinanceQueryRepository : FinanceQueryRepository() {
    private val api = ImplFinanceQueryAPI(okHttpClient)
    private val _indicesFlow = MutableStateFlow<List<MarketIndex>?>(null)
    private val _activesFlow = MutableStateFlow<List<MarketMover>?>(null)
    private val _losersFlow = MutableStateFlow<List<MarketMover>?>(null)
    private val _gainersFlow = MutableStateFlow<List<MarketMover>?>(null)
    private val _headlinesFlow = MutableStateFlow<List<News>?>(null)

    override val indices: Flow<List<MarketIndex>?> = _indicesFlow.asStateFlow()
    override val actives: Flow<List<MarketMover>?> = _activesFlow.asStateFlow()
    override val losers: Flow<List<MarketMover>?> = _losersFlow.asStateFlow()
    override val gainers: Flow<List<MarketMover>?> = _gainersFlow.asStateFlow()
    override val headlines: Flow<List<News>?> = _headlinesFlow.asStateFlow()

    init {
        refreshMarketDataPeriodically()
        refreshHeadlinesPeriodically()
    }

    private fun refreshMarketDataPeriodically() = flow<Unit> {
        while (true) {
            refreshMarketData()
            delay(REFRESH_INTERVAL)
        }
    }.flowOn(Dispatchers.IO).launchIn(CoroutineScope(Dispatchers.IO))

    private fun refreshHeadlinesPeriodically() = flow<Unit> {
        while (true) {
            refreshNews()
            delay(NEWS_REFRESH_INTERVAL)
        }
    }.flowOn(Dispatchers.IO).launchIn(CoroutineScope(Dispatchers.IO))

    override suspend fun refreshMarketData() = coroutineScope {
        val indicesDeferred = async(Dispatchers.IO) { api.getIndices() }
        val activesDeferred = async(Dispatchers.IO) { api.getActives() }
        val losersDeferred = async(Dispatchers.IO) { api.getLosers() }
        val gainersDeferred = async(Dispatchers.IO) { api.getGainers() }

        _indicesFlow.emit(indicesDeferred.await())
        _activesFlow.emit(activesDeferred.await())
        _losersFlow.emit(losersDeferred.await())
        _gainersFlow.emit(gainersDeferred.await())
    }

    override suspend fun refreshNews() {
        withContext(Dispatchers.IO) {
            _headlinesFlow.emit(api.getNews())
        }
    }

    override suspend fun getFullQuote(symbol: String): Flow<FullQuoteData> = flow {
        try {
            emit(api.getQuote(symbol))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getSimpleQuote(symbol: String): Flow<SimpleQuoteData> = flow {
        try {
            emit(api.getSimpleQuote(symbol))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getBulkQuote(symbols: List<String>): Flow<List<SimpleQuoteData>> = flow {
        try {
            emit(api.getBulkQuote(symbols))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getNewsForSymbol(symbol: String): Flow<List<News>> = flow {
        try {
            emit(api.getNewsForSymbol(symbol))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getSimilarStocks(symbol: String): Flow<List<SimpleQuoteData>> = flow {
        try {
            emit(api.getSimilarSymbols(symbol))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getTimeSeries(
        symbol: String,
        timePeriod: TimePeriod,
        interval: Interval,
    ): Flow<Map<String, HistoricalData>> = flow {
        try {
            emit(api.getHistoricalData(symbol, timePeriod, interval))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }.flowOn(Dispatchers.IO)

    companion object {
        private var repo: ImplFinanceQueryRepository? = null

        /**
         * Get the implementation of [ImplFinanceQueryRepository]
         */
        @Synchronized
        fun FinanceQueryRepository.Companion.get(): FinanceQueryRepository {
            if (repo == null) {
                repo = ImplFinanceQueryRepository()
            }
            return repo!!
        }
    }
}