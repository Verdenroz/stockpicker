package com.farmingdale.stockscreener.providers

import com.farmingdale.stockscreener.BuildConfig
import com.farmingdale.stockscreener.model.local.AnalysisType
import com.farmingdale.stockscreener.model.remote.AnalysisDataResponse
import com.farmingdale.stockscreener.model.remote.QuoteData
import com.farmingdale.stockscreener.model.remote.QuoteDataResponse
import com.farmingdale.stockscreener.model.remote.TechnicalAnalysis
import com.farmingdale.stockscreener.model.utils.ALPHA_VANTAGE_API_URL
import com.farmingdale.stockscreener.model.utils.executeAsync
import com.farmingdale.stockscreener.providers.base.AlphaVantageAPI
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.InputStream

class ImplAlphaVantageAPI(private val client: OkHttpClient): AlphaVantageAPI {
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
            .build()
        val call = client.newCall(request)
        val response = call.executeAsync()
        return response.body!!.byteStream()
    }
    override suspend fun getQuote(symbol: String): QuoteData {
        val stream = getByteStream(
            ALPHA_VANTAGE_API_URL.newBuilder().apply {
                addPathSegments("query")
                addQueryParameter("function", "GLOBAL_QUOTE")
                addQueryParameter("symbol", symbol)
                addQueryParameter("apikey", BuildConfig.alphaVantageAPI)
            }.build()
        )

        val quoteDataResponse: QuoteDataResponse

        try {
            quoteDataResponse = parser.decodeFromStream(QuoteDataResponse.serializer(), stream)
        } catch (e: SerializationException) {
            throw RuntimeException("Failed to parse JSON response", e)
        }

        return with(quoteDataResponse.globalQuote) {
            QuoteData(
                symbol = this.symbol,
                open = this.open,
                high = this.high,
                low = this.low,
                price = this.price,
                volume = this.volume,
                latestTradingDay = this.latestTradingDay,
                previousClose = this.previousClose,
                change = this.change,
                changePercent = this.changePercent
            )
        }
    }

    override suspend fun getTechnicalAnalysis(
        function: AnalysisType,
        symbol: String,
        interval: String,
        timePeriod: Int,
        seriesType: String
    ): TechnicalAnalysis {
        val stream = getByteStream(
            ALPHA_VANTAGE_API_URL.newBuilder().apply {
                addPathSegments("query")
                addQueryParameter("function", function.name)
                addQueryParameter("symbol", symbol)
                addQueryParameter("interval", interval)
                addQueryParameter("time_period", timePeriod.toString())
                addQueryParameter("series_type", seriesType)
                addQueryParameter("apikey", BuildConfig.alphaVantageAPI)
            }.build()
        )

        val rsiResponse: RSIDataResponse = parser.decodeFromStream(RSIDataResponse.serializer(), stream)

        val analyses = rsiResponse.technicalAnalysis.map { (date, analysis) ->
            TechnicalAnalysis(
                date = date,
                value = analysis.RSI
            )
        }

        return TechnicalAnalysisHistory(analyses)
    }

    override suspend fun getADX(
        function: AnalysisType,
        symbol: String,
        interval: String,
        timePeriod: Int
    ): TechnicalAnalysisHistory {
        TODO("Not yet implemented")
    }

    override suspend fun getCCI(
        function: AnalysisType,
        symbol: String,
        interval: String,
        timePeriod: Int
    ): TechnicalAnalysisHistory {
        TODO("Not yet implemented")
    }

    override suspend fun getAROON(
        function: AnalysisType,
        symbol: String,
        interval: String,
        timePeriod: Int
    ): TechnicalAnalysisHistory {
        TODO("Not yet implemented")
    }

    override suspend fun getBBANDS(
        function: AnalysisType,
        symbol: String,
        interval: String,
        timePeriod: Int,
        seriesType: String,
        nbDevUp: Int?,
        nbDevDown: Int?,
        matype: Int?
    ): TechnicalAnalysisHistory {
        TODO("Not yet implemented")
    }

        val mostRecentRSIEntry = rsiResponse.technicalAnalysis.entries.first()

        return TechnicalAnalysis(
            date = mostRecentRSIEntry.key,
            value = mostRecentRSIEntry.value.analysis
        )
    }

}