package com.farmingdale.stockscreener.viewmodels

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.farmingdale.stockscreener.model.local.GeneralSearchData
import com.farmingdale.stockscreener.model.local.UnitedStatesExchanges
import com.farmingdale.stockscreener.model.local.WatchList
import com.farmingdale.stockscreener.repos.ImplFinancialModelPrepRepository.Companion.get
import com.farmingdale.stockscreener.repos.base.FinancialModelPrepRepository
import com.farmingdale.stockscreener.viewmodels.base.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.collections.mutableMapOf
import kotlin.collections.set

class ImplMainViewModel(application: Application) : MainViewModel(application) {
    private val financialModelRepo = FinancialModelPrepRepository.get(application)

    private val _query = MutableStateFlow("")
    override val query: StateFlow<String> = _query.asStateFlow()

    private val _searchResults = MutableStateFlow<GeneralSearchData?>(null)
    override val searchResults: StateFlow<GeneralSearchData?> = _searchResults.asStateFlow()

    private val _watchList = MutableStateFlow<WatchList?>(null)
    override val watchList: StateFlow<WatchList?> = _watchList.asStateFlow()

    private val searchCache = mutableMapOf<String, GeneralSearchData?>()

    init {
        updateWatchList()
    }

    override fun updateQuery(query: String) {
        _query.value = query
    }

    override fun search(query: String) {
        if (query.isNotBlank()) {
            viewModelScope.launch {
                val cachedResults = searchCache[query]
                if (cachedResults != null) {
                    _searchResults.value = cachedResults
                } else {
                    val searchData = async(Dispatchers.IO) {
                        financialModelRepo.generalSearch(
                            query = query,
                            exchange = UnitedStatesExchanges.NASDAQ
                        )
                    }.await()

                    searchData.collectLatest { results ->
                        _searchResults.value = results
                        searchCache[query] = results
                    }
                }
            }
        }
    }

    override fun updateWatchList() {
        viewModelScope.launch(Dispatchers.IO) {
            financialModelRepo.getWatchList().collect { updatedWatchList ->
                _watchList.value = updatedWatchList
            }
        }
    }

    override fun addToWatchList(symbol: String) {
        viewModelScope.launch(Dispatchers.IO) {
            financialModelRepo.addToWatchList(symbol)
            updateWatchList()
        }
    }
}