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
     * The list of market indices as [MarketIndex]
     */
    abstract val indices: StateFlow<List<MarketIndex>?>

    /**
     * The user's watchlist
     */
    abstract val watchList: StateFlow<WatchList?>

    /**
     * The news for the user's preferred category as a [News] object
     */
    abstract val news: StateFlow<News?>

    /**
     * The user's preferred news [Category]
     */
    abstract val preferredCategory: StateFlow<Category?>

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
     * Set the user's preferred news category
     */
    abstract fun setPreferredCategory(category: Category)

    /**
     * Updates [news] for a given category, caching them if the same category, and shuffles randomly the returned headlines
     */
    abstract fun getHeadlines(category: Category?)

    /**
     * Update the user's [watchList] with fresh or new data
     */
    abstract fun updateWatchList()

    /**
     * Get the market indices and updates [indices]
     */
    abstract fun getIndices()

    /**
     * Get the list of active stocks and updates [actives]
     */
    abstract fun getActives()

    /**
     * Get the list of losing stocks and updates [losers]
     */
    abstract fun getLosers()

    /**
     * Get the list of gaining stocks and updates [gainers]
     */
    abstract fun getGainers()

    /**
     * Refresh the home screen with new data
     */
    abstract fun refresh()

}