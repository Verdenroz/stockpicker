package com.farmingdale.stockscreener.viewmodels

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.farmingdale.stockscreener.model.local.WatchList
import com.farmingdale.stockscreener.model.local.googlefinance.GoogleFinanceStock
import com.farmingdale.stockscreener.model.local.googlefinance.MarketIndex
import com.farmingdale.stockscreener.model.local.news.Category
import com.farmingdale.stockscreener.model.local.news.News
import com.farmingdale.stockscreener.repos.ImplFinancialModelPrepRepository.Companion.get
import com.farmingdale.stockscreener.repos.ImplGoogleFinanceRepository.Companion.get
import com.farmingdale.stockscreener.repos.ImplNewsRepository.Companion.get
import com.farmingdale.stockscreener.repos.base.FinancialModelPrepRepository
import com.farmingdale.stockscreener.repos.base.GoogleFinanceRepository
import com.farmingdale.stockscreener.repos.base.NewsRepository
import com.farmingdale.stockscreener.viewmodels.base.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ImplHomeViewModel(application: Application): HomeViewModel(application) {
    private val financialModelRepo = FinancialModelPrepRepository.get(application)
    private val newsRepo = NewsRepository.get(application)
    private val googleFinanceRepo = GoogleFinanceRepository.get()

    override val watchList: StateFlow<WatchList?> = financialModelRepo.getWatchList().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    private val _preferredCategory: MutableStateFlow<Category?> = MutableStateFlow(newsRepo.getPreferredCategory())
    override val preferredCategory: StateFlow<Category?> = _preferredCategory.asStateFlow()

    override val news: StateFlow<News?> = newsRepo.headlines.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    override val indices: StateFlow<List<MarketIndex>?> = googleFinanceRepo.indices.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    override val actives: StateFlow<List<GoogleFinanceStock>?> = googleFinanceRepo.actives.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    override val losers: StateFlow<List<GoogleFinanceStock>?> = googleFinanceRepo.losers.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    override val gainers: StateFlow<List<GoogleFinanceStock>?> = googleFinanceRepo.gainers.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    override fun setPreferredCategory(category: Category) {
        newsRepo.setPreferredCategory(category)
        _preferredCategory.value = category
    }

    override fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            val refreshValuesDeferred = async { googleFinanceRepo.refreshValues() }
            val updateWatchListDeferred = async { financialModelRepo.updateWatchList() }
            val refreshHeadlinesDeferred = async { newsRepo.refreshHeadlines() }

            refreshValuesDeferred.await()
            updateWatchListDeferred.await()
            refreshHeadlinesDeferred.await()
        }
    }
}