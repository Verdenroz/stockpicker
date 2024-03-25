package com.farmingdale.stockscreener.providers

import android.util.Log
import com.farmingdale.stockscreener.BuildConfig
import okhttp3.Cache
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import java.io.File
import java.util.concurrent.TimeUnit

private var _okHttpClient: OkHttpClient? = null

/**
 * OkHttpClient to use in the application
 */
val okHttpClient: OkHttpClient
    @Synchronized
    get() {
        if (_okHttpClient == null) {
            val cacheSize = 10 * 1024 * 1024 // 10 MB
            val cache = Cache(File("okhttp/cache"), cacheSize.toLong())

            _okHttpClient = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    // Get the request
                    val request = chain.request()

                    // Log the request URL
                    if (BuildConfig.DEBUG) {
                        Log.v("OkHTTPClient", request.url.toString())
                    }

                    // proceed on the chain (returns as last line)
                    chain.proceed(request)
                }
                .cache(cache)
                .connectionPool(ConnectionPool(5, 5, TimeUnit.MINUTES))
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()
        }

        return _okHttpClient!!
    }