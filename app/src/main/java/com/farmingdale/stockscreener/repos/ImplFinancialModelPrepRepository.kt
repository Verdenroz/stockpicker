package com.farmingdale.stockscreener.repos

import android.content.Context
import com.farmingdale.stockscreener.model.database.AppDatabase
import com.farmingdale.stockscreener.model.database.DBQuoteData
import com.farmingdale.stockscreener.model.local.FullQuoteData
import com.farmingdale.stockscreener.model.local.GeneralSearchData
import com.farmingdale.stockscreener.model.local.WatchList
import com.farmingdale.stockscreener.providers.ImplFinancialModelPrepAPI
import com.farmingdale.stockscreener.providers.okHttpClient
import com.farmingdale.stockscreener.repos.base.FinancialModelPrepRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext


class ImplFinancialModelPrepRepository(application: Context) : FinancialModelPrepRepository() {
    private val api = ImplFinancialModelPrepAPI(okHttpClient)
    private val db = AppDatabase.get(application).quoteDao()

    override suspend fun generalSearch(query: String): Flow<GeneralSearchData> = flow {
        try{
            emit(api.generalSearch(query))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getFullQuote(symbol: String): Flow<FullQuoteData> = flow {
        try{
            emit(api.getFullQuote(symbol))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getWatchList(): Flow<WatchList> {
        return db.getAllFullQuoteDataFlow().map { it.toWatchList()
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun addToWatchList(symbol: String) {
        withContext(Dispatchers.IO) {
            val quote = api.getFullQuote(symbol)
            db.insert(quote.toDB())
        }
    }

    override suspend fun deleteFromWatchList(symbol: String) {
        withContext(Dispatchers.IO) {
            db.delete(symbol)
        }
    }

    override suspend fun updateWatchList() {
        val symbols = db.getAllFullQuoteDataFlow()
            .flowOn(Dispatchers.IO)
            .flatMapConcat { it.asFlow() }
            .map { it.symbol }
            .toList()

        val updatedQuotes = api.getBulkQuote(symbols)
        db.updateAll(updatedQuotes.quotes.map { it.toDB() })
    }

    private fun FullQuoteData.toDB() = DBQuoteData(
        symbol = symbol,
        name = name,
        price = price,
        changesPercentage = changesPercentage,
        change = change,
        dayLow = dayLow,
        dayHigh = dayHigh,
        yearHigh = yearHigh,
        yearLow = yearLow,
        marketCap = marketCap,
        priceAvg50 = priceAvg50,
        priceAvg200 = priceAvg200,
        volume = volume,
        avgVolume = avgVolume,
        exchange = exchange,
        open = open,
        previousClose = previousClose,
        eps = eps,
        pe = pe,
        earningsAnnouncement = earningsAnnouncement,
        sharesOutstanding = sharesOutstanding,
        timestamp = timestamp
    )

    private fun List<FullQuoteData>.toWatchList() = WatchList(this)

    companion object {
        private var repo: ImplFinancialModelPrepRepository? = null

        /**
         * Get the implementation of [ImplFinancialModelPrepRepository]
         */
        @Synchronized
        fun FinancialModelPrepRepository.Companion.get(
            context: Context
        ): FinancialModelPrepRepository {
            if (repo == null) {
                repo = ImplFinancialModelPrepRepository(context)
            }

            return repo!!
        }
    }
}