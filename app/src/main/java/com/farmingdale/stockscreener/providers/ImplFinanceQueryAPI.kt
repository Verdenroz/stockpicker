package com.farmingdale.stockscreener.providers

import com.farmingdale.stockscreener.BuildConfig
import com.farmingdale.stockscreener.model.local.FullQuoteData
import com.farmingdale.stockscreener.model.local.HistoricalData
import com.farmingdale.stockscreener.model.local.Interval
import com.farmingdale.stockscreener.model.local.MarketSector
import com.farmingdale.stockscreener.model.local.News
import com.farmingdale.stockscreener.model.local.SimpleQuoteData
import com.farmingdale.stockscreener.model.local.TimePeriod
import com.farmingdale.stockscreener.model.local.TimeSeriesData
import com.farmingdale.stockscreener.model.local.MarketIndex
import com.farmingdale.stockscreener.model.local.MarketMover
import com.farmingdale.stockscreener.model.remote.SectorResponse
import com.farmingdale.stockscreener.model.remote.TimeSeriesResponse
import com.farmingdale.stockscreener.model.remote.FullQuoteResponse
import com.farmingdale.stockscreener.model.remote.SimpleQuoteResponse
import com.farmingdale.stockscreener.model.remote.IndexResponse
import com.farmingdale.stockscreener.model.remote.MarketMoverResponse
import com.farmingdale.stockscreener.model.remote.NewsResponse
import com.farmingdale.stockscreener.providers.base.FinanceQueryAPI
import com.farmingdale.stockscreener.utils.FINANCE_QUERY_API_URL
import com.farmingdale.stockscreener.utils.executeAsync
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.InputStream

@OptIn(ExperimentalSerializationApi::class)
class ImplFinanceQueryAPI(private val client: OkHttpClient) : FinanceQueryAPI {

