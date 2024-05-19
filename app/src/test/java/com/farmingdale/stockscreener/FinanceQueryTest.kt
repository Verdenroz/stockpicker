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
        val info = runBlocking {
            api.getQuote("AAPL")
        }
        println(info)
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
        val info = runBlocking {
            api.getHistoricalData("AAPL", time = TimePeriod.THREE_MONTH, interval = Interval.DAILY)
        }
        println(info)
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
}