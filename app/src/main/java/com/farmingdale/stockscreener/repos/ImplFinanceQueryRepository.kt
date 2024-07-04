package com.farmingdale.stockscreener.repos

import android.util.Log
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
import com.farmingdale.stockscreener.utils.DataError
import com.farmingdale.stockscreener.utils.DataException
import com.farmingdale.stockscreener.utils.HttpException
import com.farmingdale.stockscreener.utils.NetworkException
import com.farmingdale.stockscreener.utils.Resource
import kotlinx.collections.immutable.ImmutableList
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
import kotlinx.serialization.SerializationException

@Suppress("RemoveExplicitTypeArguments")
class ImplFinanceQueryRepository : FinanceQueryRepository() {
    private val api = ImplFinanceQueryAPI(okHttpClient)
    private val indicesChannel =
        Channel<Resource<ImmutableList<MarketIndex>, DataError.Network>>(Channel.CONFLATED)
    private val activesChannel =
        Channel<Resource<ImmutableList<MarketMover>, DataError.Network>>(Channel.CONFLATED)
    private val losersChannel =
        Channel<Resource<ImmutableList<MarketMover>, DataError.Network>>(Channel.CONFLATED)
    private val gainersChannel =
        Channel<Resource<ImmutableList<MarketMover>, DataError.Network>>(Channel.CONFLATED)
    private val headlinesChannel =
        Channel<Resource<ImmutableList<News>, DataError.Network>>(Channel.CONFLATED)
    private val sectorsChannel =
        Channel<Resource<ImmutableList<MarketSector>, DataError.Network>>(Channel.CONFLATED)

    override val indices: Flow<Resource<ImmutableList<MarketIndex>, DataError.Network>> =
        indicesChannel.receiveAsFlow()
    override val actives: Flow<Resource<ImmutableList<MarketMover>, DataError.Network>> =
        activesChannel.receiveAsFlow()
    override val losers: Flow<Resource<ImmutableList<MarketMover>, DataError.Network>> =
        losersChannel.receiveAsFlow()
    override val gainers: Flow<Resource<ImmutableList<MarketMover>, DataError.Network>> =
        gainersChannel.receiveAsFlow()
    override val headlines: Flow<Resource<ImmutableList<News>, DataError.Network>> =
        headlinesChannel.receiveAsFlow()
    override val sectors: Flow<Resource<ImmutableList<MarketSector>, DataError.Network>> =
        sectorsChannel.receiveAsFlow()


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
        val indicesDeferred = async(Dispatchers.IO) { refreshIndices() }
        val activesDeferred = async(Dispatchers.IO) { refreshActives() }
        val losersDeferred = async(Dispatchers.IO) { refreshLosers() }
        val gainersDeferred = async(Dispatchers.IO) { refreshGainers() }

