package com.farmingdale.stockscreener.views.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.farmingdale.stockscreener.model.local.MarketMover
import com.farmingdale.stockscreener.ui.theme.negativeBackgroundColor
import com.farmingdale.stockscreener.ui.theme.negativeTextColor
import com.farmingdale.stockscreener.ui.theme.positiveBackgroundColor
import com.farmingdale.stockscreener.ui.theme.positiveTextColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MarketMovers(
    actives: List<MarketMover>?,
    losers: List<MarketMover>?,
    gainers: List<MarketMover>?,
    refresh: () -> Unit,
) {
    val state = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, start = 16.dp, end = 16.dp)
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
                        0 -> stringResource(id = R.string.mostActive)
                        1 -> stringResource(id = R.string.topGainers)
                        2 -> stringResource(id = R.string.topLosers)
                        else -> stringResource(id = R.string.mostActive)
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
            modifier = Modifier
                .padding(horizontal = 16.dp)
        ) { page ->
            when (page) {
                0 -> {
                    MarketMoversList(
                        stocks = actives,
                        refresh = refresh
                    )
                }

                1 -> {
                    MarketMoversList(
                        stocks = gainers,
                        refresh = refresh
                    )
                }

                2 -> {
                    MarketMoversList(
                        stocks = losers,
                        refresh = refresh
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewMarketMovers() {
    MarketMovers(
        actives = null,
        losers = null,
        gainers = null,
        refresh = {}
    )
}

@Composable
fun MarketMoversList(
    stocks: List<MarketMover>?,
    refresh: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (stocks.isNullOrEmpty()) {
            ErrorCard(refresh = refresh)
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                stocks.forEach { stock ->
                    item {
                        MarketMoverStock(stock = stock)
                    }
                }
            }
        }

    }

}

@Composable
fun MarketMoverStock(stock: MarketMover) {
    Row(
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stock.symbol,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = stock.name,
                style = MaterialTheme.typography.labelSmall
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = String.format(Locale.US, "%.2f", stock.price.toDouble()),
            modifier = Modifier
                .weight(1f)
                .wrapContentSize()
                .padding(4.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = stock.change,
            color = let { if (stock.change.startsWith("-")) negativeTextColor else positiveTextColor },
            modifier = Modifier
                .weight(1f)
                .wrapContentSize()
                .background(
                    if (stock.change.startsWith("-")) negativeBackgroundColor else positiveBackgroundColor
                )
                .padding(4.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = stock.percentChange,
            color = let { if (stock.change.startsWith("-")) negativeTextColor else positiveTextColor },
            modifier = Modifier
                .weight(1f)
                .wrapContentSize()
                .background(
                    if (stock.change.startsWith("-")) negativeBackgroundColor else positiveBackgroundColor
                )
                .padding(4.dp)
        )
    }
}
