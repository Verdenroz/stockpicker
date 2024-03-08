package com.farmingdale.stockscreener

import com.farmingdale.stockscreener.providers.ImplNewsAPI
import com.farmingdale.stockscreener.providers.base.NewsAPI
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Before
import org.junit.Test

class NewsAPITest {
    private lateinit var api: NewsAPI

    @Before
    fun setup() {
        val client = OkHttpClient.Builder().addInterceptor {
            val request = it.request()
            println(request.url)
            it.proceed(request)
        }.build()

        api = ImplNewsAPI(client)
    }

    @Test
    fun testGetHeadlines() {
        val info = runBlocking {
            api.getHeadlines(null)
        }
        println(info)
    }
}