package com.farmingdale.stockscreener.views.stock

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import com.farmingdale.stockscreener.R
import com.farmingdale.stockscreener.model.local.FullQuoteData
import com.farmingdale.stockscreener.model.local.HistoricalData
import com.farmingdale.stockscreener.model.local.Interval
import com.farmingdale.stockscreener.model.local.TimePeriod
import com.farmingdale.stockscreener.ui.theme.StockScreenerTheme
import com.farmingdale.stockscreener.ui.theme.negativeTextColor
import com.farmingdale.stockscreener.ui.theme.positiveTextColor
import com.farmingdale.stockscreener.viewmodels.ImplStockViewModel
import com.farmingdale.stockscreener.viewmodels.base.StockViewModel
import java.util.Locale

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
    StockScreenerTheme {
        StockContent(
            quote = quote,
            timeSeries = timeSeries,
            updateTimeSeries = stockViewModel::getTimeSeries,
        )
    }
}

@Composable
fun StockContent(
    quote: FullQuoteData?,
    timeSeries: Map<String, HistoricalData> = emptyMap(),
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(bg),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(.8f),
                        verticalArrangement = Arrangement.SpaceBetween,
                    )
                    {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = quote.price.toString(),
                                style = MaterialTheme.typography.displaySmall,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            Text(
                                text = quote.change,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = let { if (quote.change.startsWith("-")) negativeTextColor else positiveTextColor },
                            )
                            Text(
                                text = "(${quote.percentChange})",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = let { if (quote.change.startsWith("-")) negativeTextColor else positiveTextColor },
                            )
                        }
                        if (quote.postMarketPrice != null) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(id = R.string.after_hours) + quote.postMarketPrice.toString(),
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                                Text(
                                    text = String.format(
                                        Locale.US,
                                        "%.2f",
                                        (quote.postMarketPrice - quote.price)
                                    ),
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    color = let { if (quote.postMarketPrice < quote.price) negativeTextColor else positiveTextColor },
                                )
                                Text(
                                    text = String.format(
                                        Locale.US,
                                        "(%.2f%%)",
                                        (quote.postMarketPrice - quote.price) / quote.price * 100
                                    ),
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    color = let { if (quote.postMarketPrice < quote.price) negativeTextColor else positiveTextColor },
                                )
                            }
                        }
                    }
                    if (quote.logo != null) {
                        // Filter the logo with the background color
                        AsyncImage(
                            model = quote.logo,
                            contentDescription = stringResource(id = R.string.logo),
                            imageLoader = ImageLoader(LocalContext.current),
                            colorFilter = ColorFilter.lighting(
                                add = Color.Transparent,
                                multiply = bg
                            ),
                            filterQuality = FilterQuality.High,
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp)
                        )
                    }
                }
                if (timeSeries.isEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator()
                    }
                }
                else{
                    StockChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(.3f),
                        symbol = quote.symbol,
                        timeSeries = timeSeries.entries.toList().asReversed().associate { it.key to it.value },
                        positiveChart = timeSeries.values.first().close > timeSeries.values.last().close,
                        backgroundColor = bg,
                        updateTimeSeries = updateTimeSeries,
                    )
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
        exDividend = 1.23,
        earningsDate = "2022-01-01",
        avgVolume = 12345678,
        sector = "Technology",
        industry = "Consumer Electronics",
        about = "Apple Inc. is an American multinational technology company that designs, manufactures, and markets consumer electronics, computer software, and online services. It is considered one of the Big Five companies in the U.S. information technology industry, along with Amazon, Google, Microsoft, and Facebook.",
        logo = "https://logo.clearbit.com/apple.com"
    )
) {
    StockView(quote = quote)
}