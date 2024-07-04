package com.farmingdale.stockscreener.screens.stock

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.farmingdale.stockscreener.R
import com.farmingdale.stockscreener.model.local.SimpleQuoteData
import com.farmingdale.stockscreener.utils.DataError
import com.farmingdale.stockscreener.utils.Resource
import com.farmingdale.stockscreener.utils.UiText
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList


@Composable
fun SimilarStockFeed(
    symbol: String,
    similarStocks: Resource<ImmutableList<SimpleQuoteData>, DataError.Network>,
    navController: NavController,
    snackbarHost: SnackbarHostState,
) {
    val context = LocalContext.current
    when (similarStocks) {
        is Resource.Loading -> {
            SimilarStockFeedSkeleton()
        }

        is Resource.Error -> {
            SimilarStockFeedSkeleton()

            LaunchedEffect(similarStocks.error) {
                snackbarHost.showSnackbar(
                    message = similarStocks.error.asUiText().asString(context),
                    actionLabel = UiText.StringResource(R.string.dismiss).asString(context),
                    duration = SnackbarDuration.Short
                )
            }
        }

        is Resource.Success -> {
            if (similarStocks.data.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.similar_stocks) + ": $symbol",
                        style = MaterialTheme.typography.titleMedium,
                        letterSpacing = 1.25.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(
                            items = similarStocks.data,
                            key = { stock -> stock.symbol }
                        ) {
                            StockCard(
                                quote = it,
                                navController = navController
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
fun PreviewSimilarStockFeed() {
    SimilarStockFeed(
        symbol = "AAPL",
        similarStocks = Resource.Success(
            listOf(
                SimpleQuoteData(
                    symbol = "AAPL",
                    name = "Apple Inc.",
                    price = 145.12,
                    change = "+0.12",
                    percentChange = "+0.12%",
                ),
                SimpleQuoteData(
                    symbol = "GOOGL",
                    name = "Alphabet Inc.",
                    price = 145.12,
                    change = "+0.12",
                    percentChange = "+0.12%",
                ),
                SimpleQuoteData(
                    symbol = "MSFT",
                    name = "Microsoft Corporation",
                    price = 145.12,
                    change = "+0.12",
                    percentChange = "+0.12%",
                )
            ).toImmutableList(),
        ),
        navController = rememberNavController(),
        snackbarHost = SnackbarHostState()
    )
}

@Composable
fun SimilarStockFeedSkeleton(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surfaceContainer
) {
    LazyRow {
        repeat(5) {
            item(key = it) {
                Card(
                    modifier = modifier.size(width = 125.dp, height = 75.dp),
                    colors = CardDefaults.cardColors(containerColor = color)
                ) {
                    // skeleton
                }
            }
        }
    }
}