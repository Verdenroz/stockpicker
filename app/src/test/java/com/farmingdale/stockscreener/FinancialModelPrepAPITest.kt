package com.farmingdale.stockscreener

import com.farmingdale.stockscreener.providers.ImplFinancialModelPrepAPI
import com.farmingdale.stockscreener.providers.base.FinancialModelPrepAPI
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Before
import org.junit.Test

class FinancialModelPrepAPITest {
    private lateinit var api: FinancialModelPrepAPI

    @Before
    fun setup() {
        val client = OkHttpClient.Builder().addInterceptor {
            val request = it.request()
            println(request.url)
            it.proceed(request)
        }.build()

        api = ImplFinancialModelPrepAPI(client)
    }

    @Test
    fun generalSearchTest(){
        val info = runBlocking {
            api.generalSearch("AAPL")
        }
        println(info)
    }

    @Test
    fun getSymbolListTest() {
        val info = runBlocking {
            api.getSymbolList()
        }
        println(info)
    }

    @Test
    fun getFullQuoteTest(){
        val info =
            runBlocking {
            api.getFullQuote("AAPL")
        }
        println(info)
    }

    @Test
    fun getBulkQuoteTest(){
        val info = runBlocking {
            api.getBulkQuote(listOf("AAPL", "MSFT"))
        }
        println(info)
    }
}