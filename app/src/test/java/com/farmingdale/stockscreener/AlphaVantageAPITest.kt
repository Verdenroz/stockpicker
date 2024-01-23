package com.farmingdale.stockscreener

import com.farmingdale.stockscreener.model.local.AnalysisType
import com.farmingdale.stockscreener.model.local.Interval
import com.farmingdale.stockscreener.model.local.SeriesType
import com.farmingdale.stockscreener.providers.ImplAlphaVantageAPI
import com.farmingdale.stockscreener.providers.base.AlphaVantageAPI
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Before
import org.junit.Test

class AlphaVantageAPITest {

    private lateinit var api: AlphaVantageAPI

    @Before
    fun setup() {
        val client = OkHttpClient.Builder().addInterceptor {
            val request = it.request()
            println(request.url)
            it.proceed(request)
        }.build()

        api = ImplAlphaVantageAPI(client)
    }

    @Test
    fun getQuoteTest() {
        val info = runBlocking {
            api.getQuote("IBM")
        }
        println(info)
    }

    @Test
    fun getTechnicalAnalysisTest() {
        val info = runBlocking {
            api.getTechnicalAnalysis(
                function = AnalysisType.RSI,
                symbol = "IBM",
                interval = Interval.DAILY.value,
                timePeriod = 60,
                seriesType = SeriesType.CLOSE.value
            )
        }
        println(info)
    }
}