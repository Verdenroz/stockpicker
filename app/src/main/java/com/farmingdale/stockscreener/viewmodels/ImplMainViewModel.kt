package com.farmingdale.stockscreener.viewmodels

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.farmingdale.stockscreener.model.local.GeneralSearchData
import com.farmingdale.stockscreener.model.local.WatchList
import com.farmingdale.stockscreener.repos.ImplFinancialModelPrepRepository.Companion.get
import com.farmingdale.stockscreener.repos.base.FinancialModelPrepRepository
import com.farmingdale.stockscreener.viewmodels.base.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ImplMainViewModel(application: Application) : MainViewModel(application){
    private val financialModelRepo = FinancialModelPrepRepository.get(application)

    private val _query = MutableStateFlow("")
    override val query: StateFlow<String> = _query.asStateFlow()

    private val _searchResults = MutableStateFlow<GeneralSearchData?>(null)
    override val searchResults: StateFlow<GeneralSearchData?> = _searchResults.asStateFlow()

    private val _watchList = MutableStateFlow<WatchList?>(null)
    override val watchList: StateFlow<WatchList?> = _watchList.asStateFlow()

    private val searchCache =  mutableMapOf<String, GeneralSearchData?>()

    override fun updateQuery(query: String) {
        _query.value = query
    }

    override fun search(query: String) {
        if(query.isNotBlank()){
            viewModelScope.launch(Dispatchers.IO) {
                val cachedResults = searchCache[query]
                if (cachedResults != null) {
                    _searchResults.value = cachedResults
                } else {
                    financialModelRepo.generalSearch(query)
                        .collectLatest { searchData ->
                            _searchResults.value = searchData
                            searchCache[query] = searchData
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

    override fun deleteFromWatchList(symbol: String) {
        viewModelScope.launch(Dispatchers.IO) {
            financialModelRepo.deleteFromWatchList(symbol)
        }
    }

    override fun clearWatchList() {
        viewModelScope.launch(Dispatchers.IO) {
            financialModelRepo.clearWatchList()
        }
    }
}