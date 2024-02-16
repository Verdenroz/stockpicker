package com.farmingdale.stockscreener.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.farmingdale.stockscreener.R
import com.farmingdale.stockscreener.model.local.GeneralSearchData
import com.farmingdale.stockscreener.model.local.GeneralSearchMatch
import com.farmingdale.stockscreener.model.local.WatchList
import com.farmingdale.stockscreener.ui.theme.StockScreenerTheme
import com.farmingdale.stockscreener.ui.theme.background
import com.farmingdale.stockscreener.viewmodels.ImplMainViewModel
import com.farmingdale.stockscreener.viewmodels.base.MainViewModel
import kotlinx.coroutines.delay

@Composable
fun MainView(){
    val mainViewModel: MainViewModel = viewModel<ImplMainViewModel>()
    val results by mainViewModel.searchResults.collectAsState()
    val query by mainViewModel.query.collectAsState()
    val watchList by mainViewModel.watchList.collectAsState()

    LaunchedEffect(key1 = query){
        delay(500)
        mainViewModel.search(query)
    }
    StockScreenerTheme {
        SearchContent(
            searchResults = results,
            watchList = watchList,
            updateQuery = mainViewModel::updateQuery,
            addToWatchList = mainViewModel::addToWatchList,
            deleteFromWatchList = mainViewModel::deleteFromWatchList,
            clearWatchList = mainViewModel::clearWatchList
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchContent(
    searchResults: GeneralSearchData?,
    watchList: WatchList?,
    updateQuery: (String) -> Unit,
    addToWatchList: (String) -> Unit,
    deleteFromWatchList: (String) -> Unit,
    clearWatchList: () -> Unit
){
    var query by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }
    Scaffold(
        topBar = {
            DockedSearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = SearchBarDefaults.colors(
                    containerColor = Color.White,
                    dividerColor = Color.Transparent,
                    inputFieldColors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent,
                    )
                ),
                query = query,
                onQueryChange = {
                    query = it
                    updateQuery(query)
                },
                onSearch = {
                    active = false
                },
                active = active,
                onActiveChange = { active = it },
                placeholder = { Text("Search") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = stringResource(id = R.string.search_description)) },
            ) {
                searchResults?.matches?.forEach { match ->
                    ListItem(
                        modifier = Modifier
                            .clickable {
                                active = false
                            }
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        headlineContent = { Text(match.name) },
                        leadingContent = { Text(match.symbol) },
                        trailingContent = {
                            IconButton(onClick = { addToWatchList(match.symbol) }) {
                                Icon(Icons.Default.AddCircle, contentDescription = stringResource(id = R.string.add_description))
                            }
                        }
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(background)
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                content = {
                    item {
                        Text(
                            text = "Watchlist",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.displayMedium
                        )
                    }
                    watchList?.quotes?.forEach { quote ->
                        item {
                            ListItem(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 4.dp),
                                headlineContent = { Text(quote.name) },
                                leadingContent = { Text(quote.symbol) },
                                trailingContent = {
                                    IconButton(onClick = { deleteFromWatchList(quote.symbol) }) {
                                        Icon(Icons.Default.Clear, contentDescription = stringResource(id = R.string.delete))
                                    }
                                }
                            )
                        }
                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun PreviewSearchContent(){
    SearchContent(
        searchResults = null,
        watchList = null,
        updateQuery = {},
        addToWatchList = {},
        deleteFromWatchList = {},
        clearWatchList = {}
    )
}

@Preview
@Composable
fun PreviewSearchList(){
    val match = GeneralSearchMatch(
        symbol = "AAPL",
        name = "Apple Inc.",
        currency = "USD",
        stockExchange = "NASDAQ",
        exchangeShortName = "NASDAQ"
    )
    ListItem(
        headlineContent = { Text(match.name) },
        leadingContent = { Text(match.symbol) },
        trailingContent = { Icon(Icons.Default.AddCircle, contentDescription = null) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    )
}

