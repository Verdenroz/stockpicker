package com.farmingdale.stockscreener.repos

import android.content.Context
import com.farmingdale.stockscreener.model.database.AppDatabase
import com.farmingdale.stockscreener.model.database.DBQuoteData
import com.farmingdale.stockscreener.model.local.SimpleQuoteData
import com.farmingdale.stockscreener.providers.ImplFinanceQueryAPI
import com.farmingdale.stockscreener.providers.okHttpClient
import com.farmingdale.stockscreener.repos.base.WatchlistRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext

class ImplWatchlistRepository(application: Context): WatchlistRepository() {
    private val db = AppDatabase.get(application).quoteDao()
    private val api = ImplFinanceQueryAPI(okHttpClient)

    override val watchlist: Flow<List<SimpleQuoteData>> = db.getAllQuoteDataFlow().flowOn(Dispatchers.IO)
    init {
        refreshWatchListPeriodically()
    }
    private fun refreshWatchListPeriodically() = flow<Unit> {
        while (true) {
            refreshWatchList()
            delay(REFRESH_INTERVAL)
        }
    }.flowOn(Dispatchers.IO).launchIn(CoroutineScope(Dispatchers.IO))

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun refreshWatchList() {
        val symbols = db.getAllQuoteDataFlow()
            .flowOn(Dispatchers.IO)
            .flatMapConcat { it.asFlow() }
            .map { it.symbol }
            .toList()

        val updatedQuotes = api.getBulkQuote(symbols)
        db.updateAll(updatedQuotes.map { it.toDB() })
    }

    override suspend fun addToWatchList(symbol: String) {
        withContext(Dispatchers.IO) {
            val quote = api.getSimpleQuote(symbol)
            db.insert(quote.toDB())
        }
    }

    override suspend fun deleteFromWatchList(symbol: String) {
        withContext(Dispatchers.IO) {
            db.delete(symbol)
        }
    }

    override suspend fun clearWatchList() {
        withContext(Dispatchers.IO) {
            db.deleteAll()
        }
    }


    private fun SimpleQuoteData.toDB() = DBQuoteData(
        symbol = symbol,
        name = name,
        price = price,
        change = change,
        percentChange = percentChange
    )

    companion object {
        private var repo: ImplWatchlistRepository? = null

        @Synchronized
        fun WatchlistRepository.Companion.get(application: Context): WatchlistRepository {
            if (repo == null) {
                repo = ImplWatchlistRepository(application)
            }
            return repo!!
        }
    }
}