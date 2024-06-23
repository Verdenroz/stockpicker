package com.farmingdale.stockscreener.views.stock

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import com.farmingdale.stockscreener.R
import com.farmingdale.stockscreener.model.local.FullQuoteData
import com.farmingdale.stockscreener.ui.theme.negativeTextColor
import com.farmingdale.stockscreener.ui.theme.positiveTextColor
import java.util.Locale

@Composable
fun StockHeadline(
    quote: FullQuoteData,
    bg: Color = MaterialTheme.colorScheme.surface
) {
    ListItem(
        headlineContent = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = String.format(Locale.US,"%.2f", quote.price),
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
        },
        supportingContent = {
            if (quote.postMarketPrice != null) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.after_hours) + quote.postMarketPrice.toString(),
                        style = MaterialTheme.typography.labelLarge,
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
        },
        trailingContent = {
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
                    modifier = Modifier.size(48.dp)
                )
            }
        },
        colors = ListItemDefaults.colors(
            containerColor = bg
        )
    )
    HorizontalDivider(
        thickness = Dp.Hairline,
        color = MaterialTheme.colorScheme.outline,
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview
@Composable
fun PreviewStockHeadline(
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
        ytdReturn = "1.23%",
        yearReturn = "1.23%",
        threeYearReturn = "1.23%",
        fiveYearReturn = "1.23%",
        logo = null
    )
) {
    StockHeadline(
        quote = quote
    )
}


