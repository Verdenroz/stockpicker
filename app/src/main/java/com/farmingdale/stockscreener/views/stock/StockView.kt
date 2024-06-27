package com.farmingdale.stockscreener.views.stock

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.farmingdale.stockscreener.viewmodels.ImplStockViewModel
import com.farmingdale.stockscreener.viewmodels.base.StockViewModel

@Composable
fun StockView(
    symbol: String,
    navController: NavController,
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
            updateTimeSeries = stockViewModel::getTimeSeries,
            updateAnalysisInterval = stockViewModel::getAnalysis,
            addToWatchList = addToWatchList,
            deleteFromWatchList = deleteFromWatchList
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StockContent(
    navController: NavController,
    symbol: String,
    quote: FullQuoteData?,
    timeSeries: Map<String, HistoricalData> = emptyMap(),
    similarStocks: List<SimpleQuoteData> = emptyList(),
    sectorPerformance: MarketSector? = null,
    news: List<News> = emptyList(),
    analysis: Analysis? = null,
    signals: Map<AnalysisIndicators, String> = emptyMap(),
    movingAverageSummary: Double,
    oscillatorsSummary: Double,
    trendsSummary: Double,
    overallSummary: Double,
    watchList: List<SimpleQuoteData> = emptyList(),
    updateTimeSeries: (String, TimePeriod, Interval) -> Unit,
    updateAnalysisInterval: (String, Interval) -> Unit,
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
        val listState = rememberLazyListState()
        if (quote != null) {
            Box(
                modifier = Modifier.nestedScroll(rememberNestedScrollInteropConnection()),
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .background(bg),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    stickyHeader {
                        StockHeadline(quote = quote, bg = bg)
                    }
                    item {
                        if (timeSeries.isEmpty()) {
                            LinearProgressIndicator(Modifier.fillMaxWidth())
                        } else {
                            StockChart(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 200.dp, max = 400.dp),
                                listState = listState,
                                symbol = quote.symbol,
                                timeSeries = timeSeries.entries.toList().asReversed()
                                    .associate { it.key to it.value },
                                positiveChart = timeSeries.values.first().close > timeSeries.values.last().close,
                                backgroundColor = bg,
                                updateTimeSeries = updateTimeSeries,
                            )
                        }
                    }
                    item {
                        StockPerformance(
                            quote = quote,
                            sectorPerformance = sectorPerformance
                        )
                    }
                    if (similarStocks.isNotEmpty()) {
                        item {
                            SimilarStockFeed(
                                symbol = quote.symbol,
                                similarStocks = similarStocks,
                                navController = navController
                            )
                        }
                    }
                    item {
                        StockViewPager(
                            quote = quote,
                            news = news,
                            analysis = analysis,
                            signals = signals,
                            movingAverageSummary = movingAverageSummary,
                            oscillatorsSummary = oscillatorsSummary,
                            trendsSummary = trendsSummary,
                            overallSummary = overallSummary,
                            updateAnalysisInterval = updateAnalysisInterval
                        )
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Text(text = stringResource(id = R.string.loading_quote))
            }
        }
    }
}

@Preview
@Composable
fun PreviewStockView() {
    StockView(
        symbol = "AAPL",
        navController = rememberNavController(),
        addToWatchList = {},
        deleteFromWatchList = {}
    )
}