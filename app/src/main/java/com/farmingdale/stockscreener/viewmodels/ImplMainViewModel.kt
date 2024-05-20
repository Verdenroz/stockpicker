package com.farmingdale.stockscreener.viewmodels

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.algolia.search.model.search.Query
import com.farmingdale.stockscreener.BuildConfig
import com.farmingdale.stockscreener.model.local.SearchResult
import com.farmingdale.stockscreener.model.local.SimpleQuoteData
import com.farmingdale.stockscreener.repos.ImplWatchlistRepository.Companion.get
import com.farmingdale.stockscreener.repos.base.WatchlistRepository
import com.farmingdale.stockscreener.viewmodels.base.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ImplMainViewModel(application: Application) : MainViewModel(application) {
    private val watchlistRepo = WatchlistRepository.get(application)

    private val _query = MutableStateFlow("")
    override val query: StateFlow<String> = _query.asStateFlow()

    private val _searchResults = MutableStateFlow<List<SearchResult>?>(null)
    override val searchResults: StateFlow<List<SearchResult>?> = _searchResults.asStateFlow()

    override val watchList: StateFlow<List<SimpleQuoteData>?> =
        watchlistRepo.watchlist.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    private val index = IndexName("stocks")
    private val searcher = HitsSearcher(
        applicationID = ApplicationID(BuildConfig.algoliaAppID),
        apiKey = APIKey(BuildConfig.algoliaAPIKey),
        indexName = index,
        query = Query(hitsPerPage = 5)
    )

    init {
        searcher.response.subscribe { response ->
            _searchResults.value = response?.hits?.mapNotNull { hit ->
                hit.deserialize(SearchResult.serializer()).takeIf { it.name.isNotBlank() }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        searcher.cancel()
    }

    override fun updateQuery(query: String) {
        _query.value = query
    }

    override fun search(query: String) {
        if (query.isEmpty()) {
            _searchResults.value = null
        } else {
            searcher.setQuery(query)
            searcher.searchAsync()
        }
    }

    override fun refreshWatchList() {
        viewModelScope.launch(Dispatchers.IO) {
            watchlistRepo.refreshWatchList()
        }
    }

    override fun addToWatchList(symbol: String) {
        viewModelScope.launch(Dispatchers.IO) {
            watchlistRepo.addToWatchList(symbol)
            refreshWatchList()
        }
    }

    override fun deleteFromWatchList(symbol: String) {
        viewModelScope.launch(Dispatchers.IO) {
            watchlistRepo.deleteFromWatchList(symbol)
            refreshWatchList()
        }
    }
}