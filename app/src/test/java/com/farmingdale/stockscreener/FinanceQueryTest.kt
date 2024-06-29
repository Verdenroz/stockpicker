package com.farmingdale.stockscreener

import com.farmingdale.stockscreener.model.local.Interval
import com.farmingdale.stockscreener.model.local.TimePeriod
import com.farmingdale.stockscreener.providers.ImplFinanceQueryAPI
import com.farmingdale.stockscreener.providers.base.FinanceQueryAPI
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Before
import org.junit.Test

class FinanceQueryTest {
    private lateinit var api: FinanceQueryAPI

    @Before
    fun setup() {
        val client = OkHttpClient.Builder().addInterceptor {
            val request = it.request()
            println(request.url)
            it.proceed(request)
        }.build()

        api = ImplFinanceQueryAPI(client)
    }

    @Test
    fun getQuote() {
        runBlocking {
            println(api.getQuote("AAPL"))
            println(api.getQuote("TQQQ"))
            println(api.getQuote("GTLOX"))
            println(api.getQuote("005930.KS"))
        }
    }

    @Test
    fun getSimpleQuote() {
        val info = runBlocking {
            api.getSimpleQuote("AAPL")
        }
        println(info)
    }

    @Test
    fun getBulkQuotes() {
        val info = runBlocking {
            api.getBulkQuote(listOf("AAPL", "GOOGL", "MSFT"))
        }
        println(info)
    }

    @Test
    fun getHistoricalData() {
         runBlocking {
            println(api.getHistoricalData("AAPL", time = TimePeriod.FIVE_DAY, interval = Interval.FIFTEEN_MINUTE))
            println(api.getHistoricalData("AAPL", time = TimePeriod.ONE_MONTH, interval = Interval.DAILY))
        }
    }

    @Test
    fun getIndices() {
        val info = runBlocking {
            api.getIndices()
        }
        println(info)
    }

    @Test
    fun getSectors() {
        val info = runBlocking {
            api.getSectors()
        }
        println(info)
    }

    @Test
    fun getGainers() {
        val info = runBlocking {
            api.getGainers()
        }
        println(info)
    }

    @Test
    fun getLosers() {
        val info = runBlocking {
            api.getLosers()
        }
        println(info)
    }

    @Test
    fun getActives() {
        val info = runBlocking {
            api.getActives()
        }
        println(info)
    }

    @Test
    fun getNews() {
        val info = runBlocking {
            api.getNews()
        }
        println(info)
    }

    @Test
    fun getNewsForSymbol() {
        val info = runBlocking {
            api.getNewsForSymbol("AAPL")
        }
        println(info)
    }

    @Test
    fun getSimilarSymbols() {
        val info = runBlocking {
            api.getSimilarSymbols("AAPL")
        }
        println(info)
    }

    @Test
    fun getAnalysis(){
        runBlocking {
            println(api.getSummaryAnalysis("AAPL"))
            println(api.getSummaryAnalysis("AAPL", Interval.FIFTEEN_MINUTE))
            println(api.getSummaryAnalysis("GOOGL", Interval.MONTHLY))
            println(api.getSummaryAnalysis("GOOGL", Interval.WEEKLY))
        }
    }
}