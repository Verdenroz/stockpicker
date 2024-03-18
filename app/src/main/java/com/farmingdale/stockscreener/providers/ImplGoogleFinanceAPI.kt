package com.farmingdale.stockscreener.providers

import com.farmingdale.stockscreener.model.local.UnitedStatesExchanges
import com.farmingdale.stockscreener.model.local.googlefinance.GoogleFinanceNews
import com.farmingdale.stockscreener.model.local.googlefinance.GoogleFinanceStock
import com.farmingdale.stockscreener.model.local.googlefinance.MarketIndex
import com.farmingdale.stockscreener.model.remote.googlefinanceResponses.GoogleFinanceIndexResponse
import com.farmingdale.stockscreener.model.remote.googlefinanceResponses.GoogleFinanceNewsResponse
import com.farmingdale.stockscreener.model.remote.googlefinanceResponses.GoogleFinanceStockResponse
import com.farmingdale.stockscreener.providers.base.GoogleFinanceAPI
import com.farmingdale.stockscreener.utils.GOOGLE_API_URL
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
import java.net.URL

@OptIn(ExperimentalSerializationApi::class)
class ImplGoogleFinanceAPI(private val client: OkHttpClient) : GoogleFinanceAPI {
    companion object {
        private val parser: Json by lazy {
            Json {
                ignoreUnknownKeys = true
            }
        }
    }

    private suspend fun getByteStream(url: HttpUrl): InputStream {
        val request = Request.Builder()
            .url(url)
            .get()
            .build()
        val call = client.newCall(request)
        val response = call.executeAsync()
        return response.body!!.byteStream()
    }

    override suspend fun getIndices(): List<MarketIndex> {
        val stream = getByteStream(
            GOOGLE_API_URL.newBuilder().apply {
                addPathSegment("indices")
                addQueryParameter("region", "americas")
                addQueryParameter("country", "US")
            }.build()
        )
        val indexResponse: List<GoogleFinanceIndexResponse>
        try {
            indexResponse = parser.decodeFromStream(
                ListSerializer(GoogleFinanceIndexResponse.serializer()),
                stream
            )
        } catch (e: SerializationException) {
            throw RuntimeException("Failed to parse JSON response", e)
        }
        return indexResponse.map {
            MarketIndex(
                name = it.name,
                score = it.score,
                change = it.change,
                percentChange = it.percentChange
            )
        }
    }

    override suspend fun getActiveStocks(): List<GoogleFinanceStock> {
        val stream = getByteStream(
            GOOGLE_API_URL.newBuilder().apply {
                addPathSegment("active")
            }.build()
        )
        val activeStocks: List<GoogleFinanceStockResponse>
        try {
            activeStocks = parser.decodeFromStream(
                ListSerializer(GoogleFinanceStockResponse.serializer()),
                stream
            )
        } catch (e: SerializationException) {
            throw RuntimeException("Failed to parse JSON response", e)
        }

        return activeStocks.map {
            GoogleFinanceStock(
                symbol = it.symbol,
                name = it.name,
                current = it.current,
                change = it.change,
                percentChange = it.percentChange
            )
        }
    }

    override suspend fun getGainers(): List<GoogleFinanceStock> {
        val stream = getByteStream(
            GOOGLE_API_URL.newBuilder().apply {
                addPathSegment("gainers")
            }.build()
        )
        val gainers: List<GoogleFinanceStockResponse>
        try {
            gainers = parser.decodeFromStream(
                ListSerializer(GoogleFinanceStockResponse.serializer()),
                stream
            )
        } catch (e: SerializationException) {
            throw RuntimeException("Failed to parse JSON response", e)
        }
        return gainers.map {
            GoogleFinanceStock(
                symbol = it.symbol,
                name = it.name,
                current = it.current,
                change = it.change,
                percentChange = it.percentChange
            )
        }
    }

    override suspend fun getLosers(): List<GoogleFinanceStock> {
        val stream = getByteStream(
            GOOGLE_API_URL.newBuilder().apply {
                addPathSegment("losers")
            }.build()
        )
        val losers: List<GoogleFinanceStockResponse>
        try {
            losers = parser.decodeFromStream(
                ListSerializer(GoogleFinanceStockResponse.serializer()),
                stream
            )
        } catch (e: SerializationException) {
            throw RuntimeException("Failed to parse JSON response", e)
        }
        return losers.map {
            GoogleFinanceStock(
                symbol = it.symbol,
                name = it.name,
                current = it.current,
                change = it.change,
                percentChange = it.percentChange
            )
        }
    }

    override suspend fun getRelatedNews(
        symbol: String,
        exchange: UnitedStatesExchanges
    ): List<GoogleFinanceNews> {
        val stream = getByteStream(
            GOOGLE_API_URL.newBuilder().apply {
                addPathSegments("news")
                addQueryParameter("symbol", symbol)
                addQueryParameter("exchange", exchange.name)
            }.build()
        )
        val news: List<GoogleFinanceNewsResponse>
        try {
            news = parser.decodeFromStream(
                ListSerializer(GoogleFinanceNewsResponse.serializer()),
                stream
            )
        } catch (e: SerializationException) {
            throw RuntimeException("Failed to parse JSON response", e)
        }
        return news.map {
            GoogleFinanceNews(
                headline = it.headline,
                image = URL(it.image),
                source = it.source,
                url = URL(it.url)
            )
        }
    }
}