    companion object {
        private val parser: Json by lazy {
            Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            }
        }
    }

    private suspend fun getByteStream(url: HttpUrl): InputStream {
        val request = Request.Builder()
            .url(url)
            .addHeader("x-api-key", BuildConfig.financeQueryAPIKey)
            .build()
        val call = client.newCall(request)
        val response = call.executeAsync()
        return response.body!!.byteStream()
    }

    override suspend fun getQuote(symbol: String): FullQuoteData {
        val stream = getByteStream(
            FINANCE_QUERY_API_URL.newBuilder().apply {
                addPathSegments("quotes")
                addQueryParameter("symbols", symbol)
            }.build()
        )
        val quoteResponseList: List<FullQuoteResponse>

        try {
            quoteResponseList =
                parser.decodeFromStream(ListSerializer(FullQuoteResponse.serializer()), stream)
        } catch (e: SerializationException) {
            throw RuntimeException("Failed to parse JSON response", e)
        }
        return quoteResponseList.map {
            FullQuoteData(
                symbol = it.symbol,
                name = it.name,
                price = it.price,
                postMarketPrice = it.after_hours_price,
                change = it.change,
                percentChange = it.percent_change,
                open = it.open,
                high = it.high,
                low = it.low,
                yearHigh = it.year_high,
                yearLow = it.year_low,
                volume = it.volume,
                avgVolume = it.avg_volume,
                marketCap = it.market_cap,
                beta = it.beta,
                eps = it.eps,
                pe = it.pe,
                dividend = it.dividend,
                exDividend = it.ex_dividend,
                earningsDate = it.earnings_date,
                sector = it.sector,
                industry = it.industry,
                about = it.about,
                logo = it.logo
            )
        }.first()
    }

    override suspend fun getSimpleQuote(symbol: String): SimpleQuoteData {
        val stream = getByteStream(
            FINANCE_QUERY_API_URL.newBuilder().apply {
                addPathSegments("simple-quotes")
                addQueryParameter("symbols", symbol)
            }.build()
        )
        val quoteResponseList: List<SimpleQuoteResponse>

        try {
            quoteResponseList =
                parser.decodeFromStream(ListSerializer(SimpleQuoteResponse.serializer()), stream)
        } catch (e: SerializationException) {
            throw RuntimeException("Failed to parse JSON response", e)
        }
        return quoteResponseList.map {
            SimpleQuoteData(
                symbol = it.symbol,
                name = it.name,
                price = it.price,
                change = it.change,
                percentChange = it.percent_change
            )
        }.first()
    }

    override suspend fun getBulkQuote(symbols: List<String>): List<SimpleQuoteData> {
        val symbolList = symbols.joinToString(",")
        val stream = getByteStream(
            FINANCE_QUERY_API_URL.newBuilder().apply {
                addPathSegments("quotes")
                addQueryParameter("symbols", symbolList)
            }.build()
        )
        val quoteResponseList: List<SimpleQuoteResponse>

        try {
            quoteResponseList =
                parser.decodeFromStream(ListSerializer(SimpleQuoteResponse.serializer()), stream)
        } catch (e: SerializationException) {
            throw RuntimeException("Failed to parse JSON response", e)
        }
        return quoteResponseList.map {
            SimpleQuoteData(
                symbol = it.symbol,
                name = it.name,
                price = it.price,
                change = it.change,
                percentChange = it.percent_change
            )
        }

    }

    override suspend fun getHistoricalData(
        symbol: String,
        time: TimePeriod,
        interval: Interval
    ): TimeSeriesData {
        val stream = getByteStream(
            FINANCE_QUERY_API_URL.newBuilder().apply {
                addPathSegments("historical")
                addQueryParameter("symbol", symbol)
                addQueryParameter("time", time.value)
                addQueryParameter("interval", interval.value)
            }.build()
        )

        val timeSeriesResponse: TimeSeriesResponse

        try {
            timeSeriesResponse = parser.decodeFromStream(TimeSeriesResponse.serializer(), stream)
        } catch (e: SerializationException) {
            throw RuntimeException("Failed to parse JSON response", e)
        }

        return TimeSeriesData(
            data = timeSeriesResponse.data.mapValues {
                HistoricalData(
                    open = it.value.open,
                    high = it.value.high,
                    low = it.value.low,
                    close = it.value.adj_close,
                    volume = it.value.volume
                )
            }
        )
    }

    override suspend fun getIndices(): List<MarketIndex> {
        val stream = getByteStream(
            FINANCE_QUERY_API_URL.newBuilder().apply {
                addPathSegments("indices")
            }.build()
        )
        val indexResponseList: List<IndexResponse>

        try {
            indexResponseList =
                parser.decodeFromStream(ListSerializer(IndexResponse.serializer()), stream)
        } catch (e: SerializationException) {
            throw RuntimeException("Failed to parse JSON response", e)
        }
        return indexResponseList.map {
            MarketIndex(
                name = it.name,
                value = it.value,
                change = it.change,
                percentChange = it.percent_change
            )
        }
    }

    override suspend fun getSectors(): List<MarketSector> {
        val stream = getByteStream(
            FINANCE_QUERY_API_URL.newBuilder().apply {
                addPathSegments("sectors")
            }.build()
        )
        val sectorResponseList: List<SectorResponse>

        try {
            sectorResponseList =
                parser.decodeFromStream(ListSerializer(SectorResponse.serializer()), stream)
        } catch (e: SerializationException) {
            throw RuntimeException("Failed to parse JSON response", e)
        }
        return sectorResponseList.map {
            MarketSector(
                sector = it.sector,
                dayReturn = it.day_return,
                ytdReturn = it.ytd_return,
                yearReturn = it.year_return,
                threeYearReturn = it.three_year_return,
                fiveYearReturn = it.five_year_return
            )
        }
    }

    override suspend fun getActives(): List<MarketMover> {
        val stream = getByteStream(
            FINANCE_QUERY_API_URL.newBuilder().apply {
                addPathSegments("actives")
            }.build()
        )
        val marketMoverList: List<MarketMoverResponse>

        try {
            marketMoverList =
                parser.decodeFromStream(ListSerializer(MarketMoverResponse.serializer()), stream)
        } catch (e: SerializationException) {
            throw RuntimeException("Failed to parse JSON response", e)
        }
        return marketMoverList.map {
            MarketMover(
                symbol = it.symbol,
                name = it.name,
                price = it.price,
                change = it.change,
                percentChange = it.percent_change
            )
        }
    }

    override suspend fun getGainers(): List<MarketMover> {
        val stream = getByteStream(
            FINANCE_QUERY_API_URL.newBuilder().apply {
                addPathSegments("gainers")
            }.build()
        )

        val marketMoverList: List<MarketMoverResponse>

        try {
            marketMoverList =
                parser.decodeFromStream(ListSerializer(MarketMoverResponse.serializer()), stream)
        } catch (e: SerializationException) {
            throw RuntimeException("Failed to parse JSON response", e)
        }

        return marketMoverList.map {
            MarketMover(
                symbol = it.symbol,
                name = it.name,
                price = it.price,
                change = it.change,
                percentChange = it.percent_change
            )
        }
    }

    override suspend fun getLosers(): List<MarketMover> {
        val stream = getByteStream(
            FINANCE_QUERY_API_URL.newBuilder().apply {
                addPathSegments("losers")
            }.build()
        )

        val marketMoverList: List<MarketMoverResponse>

        try {
            marketMoverList =
                parser.decodeFromStream(ListSerializer(MarketMoverResponse.serializer()), stream)
        } catch (e: SerializationException) {
            throw RuntimeException("Failed to parse JSON response", e)
        }

        return marketMoverList.map {
            MarketMover(
                symbol = it.symbol,
                name = it.name,
                price = it.price,
                change = it.change,
                percentChange = it.percent_change
            )
        }
    }

    override suspend fun getNews(): List<News> {
        val stream = getByteStream(
            FINANCE_QUERY_API_URL.newBuilder().apply {
                addPathSegments("news")
            }.build()
        )

        val newsList: List<NewsResponse>

        try {
            newsList =
                parser.decodeFromStream(ListSerializer(NewsResponse.serializer()), stream)
        } catch (e: SerializationException) {
            throw RuntimeException("Failed to parse JSON response", e)
        }

        return newsList.shuffled().map {
            News(
                title = it.title,
                link = it.link,
                source = it.source,
                img = it.img,
                time = it.time
            )
        }
    }

    override suspend fun getNewsForSymbol(symbol: String): List<News> {
        val stream = getByteStream(
            FINANCE_QUERY_API_URL.newBuilder().apply {
                addPathSegments("news")
                addQueryParameter("symbol", symbol)
            }.build()
        )

        val newsList: List<NewsResponse>

        try {
            newsList =
                parser.decodeFromStream(ListSerializer(NewsResponse.serializer()), stream)
        } catch (e: SerializationException) {
            throw RuntimeException("Failed to parse JSON response", e)
        }

        return newsList.map {
            News(
                title = it.title,
                link = it.link,
                source = it.source,
                img = it.img,
                time = it.time
            )
        }
    }

    override suspend fun getSimilarSymbols(symbol: String): List<SimpleQuoteData> {
        val stream = getByteStream(
            FINANCE_QUERY_API_URL.newBuilder().apply {
                addPathSegments("similar-stocks")
                addQueryParameter("symbol", symbol)
            }.build()
        )

        val quoteResponseList: List<SimpleQuoteResponse>

        try {
            quoteResponseList =
                parser.decodeFromStream(ListSerializer(SimpleQuoteResponse.serializer()), stream)
        } catch (e: SerializationException) {
            throw RuntimeException("Failed to parse JSON response", e)
        }

        return quoteResponseList.map {
            SimpleQuoteData(
                symbol = it.symbol,
                name = it.name,
                price = it.price,
                change = it.change,
                percentChange = it.percent_change
            )
        }
    }

    override suspend fun getTechnicalIndicator(symbol: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getSummaryAnalysis(symbol: String) {
        TODO("Not yet implemented")
    }
}