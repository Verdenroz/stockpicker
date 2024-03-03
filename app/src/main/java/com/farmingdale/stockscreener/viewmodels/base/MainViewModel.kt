package com.farmingdale.stockscreener.viewmodels.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.farmingdale.stockscreener.model.local.GeneralSearchData
import com.farmingdale.stockscreener.model.local.WatchList
import com.farmingdale.stockscreener.model.local.googlefinance.MarketIndex
import com.farmingdale.stockscreener.model.local.news.Category
import com.farmingdale.stockscreener.model.local.news.News
import kotlinx.coroutines.flow.StateFlow

abstract class MainViewModel(application: Application) : AndroidViewModel(application) {

    abstract val query: StateFlow<String>

    abstract val searchResults: StateFlow<GeneralSearchData?>

    abstract val watchList: StateFlow<WatchList?>

    abstract val preferredCategory: StateFlow<Category?>

    abstract val news: StateFlow<News?>

    abstract val indices: StateFlow<List<MarketIndex>?>

    abstract val isRefreshing: StateFlow<Boolean>

    abstract val isLoading: StateFlow<Boolean>

    abstract fun updateQuery(query: String)

    abstract fun search(query: String)

    abstract fun updateWatchList()

    abstract fun addToWatchList(symbol: String)

    abstract fun deleteFromWatchList(symbol: String)

    abstract fun clearWatchList()

    abstract fun setPreferredCategory(category: Category)

    abstract fun getHeadlines(category: Category?)

    abstract fun getIndices()

    abstract fun refresh()


}