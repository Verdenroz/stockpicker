package com.farmingdale.stockscreener.providers

import com.farmingdale.stockscreener.BuildConfig
import com.farmingdale.stockscreener.model.local.googlefinance.StockIndex
import com.farmingdale.stockscreener.model.remote.googlefinanceResponses.StockIndexWrapper
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

class ImplGoogleFinanceAPI(private val client: OkHttpClient): GoogleFinanceAPI {

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
            .addHeader("X-RapidAPI-Key", BuildConfig.alphaVantageAPI)
            .addHeader("X-RapidAPI-Host", "alpha-vantage.p.rapidapi.com")
            .build()
        val call = client.newCall(request)
        val response = call.executeAsync()
        return response.body!!.byteStream()
    }
    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun getIndices(): List<StockIndex> {
        val stream = getByteStream(
            GOOGLE_API_URL.newBuilder().apply{
                addPathSegments("US")
                addPathSegment("indices")
            }.build()
        )
        val indexResponse: List<StockIndexWrapper>
        try{
            indexResponse = parser.decodeFromStream(ListSerializer(StockIndexWrapper.serializer()), stream)
        }
        catch (e: SerializationException){
            throw RuntimeException("Failed to parse JSON response", e)
        }
        return indexResponse.map {
            StockIndex(
                name = it.stockIndex.name,
                score = it.stockIndex.score,
                change = it.stockIndex.change,
                percentChange = it.stockIndex.percentChange
            )
        }
    }
}