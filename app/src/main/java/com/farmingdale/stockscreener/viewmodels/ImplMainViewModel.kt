package com.farmingdale.stockscreener.viewmodels

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.farmingdale.stockscreener.model.local.GeneralSearchData
import com.farmingdale.stockscreener.model.local.SimpleQuoteData
import com.farmingdale.stockscreener.model.local.UnitedStatesExchanges
import com.farmingdale.stockscreener.repos.ImplFinanceQueryRepository.Companion.get
import com.farmingdale.stockscreener.repos.ImplWatchlistRepository.Companion.get
import com.farmingdale.stockscreener.repos.base.FinanceQueryRepository
import com.farmingdale.stockscreener.repos.base.WatchlistRepository
import com.farmingdale.stockscreener.viewmodels.base.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.collections.set

class ImplMainViewModel(application: Application) : MainViewModel(application) {
    private val watchlistRepo = WatchlistRepository.get(application)

    private val _query = MutableStateFlow("")
    override val query: StateFlow<String> = _query.asStateFlow()

    private val _searchResults = MutableStateFlow<GeneralSearchData?>(null)
    override val searchResults: StateFlow<GeneralSearchData?> = _searchResults.asStateFlow()

    override val watchList: StateFlow<List<SimpleQuoteData>?> =
        watchlistRepo.watchlist.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    override fun updateQuery(query: String) {
        _query.value = query
    }

    override fun search(query: String) {
        TODO()
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