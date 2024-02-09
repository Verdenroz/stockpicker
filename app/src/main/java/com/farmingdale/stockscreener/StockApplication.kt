package com.farmingdale.stockscreener

import android.app.Application
import android.os.Handler
import android.os.Looper
import com.farmingdale.stockscreener.workers.WatchListWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StockApplication : Application() {
    private val handler = Handler(Looper.getMainLooper())
    private val runnableCode: Runnable = object : Runnable {
        override fun run() {
            CoroutineScope(Dispatchers.IO).launch {
                WatchListWorker.updateWatchList(applicationContext)
            }
            handler.postDelayed(this, 30000)
        }
    }

    override fun onCreate() {
        super.onCreate()
        handler.post(runnableCode)
    }

    override fun onTerminate() {
        super.onTerminate()
        handler.removeCallbacks(runnableCode)
    }
}