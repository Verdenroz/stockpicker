package com.farmingdale.stockscreener.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.farmingdale.stockscreener.repos.ImplFinancialModelPrepRepository.Companion.get
import com.farmingdale.stockscreener.repos.base.FinancialModelPrepRepository
import kotlinx.coroutines.delay

class WatchListWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        updateWatchList(applicationContext)
        return Result.success()
    }

    companion object {
        /**
         * Updates the watchlist every 30 seconds
         */
        suspend fun updateWatchList(context: Context) {
            val repo = FinancialModelPrepRepository.get(context)

            while (true) {
                try {
                    Log.d("WatchListWorker", "Updating watchlist")
                    repo.updateWatchList()
                    delay(30000)
                } catch (e: Exception) {
                    Log.e("WatchListWorker", "Error updating watchlist", e)
                    throw e
                }
            }
        }
    }
}