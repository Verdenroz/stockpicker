package com.farmingdale.stockscreener.model.utils

import okhttp3.HttpUrl.Companion.toHttpUrl


//https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=IBM&apikey=demo
//https://www.alphavantage.co/query?function=EMA&symbol=IBM&interval=weekly&time_period=10&series_type=open&apikey=demo

val ALPHA_VANTAGE_API_URL = "https://www.alphavantage.co/query?".toHttpUrl()
