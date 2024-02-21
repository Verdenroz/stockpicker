package com.farmingdale.stockscreener.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
    val refreshState by mainViewModel.refreshState.collectAsState()
    val isLoading by mainViewModel.isLoading.collectAsState()
    val preferredCategory by mainViewModel.preferredCategory.collectAsState()
    val preferredQuery by mainViewModel.preferredQuery.collectAsState()

    LaunchedEffect(key1 = query) {
        delay(500)
        mainViewModel.search(query)
    }

    StockScreenerTheme {
        MainContent(
            searchResults = results,
            watchList = watchList,
            news = news,
            preferredCategory = preferredCategory,
            preferredQuery = preferredQuery,
            refreshState = refreshState,
            isLoading = isLoading,
            updateQuery = mainViewModel::updateQuery,
            addToWatchList = mainViewModel::addToWatchList,
            setPreferredCategory = mainViewModel::setPreferredCategory,
            setPreferredQuery = mainViewModel::setPreferredQuery,
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
    preferredCategory: Category?,
    preferredQuery: String?,
    refreshState: Boolean,
    isLoading: Boolean,
    updateQuery: (String) -> Unit,
    addToWatchList: (String) -> Unit,
    setPreferredCategory: (Category) -> Unit,
    setPreferredQuery: (String) -> Unit,
    refresh: () -> Unit
) {
    val pullRefreshState = rememberPullRefreshState(refreshing = refreshState, onRefresh = refresh)
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
            NewsFeed(
                news = news,
                isLoading = isLoading,
                preferredCategory = preferredCategory,
                onCategorySelected = setPreferredCategory,
                preferredQuery = preferredQuery,
                onQuerySelected = setPreferredQuery,
                refresh = refresh
            )
            PullRefreshIndicator(
                refreshing = refreshState,
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
        preferredCategory = null,
        preferredQuery = null,
        refreshState = false,
        isLoading = false,
        updateQuery = {},
        addToWatchList = {},
        setPreferredCategory = {},
        setPreferredQuery = {},
        refresh = {}
    )
}