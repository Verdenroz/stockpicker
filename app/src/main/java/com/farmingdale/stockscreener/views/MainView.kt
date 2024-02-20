package com.farmingdale.stockscreener.views

import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.imageLoader
import coil.request.ImageRequest
import com.farmingdale.stockscreener.R
import com.farmingdale.stockscreener.model.local.GeneralSearchData
import com.farmingdale.stockscreener.model.local.GeneralSearchMatch
import com.farmingdale.stockscreener.model.local.WatchList
import com.farmingdale.stockscreener.model.local.news.Article
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
            refreshState = refreshState,
            updateQuery = mainViewModel::updateQuery,
            addToWatchList = mainViewModel::addToWatchList,
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
    refreshState: Boolean,
    updateQuery: (String) -> Unit,
    addToWatchList: (String) -> Unit,
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
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(id = R.string.news))
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = stringResource(id = R.string.news_settings)
                        )
                    }
                }
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    content = {
                        news?.articles?.forEach { article ->
                            item {
                                ContentCard(article)
                            }
                        }
                    }
                )
            }
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
        refreshState = false,
        updateQuery = {},
        addToWatchList = {},
        refresh = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    searchResults: GeneralSearchData?,
    updateQuery: (String) -> Unit,
    addToWatchList: (String) -> Unit,
) {
    var query by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }
    DockedSearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(2.dp, Color.LightGray, RoundedCornerShape(10)),
        colors = SearchBarDefaults.colors(
            containerColor = Color.White,
            dividerColor = Color.Transparent,
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
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = stringResource(id = R.string.search_description)
            )
        },
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
                        Icon(
                            Icons.Default.AddCircle,
                            contentDescription = stringResource(id = R.string.add_description)
                        )
                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun PreviewSearchList() {
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

@Composable
fun ContentCard(
    article: Article?
) {
    val context = LocalContext.current
    val gradient = Brush.verticalGradient(
        colors = listOf(Color.White, Color.LightGray)
    )

    var image by remember { mutableStateOf<ImageBitmap?>(null) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf(false) }

    LaunchedEffect(article!!.urlToImage) {
        loading = true
        error = false
        val request = ImageRequest.Builder(context)
            .data(article.urlToImage)
            .build()
        val result = (context.imageLoader.execute(request).drawable as? BitmapDrawable)?.bitmap
        image = result?.asImageBitmap()
        loading = false
        error = image == null
    }

    Box(
        modifier = Modifier
            .background(gradient)
            .size(300.dp, 150.dp)
            .shadow(1.dp),
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(10),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 5,
                    softWrap = true,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth(.4f)
                        .padding(8.dp)
                )
                when {
                    loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterVertically))
                    }

                    error -> {
                        Icon(Icons.Default.Warning, contentDescription = null)
                    }

                    else -> {
                        Image(bitmap = image!!, contentDescription = null)
                    }
                }
            }
        }
    }
}

@Composable
fun BottomBar() {
    Row(
        modifier = Modifier
            .background(Color.LightGray)
            .padding(8.dp)
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        BottomIcon(stringResource(id = R.string.home), Icons.Default.Home) {}
        BottomIcon(stringResource(id = R.string.watchlist), Icons.AutoMirrored.Filled.List) {}
        BottomIcon(stringResource(id = R.string.simulate), Icons.Default.Create) {}
        BottomIcon(stringResource(id = R.string.alerts), Icons.Default.Warning) {}
    }

}

@Composable
fun BottomIcon(
    text: String,
    icon: ImageVector,
    nav: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = { /*TODO*/ },
        ) {
            Icon(icon, contentDescription = text)
        }
        Text(
            text = text,
            modifier = Modifier.padding(top = 40.dp)
        )
    }
}