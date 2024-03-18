package com.farmingdale.stockscreener.repos

import com.farmingdale.stockscreener.model.local.UnitedStatesExchanges
import com.farmingdale.stockscreener.model.local.googlefinance.GoogleFinanceNews
import com.farmingdale.stockscreener.model.local.googlefinance.GoogleFinanceStock
import com.farmingdale.stockscreener.model.local.googlefinance.MarketIndex
import com.farmingdale.stockscreener.providers.ImplGoogleFinanceAPI
import com.farmingdale.stockscreener.providers.okHttpClient
import com.farmingdale.stockscreener.repos.base.GoogleFinanceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ImplGoogleFinanceRepository() : GoogleFinanceRepository() {
    private val api = ImplGoogleFinanceAPI(okHttpClient)
    override suspend fun getIndices(): Flow<List<MarketIndex>> = flow {
        try {
            emit(api.getIndices())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getActives(): Flow<List<GoogleFinanceStock>> = flow {
        try {
            emit(api.getActiveStocks())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getLosers(): Flow<List<GoogleFinanceStock>> = flow {
        try {
            emit(api.getLosers())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }.flowOn(Dispatchers.IO)


    override suspend fun getGainers(): Flow<List<GoogleFinanceStock>> = flow {
        try {
            emit(api.getGainers())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }.flowOn(Dispatchers.IO)

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