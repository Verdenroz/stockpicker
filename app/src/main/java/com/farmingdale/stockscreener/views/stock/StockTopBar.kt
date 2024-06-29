package com.farmingdale.stockscreener.views.stock

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmingdale.stockscreener.R
import com.farmingdale.stockscreener.model.local.FullQuoteData
import com.farmingdale.stockscreener.model.local.SimpleQuoteData
import com.farmingdale.stockscreener.ui.theme.StockScreenerTheme

@Composable
fun StockTopBar(
    symbol: String,
    quote: FullQuoteData?,
    watchList: List<SimpleQuoteData>,
    addToWatchList: (String) -> Unit,
    deleteFromWatchList: (String) -> Unit,
) {
    Column(
        modifier = Modifier.background(MaterialTheme.colorScheme.surfaceDim),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = stringResource(id = R.string.back),
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                quote?.let {
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
                Text(
                    text = symbol,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .clip(RoundedCornerShape(25))
                        .background(MaterialTheme.colorScheme.onPrimaryContainer)
                        .padding(4.dp)
                )
            }
            if (watchList.any { it.symbol == symbol }) {
                IconButton(onClick = { deleteFromWatchList(symbol) }) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = stringResource(id = R.string.remove_description)
                    )
                }
            } else {
                IconButton(onClick = { addToWatchList(symbol) }) {
                    Icon(
                        Icons.Default.AddCircle,
                        contentDescription = stringResource(id = R.string.add_description)
                    )
                }
            }
        }
        HorizontalDivider(
            thickness = Dp.Hairline,
            color = MaterialTheme.colorScheme.surfaceTint,
        )
    }
}

@Preview
@Composable
fun PreviewStockTopBar(
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
        logo = "https://logo.clearbit.com/apple.com"
    )
) {
    StockScreenerTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            StockTopBar(
                symbol = quote?.symbol.orEmpty(),
                quote = quote,
                watchList = emptyList(),
                addToWatchList = {},
                deleteFromWatchList = {}
            )
        }
    }
}