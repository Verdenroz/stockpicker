package com.farmingdale.stockscreener.repos

import android.content.Context
import com.farmingdale.stockscreener.model.database.AppDatabase
import com.farmingdale.stockscreener.model.database.DBQuoteData
import com.farmingdale.stockscreener.model.local.SimpleQuoteData
import com.farmingdale.stockscreener.providers.ImplFinanceQueryAPI
import com.farmingdale.stockscreener.providers.okHttpClient
import com.farmingdale.stockscreener.repos.base.WatchlistRepository
import com.farmingdale.stockscreener.utils.DataError
import com.farmingdale.stockscreener.utils.DataException
import com.farmingdale.stockscreener.utils.HttpException
import com.farmingdale.stockscreener.utils.NetworkException
import com.farmingdale.stockscreener.utils.Resource
import com.farmingdale.stockscreener.utils.UnknownException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException

class ImplWatchlistRepository(
    application: Context,
) : WatchlistRepository() {
    private val db = AppDatabase.get(application).quoteDao()
    private val api = ImplFinanceQueryAPI(okHttpClient)

    override val watchlist: Flow<List<SimpleQuoteData>> =
        db.getAllQuoteDataFlow().flowOn(Dispatchers.IO)

    init {
        refreshWatchListPeriodically()
    }

    private fun refreshWatchListPeriodically() {
        flow<Unit> {
            while (true) {
                refreshWatchList()
                delay(refreshInterval)
            }
        }.flowOn(Dispatchers.IO).launchIn(CoroutineScope(Dispatchers.IO))
    }

    override suspend fun refreshWatchList(): Resource<Unit, DataError.Network> {
        return try {
            val symbols = db.getAllQuoteData().map { it.symbol }
            if (symbols.isNotEmpty()) {
                val updatedQuotes = api.getBulkQuote(symbols)
                db.updateAll(updatedQuotes.map { it.toDB() })
            }
            Resource.Success(Unit)
        } catch (e: DataException) {
            when (e) {
                is HttpException -> {
                    when (e.code) {
                        400 -> Resource.Error(DataError.Network.BAD_REQUEST)
                        401, 403 -> Resource.Error(DataError.Network.DENIED)
                        404 -> Resource.Error(DataError.Network.NOT_FOUND)
                        408 -> Resource.Error(DataError.Network.TIMEOUT)
                        429 -> Resource.Error(DataError.Network.THROTTLED)
                        500, 504 -> Resource.Error(DataError.Network.SERVER_DOWN)
                        else -> Resource.Error(DataError.Network.UNKNOWN)
                    }
                }
                is NetworkException -> Resource.Error(DataError.Network.NO_INTERNET)
                is UnknownException -> Resource.Error(DataError.Network.UNKNOWN)
            }
        } catch (e: SerializationException) {
            Resource.Error(DataError.Network.SERIALIZATION)
        }
    }

    override suspend fun addToWatchList(symbol: String): Resource<Unit, DataError.Network> {
        return try {
            val quote = api.getSimpleQuote(symbol)
            db.insert(quote.toDB())
            return Resource.Success(Unit)
        } catch (e: DataException) {
            when (e) {
                is HttpException -> {
                    when (e.code) {
                        400 -> Resource.Error(DataError.Network.BAD_REQUEST)
                        401, 403 -> Resource.Error(DataError.Network.DENIED)
                        404 -> Resource.Error(DataError.Network.NOT_FOUND)
                        408 -> Resource.Error(DataError.Network.TIMEOUT)
                        429 -> Resource.Error(DataError.Network.THROTTLED)
                        500, 504 -> Resource.Error(DataError.Network.SERVER_DOWN)
                        else -> Resource.Error(DataError.Network.UNKNOWN)
                    }
                }

                is NetworkException -> Resource.Error(DataError.Network.NO_INTERNET)
                is UnknownException -> Resource.Error(DataError.Network.UNKNOWN)
            }
        } catch (e: SerializationException) {
            Resource.Error(DataError.Network.SERIALIZATION)
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
        fun WatchlistRepository.Companion.get(
            application: Context,
        ): WatchlistRepository {
            if (repo == null) {
                repo = ImplWatchlistRepository(application)
            }
            return repo!!
        }
    }
}