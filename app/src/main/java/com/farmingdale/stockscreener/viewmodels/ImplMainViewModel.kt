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
    private val marketStatusChecker =
        MarketStatusChecker(watchlistRepo, FinanceQueryRepository.get())

    private val _regionFilter = MutableStateFlow(RegionFilter.US)
    override val regionFilter: StateFlow<RegionFilter> = _regionFilter.asStateFlow()

    private val _typeFilter =
        MutableStateFlow(listOf(TypeFilter.STOCK, TypeFilter.ETF, TypeFilter.TRUST))
    override val typeFilter: StateFlow<List<TypeFilter>> = _typeFilter.asStateFlow()

    private val _query = MutableStateFlow("")
    override val query: StateFlow<String> = _query.asStateFlow()

    private val _searchQuery = MutableStateFlow(Query(
        hitsPerPage = 10,
        facetFilters = (listOf(
            regionFilter.value.exchanges.map { "exchangeShortName:$it" },
            typeFilter.value.map { "type:${it.type}" }
        ))))

    override val searchQuery: StateFlow<Query> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<SearchResult>?>(null)
    override val searchResults: StateFlow<List<SearchResult>?> = _searchResults.asStateFlow()

    override val watchList: StateFlow<List<SimpleQuoteData>> =
        watchlistRepo.watchlist.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
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

        searcher.response.subscribe { response ->
            _searchResults.value = response?.hits?.take(5)?.mapNotNull { hit ->
                hit.deserialize(SearchResult.serializer()).takeIf { it.name.isNotBlank() }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        searcher.cancel()
        marketStatusChecker.stopChecking()
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
                _typeFilter.value += type
            }
        } else {
            _typeFilter.value -= type
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
            _searchResults.value = null
        } else {
            println(typeFilter.value)
            println(searchQuery.value.facetFilters)

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