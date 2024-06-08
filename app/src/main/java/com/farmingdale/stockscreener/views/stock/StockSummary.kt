package com.farmingdale.stockscreener.views.stock

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmingdale.stockscreener.R
import com.farmingdale.stockscreener.model.local.FullQuoteData

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StockSummary(quote: FullQuoteData) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainerLow),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StockProfile(quote = quote)

        FlowRow(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StockDetailCell(
                label = stringResource(id = R.string.open),
                detailValue = { SimpleDetailText(text = quote.open.toString()) },
                weight = 1f
            )
            StockDetailCell(
                label = stringResource(id = R.string.days_range),
                detailValue = {
                    PriceRangeLine(
                        low = quote.low,
                        high = quote.high,
                        current = quote.price
                    )
                },
                weight = 1f
            )
            StockDetailCell(
                label = stringResource(id = R.string.fifty_two_week_range),
                detailValue = {
                    PriceRangeLine(
                        low = quote.yearLow,
                        high = quote.yearHigh,
                        current = quote.price
                    )
                },
                weight = 1f
            )
            StockDetailCell(
                label = stringResource(id = R.string.volume),
                detailValue = { SimpleDetailText(text = formatVolume(quote.volume)) },
                weight = 1f
            )
            StockDetailCell(
                label = stringResource(id = R.string.avg_volume),
                detailValue = { SimpleDetailText(text = formatVolume(quote.avgVolume)) },
                weight = 1f
            )
            StockDetailCell(
                label = stringResource(id = R.string.market_cap),
                detailValue = { SimpleDetailText(text = quote.marketCap) },
                weight = 1f
            )

            quote.netAssets?.let {
                StockDetailCell(
                    label = stringResource(id = R.string.net_assets),
                    detailValue = { SimpleDetailText(text = it) },
                    weight = 1f
                )
            }

            quote.nav?.let {
                StockDetailCell(
                    label = stringResource(id = R.string.nav),
                    detailValue = { SimpleDetailText(text = it.toString()) },
                    weight = 1f
                )
            }

            quote.pe?.let {
                StockDetailCell(
                    label = stringResource(id = R.string.pe_ratio),
                    detailValue = { SimpleDetailText(text = it.toString()) },
                    weight = 1f
                )
            }
            quote.eps?.let {
                StockDetailCell(
                    label = stringResource(id = R.string.eps),
                    detailValue = { SimpleDetailText(text = it.toString()) },
                    weight = 1f
                )
            }
            quote.beta?.let {
                StockDetailCell(
                    label = stringResource(id = R.string.beta),
                    detailValue = { SimpleDetailText(text = it.toString()) },
                    weight = 1f
                )
            }

            quote.expenseRatio?.let {
                StockDetailCell(
                    label = stringResource(id = R.string.expense_ratio),
                    detailValue = { SimpleDetailText(text = it) },
                    weight = 1f
                )
            }

            quote.dividend?.let {
                StockDetailCell(
                    label = stringResource(id = R.string.dividend_yield),
                    detailValue = { SimpleDetailText(text = it.toString() + " (" + quote.yield + ")") },
                    weight = 1f
                )
            }

            quote.exDividend?.let {
                StockDetailCell(
                    label = stringResource(id = R.string.ex_dividend),
                    detailValue = { SimpleDetailText(text = it) },
                    weight = 1f
                )
            }

            quote.earningsDate?.let {
                StockDetailCell(
                    label = stringResource(id = R.string.earnings_date),
                    detailValue = { SimpleDetailText(text = it) },
                    weight = 1f
                )
            }
        }
    }
}

@Composable
fun StockProfile(quote: FullQuoteData) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        quote.about?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                letterSpacing = 1.25.sp,
                maxLines = 10,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            quote.sector?.let {
                OutlinedButton(
                    onClick = { /*TODO*/ },
                    shape = CircleShape
                ) {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            quote.industry?.let {
                OutlinedButton(
                    onClick = { /*TODO*/ },
                    shape = CircleShape,
                ) {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRowScope.StockDetailCell(
    label: String,
    detailValue: @Composable () -> Unit,
    weight: Float
) {
    ListItem(
        headlineContent = {
            Text(
                text = label,
                style = MaterialTheme.typography.titleSmall,
                letterSpacing = 1.25.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        },
        trailingContent = {
            detailValue()
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        modifier = Modifier
            .let {
                if (weight == 1f) it.fillMaxWidth() else it.weight(weight)
            }
    )
}

@Composable
fun PriceRangeLine(low: Double, high: Double, current: Double) {
    val fraction = ((current - low) / (high - low)).toFloat().coerceAtLeast(.01f)

    Row(
        modifier = Modifier
            .fillMaxWidth(.65f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SimpleDetailText(text = low.toString())
        Box(
            modifier = Modifier
                .weight(fraction)
                .height(1.dp)
                .background(MaterialTheme.colorScheme.onSurface),
        )
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSurface),
        )
        Box(
            modifier = Modifier
                .weight((1 - fraction).coerceAtLeast(.01f))
                .height(1.dp)
                .background(MaterialTheme.colorScheme.onSurface),
        )
        SimpleDetailText(text = high.toString())
    }
}

@Composable
fun SimpleDetailText(
    text: String
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        letterSpacing = 1.5.sp,
        modifier = Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(4.dp)
    )
}

@Preview
@Composable
fun PreviewStockSummary(
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
        volume = 12345678,
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
        exDividend = "Jan 1, 2022",
        earningsDate = "Jan 1, 2022",
        avgVolume = 12345678,
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
        StockSummary(quote = quote)
    }
}