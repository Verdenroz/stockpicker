package com.farmingdale.stockscreener

import com.farmingdale.stockscreener.providers.ImplGoogleFinanceAPI
import com.farmingdale.stockscreener.providers.base.GoogleFinanceAPI
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Before
import org.junit.Test

class GoogleFinanceAPITest {
    private lateinit var api: GoogleFinanceAPI
    @Before
    fun setup(){
        val client = OkHttpClient.Builder().addInterceptor {
            val request = it.request()
            println(request.url)
            it.proceed(request)
        }.build()
        api = ImplGoogleFinanceAPI(client)
    }

    @Test
    fun getIndicesTest(){
        val info = runBlocking {
            api.getIndices()
        }
        println(info)
    }
}