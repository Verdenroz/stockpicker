package com.farmingdale.stockscreener.viewmodels.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.farmingdale.stockscreener.model.local.WatchList
import com.farmingdale.stockscreener.model.local.googlefinance.GoogleFinanceStock
import com.farmingdale.stockscreener.model.local.googlefinance.MarketIndex
import com.farmingdale.stockscreener.model.local.news.Category
import com.farmingdale.stockscreener.model.local.news.News
import kotlinx.coroutines.flow.StateFlow

abstract class HomeViewModel(application: Application): AndroidViewModel(application) {

    /**
     * The user's watchlist
     */
    abstract val watchList: StateFlow<WatchList?>

    /**
     * The user's preferred news [Category]
     */
    abstract val preferredCategory: StateFlow<Category?>

    /**
     * The news for the user's preferred category as a [News] object
     */
    abstract val news: StateFlow<News?>

    /**
     * The list of market indices as [MarketIndex]
     */
    abstract val indices: StateFlow<List<MarketIndex>?>

    /**
     * List of active stocks as [GoogleFinanceStock]
     */
    abstract val actives: StateFlow<List<GoogleFinanceStock>?>

    /**
     * List of losing stocks as [GoogleFinanceStock]
     */
    abstract val gainers: StateFlow<List<GoogleFinanceStock>?>

    /**
     * List of gaining stocks as [GoogleFinanceStock]
     */
    abstract val losers: StateFlow<List<GoogleFinanceStock>?>

    /**
     * Set the user's preferred news [Category]
     */
    abstract fun setPreferredCategory(category: Category)

    /**
     * Refresh the home screen with new data
     */
    abstract fun refresh()

}