package com.farmingdale.stockscreener.viewmodels.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.farmingdale.stockscreener.model.local.News
import com.farmingdale.stockscreener.model.local.SimpleQuoteData
import com.farmingdale.stockscreener.model.local.MarketMover
import com.farmingdale.stockscreener.model.local.MarketIndex
import com.farmingdale.stockscreener.model.local.MarketSector
import kotlinx.coroutines.flow.StateFlow

abstract class HomeViewModel(application: Application): AndroidViewModel(application) {

    /**
     * The user's watchlist as a list of [SimpleQuoteData]
     */
    abstract val watchList: StateFlow<List<SimpleQuoteData>?>

    /**
     * Current [News] headlines displayed on the home screen
     */
    abstract val news: StateFlow<List<News>?>

    /**
     * The list of market indices as [MarketIndex]
     */
    abstract val indices: StateFlow<List<MarketIndex>?>

    /**
     * List of market sectors as [MarketSector]
     */
    abstract val sectors: StateFlow<List<MarketSector>?>

    /**
     * List of active stocks as [MarketMover]
     */
    abstract val actives: StateFlow<List<MarketMover>?>

    /**
     * List of losing stocks as [MarketMover]
     */
    abstract val gainers: StateFlow<List<MarketMover>?>

    /**
     * List of gaining stocks as [MarketMover]
     */
    abstract val losers: StateFlow<List<MarketMover>?>

    /**
     * Refresh the home screen with new data
     */
    abstract fun refresh()

}