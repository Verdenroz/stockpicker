package com.farmingdale.stockscreener.viewmodels

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.algolia.search.model.search.Query
import com.farmingdale.stockscreener.BuildConfig
import com.farmingdale.stockscreener.model.local.RegionFilter
import com.farmingdale.stockscreener.model.local.SearchResult
import com.farmingdale.stockscreener.model.local.SimpleQuoteData
import com.farmingdale.stockscreener.model.local.TypeFilter
import com.farmingdale.stockscreener.repos.ImplFinanceQueryRepository.Companion.get
import com.farmingdale.stockscreener.repos.ImplWatchlistRepository.Companion.get
import com.farmingdale.stockscreener.repos.base.FinanceQueryRepository
import com.farmingdale.stockscreener.repos.base.WatchlistRepository
import com.farmingdale.stockscreener.utils.MarketStatusChecker
import com.farmingdale.stockscreener.utils.NetworkConnectionManager
import com.farmingdale.stockscreener.utils.NetworkConnectionManagerImpl.Companion.get
import com.farmingdale.stockscreener.utils.Resource
import com.farmingdale.stockscreener.viewmodels.base.MainEvent
import com.farmingdale.stockscreener.viewmodels.base.MainViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ImplMainViewModel(application: Application) : MainViewModel(application) {
    private val watchlistRepo = WatchlistRepository.get(application)
    private val marketStatusChecker =
        MarketStatusChecker(watchlistRepo, FinanceQueryRepository.get())
    private val connectionManager = NetworkConnectionManager.get(application)

    override val isNetworkConnected: StateFlow<Boolean> =
        connectionManager.isNetworkConnectedFlow.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            true
        )

    private val eventsChannel = Channel<MainEvent>()
    override val events: Flow<MainEvent> = eventsChannel.receiveAsFlow()

    private val _regionFilter = MutableStateFlow(RegionFilter.US)
    override val regionFilter: StateFlow<RegionFilter> = _regionFilter.asStateFlow()

    private val _typeFilter =
        MutableStateFlow(setOf(TypeFilter.STOCK, TypeFilter.ETF, TypeFilter.TRUST).toImmutableSet())
    override val typeFilter: StateFlow<ImmutableSet<TypeFilter>> = _typeFilter.asStateFlow()

    private val _query = MutableStateFlow("")
    override val query: StateFlow<String> = _query.asStateFlow()

    private val _searchQuery = MutableStateFlow(Query(
        hitsPerPage = 10,
        facetFilters = (listOf(
            regionFilter.value.exchanges.map { "exchangeShortName:$it" },
            typeFilter.value.map { "type:${it.type}" }
        ))))

    override val searchQuery: StateFlow<Query> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow(emptyList<SearchResult>().toImmutableList())
    override val searchResults: StateFlow<ImmutableList<SearchResult>?> =
        _searchResults.asStateFlow()

    override val watchList: StateFlow<ImmutableList<SimpleQuoteData>> =
        watchlistRepo.watchlist.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList<SimpleQuoteData>().toImmutableList()
        )

    private val index = IndexName("stocks")
    private val searcher = HitsSearcher(
        applicationID = ApplicationID(BuildConfig.algoliaAppID),
        apiKey = APIKey(BuildConfig.algoliaAPIKey),
        indexName = index,
        query = searchQuery.value
    )

    init {
        // Start checking the market status to update the refresh interval of the repositories
        marketStatusChecker.startChecking()
        connectionManager.startListenNetworkState()
        searcher.response.subscribe { response ->
            _searchResults.value = response?.hits?.take(5)?.mapNotNull { hit ->
                hit.deserialize(SearchResult.serializer()).takeIf { it.name.isNotBlank() }
            }?.toImmutableList() ?: emptyList<SearchResult>().toImmutableList()
        }
    }

    override fun onCleared() {
        super.onCleared()
        searcher.cancel()
        marketStatusChecker.stopChecking()
        connectionManager.stopListenNetworkState()
    }

    override fun updateRegionFilter(region: RegionFilter) {
        _regionFilter.value = region

        _searchQuery.value = _searchQuery.value.copy(
            facetFilters = (listOf(
                region.exchanges.map { "exchangeShortName:$it" },
                typeFilter.value.map { "type:${it.type}" }
            ))
        )

        searcher.query.facetFilters = (listOf(
            regionFilter.value.exchanges.map { "exchangeShortName:$it" },
            typeFilter.value.map { "type:${it.type}" }
        ))
        searcher.searchAsync()
    }

    override fun toggleTypeFilter(type: TypeFilter, isChecked: Boolean) {
        if (isChecked) {
            if (!_typeFilter.value.contains(type)) {
                val newFilters = _typeFilter.value.toMutableSet()
                newFilters.add(type)
                _typeFilter.value = newFilters.toImmutableSet()
            }
        } else {
            val newFilters = _typeFilter.value.toMutableSet()
            newFilters.remove(type)
            _typeFilter.value = newFilters.toImmutableSet()
        }

        searcher.query.facetFilters = (listOf(
            regionFilter.value.exchanges.map { "exchangeShortName:$it" },
            typeFilter.value.map { "type:${it.type}" }
        ))

        searcher.searchAsync()
    }

    override fun updateQuery(query: String) {
        _query.value = query
    }

    override fun search(query: String) {
        if (query.isEmpty()) {
            _searchResults.value = emptyList<SearchResult>().toImmutableList()
        } else {
            searcher.setQuery(query)
            searcher.searchAsync()
        }
    }

    override fun refreshWatchList() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = watchlistRepo.refreshWatchList()) {
                is Resource.Error -> {
                    eventsChannel.send(MainEvent.Error(result.error.asUiText()))
                }

                else -> {
                    // Do nothing
                }
            }
        }
    }

    override fun addToWatchList(symbol: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = watchlistRepo.addToWatchList(symbol)) {
                is Resource.Error -> {
                    eventsChannel.send(MainEvent.Error(result.error.asUiText()))
                }

                else -> {
                    refreshWatchList()
                }
            }
        }
    }

    override fun deleteFromWatchList(symbol: String) {
        viewModelScope.launch(Dispatchers.IO) {
            watchlistRepo.deleteFromWatchList(symbol)
            refreshWatchList()
        }
    }
}