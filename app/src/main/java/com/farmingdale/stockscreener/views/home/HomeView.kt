package com.farmingdale.stockscreener.views.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.farmingdale.stockscreener.model.local.MarketIndex
import com.farmingdale.stockscreener.model.local.MarketMover
import com.farmingdale.stockscreener.model.local.MarketSector
import com.farmingdale.stockscreener.model.local.News
import com.farmingdale.stockscreener.viewmodels.ImplHomeViewModel
import com.farmingdale.stockscreener.viewmodels.base.HomeViewModel
import kotlinx.coroutines.delay

@Composable
fun HomeView(
    navController: NavController
) {
    val homeViewModel: HomeViewModel = viewModel<ImplHomeViewModel>()
    val sectors by homeViewModel.sectors.collectAsState()
    val news by homeViewModel.news.collectAsState()
    val indices by homeViewModel.indices.collectAsState()
    val actives by homeViewModel.actives.collectAsState()
    val losers by homeViewModel.losers.collectAsState()
    val gainers by homeViewModel.gainers.collectAsState()

    HomeContent(
        navController = navController,
        sectors = sectors,
        news = news,
        indices = indices,
        actives = actives,
        losers = losers,
        gainers = gainers,
        refresh = homeViewModel::refresh
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    navController: NavController,
    sectors: List<MarketSector>?,
    news: List<News>?,
    indices: List<MarketIndex>?,
    actives: List<MarketMover>?,
    losers: List<MarketMover>?,
    gainers: List<MarketMover>?,
    refresh: () -> Unit,
) {
    val pullRefreshState = rememberPullToRefreshState()
    if (pullRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            refresh()
            delay(1000L)
            pullRefreshState.endRefresh()
        }
    }
    val listState = rememberLazyListState()
    Box(
        modifier = Modifier.nestedScroll(pullRefreshState.nestedScrollConnection),
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                MarketIndices(
                    indices = indices,
                    refresh = refresh,
                )
            }
            item {
                MarketSectors(
                    sectors = sectors,
                    refresh = refresh,
                )
            }
            item {
                NewsFeed(
                    news = news,
                    refresh = refresh,
                )
            }
            item {
                MarketMovers(
                    listState = listState,
                    navController = navController,
                    actives = actives,
                    losers = losers,
                    gainers = gainers,
                    refresh = refresh,
                )
            }
        }
        PullToRefreshContainer(
            state = pullRefreshState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-16).dp)
        )
    }
}

@Preview
@Composable
fun PreviewHomeContent() {
    HomeContent(
        navController = rememberNavController(),
        sectors = null,
        news = null,
        indices = null,
        actives = null,
        losers = null,
        gainers = null,
        refresh = {},
    )
}