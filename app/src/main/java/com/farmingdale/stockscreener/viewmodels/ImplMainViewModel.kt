package com.farmingdale.stockscreener.viewmodels

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.farmingdale.stockscreener.model.local.GeneralSearchData
import com.farmingdale.stockscreener.repos.ImplFinancialModelPrepRepository.Companion.get
import com.farmingdale.stockscreener.repos.base.FinancialModelPrepRepository
import com.farmingdale.stockscreener.viewmodels.base.SearchViewModel
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
}