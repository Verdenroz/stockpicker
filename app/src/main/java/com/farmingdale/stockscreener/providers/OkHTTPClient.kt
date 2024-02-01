package com.farmingdale.stockscreener.providers

import android.util.Log
import com.farmingdale.stockscreener.BuildConfig
import okhttp3.OkHttpClient

private var _okHttpClient: OkHttpClient? = null

/**
 * OkHttpClient to use in the application
 */
val okHttpClient: OkHttpClient
    @Synchronized
    get() {
        if (_okHttpClient == null) {
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
                .build()
        }

        return _okHttpClient!!
    }