package com.farmingdale.stockscreener.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.farmingdale.stockscreener.model.local.WatchList
import com.farmingdale.stockscreener.model.local.googlefinance.GoogleFinanceStock
import com.farmingdale.stockscreener.model.local.googlefinance.MarketIndex
import com.farmingdale.stockscreener.model.local.news.Category
import com.farmingdale.stockscreener.model.local.news.News
import com.farmingdale.stockscreener.viewmodels.base.HomeViewModel
import com.farmingdale.stockscreener.viewmodels.ImplHomeViewModel

@Composable
fun HomeView() {
    val homeViewModel: HomeViewModel = viewModel<ImplHomeViewModel>()
    val watchList by homeViewModel.watchList.collectAsState()
    val news by homeViewModel.news.collectAsState()
    val indices by homeViewModel.indices.collectAsState()
    val actives by homeViewModel.actives.collectAsState()
    val losers by homeViewModel.losers.collectAsState()
    val gainers by homeViewModel.gainers.collectAsState()
    val isRefreshing by homeViewModel.isRefreshing.collectAsState()
    val isLoading by homeViewModel.isLoading.collectAsState()
    val preferredCategory by homeViewModel.preferredCategory.collectAsState()

    HomeContent(
        watchList = watchList,
        news = news,
        indices = indices,
        actives = actives,
        losers = losers,
        gainers = gainers,
        preferredCategory = preferredCategory,
        isLoading = isLoading,
        isRefreshing = isRefreshing,
        setPreferredCategory = homeViewModel::setPreferredCategory,
        refresh = homeViewModel::refresh
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeContent(
    watchList: WatchList?,
    news: News?,
    indices: List<MarketIndex>?,
    actives: List<GoogleFinanceStock>?,
    losers: List<GoogleFinanceStock>?,
    gainers: List<GoogleFinanceStock>?,
    preferredCategory: Category?,
    isLoading: Boolean,
    isRefreshing: Boolean,
    setPreferredCategory: (Category) -> Unit,
    refresh: () -> Unit,
) {
    val pullRefreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = refresh)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
            .verticalScroll(rememberScrollState()),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MarketIndices(
                indices = indices,
                isLoading = isLoading,
                refresh = refresh,
            )
            Portfolio(
                watchList = watchList,
                isLoading = isLoading,
            )
            NewsFeed(
                news = news,
                preferredCategory = preferredCategory,
                onCategorySelected = setPreferredCategory,
                isLoading = isLoading,
                refresh = refresh,
            )
            MarketMovers(
                actives = actives,
                losers = losers,
                gainers = gainers,
                isLoading = isLoading,
                refresh = refresh,
            )
        }
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Preview
@Composable
fun PreviewHomeContent() {
    HomeContent(
        watchList = null,
        news = null,
        indices = null,
        actives = null,
        losers = null,
        gainers = null,
        preferredCategory = null,
        isLoading = false,
        isRefreshing = false,
        setPreferredCategory = {},
        refresh = {},
    )
}