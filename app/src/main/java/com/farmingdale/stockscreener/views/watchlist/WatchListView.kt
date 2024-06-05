package com.farmingdale.stockscreener.views.watchlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.farmingdale.stockscreener.model.local.SimpleQuoteData
import com.farmingdale.stockscreener.ui.theme.negativeBackgroundColor
import com.farmingdale.stockscreener.ui.theme.negativeTextColor
import com.farmingdale.stockscreener.ui.theme.positiveBackgroundColor
import com.farmingdale.stockscreener.ui.theme.positiveTextColor
import java.util.Locale

@Composable
fun WatchListView(
    watchList: List<SimpleQuoteData>?,
    deleteFromWatchList: (String) -> Unit
) {
    val navController = rememberNavController()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        content = {
            watchList?.forEach { quote ->
                item {
                    WatchListStock(
                        quoteData = quote,
                        navController = navController,
                        deleteFromWatchList = deleteFromWatchList,
                    )
                }
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