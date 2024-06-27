package com.farmingdale.stockscreener.views.stock

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.farmingdale.stockscreener.R
import com.farmingdale.stockscreener.model.local.Analysis
import com.farmingdale.stockscreener.model.local.FullQuoteData
import com.farmingdale.stockscreener.model.local.Interval
import com.farmingdale.stockscreener.model.local.News
import com.farmingdale.stockscreener.model.local.indicators.AnalysisIndicators
import com.farmingdale.stockscreener.views.stock.analysis.StockAnalysis
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StockViewPager(
    quote: FullQuoteData,
    news: List<News>,
    analysis: Analysis?,
    signals: Map<AnalysisIndicators, String>,
    movingAverageSummary: Double,
    oscillatorsSummary: Double,
    trendsSummary: Double,
    overallSummary: Double,
    updateAnalysisInterval: (String, Interval) -> Unit
) {
    val state = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier
            .requiredHeightIn(
                min = 300.dp,
                max = if (news.size < 5 && state.currentPage == 1 || (analysis == null && state.currentPage == 2)) 300.dp else 900.dp
            )
            .fillMaxWidth(),
        topBar = {
            StockPagerTabs(
                state = state,
                scope = scope
            )
        }
    ) { padding ->
        HorizontalPager(
            state = state,
            modifier = Modifier
                .padding(padding)
        ) { page ->
            when (page) {
                0 -> {
                    StockSummary(quote = quote)
                }

                1 -> {
                    StockNewsFeed(news = news)
                }

                2 -> {
                    StockAnalysis(
                        symbol = quote.symbol,
                        analysis = analysis,
                        signals = signals,
                        movingAverageSummary = movingAverageSummary,
                        oscillatorSummary = oscillatorsSummary,
                        trendSummary = trendsSummary,
                        overallSummary = overallSummary,
                        updateInterval = updateAnalysisInterval
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StockPagerTabs(
    state: PagerState,
    scope: CoroutineScope
) {
    val tabTitles = listOf(
        stringResource(id = R.string.summary),
        stringResource(id = R.string.news),
        stringResource(id = R.string.analysis)
    )
    TabRow(
        selectedTabIndex = state.currentPage,
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurface,
        indicator = { tabPositions ->
            SecondaryIndicator(
                Modifier.tabIndicatorOffset(tabPositions[state.currentPage]),
            )
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        tabTitles.forEachIndexed { index, title ->
            Tab(
                text = { Text(title) },
                selected = state.currentPage == index,
                onClick = {
                    scope.launch {
                        state.animateScrollToPage(index)
                    }
                }
            )
        }
    }
}


@Preview
@Composable
fun PreviewStockViewPager(
    quote: FullQuoteData = FullQuoteData(
        name = "Apple Inc.",
        symbol = "AAPL",
        price = 113.2,
        postMarketPrice = 179.74,
        change = "+1.23",
        percentChange = "+1.5%",
        high = 143.45,
        low = 110.45,
        open = 123.45,
        volume = "12345678",
        marketCap = "1.23T",
        pe = 12.34,
        eps = 1.23,
        beta = 1.23,
        yearHigh = 163.45,
        yearLow = 100.45,
        dividend = 1.23,
        yield = "1.23%",
        netAssets = null,
        nav = null,
        expenseRatio = null,
        category = "Blend",
        lastCapitalGain = "10.00",
        morningstarRating = "★★",
        morningstarRisk = "Low",
        holdingsTurnover = "1.23%",
        lastDividend = "0.05",
        inceptionDate = "Jan 1, 2022",
        exDividend = "Jan 1, 2022",
        earningsDate = "Jan 1, 2022",
        avgVolume = "12345678",
        sector = "Technology",
        industry = "Consumer Electronics",
        about = "Apple Inc. is an American multinational technology company that designs, manufactures, and markets consumer electronics, computer software, and online services. It is considered one of the Big Five companies in the U.S. information technology industry, along with Amazon, Google, Microsoft, and Facebook.",
        ytdReturn = "1.23%",
        yearReturn = "1.23%",
        threeYearReturn = "1.23%",
        fiveYearReturn = "1.23%",
        logo = "https://logo.clearbit.com/apple.com"
    )
) {
    Surface {
        StockViewPager(
            quote = quote,
            news = emptyList(),
            analysis = null,
            signals = emptyMap(),
            movingAverageSummary = 0.0,
            oscillatorsSummary = 0.0,
            trendsSummary = 0.0,
            overallSummary = 0.0,
            updateAnalysisInterval = { _, _ -> }
        )
    }
}