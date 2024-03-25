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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ImplHomeViewModel(application: Application): HomeViewModel(application) {
    private val financialModelRepo = FinancialModelPrepRepository.get(application)
    private val newsRepo = NewsRepository.get(application)
    private val googleFinanceRepo = GoogleFinanceRepository.get()

    private val _indices: MutableStateFlow<List<MarketIndex>?> = MutableStateFlow(null)
    override val indices: StateFlow<List<MarketIndex>?> = _indices.asStateFlow()

    private val _watchList = MutableStateFlow<WatchList?>(null)
    override val watchList: StateFlow<WatchList?> = _watchList.asStateFlow()

    private val _news: MutableStateFlow<News?> = MutableStateFlow(null)
    override val news: StateFlow<News?> = _news.asStateFlow()

    private val _preferredCategory: MutableStateFlow<Category?> = MutableStateFlow(newsRepo.getPreferredCategory())
    override val preferredCategory: StateFlow<Category?> = _preferredCategory.asStateFlow()

    private val _actives: MutableStateFlow<List<GoogleFinanceStock>?> = MutableStateFlow(null)
    override val actives: StateFlow<List<GoogleFinanceStock>?> = _actives.asStateFlow()

    private val _losers: MutableStateFlow<List<GoogleFinanceStock>?> = MutableStateFlow(null)
    override val losers: StateFlow<List<GoogleFinanceStock>?> = _losers.asStateFlow()

    private val _gainers: MutableStateFlow<List<GoogleFinanceStock>?> = MutableStateFlow(null)
    override val gainers: StateFlow<List<GoogleFinanceStock>?> = _gainers.asStateFlow()

    private val newsCache = mutableMapOf<Category?, News?>()

    init {
        refresh()
    }

    override fun setPreferredCategory(category: Category) {
        newsRepo.setPreferredCategory(category)
        _preferredCategory.value = category
    }

    override fun getHeadlines(category: Category?) {
        viewModelScope.launch {
            val cachedResults = newsCache[category]
            if (cachedResults != null) {
                _news.value = News(cachedResults.articles.shuffled())
            } else {
                val headlines = newsRepo.getHeadlines(category)

                headlines.collectLatest { headLines ->
                    _news.value = News(headLines.articles.shuffled())
                    newsCache[category] = headLines
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

    override fun getIndices() {
        viewModelScope.launch {
            val indices = googleFinanceRepo.getIndices()
            indices.collectLatest {
                _indices.value = it
            }
        }
    }

    override fun getActives() {
        viewModelScope.launch {
            val actives = googleFinanceRepo.getActives()
            actives.collectLatest {
                _actives.value = it
            }
        }
    }

    override fun getLosers() {
        viewModelScope.launch {
            val losers = googleFinanceRepo.getLosers()
            losers.collectLatest {
                _losers.value = it
            }
        }
    }

    override fun getGainers() {
        viewModelScope.launch {
            val gainers = googleFinanceRepo.getGainers()
            gainers.collectLatest {
                _gainers.value = it
            }
        }
    }

    override fun refresh() {
        viewModelScope.launch {
            val headlines = async(Dispatchers.IO) { getHeadlines(preferredCategory.value) }
            val indices = async(Dispatchers.IO) { getIndices() }
            val actives = async(Dispatchers.IO) { getActives() }
            val losers = async(Dispatchers.IO) { getLosers() }
            val gainers = async(Dispatchers.IO) { getGainers() }
            headlines.await()
            indices.await()
            actives.await()
            losers.await()
            gainers.await()
            updateWatchList()
        }
    }
}