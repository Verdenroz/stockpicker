package com.farmingdale.stockscreener.viewmodels

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.farmingdale.stockscreener.model.local.News
import com.farmingdale.stockscreener.model.local.SimpleQuoteData
import com.farmingdale.stockscreener.model.local.MarketIndex
import com.farmingdale.stockscreener.model.local.MarketMover
import com.farmingdale.stockscreener.model.local.MarketSector
import com.farmingdale.stockscreener.repos.ImplFinanceQueryRepository.Companion.get
import com.farmingdale.stockscreener.repos.ImplWatchlistRepository.Companion.get
import com.farmingdale.stockscreener.repos.base.FinanceQueryRepository
import com.farmingdale.stockscreener.repos.base.WatchlistRepository
import com.farmingdale.stockscreener.utils.MarketStatusChecker
import com.farmingdale.stockscreener.viewmodels.base.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ImplHomeViewModel(application: Application): HomeViewModel(application) {
    private val financeQueryRepo = FinanceQueryRepository.get()
    private val watchlistRepo = WatchlistRepository.get(application)
    private val marketStatusChecker = MarketStatusChecker(watchlistRepo, financeQueryRepo)

    init {
        // Start checking the market status to update the refresh interval of the repositories
        marketStatusChecker.startChecking()
    }

    override val watchList: StateFlow<List<SimpleQuoteData>?> = watchlistRepo.watchlist.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    override val news: StateFlow<List<News>?> = financeQueryRepo.headlines.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    override val indices: StateFlow<List<MarketIndex>?> = financeQueryRepo.indices.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    override val sectors: StateFlow<List<MarketSector>?> = financeQueryRepo.sectors.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    override val actives: StateFlow<List<MarketMover>?> = financeQueryRepo.actives.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    override val losers: StateFlow<List<MarketMover>?> = financeQueryRepo.losers.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    override val gainers: StateFlow<List<MarketMover>?> = financeQueryRepo.gainers.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    override fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            val refreshDataDeferred = async { financeQueryRepo.refreshMarketData() }
            val refreshNewsDeferred = async { financeQueryRepo.refreshNews() }
            val refreshWatchlistDeferred = async { watchlistRepo.refreshWatchList() }

            refreshDataDeferred.await()
            refreshNewsDeferred.await()
            refreshWatchlistDeferred.await()
        }
    }
}