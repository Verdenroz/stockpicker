package com.farmingdale.stockscreener.viewmodels.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.farmingdale.stockscreener.model.local.GeneralSearchData
import com.farmingdale.stockscreener.model.local.WatchList
import kotlinx.coroutines.flow.StateFlow

abstract class MainViewModel(application: Application) : AndroidViewModel(application) {

    abstract val query: StateFlow<String>

    abstract val searchResults: StateFlow<GeneralSearchData?>

    abstract val watchList: StateFlow<WatchList?>



    abstract fun updateQuery(query: String)

    abstract fun search(query: String)

    abstract fun updateWatchList()

    abstract fun addToWatchList(symbol: String)

    abstract fun deleteFromWatchList(symbol: String)

    abstract fun clearWatchList()



}