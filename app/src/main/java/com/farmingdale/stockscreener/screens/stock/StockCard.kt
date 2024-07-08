package com.farmingdale.stockscreener.screens.stock

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.farmingdale.stockscreener.model.local.SimpleQuoteData
import com.farmingdale.stockscreener.ui.theme.negativeTextColor
import com.farmingdale.stockscreener.ui.theme.positiveTextColor

@Composable
fun StockCard(
    quote: SimpleQuoteData,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .size(width = 125.dp, height = 75.dp)
            .clickable {
                navController.navigate("stock/${quote.symbol}")
            }
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
                color = MaterialTheme.colorScheme.onSurface
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
fun PreviewStockCard() {
    Row {
        val dummyQuote = SimpleQuoteData(
            symbol = "DUMMY",
            name = "Dummy Inc.",
            price = 100.0,
            change = "+10.0",
            percentChange = "+10%"
        )
        StockCard(
            quote = dummyQuote,
            navController = rememberNavController()
        )
        StockCard(
            quote = dummyQuote,
            navController = rememberNavController()
        )
    }
}