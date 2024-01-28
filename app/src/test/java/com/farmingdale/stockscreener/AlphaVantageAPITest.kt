package com.farmingdale.stockscreener

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
    fun searchSymbolTest() {
        val info = runBlocking {
            api.searchSymbol("IBM")
        }
        println(info)
    }

    @Test
    fun getQuoteTest() {
        val info = runBlocking {
            api.getQuote("IBM")
        }
        println(info)
    }

    @Test
    fun getSMATest() {
        val info = runBlocking {
            api.getSMA(
                symbol = "IBM",
                interval = Interval.DAILY.value,
                timePeriod = 60,
                seriesType = SeriesType.CLOSE.value
            )
        }
        println(info)
    }

    @Test
    fun getEMATest() {
        val info = runBlocking {
            api.getEMA(
                symbol = "IBM",
                interval = Interval.DAILY.value,
                timePeriod = 60,
                seriesType = SeriesType.CLOSE.value
            )
        }
        println(info)
    }

    @Test
    fun getSTOCHTest() {
        val info = runBlocking {
            api.getSTOCH(
                symbol = "IBM",
                interval = Interval.DAILY.value,
                fastKPeriod = 5,
                slowKPeriod = 3,
                slowDPeriod = 3,
                slowKMAType = 0,
                slowDMAType = 0
            )
        }
        println(info)
    }

    @Test
    fun getRSITest() {
        val info = runBlocking {
            api.getRSI(
                symbol = "IBM",
                interval = Interval.DAILY.value,
                timePeriod = 60,
                seriesType = SeriesType.CLOSE.value
            )
        }
        println(info)
    }

    @Test
    fun getADXTest() {
        val info = runBlocking {
            api.getADX(
                symbol = "IBM",
                interval = Interval.DAILY.value,
                timePeriod = 60
            )
        }
        println(info)
    }

    @Test
    fun getCCITest() {
        val info = runBlocking {
            api.getCCI(
                symbol = "IBM",
                interval = Interval.DAILY.value,
                timePeriod = 60
            )
        }
        println(info)
    }

    @Test
    fun getAROON() {
        val info = runBlocking {
            api.getAROON(
                symbol = "IBM",
                interval = Interval.DAILY.value,
                timePeriod = 60
            )
        }
        println(info)
    }

    @Test
    fun getBBANDSTest() {
        val info = runBlocking {
            api.getBBANDS(
                symbol = "IBM",
                interval = Interval.DAILY.value,
                timePeriod = 60,
                seriesType = SeriesType.CLOSE.value,
                nbDevUp = 3,
                nbDevDown = 3,
                matype = 0
            )
        }
        println(info)
    }

    @Test
    fun getADTest() {
        val info = runBlocking {
            api.getAD(
                symbol = "IBM",
                interval = Interval.DAILY.value
            )
        }
        println(info)
    }

    @Test
    fun getOBVTest() {
        val info = runBlocking {
            api.getOBV(
                symbol = "IBM",
                interval = Interval.DAILY.value
            )
        }
        println(info)
    }

}