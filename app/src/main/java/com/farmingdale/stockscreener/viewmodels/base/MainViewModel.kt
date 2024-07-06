package com.farmingdale.stockscreener.viewmodels.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.farmingdale.stockscreener.model.local.RegionFilter
import com.farmingdale.stockscreener.model.local.SearchResult
import com.farmingdale.stockscreener.model.local.SimpleQuoteData
import com.farmingdale.stockscreener.model.local.TypeFilter
import com.algolia.search.model.search.Query
import com.farmingdale.stockscreener.utils.UiText
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

abstract class MainViewModel(application: Application) : AndroidViewModel(application) {
    /**
     * A [StateFlow] that emits true if the device is connected to the internet, false otherwise
     */
    abstract val isNetworkConnected: StateFlow<Boolean>

    /**
     * A [Flow] of [MainEvent] that emits events to be handled by the UI
     */
    abstract val events: Flow<MainEvent>

    /**
     * The current [RegionFilter] to filter search results by exchanges
     */
    abstract val regionFilter: StateFlow<RegionFilter>

    /**
     * The [TypeFilter]s to filter search results by stock type (stock, etf, trust)
     */
    abstract val typeFilter: StateFlow<ImmutableSet<TypeFilter>>

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
    abstract val searchResults: StateFlow<ImmutableList<SearchResult>?>

    /**
     * The user's watchlist as a list of [SimpleQuoteData]
     */
    abstract val watchList: StateFlow<ImmutableList<SimpleQuoteData>>

    /**
     * Update the current [region] filter
     */
    abstract fun updateRegionFilter(region: RegionFilter)

    /**
     * Toggle the [type] filter on or off
     */
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

sealed interface MainEvent {
    data class Error(val message: UiText) : MainEvent
}
