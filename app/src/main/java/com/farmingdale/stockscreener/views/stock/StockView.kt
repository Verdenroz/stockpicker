package com.farmingdale.stockscreener.views.stock

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.farmingdale.stockscreener.model.local.FullQuoteData
import com.farmingdale.stockscreener.model.local.HistoricalData
import com.farmingdale.stockscreener.model.local.Interval
import com.farmingdale.stockscreener.model.local.News
import com.farmingdale.stockscreener.model.local.SimpleQuoteData
import com.farmingdale.stockscreener.model.local.TimePeriod
import com.farmingdale.stockscreener.ui.theme.StockScreenerTheme
import com.farmingdale.stockscreener.viewmodels.ImplStockViewModel
import com.farmingdale.stockscreener.viewmodels.base.StockViewModel

@Composable
fun StockView(
    quote: FullQuoteData?
) {
    val stockViewModel: StockViewModel = viewModel<ImplStockViewModel>(
        key = quote?.symbol ?: "",
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ImplStockViewModel(quote?.symbol ?: "") as T
            }
        })
    val timeSeries by stockViewModel.timeSeries.collectAsState()
    val similarStocks by stockViewModel.similarStocks.collectAsState()
    val news by stockViewModel.news.collectAsState()
    StockScreenerTheme {
        StockContent(
            quote = quote,
            timeSeries = timeSeries,
            similarStocks = similarStocks,
            news = news,
            updateTimeSeries = stockViewModel::getTimeSeries,
        )
    }
}

@Composable
fun StockContent(
    quote: FullQuoteData?,
    timeSeries: Map<String, HistoricalData> = emptyMap(),
    similarStocks: List<SimpleQuoteData> = emptyList(),
    news: List<News> = emptyList(),
    updateTimeSeries: (String, TimePeriod, Interval) -> Unit,
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
            StockTopBar(quote = quote)
        }
    ) { padding ->
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
                    item {
                        StockHeadline(quote = quote, bg = bg)
                    }
                    item {
                        if (timeSeries.isEmpty()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                LinearProgressIndicator()
                            }
                        } else {
                            StockChart(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 200.dp, max = 400.dp),
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
                        SimilarStockFeed(similarStocks = similarStocks)
                    }
                    item {
                        StockViewPager(
                            quote = quote,
                            news = news
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewStockView(
    quote: FullQuoteData? = FullQuoteData(
        name = "Apple Inc.",
        symbol = "AAPL",
        price = 180.11,
        postMarketPrice = 179.74,
        change = "+1.23",
        percentChange = "+1.5%",
        high = 123.45,
        low = 123.45,
        open = 123.45,
        volume = 12345678,
        marketCap = "1.23T",
        pe = 12.34,
        eps = 1.23,
        beta = 1.23,
        yearHigh = 123.45,
        yearLow = 123.45,
        dividend = 1.23,
        yield = "1.23%",
        netAssets = null,
        nav = null,
        expenseRatio = null,
        exDividend = "2022-01-01",
        earningsDate = "2022-01-01",
        avgVolume = 12345678,
        sector = "Technology",
        industry = "Consumer Electronics",
        about = "Apple Inc. is an American multinational technology company that designs, manufactures, and markets consumer electronics, computer software, and online services. It is considered one of the Big Five companies in the U.S. information technology industry, along with Amazon, Google, Microsoft, and Facebook.",
        ytdReturn = "1.23%",
        yearReturn = "1.23%",
        threeYearReturn = "1.23%",
        fiveYearReturn = "1.23%",
        logo = null
    )
) {
    StockView(quote = quote)
}