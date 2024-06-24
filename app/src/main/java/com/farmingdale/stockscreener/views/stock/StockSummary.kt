package com.farmingdale.stockscreener.views.stock

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmingdale.stockscreener.R
import com.farmingdale.stockscreener.model.local.FullQuoteData
import java.util.Locale

@Composable
fun StockSummary(quote: FullQuoteData) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.surfaceContainerLow),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StockProfile(quote = quote)
        StockDetailCell(
            label = stringResource(id = R.string.open),
            detailValue = { SimpleDetailText(text = quote.open.toString()) },
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

            )
        StockDetailCell(
            label = stringResource(id = R.string.volume),
            detailValue = { SimpleDetailText(text = formatVolume(quote.volume)) },
        )
        StockDetailCell(
            label = stringResource(id = R.string.avg_volume),
            detailValue = { SimpleDetailText(text = formatVolume(quote.avgVolume)) },
        )

        quote.marketCap?.let {
            StockDetailCell(
                label = stringResource(id = R.string.market_cap),
                detailValue = { SimpleDetailText(text = it) },
            )
        }

        quote.netAssets?.let {
            StockDetailCell(
                label = stringResource(id = R.string.net_assets),
                detailValue = { SimpleDetailText(text = it) },
            )
        }

        quote.nav?.let {
            StockDetailCell(
                label = stringResource(id = R.string.nav),
                detailValue = { SimpleDetailText(text = it.toString()) },
            )
        }

        quote.pe?.let {
            StockDetailCell(
                label = stringResource(id = R.string.pe_ratio),
                detailValue = { SimpleDetailText(text = it.toString()) },
            )
        }
        quote.eps?.let {
            StockDetailCell(
                label = stringResource(id = R.string.eps),
                detailValue = { SimpleDetailText(text = it.toString()) },
            )
        }
        quote.beta?.let {
            StockDetailCell(
                label = stringResource(id = R.string.beta),
                detailValue = { SimpleDetailText(text = it.toString()) },
            )
        }

        quote.expenseRatio?.let {
            StockDetailCell(
                label = stringResource(id = R.string.expense_ratio),
                detailValue = { SimpleDetailText(text = it) },
            )
        }

        quote.dividend?.let {
            StockDetailCell(
                label = stringResource(id = R.string.dividend_yield),
                detailValue = { SimpleDetailText(text = it.toString() + " (" + quote.yield + ")") },
            )
        }

        quote.exDividend?.let {
            StockDetailCell(
                label = stringResource(id = R.string.ex_dividend),
                detailValue = { SimpleDetailText(text = it) },
            )
        }

        quote.earningsDate?.let {
            StockDetailCell(
                label = stringResource(id = R.string.earnings_date),
                detailValue = { SimpleDetailText(text = it) },
            )
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
            val isExpanded = remember { mutableStateOf(it.length < 300) }
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    letterSpacing = 1.25.sp,
                    maxLines = if (isExpanded.value) Int.MAX_VALUE else 10,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.clickable { isExpanded.value = !isExpanded.value }
                )
                if (it.length > 300) {
                    ClickableText(
                        text = if (isExpanded.value) AnnotatedString(stringResource(id = R.string.show_less))
                        else AnnotatedString(stringResource(id = R.string.show_more)),
                        modifier = Modifier.align(Alignment.End),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            letterSpacing = 1.25.sp,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.End
                        ),
                        onClick = { isExpanded.value = !isExpanded.value },
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            quote.sector?.let {
                OutlinedButton(
                    onClick = { TODO("Navigate to sector") },
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
                    onClick = { TODO("Navigate to industry") },
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

@Composable
fun StockDetailCell(
    label: String,
    detailValue: @Composable () -> Unit,
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
        )
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
        SimpleDetailText(text = String.format(Locale.US, "%.2f", low))
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
        SimpleDetailText(text = String.format(Locale.US, "%.2f", high))
    }
}

@Composable
fun SimpleDetailText(
    text: String
) {
    Text(
        text = formatText(text),
        style = MaterialTheme.typography.labelLarge,
        letterSpacing = 1.5.sp,
        modifier = Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(4.dp)
    )
}

private fun formatText(text: String): String {
    // For Dividend Yield
    if (text.contains('(') && text.contains(')')) {
        val parts = text.split(' ')
        val firstPart = parts[0]
        val secondPart = parts[1].removeSurrounding("(", "%)")

        val formattedFirstPart = try {
            String.format(Locale.US, "%.2f", firstPart.toDouble())
        } catch (e: NumberFormatException) {
            firstPart
        }

        val formattedSecondPart = try {
            String.format(Locale.US, "%.2f", secondPart.toDouble())
        } catch (e: NumberFormatException) {
            secondPart
        }

        return "$formattedFirstPart ($formattedSecondPart%)"
    }

    return try {
        String.format(Locale.US, "%.2f", text.toDouble())
    } catch (e: NumberFormatException) {
        text
    }
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