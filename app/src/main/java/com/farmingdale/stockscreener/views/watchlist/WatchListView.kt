package com.farmingdale.stockscreener.views.watchlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.farmingdale.stockscreener.model.local.FullQuoteData
import com.farmingdale.stockscreener.model.local.WatchList
import com.farmingdale.stockscreener.ui.theme.negativeBackgroundColor
import com.farmingdale.stockscreener.ui.theme.negativeTextColor
import com.farmingdale.stockscreener.ui.theme.positiveBackgroundColor
import com.farmingdale.stockscreener.ui.theme.positiveTextColor

@Composable
fun WatchListView(
    watchList: WatchList?,
    deleteFromWatchList: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        content = {
            watchList?.quotes?.forEach { quote ->
                item {
                    WatchListStock(
                        quoteData = (quote),
                        deleteFromWatchList = deleteFromWatchList,
                    )
                }
            }
        }
    )
}

@Composable
fun WatchListStock(
    quoteData: FullQuoteData,
    deleteFromWatchList: (String) -> Unit
) {
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
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
                text = quoteData.price.toString(),
                style = MaterialTheme.typography.labelLarge,
                color = if (quoteData.change > 0) positiveTextColor else negativeTextColor,
                modifier = Modifier
                    .background(
                        if (quoteData.change > 0) positiveBackgroundColor else negativeBackgroundColor
                    )
                    .padding(8.dp)
            )
        }
    )
}