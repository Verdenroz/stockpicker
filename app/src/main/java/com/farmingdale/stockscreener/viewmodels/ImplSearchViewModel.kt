package com.farmingdale.stockscreener.viewmodels

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.farmingdale.stockscreener.model.local.SearchData
import com.farmingdale.stockscreener.providers.ImplAlphaVantageAPI
import com.farmingdale.stockscreener.providers.okHttpClient
import com.farmingdale.stockscreener.repos.ImplAlphaVantageRepository
import com.farmingdale.stockscreener.viewmodels.base.SearchViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

class ImplSearchViewModel: SearchViewModel(){
    private val api = ImplAlphaVantageAPI(okHttpClient)
    private val repo = ImplAlphaVantageRepository(api)

    private val _query = MutableStateFlow("")
    override val query: StateFlow<String> = _query.asStateFlow()

    private val _searchResults = MutableStateFlow<SearchData?>(null)
    override val searchResults: StateFlow<SearchData?> = _searchResults.asStateFlow()

    override fun updateQuery(query: String) {
        _query.value = query
    }

    override fun search(query: String) {
        if(query.isNotBlank()){
            viewModelScope.launch {
                repo.querySymbols(query)
                    .debounce(3000)
                    .collectLatest { searchData ->
                    _searchResults.value = searchData
                }
            }
        }
    }
}