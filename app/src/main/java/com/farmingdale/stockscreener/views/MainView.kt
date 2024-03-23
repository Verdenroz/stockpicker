package com.farmingdale.stockscreener.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.farmingdale.stockscreener.model.local.GeneralSearchData
import com.farmingdale.stockscreener.model.local.WatchList
import com.farmingdale.stockscreener.model.local.googlefinance.GoogleFinanceStock
import com.farmingdale.stockscreener.model.local.googlefinance.MarketIndex
import com.farmingdale.stockscreener.model.local.news.Category
import com.farmingdale.stockscreener.model.local.news.News
import com.farmingdale.stockscreener.ui.theme.StockScreenerTheme
import com.farmingdale.stockscreener.viewmodels.ImplMainViewModel
import com.farmingdale.stockscreener.viewmodels.base.MainViewModel
import kotlinx.coroutines.delay

@Composable
fun MainView() {
    val mainViewModel: MainViewModel = viewModel<ImplMainViewModel>()
    val results by mainViewModel.searchResults.collectAsState()
    val query by mainViewModel.query.collectAsState()
    val watchList by mainViewModel.watchList.collectAsState()
    val news by mainViewModel.news.collectAsState()
    val indices by mainViewModel.indices.collectAsState()
    val actives by mainViewModel.actives.collectAsState()
    val losers by mainViewModel.losers.collectAsState()
    val gainers by mainViewModel.gainers.collectAsState()
    val isRefreshing by mainViewModel.isRefreshing.collectAsState()
    val isLoading by mainViewModel.isLoading.collectAsState()
    val preferredCategory by mainViewModel.preferredCategory.collectAsState()

    LaunchedEffect(key1 = query) {
        delay(500)
        mainViewModel.search(query)
    }
    StockScreenerTheme {
        MainContent(
            searchResults = results,
            watchList = watchList,
            news = news,
            indices = indices,
            actives = actives,
            losers = losers,
            gainers = gainers,
            preferredCategory = preferredCategory,
            isLoading = isLoading,
            isRefreshing = isRefreshing,
            updateQuery = mainViewModel::updateQuery,
            addToWatchList = mainViewModel::addToWatchList,
            setPreferredCategory = mainViewModel::setPreferredCategory,
            refresh = mainViewModel::refresh
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainContent(
    searchResults: GeneralSearchData?,
    watchList: WatchList?,
    news: News?,
    indices: List<MarketIndex>?,
    actives: List<GoogleFinanceStock>?,
    losers: List<GoogleFinanceStock>?,
    gainers: List<GoogleFinanceStock>?,
    preferredCategory: Category?,
    isLoading: Boolean,
    isRefreshing: Boolean,
    updateQuery: (String) -> Unit,
    addToWatchList: (String) -> Unit,
    setPreferredCategory: (Category) -> Unit,
    refresh: () -> Unit,
) {
    val pullRefreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = refresh)
    Scaffold(
        topBar = {
            SearchBar(
                searchResults = searchResults,
                updateQuery = updateQuery,
                addToWatchList = addToWatchList
            )
        },
        bottomBar = {
            BottomBar()
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
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
}

@Preview
@Composable
fun PreviewMainContent() {
    MainContent(
        searchResults = null,
        watchList = null,
        news = null,
        indices = null,
        actives = null,
        losers = null,
        gainers = null,
        preferredCategory = null,
        isLoading = false,
        isRefreshing = false,
        updateQuery = {},
        addToWatchList = {},
        setPreferredCategory = {},
        refresh = {},
    )
}