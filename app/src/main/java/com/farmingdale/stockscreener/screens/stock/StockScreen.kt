package com.farmingdale.stockscreener.screens.stock

import android.app.Application
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.farmingdale.stockscreener.R
import com.farmingdale.stockscreener.model.local.Analysis
import com.farmingdale.stockscreener.model.local.FullQuoteData
import com.farmingdale.stockscreener.model.local.HistoricalData
import com.farmingdale.stockscreener.model.local.Interval
import com.farmingdale.stockscreener.model.local.MarketSector
import com.farmingdale.stockscreener.model.local.News
import com.farmingdale.stockscreener.model.local.SimpleQuoteData
import com.farmingdale.stockscreener.model.local.TimePeriod
import com.farmingdale.stockscreener.model.local.indicators.AnalysisIndicators
import com.farmingdale.stockscreener.ui.theme.StockScreenerTheme
import com.farmingdale.stockscreener.utils.DataError
import com.farmingdale.stockscreener.utils.Resource
import com.farmingdale.stockscreener.viewmodels.ImplStockViewModel
import com.farmingdale.stockscreener.viewmodels.base.StockViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap

@Composable
fun StockScreen(
    symbol: String,
    navController: NavController,
    snackbarHost: SnackbarHostState,
    addToWatchList: (String) -> Unit,
    deleteFromWatchList: (String) -> Unit
) {
    val application = LocalContext.current.applicationContext as Application
    val stockViewModel: StockViewModel = viewModel<ImplStockViewModel>(
        key = symbol,
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ImplStockViewModel(symbol, application) as T
            }
        })
    val quote by stockViewModel.quote.collectAsState()
    val timeSeries by stockViewModel.timeSeries.collectAsState()
    val similarStocks by stockViewModel.similarStocks.collectAsState()
    val sectorPerformance by stockViewModel.sectorPerformance.collectAsState()
    val news by stockViewModel.news.collectAsState()
    val analysis by stockViewModel.analysis.collectAsState()
    val signals by stockViewModel.signals.collectAsState()
    val movingAverageSummary by stockViewModel.movingAveragesSummary.collectAsState()
    val oscillatorsSummary by stockViewModel.oscillatorsSummary.collectAsState()
    val trendsSummary by stockViewModel.trendsSummary.collectAsState()
    val overallSummary by stockViewModel.overallSummary.collectAsState()
    val watchList by stockViewModel.watchList.collectAsState()
    StockScreenerTheme {
        StockContent(
            navController = navController,
            snackbarHost = snackbarHost,
            symbol = symbol,
            quote = quote,
            timeSeries = timeSeries,
            similarStocks = similarStocks,
            sectorPerformance = sectorPerformance,
            news = news,
            analysis = analysis,
            signals = signals,
            movingAverageSummary = movingAverageSummary,
            oscillatorsSummary = oscillatorsSummary,
            trendsSummary = trendsSummary,
            overallSummary = overallSummary,
            watchList = watchList,
            updateTimeSeries = stockViewModel::updateTimeSeries,
            updateAnalysis = stockViewModel::updateAnalysis,
            addToWatchList = addToWatchList,
            deleteFromWatchList = deleteFromWatchList
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StockContent(
    symbol: String,
    navController: NavController,
    snackbarHost: SnackbarHostState,
    quote: Resource<FullQuoteData, DataError.Network>,
    timeSeries: Resource<ImmutableMap<String, HistoricalData>, DataError.Network>,
    similarStocks: Resource<ImmutableList<SimpleQuoteData>, DataError.Network>,
    sectorPerformance: Resource<MarketSector?, DataError.Network>,
    news: Resource<ImmutableList<News>, DataError.Network>,
    analysis: Resource<Analysis?, DataError.Network>,
    signals: ImmutableMap<AnalysisIndicators, String>,
    movingAverageSummary: Double,
    oscillatorsSummary: Double,
    trendsSummary: Double,
    overallSummary: Double,
    watchList: ImmutableList<SimpleQuoteData>,
    updateTimeSeries: (String, TimePeriod, Interval) -> Unit,
    updateAnalysis: (String, Interval) -> Unit,
    addToWatchList: (String) -> Unit,
    deleteFromWatchList: (String) -> Unit
) {
    // Adjust brightness of the background color based on the system theme (For better contrast on logos in dark theme)
    val brightnessAdjustment = if (isSystemInDarkTheme()) 2f else 1f
    val bg = MaterialTheme.colorScheme.surface.copy(
        red = MaterialTheme.colorScheme.surface.red * brightnessAdjustment,
        green = MaterialTheme.colorScheme.surface.green * brightnessAdjustment,
        blue = MaterialTheme.colorScheme.surface.blue * brightnessAdjustment
    )

    Scaffold(
        topBar = {
            StockTopBar(
                navController = navController,
                symbol = symbol,
                watchList = watchList,
                addToWatchList = addToWatchList,
                deleteFromWatchList = deleteFromWatchList
            )
        }
    ) { padding ->
        when (quote) {
            is Resource.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Text(
                        text = stringResource(id = R.string.loading_quote),
                        style = MaterialTheme.typography.titleMedium,
                        letterSpacing = 1.5.sp
                    )
                }
            }

            is Resource.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = stringResource(id = R.string.error)
                    )
                    Text(text = stringResource(id = R.string.error_loading_data))
                }
            }

            is Resource.Success -> {
                val listState = rememberLazyListState()
                Box(
                    modifier = Modifier.nestedScroll(rememberNestedScrollInteropConnection()),
                ) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .background(bg),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        stickyHeader {
                            StockHeadline(quote = quote.data, bg = bg)
                        }

                        item {
                            StockChart(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 200.dp, max = 400.dp),
                                listState = listState,
                                snackbarHost = snackbarHost,
                                symbol = quote.data.symbol,
                                timeSeries = timeSeries,
                                backgroundColor = bg,
                                updateTimeSeries = updateTimeSeries,
                            )
                        }

                        quote.data.ytdReturn?.let {
                            item {
                                StockPerformance(
                                    snackbarHost = snackbarHost,
                                    quote = quote.data,
                                    sectorPerformance = sectorPerformance
                                )
                            }
                        }

                        item {
                            SimilarStockFeed(
                                symbol = quote.data.symbol,
                                similarStocks = similarStocks,
                                navController = navController,
                                snackbarHost = snackbarHost
                            )
                        }

                        item {
                            StockViewPager(
                                snackbarHost = snackbarHost,
                                quote = quote.data,
                                news = news,
                                analysis = analysis,
                                signals = signals,
                                movingAverageSummary = movingAverageSummary,
                                oscillatorsSummary = oscillatorsSummary,
                                trendsSummary = trendsSummary,
                                overallSummary = overallSummary,
                                updateAnalysis = updateAnalysis,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewStockView() {
    StockScreen(
        symbol = "AAPL",
        snackbarHost = SnackbarHostState(),
        navController = rememberNavController(),
        addToWatchList = {},
        deleteFromWatchList = {}
    )
}