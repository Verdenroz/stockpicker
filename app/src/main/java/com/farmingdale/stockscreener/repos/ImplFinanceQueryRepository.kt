package com.farmingdale.stockscreener.repos

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
import com.farmingdale.stockscreener.providers.ImplFinanceQueryAPI
import com.farmingdale.stockscreener.providers.okHttpClient
import com.farmingdale.stockscreener.repos.base.FinanceQueryRepository
import com.farmingdale.stockscreener.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImplFinanceQueryRepository : FinanceQueryRepository() {
    private val api = ImplFinanceQueryAPI(okHttpClient)
    private val _indicesChannel = Channel<Resource<List<MarketIndex>>>(Channel.CONFLATED)
    private val _activesChannel = Channel<Resource<List<MarketMover>>>(Channel.CONFLATED)
    private val _losersChannel = Channel<Resource<List<MarketMover>>>(Channel.CONFLATED)
    private val _gainersChannel = Channel<Resource<List<MarketMover>>>(Channel.CONFLATED)
    private val _headlinesChannel = Channel<Resource<List<News>>>(Channel.CONFLATED)
    private val _sectorsChannel = Channel<Resource<List<MarketSector>>>(Channel.CONFLATED)

    override val indices: Flow<Resource<List<MarketIndex>>> = _indicesChannel.receiveAsFlow()
    override val actives: Flow<Resource<List<MarketMover>>> = _activesChannel.receiveAsFlow()
    override val losers: Flow<Resource<List<MarketMover>>> = _losersChannel.receiveAsFlow()
    override val gainers: Flow<Resource<List<MarketMover>>> = _gainersChannel.receiveAsFlow()
    override val headlines: Flow<Resource<List<News>>> = _headlinesChannel.receiveAsFlow()
    override val sectors: Flow<Resource<List<MarketSector>>> = _sectorsChannel.receiveAsFlow()


    init {
        CoroutineScope(Dispatchers.IO).launch {
            refreshMarketDataPeriodically()
            refreshHeadlinesPeriodically()
            refreshSectorsPeriodically()
        }
    }

    private fun refreshMarketDataPeriodically() = CoroutineScope(Dispatchers.IO).launch {
        while (true) {
            refreshMarketData()
            delay(refreshInterval)
        }
    }

    private fun refreshHeadlinesPeriodically() = CoroutineScope(Dispatchers.IO).launch {
        while (true) {
            refreshNews()
            delay(NEWS_REFRESH_INTERVAL)
        }
    }

    private fun refreshSectorsPeriodically() = CoroutineScope(Dispatchers.IO).launch {
        while (true) {
            refreshSectors()
            delay(SECTOR_REFRESH_INTERVAL)
        }
    }

    override suspend fun refreshMarketData() = coroutineScope {
        try {
            val indicesDeferred = async(Dispatchers.IO) { api.getIndices() }
            val activesDeferred = async(Dispatchers.IO) { api.getActives() }
            val losersDeferred = async(Dispatchers.IO) { api.getLosers() }
            val gainersDeferred = async(Dispatchers.IO) { api.getGainers() }

            _indicesChannel.send(Resource.Success(indicesDeferred.await()))
            _activesChannel.send(Resource.Success(activesDeferred.await()))
            _losersChannel.send(Resource.Success(losersDeferred.await()))
            _gainersChannel.send(Resource.Success(gainersDeferred.await()))
        } catch (e: Exception) {
            _indicesChannel.send(Resource.Error(message = e.message ?: "An error occurred"))
        }
    }

    override suspend fun refreshNews() {
        try {
            withContext(Dispatchers.IO) {
                _headlinesChannel.send(Resource.Success(api.getNews()))
            }
        } catch (e: Exception) {
            _headlinesChannel.send(Resource.Error(message = e.message ?: "An error occurred"))
        }
    }

    override suspend fun refreshSectors() {
        try {
            withContext(Dispatchers.IO) {
                _sectorsChannel.send(Resource.Success(api.getSectors()))
            }
        } catch (e: Exception) {
            _sectorsChannel.send(Resource.Error(message = e.message ?: "An error occurred"))
        }
    }

    override fun getFullQuote(symbol: String): Flow<Resource<FullQuoteData>> = flow {
        try {
            val quote = api.getQuote(symbol)
            emit(Resource.Success(quote))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getSimpleQuote(symbol: String): Flow<Resource<SimpleQuoteData>> = flow {
        try {
            val simpleQuote = api.getSimpleQuote(symbol)
            emit(Resource.Success(simpleQuote))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getBulkQuote(symbols: List<String>): Flow<Resource<List<SimpleQuoteData>>> = flow {
        try {
            val quotes = api.getBulkQuote(symbols)
            emit(Resource.Success(quotes))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    override fun getNewsForSymbol(symbol: String): Flow<Resource<List<News>>> = flow {
        try {
            val news = api.getNewsForSymbol(symbol)
            emit(Resource.Success(news))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    override fun getSimilarStocks(symbol: String): Flow<Resource<List<SimpleQuoteData>>> = flow {
        try {
            val similarStocks = api.getSimilarSymbols(symbol)
            emit(Resource.Success(similarStocks))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getTimeSeries(
        symbol: String,
        timePeriod: TimePeriod,
        interval: Interval,
    ): Flow<Resource<Map<String, HistoricalData>>> = flow {
        try {
            val timeSeries = api.getHistoricalData(symbol, timePeriod, interval)
            emit(Resource.Success(timeSeries))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    override fun getAnalysis(symbol: String, interval: Interval): Flow<Resource<Analysis>> = flow {
        try {
            val analysis = api.getSummaryAnalysis(symbol, interval)
            emit(Resource.Success(analysis))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message ?: "An error occurred"))
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