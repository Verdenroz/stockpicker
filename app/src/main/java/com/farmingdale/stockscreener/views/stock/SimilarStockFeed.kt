package com.farmingdale.stockscreener.views.stock

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.farmingdale.stockscreener.model.local.SimpleQuoteData
import com.farmingdale.stockscreener.views.home.PortfolioStockCard


@Composable
fun SimilarStockFeed(similarStocks: List<SimpleQuoteData>) {
    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
    ){

        Text(
            text = "Similar Stocks",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(8.dp)
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(
                items = similarStocks,
                key = { stock -> stock.symbol }
            ) {
                PortfolioStockCard(quote = it)
            }
        }
    }

}

@Preview
@Composable
fun PreviewSimilarStockFeed(){
    SimilarStockFeed(
        similarStocks = listOf(
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
            ),
        )
    )
}