        indicesDeferred.await()
        activesDeferred.await()
        losersDeferred.await()
        gainersDeferred.await()
    }

    private suspend fun refreshIndices() = coroutineScope {
        try {
            val indices = api.getIndices()
            indicesChannel.send(Resource.Success(indices))
        } catch (e: DataException) {
            when (e) {
                is HttpException -> {
                    when (e.code) {
                        400 -> indicesChannel.send(Resource.Error(DataError.Network.BAD_REQUEST))
                        401, 403 -> indicesChannel.send(Resource.Error(DataError.Network.DENIED))
                        404 -> indicesChannel.send(Resource.Error(DataError.Network.NOT_FOUND))
                        408 -> indicesChannel.send(Resource.Error(DataError.Network.TIMEOUT))
                        429 -> indicesChannel.send(Resource.Error(DataError.Network.THROTTLED))
                        500, 504 -> indicesChannel.send(Resource.Error(DataError.Network.SERVER_DOWN))
                        else -> indicesChannel.send(Resource.Error(DataError.Network.UNKNOWN))
                    }
                }

                is NetworkException -> indicesChannel.send(Resource.Error(DataError.Network.NO_INTERNET))
            }
        } catch (e: SerializationException) {
            indicesChannel.send(Resource.Error(DataError.Network.SERIALIZATION))
        } catch (e: Exception) {
            indicesChannel.send(Resource.Error(DataError.Network.UNKNOWN))
        }
    }

    private suspend fun refreshActives() = coroutineScope {
        try {
            val actives = api.getActives()
            activesChannel.send(Resource.Success(actives))
        } catch (e: DataException) {
            when (e) {
                is HttpException -> {
                    when (e.code) {
                        400 -> activesChannel.send(Resource.Error(DataError.Network.BAD_REQUEST))
                        401, 403 -> activesChannel.send(Resource.Error(DataError.Network.DENIED))
                        404 -> activesChannel.send(Resource.Error(DataError.Network.NOT_FOUND))
                        408 -> activesChannel.send(Resource.Error(DataError.Network.TIMEOUT))
                        429 -> activesChannel.send(Resource.Error(DataError.Network.THROTTLED))
                        500, 504 -> activesChannel.send(Resource.Error(DataError.Network.SERVER_DOWN))
                        else -> activesChannel.send(Resource.Error(DataError.Network.UNKNOWN))
                    }
                }

                is NetworkException -> activesChannel.send(Resource.Error(DataError.Network.NO_INTERNET))
            }
        } catch (e: SerializationException) {
            activesChannel.send(Resource.Error(DataError.Network.SERIALIZATION))
        } catch (e: Exception) {
            activesChannel.send(Resource.Error(DataError.Network.UNKNOWN))
        }
    }

    private suspend fun refreshLosers() = coroutineScope {
        try {
            val losers = api.getLosers()
            losersChannel.send(Resource.Success(losers))
        } catch (e: DataException) {
            when (e) {
                is HttpException -> {
                    when (e.code) {
                        400 -> losersChannel.send(Resource.Error(DataError.Network.BAD_REQUEST))
                        401, 403 -> losersChannel.send(Resource.Error(DataError.Network.DENIED))
                        404 -> losersChannel.send(Resource.Error(DataError.Network.NOT_FOUND))
                        408 -> losersChannel.send(Resource.Error(DataError.Network.TIMEOUT))
                        429 -> losersChannel.send(Resource.Error(DataError.Network.THROTTLED))
                        500, 504 -> losersChannel.send(Resource.Error(DataError.Network.SERVER_DOWN))
                        else -> losersChannel.send(Resource.Error(DataError.Network.UNKNOWN))
                    }
                }

                is NetworkException -> losersChannel.send(Resource.Error(DataError.Network.NO_INTERNET))
            }
        } catch (e: SerializationException) {
            losersChannel.send(Resource.Error(DataError.Network.SERIALIZATION))
        } catch (e: Exception) {
            losersChannel.send(Resource.Error(DataError.Network.UNKNOWN))
        }
    }

    private suspend fun refreshGainers() = coroutineScope {
        try {
            val gainers = api.getGainers()
            gainersChannel.send(Resource.Success(gainers))
        } catch (e: DataException) {
            when (e) {
                is HttpException -> {
                    when (e.code) {
                        400 -> gainersChannel.send(Resource.Error(DataError.Network.BAD_REQUEST))
                        401, 403 -> gainersChannel.send(Resource.Error(DataError.Network.DENIED))
                        404 -> gainersChannel.send(Resource.Error(DataError.Network.NOT_FOUND))
                        408 -> gainersChannel.send(Resource.Error(DataError.Network.TIMEOUT))
                        429 -> gainersChannel.send(Resource.Error(DataError.Network.THROTTLED))
                        500, 504 -> gainersChannel.send(Resource.Error(DataError.Network.SERVER_DOWN))
                        else -> gainersChannel.send(Resource.Error(DataError.Network.UNKNOWN))
                    }
                }

                is NetworkException -> gainersChannel.send(Resource.Error(DataError.Network.NO_INTERNET))
            }
        } catch (e: SerializationException) {
            gainersChannel.send(Resource.Error(DataError.Network.SERIALIZATION))
        } catch (e: Exception) {
            gainersChannel.send(Resource.Error(DataError.Network.UNKNOWN))
        }
    }

    override suspend fun refreshNews() {
        try {
            withContext(Dispatchers.IO) {
                val news = api.getNews()
                headlinesChannel.send(Resource.Success(news))
            }
        } catch (e: DataException) {
            when (e) {
                is HttpException -> {
                    when (e.code) {
                        400 -> headlinesChannel.send(Resource.Error(DataError.Network.BAD_REQUEST))
                        401, 403 -> headlinesChannel.send(Resource.Error(DataError.Network.DENIED))
                        404 -> headlinesChannel.send(Resource.Error(DataError.Network.NOT_FOUND))
                        408 -> headlinesChannel.send(Resource.Error(DataError.Network.TIMEOUT))
                        429 -> headlinesChannel.send(Resource.Error(DataError.Network.THROTTLED))
                        500, 504 -> headlinesChannel.send(Resource.Error(DataError.Network.SERVER_DOWN))
                        else -> headlinesChannel.send(Resource.Error(DataError.Network.UNKNOWN))
                    }
                }

                is NetworkException -> headlinesChannel.send(Resource.Error(DataError.Network.NO_INTERNET))
            }
        } catch (e: SerializationException) {
            headlinesChannel.send(Resource.Error(DataError.Network.SERIALIZATION))
        } catch (e: Exception) {
            headlinesChannel.send(Resource.Error(DataError.Network.UNKNOWN))
        }
    }

    override suspend fun refreshSectors() {
        try {
            withContext(Dispatchers.IO) {
                val sectors = api.getSectors()
                sectorsChannel.send(Resource.Success(sectors))
            }
        } catch (e: DataException) {
            when (e) {
                is HttpException -> {
                    when (e.code) {
                        400 -> sectorsChannel.send(Resource.Error(DataError.Network.BAD_REQUEST))
                        401, 403 -> sectorsChannel.send(Resource.Error(DataError.Network.DENIED))
                        404 -> sectorsChannel.send(Resource.Error(DataError.Network.NOT_FOUND))
                        408 -> sectorsChannel.send(Resource.Error(DataError.Network.TIMEOUT))
                        429 -> sectorsChannel.send(Resource.Error(DataError.Network.THROTTLED))
                        500, 504 -> sectorsChannel.send(Resource.Error(DataError.Network.SERVER_DOWN))
                        else -> sectorsChannel.send(Resource.Error(DataError.Network.UNKNOWN))
                    }
                }

                is NetworkException -> sectorsChannel.send(Resource.Error(DataError.Network.NO_INTERNET))
            }
        } catch (e: SerializationException) {
            sectorsChannel.send(Resource.Error(DataError.Network.SERIALIZATION))
        } catch (e: Exception) {
            sectorsChannel.send(Resource.Error(DataError.Network.UNKNOWN))
        }
    }

    override fun getFullQuote(symbol: String): Flow<Resource<FullQuoteData, DataError.Network>> =
        flow {
            try {
                val quote = api.getQuote(symbol)
                emit(Resource.Success<FullQuoteData, DataError.Network>(quote))
            } catch (e: DataException) {
                when (e) {
                    is HttpException -> {
                        when (e.code) {
                            400 -> emit(Resource.Error(DataError.Network.BAD_REQUEST))
                            401, 403 -> emit(Resource.Error(DataError.Network.DENIED))
                            404 -> emit(Resource.Error(DataError.Network.NOT_FOUND))
                            408 -> emit(Resource.Error(DataError.Network.TIMEOUT))
                            429 -> emit(Resource.Error(DataError.Network.THROTTLED))
                            500, 504 -> emit(Resource.Error(DataError.Network.SERVER_DOWN))
                            else -> emit(Resource.Error(DataError.Network.UNKNOWN))
                        }
                    }

                    is NetworkException -> emit(Resource.Error(DataError.Network.NO_INTERNET))
                }
            } catch (e: SerializationException) {
                emit(Resource.Error(DataError.Network.SERIALIZATION))
            } catch (e: Exception) {
                emit(Resource.Error(DataError.Network.UNKNOWN))
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun getSimpleQuote(symbol: String): Flow<Resource<SimpleQuoteData, DataError.Network>> =
        flow {
            try {
                val simpleQuote = api.getSimpleQuote(symbol)
                emit(Resource.Success<SimpleQuoteData, DataError.Network>(simpleQuote))
            } catch (e: DataException) {
                when (e) {
                    is HttpException -> {
                        when (e.code) {
                            400 -> emit(Resource.Error(DataError.Network.BAD_REQUEST))
                            401, 403 -> emit(Resource.Error(DataError.Network.DENIED))
                            404 -> emit(Resource.Error(DataError.Network.NOT_FOUND))
                            408 -> emit(Resource.Error(DataError.Network.TIMEOUT))
                            429 -> emit(Resource.Error(DataError.Network.THROTTLED))
                            500, 504 -> emit(Resource.Error(DataError.Network.SERVER_DOWN))
                            else -> emit(Resource.Error(DataError.Network.UNKNOWN))
                        }
                    }

                    is NetworkException -> emit(Resource.Error(DataError.Network.NO_INTERNET))
                }
            } catch (e: SerializationException) {
                emit(Resource.Error(DataError.Network.SERIALIZATION))
            } catch (e: Exception) {
                emit(Resource.Error(DataError.Network.UNKNOWN))
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun getBulkQuote(symbols: List<String>): Flow<Resource<List<SimpleQuoteData>, DataError.Network>> =
        flow {
            try {
                val quotes = api.getBulkQuote(symbols)
                emit(Resource.Success<List<SimpleQuoteData>, DataError.Network>(quotes))
            } catch (e: DataException) {
                when (e) {
                    is HttpException -> {
                        when (e.code) {
                            400 -> emit(Resource.Error(DataError.Network.BAD_REQUEST))
                            401, 403 -> emit(Resource.Error(DataError.Network.DENIED))
                            404 -> emit(Resource.Error(DataError.Network.NOT_FOUND))
                            408 -> emit(Resource.Error(DataError.Network.TIMEOUT))
                            429 -> emit(Resource.Error(DataError.Network.THROTTLED))
                            500, 504 -> emit(Resource.Error(DataError.Network.SERVER_DOWN))
                            else -> emit(Resource.Error(DataError.Network.UNKNOWN))
                        }
                    }

                    is NetworkException -> emit(Resource.Error(DataError.Network.NO_INTERNET))
                }
            } catch (e: SerializationException) {
                emit(Resource.Error(DataError.Network.SERIALIZATION))
            } catch (e: Exception) {
                emit(Resource.Error(DataError.Network.UNKNOWN))
            }
        }.flowOn(Dispatchers.IO)

    override fun getNewsForSymbol(symbol: String): Flow<Resource<List<News>, DataError.Network>> =
        flow {
            try {
                val news = api.getNewsForSymbol(symbol)
                emit(Resource.Success<List<News>, DataError.Network>(news))
            } catch (e: DataException) {
                when (e) {
                    is HttpException -> {
                        when (e.code) {
                            // If 404, it means there is no news for the symbol
                            404 -> emit(Resource.Success(emptyList()))

                            400 -> emit(Resource.Error(DataError.Network.BAD_REQUEST))
                            401, 403 -> emit(Resource.Error(DataError.Network.DENIED))
                            408 -> emit(Resource.Error(DataError.Network.TIMEOUT))
                            429 -> emit(Resource.Error(DataError.Network.THROTTLED))
                            500, 504 -> emit(Resource.Error(DataError.Network.SERVER_DOWN))
                            else -> emit(Resource.Error(DataError.Network.UNKNOWN))
                        }
                    }

                    is NetworkException -> emit(Resource.Error(DataError.Network.NO_INTERNET))
                }
            } catch (e: SerializationException) {
                emit(Resource.Error(DataError.Network.SERIALIZATION))
            } catch (e: Exception) {
                emit(Resource.Error(DataError.Network.UNKNOWN))
            }
        }.flowOn(Dispatchers.IO)

    override fun getSimilarStocks(symbol: String): Flow<Resource<List<SimpleQuoteData>, DataError.Network>> =
        flow {
            try {
                val similarStocks = api.getSimilarSymbols(symbol)
                emit(Resource.Success<List<SimpleQuoteData>, DataError.Network>(similarStocks))
            } catch (e: DataException) {
                when (e) {
                    is HttpException -> {
                        Log.d("ImplFinanceQueryRepository", "getSimilarStocks: ${e.code}")
                        when (e.code) {

                            // If 404, it means there are no similar stocks
                            404 -> emit(Resource.Success(emptyList()))

                            400 -> emit(Resource.Error(DataError.Network.BAD_REQUEST))
                            401, 403 -> emit(Resource.Error(DataError.Network.DENIED))
                            408 -> emit(Resource.Error(DataError.Network.TIMEOUT))
                            429 -> emit(Resource.Error(DataError.Network.THROTTLED))
                            500, 504 -> emit(Resource.Error(DataError.Network.SERVER_DOWN))
                            else -> emit(Resource.Error(DataError.Network.UNKNOWN))

                        }
                    }

                    is NetworkException -> emit(Resource.Error(DataError.Network.NO_INTERNET))
                }
            } catch (e: SerializationException) {
                emit(Resource.Error(DataError.Network.SERIALIZATION))
            } catch (e: Exception) {
                emit(Resource.Error(DataError.Network.UNKNOWN))
            }
        }.flowOn(Dispatchers.IO)

    override fun getSectorBySymbol(symbol: String): Flow<Resource<MarketSector?, DataError.Network>> =
        flow {
            try {
                val sector = api.getSectorBySymbol(symbol)
                emit(Resource.Success<MarketSector?, DataError.Network>(sector))
            } catch (e: DataException) {
                when (e) {
                    is HttpException -> {
                        when (e.code) {
                            // If 404, it means the symbol has no sector
                            404 -> emit(Resource.Success(null))

                            400 -> emit(Resource.Error(DataError.Network.BAD_REQUEST))
                            401, 403 -> emit(Resource.Error(DataError.Network.DENIED))
                            408 -> emit(Resource.Error(DataError.Network.TIMEOUT))
                            429 -> emit(Resource.Error(DataError.Network.THROTTLED))
                            500, 504 -> emit(Resource.Error(DataError.Network.SERVER_DOWN))
                            else -> emit(Resource.Error(DataError.Network.UNKNOWN))
                        }
                    }

                    is NetworkException -> emit(Resource.Error(DataError.Network.NO_INTERNET))
                }
            } catch (e: SerializationException) {
                emit(Resource.Error(DataError.Network.SERIALIZATION))
            } catch (e: Exception) {
                emit(Resource.Error(DataError.Network.UNKNOWN))
            }
        }

    override suspend fun getTimeSeries(
        symbol: String,
        timePeriod: TimePeriod,
        interval: Interval,
    ): Flow<Resource<Map<String, HistoricalData>, DataError.Network>> = flow {
        try {
            val timeSeries = api.getHistoricalData(symbol, timePeriod, interval)
            emit(Resource.Success<Map<String, HistoricalData>, DataError.Network>(timeSeries))
        } catch (e: DataException) {
            when (e) {
                is HttpException -> {
                    when (e.code) {
                        400 -> emit(Resource.Error(DataError.Network.BAD_REQUEST))
                        401, 403 -> emit(Resource.Error(DataError.Network.DENIED))
                        404 -> emit(Resource.Error(DataError.Network.NOT_FOUND))
                        408 -> emit(Resource.Error(DataError.Network.TIMEOUT))
                        429 -> emit(Resource.Error(DataError.Network.THROTTLED))
                        500, 504 -> emit(Resource.Error(DataError.Network.SERVER_DOWN))
                        else -> emit(Resource.Error(DataError.Network.UNKNOWN))
                    }
                }

                is NetworkException -> emit(Resource.Error(DataError.Network.NO_INTERNET))
            }
        } catch (e: SerializationException) {
            emit(Resource.Error(DataError.Network.SERIALIZATION))
        } catch (e: Exception) {
            emit(Resource.Error(DataError.Network.UNKNOWN))
        }
    }.flowOn(Dispatchers.IO)

    override fun getAnalysis(
        symbol: String,
        interval: Interval
    ): Flow<Resource<Analysis?, DataError.Network>> = flow {
        try {
            val analysis = api.getSummaryAnalysis(symbol, interval)
            emit(Resource.Success<Analysis?, DataError.Network>(analysis))
        } catch (e: DataException) {
            when (e) {
                is HttpException -> {
                    when (e.code) {
                        400 -> emit(Resource.Error(DataError.Network.BAD_REQUEST))
                        401, 403 -> emit(Resource.Error(DataError.Network.DENIED))
                        404 -> emit(Resource.Error(DataError.Network.NOT_FOUND))
                        408 -> emit(Resource.Error(DataError.Network.TIMEOUT))
                        429 -> emit(Resource.Error(DataError.Network.THROTTLED))
                        500, 504 -> emit(Resource.Error(DataError.Network.SERVER_DOWN))
                        else -> emit(Resource.Error(DataError.Network.UNKNOWN))
                    }
                }

                is NetworkException -> emit(Resource.Error(DataError.Network.NO_INTERNET))
            }
        } catch (e: SerializationException) {
            // Occurs when the API returns fields with null values
            emit(Resource.Success(null))
        } catch (e: Exception) {
            emit(Resource.Error(DataError.Network.UNKNOWN))
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