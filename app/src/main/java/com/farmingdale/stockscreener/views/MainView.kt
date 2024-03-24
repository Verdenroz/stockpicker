package com.farmingdale.stockscreener.views

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.farmingdale.stockscreener.model.local.GeneralSearchData
import com.farmingdale.stockscreener.ui.theme.StockScreenerTheme
import com.farmingdale.stockscreener.viewmodels.ImplMainViewModel
import com.farmingdale.stockscreener.viewmodels.base.MainViewModel
import kotlinx.coroutines.delay

@Composable
fun MainView() {
    val mainViewModel: MainViewModel = viewModel<ImplMainViewModel>()
    val query by mainViewModel.query.collectAsState()
    val searchResults by mainViewModel.searchResults.collectAsState()
    LaunchedEffect(key1 = query) {
        delay(500)
        mainViewModel.search(query)
    }
    StockScreenerTheme {
        MainContent(
            searchResults = searchResults,
            updateQuery = { query -> mainViewModel.updateQuery(query) },
            addToWatchList = { symbol -> mainViewModel.addToWatchList(symbol) },
        )
    }
}

@Composable
fun MainContent(
    searchResults: GeneralSearchData?,
    updateQuery: (String) -> Unit,
    addToWatchList: (String) -> Unit,
) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            SearchBar(
                searchResults = searchResults,
                updateQuery = updateQuery,
                addToWatchList = addToWatchList
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                val items = listOf(
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
                Log.d("MainNavigation", "Home")
                HomeView()
            }
            composable(Screen.Watchlist.route) {
                Log.d("MainNavigation", "Watchlist")
                TODO("WatchList")
            }
            composable(Screen.Simulate.route) {
                Log.d("MainNavigation", "Simulation")
                TODO("Simulation")
            }
            composable(Screen.Alerts.route) {
                Log.d("MainNavigation", "Alerts")
                TODO("Alerts")
            }
        }
    }
}