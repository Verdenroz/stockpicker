package com.farmingdale.stockscreener.viewmodels.base

import androidx.lifecycle.ViewModel
import com.farmingdale.stockscreener.model.local.SearchData
import kotlinx.coroutines.flow.StateFlow

abstract class SearchViewModel: ViewModel() {

    abstract val query: StateFlow<String>

    abstract val searchResults: StateFlow<SearchData?>

    abstract fun updateQuery(query: String)

    abstract fun search(query: String)


}