package com.farmingdale.stockscreener.repos

import android.util.Log
import com.farmingdale.stockscreener.model.local.UnitedStatesExchanges
import com.farmingdale.stockscreener.model.local.googlefinance.GoogleFinanceNews
import com.farmingdale.stockscreener.model.local.googlefinance.GoogleFinanceStock
import com.farmingdale.stockscreener.model.local.googlefinance.MarketIndex
import com.farmingdale.stockscreener.providers.ImplGoogleFinanceAPI
import com.farmingdale.stockscreener.providers.okHttpClient
import com.farmingdale.stockscreener.repos.base.GoogleFinanceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ImplGoogleFinanceRepository : GoogleFinanceRepository() {
    private val api = ImplGoogleFinanceAPI(okHttpClient)
    private val refreshInterval = 30000L

    private val indicesFlow = MutableStateFlow<List<MarketIndex>?>(null)
    private val activesFlow = MutableStateFlow<List<GoogleFinanceStock>?>(null)
    private val losersFlow = MutableStateFlow<List<GoogleFinanceStock>?>(null)
    private val gainersFlow = MutableStateFlow<List<GoogleFinanceStock>?>(null)

    override val indices: Flow<List<MarketIndex>?> = flow {
        while (true) {
            indicesFlow.value = api.getIndices()
            emit(indicesFlow.value)
            delay(refreshInterval)
        }
    }.flowOn(Dispatchers.IO)

    override val actives: Flow<List<GoogleFinanceStock>?> = flow {
        while (true) {
            activesFlow.value = api.getActiveStocks()
            emit(activesFlow.value)
            delay(refreshInterval)
        }
    }.flowOn(Dispatchers.IO)

    override val losers: Flow<List<GoogleFinanceStock>?> = flow {
        while (true) {
            losersFlow.value = api.getLosers()
            emit(losersFlow.value)
            delay(refreshInterval)
        }
    }.flowOn(Dispatchers.IO)

    override val gainers: Flow<List<GoogleFinanceStock>?> = flow {
        while (true) {
            gainersFlow.value = api.getGainers()
            emit(gainersFlow.value)
            delay(refreshInterval)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun refreshValues() {
        Log.d("ImplGoogleFinanceRepository", "Refreshing values")
        coroutineScope {
            val indicesDeferred = async(Dispatchers.IO) { api.getIndices() }
            val activesDeferred = async(Dispatchers.IO) { api.getActiveStocks() }
            val losersDeferred = async(Dispatchers.IO) { api.getLosers() }
            val gainersDeferred = async(Dispatchers.IO) { api.getGainers() }

            indicesFlow.value = indicesDeferred.await()
            activesFlow.value = activesDeferred.await()
            losersFlow.value = losersDeferred.await()
            gainersFlow.value = gainersDeferred.await()

            indicesFlow.emit(indicesFlow.value)
            activesFlow.emit(activesFlow.value)
            losersFlow.emit(losersFlow.value)
            gainersFlow.emit(gainersFlow.value)
        }
    }

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