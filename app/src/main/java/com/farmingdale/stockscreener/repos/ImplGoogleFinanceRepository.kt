package com.farmingdale.stockscreener.repos

import com.farmingdale.stockscreener.model.local.UnitedStatesExchanges
import com.farmingdale.stockscreener.model.local.googlefinance.GoogleFinanceNews
import com.farmingdale.stockscreener.model.local.googlefinance.GoogleFinanceStock
import com.farmingdale.stockscreener.model.local.googlefinance.MarketIndex
import com.farmingdale.stockscreener.providers.ImplGoogleFinanceAPI
import com.farmingdale.stockscreener.providers.okHttpClient
import com.farmingdale.stockscreener.repos.base.GoogleFinanceRepository
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

class ImplGoogleFinanceRepository : GoogleFinanceRepository() {
    private val api = ImplGoogleFinanceAPI(okHttpClient)
    private val refreshInterval = 10000L
    private val _indicesFlow = MutableStateFlow<List<MarketIndex>?>(null)
    private val _activesFlow = MutableStateFlow<List<GoogleFinanceStock>?>(null)
    private val _losersFlow = MutableStateFlow<List<GoogleFinanceStock>?>(null)
    private val _gainersFlow = MutableStateFlow<List<GoogleFinanceStock>?>(null)

    init {
        refreshValuesPeriodically()
    }

    override val indices: Flow<List<MarketIndex>?> = _indicesFlow.asStateFlow()

    override val actives: Flow<List<GoogleFinanceStock>?> = _activesFlow.asStateFlow()

    override val losers: Flow<List<GoogleFinanceStock>?> = _losersFlow.asStateFlow()

    override val gainers: Flow<List<GoogleFinanceStock>?> = _gainersFlow.asStateFlow()

    override suspend fun refreshValues() = coroutineScope {
        val indicesDeferred = async(Dispatchers.IO) { api.getIndices() }
        val activesDeferred = async(Dispatchers.IO) { api.getActiveStocks() }
        val losersDeferred = async(Dispatchers.IO) { api.getLosers() }
        val gainersDeferred = async(Dispatchers.IO) { api.getGainers() }

        _indicesFlow.emit(indicesDeferred.await())
        _activesFlow.emit(activesDeferred.await())
        _losersFlow.emit(losersDeferred.await())
        _gainersFlow.emit(gainersDeferred.await())
    }

    private fun refreshValuesPeriodically() = flow<Unit> {
        while (true) {
            refreshValues()
            delay(refreshInterval)
        }
    }.flowOn(Dispatchers.IO).launchIn(CoroutineScope(Dispatchers.IO))

    override suspend fun getNews(
        symbol: String,
        exchange: UnitedStatesExchanges
    ): Flow<List<GoogleFinanceNews>> = flow {
        try {
            emit(api.getRelatedNews(symbol, exchange))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }.flowOn(Dispatchers.IO)

    companion object {
        private var repo: ImplGoogleFinanceRepository? = null

        /**
         * Get instance of [ImplGoogleFinanceRepository]
         */
        @Synchronized
        fun GoogleFinanceRepository.Companion.get(): GoogleFinanceRepository {
            if (repo == null) {
                repo = ImplGoogleFinanceRepository()
            }
            return repo!!
        }
    }
}