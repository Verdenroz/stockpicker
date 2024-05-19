package com.farmingdale.stockscreener.views.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.farmingdale.stockscreener.R
import com.farmingdale.stockscreener.model.local.SimpleQuoteData
import com.farmingdale.stockscreener.ui.theme.indexColor
import com.farmingdale.stockscreener.ui.theme.negativeTextColor
import com.farmingdale.stockscreener.ui.theme.positiveTextColor

@Composable
fun Portfolio(
    watchList: List<SimpleQuoteData>?,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.portfolio),
                style = MaterialTheme.typography.titleSmall
            )
        }
        if (watchList.isNullOrEmpty()) {
            Card {
                Text(
                    text = stringResource(id = R.string.portfolio_empty),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(8.dp)
                )
            }

        } else {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                content = {
                    items(watchList) { quote ->
                        PortfolioStockCard(quote = quote)
                    }
                }
            )
        }
    }
}

@Composable
fun PortfolioStockCard(quote: SimpleQuoteData) {
    Card(
        modifier = Modifier
            .size(125.dp, 75.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = quote.name,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = indexColor
            )
            Text(
                text = quote.price.toString(),
                style = MaterialTheme.typography.titleSmall
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = quote.change,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (quote.change.contains('+')) positiveTextColor else negativeTextColor
                )
                Text(
                    text = quote.percentChange,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (quote.change.contains('+')) positiveTextColor else negativeTextColor
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewPortfolioStockCard() {
    Row {
        val dummyQuote = SimpleQuoteData(
            symbol = "DUMMY",
            name = "Dummy Inc.",
            price = 100.0,
            change = "+10.0",
            percentChange = "+10%"
        )
        PortfolioStockCard(
            quote = dummyQuote
        )
        PortfolioStockCard(quote = dummyQuote)
    }
}