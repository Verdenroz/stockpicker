package com.farmingdale.stockscreener.viewmodels.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.farmingdale.stockscreener.model.local.GeneralSearchData
import kotlinx.coroutines.flow.StateFlow

abstract class SearchViewModel(application: Application) : AndroidViewModel(application) {

    abstract val query: StateFlow<String>

    abstract val searchResults: StateFlow<GeneralSearchData?>

    abstract fun updateQuery(query: String)

    abstract fun search(query: String)


}