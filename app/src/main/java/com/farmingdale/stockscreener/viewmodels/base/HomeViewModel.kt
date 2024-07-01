package com.farmingdale.stockscreener.viewmodels.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.farmingdale.stockscreener.model.local.News
import com.farmingdale.stockscreener.model.local.SimpleQuoteData
import com.farmingdale.stockscreener.model.local.MarketMover
import com.farmingdale.stockscreener.model.local.MarketIndex
import com.farmingdale.stockscreener.model.local.MarketSector
import com.farmingdale.stockscreener.utils.Resource
import kotlinx.coroutines.flow.StateFlow

abstract class HomeViewModel(application: Application): AndroidViewModel(application) {

    /**
     * Current [News] headlines displayed on the home screen
     */
    abstract val news: StateFlow<Resource<List<News>>>

    /**
     * The list of market indices as [MarketIndex]
     */
    abstract val indices: StateFlow<Resource<List<MarketIndex>>>

    /**
     * List of market sectors as [MarketSector]
     */
    abstract val sectors: StateFlow<Resource<List<MarketSector>>>

    /**
     * List of active stocks as [MarketMover]
     */
    abstract val actives: StateFlow<Resource<List<MarketMover>>>

    /**
     * List of losing stocks as [MarketMover]
     */
    abstract val gainers: StateFlow<Resource<List<MarketMover>>>

    /**
     * List of gaining stocks as [MarketMover]
     */
    abstract val losers: StateFlow<Resource<List<MarketMover>>>

    /**
     * Refresh the home screen with new data
     */
    abstract fun refresh()

}