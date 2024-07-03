package com.farmingdale.stockscreener.views.main

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.farmingdale.stockscreener.R
import com.farmingdale.stockscreener.model.local.RegionFilter
import com.farmingdale.stockscreener.model.local.SearchResult
import com.farmingdale.stockscreener.model.local.SimpleQuoteData
import com.farmingdale.stockscreener.model.local.TypeFilter
import com.farmingdale.stockscreener.ui.theme.StockScreenerTheme
import com.farmingdale.stockscreener.utils.UiText
import com.farmingdale.stockscreener.viewmodels.ImplMainViewModel
import com.farmingdale.stockscreener.viewmodels.base.MainEvent
import com.farmingdale.stockscreener.viewmodels.base.MainViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.coroutines.delay

@Composable
fun MainView() {
    val mainViewModel: MainViewModel = viewModel<ImplMainViewModel>()
    val query by mainViewModel.query.collectAsState()
    val searchResults by mainViewModel.searchResults.collectAsState()
    val watchList by mainViewModel.watchList.collectAsState()
    val regionFilter by mainViewModel.regionFilter.collectAsState()
    val typeFilters by mainViewModel.typeFilter.collectAsState()
    val isNetworkConnected by mainViewModel.isNetworkConnected.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(key1 = query) {
        delay(250)
        mainViewModel.search(query)
    }

    LaunchedEffect(Unit) {
        mainViewModel.events.collect { event ->
            when (event) {
                is MainEvent.Error -> {
                    snackbarHostState.showSnackbar(
                        message = event.message.asString(context),
                        actionLabel = UiText.StringResource(R.string.dismiss).asString(context),
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    LaunchedEffect(isNetworkConnected) {
        if (!isNetworkConnected) {
            snackbarHostState.showSnackbar(
                message = context.getString(R.string.no_internet),
                duration = SnackbarDuration.Indefinite
            )
        } else {
            snackbarHostState.currentSnackbarData?.dismiss()
        }
    }

    StockScreenerTheme {
        MainContent(
            snackbarHost = snackbarHostState,
            searchResults = searchResults,
            watchList = watchList,
            regionFilter = regionFilter,
            typeFilters = typeFilters,
            updateRegionFilter = { region -> mainViewModel.updateRegionFilter(region) },
            toggleTypeFilter = { type, isChecked -> mainViewModel.toggleTypeFilter(type, isChecked) },
            updateQuery = { query -> mainViewModel.updateQuery(query) },
            addToWatchList = { symbol -> mainViewModel.addToWatchList(symbol) },
            deleteFromWatchList = { symbol -> mainViewModel.deleteFromWatchList(symbol) },
        )
    }
}

@Composable
fun MainContent(
    snackbarHost: SnackbarHostState,
    searchResults: ImmutableList<SearchResult>?,
    watchList: ImmutableList<SimpleQuoteData>,
    regionFilter: RegionFilter,
    typeFilters: ImmutableSet<TypeFilter>,
    updateRegionFilter: (RegionFilter) -> Unit,
    toggleTypeFilter: (TypeFilter, Boolean) -> Unit,
    updateQuery: (String) -> Unit,
    addToWatchList: (String) -> Unit,
    deleteFromWatchList: (String) -> Unit,
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Scaffold(
        snackbarHost = { SnackbarHost(
            hostState = snackbarHost,
            snackbar = { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    dismissActionContentColor = MaterialTheme.colorScheme.onSurface,
                )
            }
        ) },
        topBar = {
            if (currentRoute != "stock/{symbol}") {
                SearchBar(
                    navController = navController,
                    searchResults = searchResults,
                    watchList = watchList,
                    regionFilter = regionFilter,
                    typeFilters = typeFilters,
                    updateRegionFilter = updateRegionFilter,
                    toggleTypeFilter = toggleTypeFilter,
                    updateQuery = updateQuery,
                    addToWatchList = addToWatchList,
                    deleteFromWatchList = deleteFromWatchList,
                )
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.onSurface,
            ) {
                val currentDestination = navBackStackEntry?.destination
                val items = immutableListOf(
                    Screen.Home,
                    Screen.Watchlist,
                    Screen.Simulate,
                    Screen.Alerts
                )
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                screen.icon,
                                contentDescription = stringResource(id = screen.titleResource)
                            )
                        },
                        label = { Text(stringResource(id = screen.titleResource)) },
                        selected = currentDestination?.route == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Home.route) {
                HomeView(
                    navController = navController,
                )
            }
            composable(Screen.Watchlist.route) {
                WatchListView(
                    navController = navController,
                    watchList = watchList,
                    deleteFromWatchList = deleteFromWatchList
                )
            }
            composable(Screen.Simulate.route) {
                Log.d("MainNavigation", "Simulation")
                TODO("Simulation")
            }
            composable(Screen.Alerts.route) {
                Log.d("MainNavigation", "Alerts")
                TODO("Alerts")
            }
            composable("stock/{symbol}") { backStackEntry ->
                val symbol = backStackEntry.arguments?.getString("symbol")
                if (symbol != null) {
                    StockView(
                        symbol = symbol,
                        navController = navController,
                        addToWatchList = addToWatchList,
                        deleteFromWatchList = deleteFromWatchList
                    )
                }
            }
        }
    }
}