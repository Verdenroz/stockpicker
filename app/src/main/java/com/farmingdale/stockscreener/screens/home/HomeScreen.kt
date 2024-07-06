package com.farmingdale.stockscreener.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.farmingdale.stockscreener.model.local.MarketIndex
import com.farmingdale.stockscreener.model.local.MarketMover
import com.farmingdale.stockscreener.model.local.MarketSector
import com.farmingdale.stockscreener.model.local.News
import com.farmingdale.stockscreener.utils.DataError
import com.farmingdale.stockscreener.utils.Resource
import com.farmingdale.stockscreener.viewmodels.ImplHomeViewModel
import com.farmingdale.stockscreener.viewmodels.base.HomeViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    navController: NavController,
    snackbarHost: SnackbarHostState
) {
    val homeViewModel: HomeViewModel = viewModel<ImplHomeViewModel>()
    val sectors by homeViewModel.sectors.collectAsState()
    val news by homeViewModel.news.collectAsState()
    val indices by homeViewModel.indices.collectAsState()
    val actives by homeViewModel.actives.collectAsState()
    val losers by homeViewModel.losers.collectAsState()
    val gainers by homeViewModel.gainers.collectAsState()
    val isNetworkConnected by homeViewModel.isNetworkConnected.collectAsState()

    val initialCompositionCompleted = remember { mutableStateOf(false) }

    LaunchedEffect(isNetworkConnected) {
        // Only refresh if the network is connected and after the initial composition
        if (isNetworkConnected && initialCompositionCompleted.value) {
            homeViewModel.refresh()
        }
        // Mark the initial composition as completed after the first LaunchedEffect call
        initialCompositionCompleted.value = true
    }

    HomeContent(
        navController = navController,
        snackbarHost = snackbarHost,
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
    snackbarHost: SnackbarHostState,
    sectors: Resource<ImmutableList<MarketSector>, DataError.Network>,
    news: Resource<ImmutableList<News>, DataError.Network>,
    indices: Resource<ImmutableList<MarketIndex>, DataError.Network>,
    actives: Resource<ImmutableList<MarketMover>, DataError.Network>,
    losers: Resource<ImmutableList<MarketMover>, DataError.Network>,
    gainers: Resource<ImmutableList<MarketMover>, DataError.Network>,
    refresh: () -> Unit,
) {
    val pullRefreshState = rememberPullToRefreshState()
    if (pullRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            refresh()
            delay(500)
            if (listOf(sectors, news, indices, actives, losers, gainers).all { it is Resource.Success }) {
                pullRefreshState.endRefresh()
            }
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
                    snackbarHost = snackbarHost,
                )
            }
            item {
                MarketSectors(
                    sectors = sectors,
                    snackbarHost = snackbarHost,
                )
            }
            item {
                NewsFeed(
                    news = news,
                    snackbarHost = snackbarHost,
                )
            }
            item {
                MarketMovers(
                    listState = listState,
                    navController = navController,
                    snackbarHost = snackbarHost,
                    actives = actives,
                    losers = losers,
                    gainers = gainers,
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