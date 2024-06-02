package com.farmingdale.stockscreener.viewmodels.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.farmingdale.stockscreener.model.local.RegionFilter
import com.farmingdale.stockscreener.model.local.SearchResult
import com.farmingdale.stockscreener.model.local.SimpleQuoteData
import com.farmingdale.stockscreener.model.local.TypeFilter
import com.algolia.search.model.search.Query
import kotlinx.coroutines.flow.StateFlow

abstract class MainViewModel(application: Application) : AndroidViewModel(application) {
    /**
     * The current region to filter search results by exchanges
     */
    abstract val regionFilter: StateFlow<RegionFilter>

    /**
     * The current type to filter search results by stock type (stock, etf, trust)
     */
    abstract val typeFilter: StateFlow<List<TypeFilter>>

    /**
     * The current query string in search bar
     */
    abstract val query: StateFlow<String>

    /**
     * The [Query] object for the algolia search engine
     */
    abstract val searchQuery: StateFlow<Query>

    /**
     * The search results for the current query as a list of [SearchResult]
     */
    abstract val searchResults: StateFlow<List<SearchResult>?>

    /**
     * The user's watchlist as a list of [SimpleQuoteData]
     */
    abstract val watchList: StateFlow<List<SimpleQuoteData>?>

    abstract fun updateRegionFilter(region: RegionFilter)

    abstract fun toggleTypeFilter(type: TypeFilter, isChecked: Boolean)

    /**
     * Update the current [query] string
     */
    abstract fun updateQuery(query: String)

    /**
     * Search for a query and updates [searchResults]
     */
    abstract fun search(query: String)

    /**
     * Update the user's watchlist with fresh or new data
     */
    abstract fun refreshWatchList()

    /**
     * Add a stock to the user's watchlist
     */
    abstract fun addToWatchList(symbol: String)

    /**
     * Remove a stock from the user's watchlist
     */
    abstract fun deleteFromWatchList(symbol: String)

}