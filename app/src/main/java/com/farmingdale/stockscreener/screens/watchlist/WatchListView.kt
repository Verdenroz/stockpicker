package com.farmingdale.stockscreener.screens.watchlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.farmingdale.stockscreener.model.local.SimpleQuoteData
import com.farmingdale.stockscreener.ui.theme.negativeBackgroundColor
import com.farmingdale.stockscreener.ui.theme.negativeTextColor
import com.farmingdale.stockscreener.ui.theme.positiveBackgroundColor
import com.farmingdale.stockscreener.ui.theme.positiveTextColor
import kotlinx.collections.immutable.ImmutableList
import java.util.Locale

@Composable
fun WatchListView(
    navController: NavHostController,
    watchList: ImmutableList<SimpleQuoteData>?,
    deleteFromWatchList: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        content = {
            items(
                items = watchList ?: emptyList(),
                key = { watchList -> watchList.symbol }
            ) { item ->
                WatchListStock(
                    item, navController, deleteFromWatchList
                )
            }
        }
    )
}

@Composable
fun WatchListStock(
    quoteData: SimpleQuoteData,
    navController: NavHostController,
    deleteFromWatchList: (String) -> Unit
) {
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { navController.navigate("stock/${quoteData.symbol}") },
        headlineContent = {
            Text(
                text = quoteData.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Light,
            )
        },
        leadingContent = {
            Text(
                text = quoteData.symbol,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier
                    .clip(RoundedCornerShape(25))
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(8.dp)
            )
        },
        trailingContent = {
            Text(
                text = String.format(Locale.US, "%.2f", quoteData.price),
                style = MaterialTheme.typography.labelLarge,
                color = if (quoteData.change.contains('+')) positiveTextColor else negativeTextColor,
                modifier = Modifier
                    .background(
                        if (quoteData.change.contains('+')) positiveBackgroundColor else negativeBackgroundColor
                    )
                    .padding(8.dp)
            )
        }
    )
}