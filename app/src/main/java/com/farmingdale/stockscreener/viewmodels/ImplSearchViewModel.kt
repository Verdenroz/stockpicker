package com.farmingdale.stockscreener.viewmodels

import androidx.lifecycle.viewModelScope
import com.farmingdale.stockscreener.model.local.GeneralSearchData
import com.farmingdale.stockscreener.model.local.SearchData
import com.farmingdale.stockscreener.providers.ImplAlphaVantageAPI
import com.farmingdale.stockscreener.providers.ImplFinancialModelPrepAPI
import com.farmingdale.stockscreener.providers.okHttpClient
import com.farmingdale.stockscreener.repos.ImplAlphaVantageRepository
import com.farmingdale.stockscreener.repos.ImplFinancialModelPrepRepository
import com.farmingdale.stockscreener.viewmodels.base.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ImplSearchViewModel: SearchViewModel(){
    private val avApi = ImplAlphaVantageAPI(okHttpClient)
    private val fmpApi = ImplFinancialModelPrepAPI(okHttpClient)
    private val alphaVantageRepo = ImplAlphaVantageRepository(avApi)
    private val financialModelRepo = ImplFinancialModelPrepRepository(fmpApi)

    private val _query = MutableStateFlow("")
    override val query: StateFlow<String> = _query.asStateFlow()

    private val _searchResults = MutableStateFlow<GeneralSearchData?>(null)
    override val searchResults: StateFlow<GeneralSearchData?> = _searchResults.asStateFlow()

    override fun updateQuery(query: String) {
        _query.value = query
    }

    override fun search(query: String) {
        if(query.isNotBlank()){
            viewModelScope.launch(Dispatchers.IO) {
                financialModelRepo.generalSearch(query)
                    .collectLatest { searchData ->
                    _searchResults.value = searchData
                }
            }
        }
    }
}