package com.farmingdale.stockscreener.providers

import com.farmingdale.stockscreener.BuildConfig
import com.farmingdale.stockscreener.model.local.FullQuoteData
import com.farmingdale.stockscreener.model.local.GeneralSearchData
import com.farmingdale.stockscreener.model.local.GeneralSearchMatch
import com.farmingdale.stockscreener.model.local.SymbolData
import com.farmingdale.stockscreener.model.local.SymbolList
import com.farmingdale.stockscreener.model.remote.financialmodelprepResponses.FullQuoteResponse
import com.farmingdale.stockscreener.model.remote.financialmodelprepResponses.GeneralSearchResponse
import com.farmingdale.stockscreener.model.remote.financialmodelprepResponses.SymbolDataResponse
import com.farmingdale.stockscreener.providers.base.FinancialModelPrepAPI
import com.farmingdale.stockscreener.utils.FINANCIAL_MODEL_PREP_API_URL
import com.farmingdale.stockscreener.utils.executeAsync
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.InputStream

class ImplFinancialModelPrepAPI(private val client: OkHttpClient): FinancialModelPrepAPI {
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
            .build()
        val call = client.newCall(request)
        val response = call.executeAsync()
        return response.body!!.byteStream()
    }

    override suspend fun generalSearch(query: String): GeneralSearchData {
        val stream = getByteStream(
            FINANCIAL_MODEL_PREP_API_URL.newBuilder().apply {
                addPathSegments("search")
                addQueryParameter("query", query)
                addQueryParameter("apikey", BuildConfig.financialModelPrepAPI)
            }.build()
        )
        val searchMatches: List<GeneralSearchResponse>

        try{
            searchMatches = parser.decodeFromStream(ListSerializer(GeneralSearchResponse.serializer()), stream)
        } catch (e: SerializationException) {
            throw RuntimeException("Failed to parse JSON response", e)
        }
        return GeneralSearchData(
            searchMatches.map { response ->
                GeneralSearchMatch(
                    symbol = response.symbol,
                    name = response.name,
                    currency = response.currency,
                    stockExchange = response.stockExchange,
                    exchangeShortName = response.exchangeShortName
                )
            }
        )
    }

    override suspend fun getSymbolList(): SymbolList {
        val stream = getByteStream(
            FINANCIAL_MODEL_PREP_API_URL.newBuilder().apply {
                addPathSegments("stock")
                addPathSegments("list")
                addQueryParameter("apikey", BuildConfig.financialModelPrepAPI)
            }.build()
        )
        val symbolDataList: List<SymbolDataResponse>

        try {
            symbolDataList = parser.decodeFromStream(ListSerializer(SymbolDataResponse.serializer()), stream)
        } catch (e: SerializationException) {
            throw RuntimeException("Failed to parse JSON response", e)
        }

        return SymbolList(
            symbolDataList.map {response ->
                SymbolData(
                    symbol = response.symbol,
                    name = response.name,
                    price = response.price,
                    exchange = response.exchange,
                    exchangeShortName = response.exchangeShortName,
                    type = response.type
                )
            }
        )
    }

    override suspend fun getFullQuote(symbol: String): FullQuoteData {
        val stream = getByteStream(
            FINANCIAL_MODEL_PREP_API_URL.newBuilder().apply {
                addPathSegments("quote")
                addPathSegments(symbol)
                addQueryParameter("apikey", BuildConfig.financialModelPrepAPI)
            }.build()
        )
        val fullQuoteResponse: List<FullQuoteResponse>

        try{
            fullQuoteResponse = parser.decodeFromStream(ListSerializer(FullQuoteResponse.serializer()), stream)
        } catch (e: SerializationException) {
            throw RuntimeException("Failed to parse JSON response", e)
        }

        return fullQuoteResponse.map { response ->
            FullQuoteData(
                symbol = response.symbol,
                name = response.name,
                price = response.price,
                changesPercentage = response.changesPercentage,
                change = response.change,
                dayLow = response.dayLow,
                dayHigh = response.dayHigh,
                yearHigh = response.yearHigh,
                yearLow = response.yearLow,
                marketCap = response.marketCap,
                priceAvg50 = response.priceAvg50,
                priceAvg200 = response.priceAvg200,
                exchange = response.exchange,
                volume = response.volume,
                avgVolume = response.avgVolume,
                open = response.open,
                previousClose = response.previousClose,
                eps = response.eps,
                pe = response.pe,
                earningsAnnouncement = response.earningsAnnouncement,
                sharesOutstanding = response.sharesOutstanding,
                timestamp = response.timestamp
            )
        }.first()

    }
}