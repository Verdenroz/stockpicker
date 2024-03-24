package com.farmingdale.stockscreener.viewmodels.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.farmingdale.stockscreener.model.local.GeneralSearchData
import com.farmingdale.stockscreener.model.local.WatchList
import kotlinx.coroutines.flow.StateFlow

abstract class MainViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * The current query string in search bar
     */
    abstract val query: StateFlow<String>

    /**
     * The search results for the current query as [GeneralSearchData]
     */
    abstract val searchResults: StateFlow<GeneralSearchData?>

    /**
     * The user's watch list
     */
    abstract val watchList: StateFlow<WatchList?>

    /**
     * Update the current [query] string
     */
    abstract fun updateQuery(query: String)

    /**
     * Search for a query and updates [searchResults]
     */
    abstract fun search(query: String)

    /**
     * Update the user's [watchList] with fresh or new data
     */
    abstract fun updateWatchList()

    /**
     * Add a stock to the user's [watchList]
     */
    abstract fun addToWatchList(symbol: String)

}