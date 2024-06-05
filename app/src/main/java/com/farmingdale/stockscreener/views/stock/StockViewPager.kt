package com.farmingdale.stockscreener.views.stock

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.farmingdale.stockscreener.R
import com.farmingdale.stockscreener.model.local.FullQuoteData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StockViewPager(
    quote: FullQuoteData,
) {
    val state = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainer)
        ) {
            IconButton(
                onClick = {
                    scope.launch(Dispatchers.Default) {
                        state.animateScrollToPage(state.currentPage - 1)
                    }
                },
                enabled = state.currentPage > 0
            ) {
                Icon(
                    Icons.AutoMirrored.Default.KeyboardArrowLeft,
                    contentDescription = stringResource(
                        id = R.string.previous
                    )
                )
            }
            Text(
                text = let {
                    when (state.currentPage) {
                        0 -> stringResource(id = R.string.summary)
                        1 -> stringResource(id = R.string.news)
                        2 -> stringResource(id = R.string.analysis)
                        else -> stringResource(id = R.string.summary)
                    }
                },
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            IconButton(
                onClick = {
                    scope.launch(Dispatchers.Default) {
                        state.animateScrollToPage(state.currentPage + 1)
                    }
                },
                enabled = state.currentPage < 2
            ) {
                Icon(
                    Icons.AutoMirrored.Default.KeyboardArrowRight,
                    contentDescription = stringResource(
                        id = R.string.forward
                    )
                )
            }
        }
        HorizontalPager(
            state = state,
        ) { page ->
            when (page) {
                0 -> {
                    StockSummary(quote = quote)
                }

                1 -> {
                    TODO("News not yet implemented")
                }

                2 -> {
                    TODO(" Analysis not yet implemented")
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewStockViewPager(
    quote: FullQuoteData = FullQuoteData(
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
        logo = "https://logo.clearbit.com/apple.com"
    )
) {
    Surface {
        StockViewPager(quote = quote)
    }
}