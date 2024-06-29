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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmingdale.stockscreener.R
import com.farmingdale.stockscreener.model.local.SimpleQuoteData


@Composable
fun SimilarStockFeed(
    symbol: String,
    similarStocks: List<SimpleQuoteData>
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
    ){
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
                items = similarStocks,
                key = { stock -> stock.symbol }
            ) {
                StockCard(quote = it)
            }
        }
    }

}

@Preview
@Composable
fun PreviewSimilarStockFeed(){
    SimilarStockFeed(
        symbol = "AAPL",
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