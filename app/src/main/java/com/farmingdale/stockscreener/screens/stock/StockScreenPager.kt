package com.farmingdale.stockscreener.screens.stock

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.farmingdale.stockscreener.R
import com.farmingdale.stockscreener.model.local.Analysis
import com.farmingdale.stockscreener.model.local.FullQuoteData
import com.farmingdale.stockscreener.model.local.Interval
import com.farmingdale.stockscreener.model.local.News
import com.farmingdale.stockscreener.model.local.indicators.AnalysisIndicators
import com.farmingdale.stockscreener.utils.DataError
import com.farmingdale.stockscreener.utils.Resource
import com.farmingdale.stockscreener.screens.stock.analysis.StockAnalysis
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StockViewPager(
    snackbarHost: SnackbarHostState,
    quote: FullQuoteData,
    news: Resource<ImmutableList<News>, DataError.Network>,
    analysis: Resource<Analysis?, DataError.Network>,
    signals: ImmutableMap<AnalysisIndicators, String>,
    movingAverageSummary: Double,
    oscillatorsSummary: Double,
    trendsSummary: Double,
    overallSummary: Double,
    updateAnalysis: (String, Interval) -> Unit,
) {
    val state = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier
            .requiredHeightIn(
                min = 300.dp,
                max = if (
                    (news is Resource.Success && news.data.size < 5 && state.currentPage == 1) ||
                    (analysis is Resource.Success && analysis.data == null && state.currentPage == 2) ||
                    (analysis is Resource.Error && state.currentPage == 2)
                ) 300.dp else 900.dp
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
                    StockNewsFeed(
                        snackbarHost = snackbarHost,
                        news = news
                    )
                }

                2 -> {
                    StockAnalysis(
                        symbol = quote.symbol,
                        snackbarHost = snackbarHost,
                        analysis = analysis,
                        signals = signals,
                        movingAverageSummary = movingAverageSummary,
                        oscillatorSummary = oscillatorsSummary,
                        trendSummary = trendsSummary,
                        overallSummary = overallSummary,
                        updateAnalysis = updateAnalysis,
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