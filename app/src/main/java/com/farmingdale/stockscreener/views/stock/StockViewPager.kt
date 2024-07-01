package com.farmingdale.stockscreener.views.stock

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.farmingdale.stockscreener.R
import com.farmingdale.stockscreener.model.local.Analysis
import com.farmingdale.stockscreener.model.local.FullQuoteData
import com.farmingdale.stockscreener.model.local.Interval
import com.farmingdale.stockscreener.model.local.News
import com.farmingdale.stockscreener.model.local.indicators.AnalysisIndicators
import com.farmingdale.stockscreener.utils.Resource
import com.farmingdale.stockscreener.views.stock.analysis.StockAnalysis
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StockViewPager(
    quote: FullQuoteData,
    news: Resource<List<News>>,
    analysis: Resource<Analysis>,
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
                max = if ((news.data?.size
                        ?: 0) < 5 && state.currentPage == 1 || (analysis.data == null && state.currentPage == 2)
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
                    when (news) {
                        is Resource.Error -> {
                            StockError(
                                modifier = Modifier
                                    .height(300.dp)
                                    .fillMaxWidth(),
                            )
                        }

                        is Resource.Loading -> {
                            Box(
                                modifier = Modifier
                                    .height(300.dp)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        is Resource.Success -> {
                            if (news.data.isNullOrEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .height(300.dp)
                                        .background(MaterialTheme.colorScheme.surfaceContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.no_news),
                                        style = MaterialTheme.typography.titleSmall,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(100))
                                            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                                    )
                                }
                            } else {
                                StockNewsFeed(news = news.data)
                            }
                        }
                    }
                }

                2 -> {
                    when (analysis) {
                        is Resource.Error -> {
                            StockError(
                                modifier = Modifier
                                    .height(300.dp)
                                    .fillMaxWidth(),
                            )
                        }

                        is Resource.Loading -> {
                            Box(
                                modifier = Modifier
                                    .height(300.dp)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        is Resource.Success -> {
                            if (analysis.data == null) {
                                Box(
                                    modifier = Modifier
                                        .height(300.dp)
                                        .background(MaterialTheme.colorScheme.surfaceContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.no_analysis),
                                        style = MaterialTheme.typography.titleSmall,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(100))
                                            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                                    )
                                }
                            } else {
                                StockAnalysis(
                                    symbol = quote.symbol,
                                    analysis = analysis.data,
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