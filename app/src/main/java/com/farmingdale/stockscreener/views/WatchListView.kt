package com.farmingdale.stockscreener.views

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.farmingdale.stockscreener.R
import com.farmingdale.stockscreener.model.local.WatchList

@Composable
fun WatchListView(
    watchList: WatchList?,
    deleteFromWatchList: (String) -> Unit
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize(),
        content = {
            item {
                Text(
                    text = stringResource(id = R.string.welcome),
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.displayMedium
                )
            }
            watchList?.quotes?.forEach { quote ->
                item {
                    ListItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        headlineContent = { Text(quote.name) },
                        leadingContent = { Text(quote.symbol) },
                        trailingContent = {
                            IconButton(onClick = { deleteFromWatchList(quote.symbol) }) {
                                Icon(
                                    Icons.Default.Clear,
                                    contentDescription = stringResource(id = R.string.delete)
                                )
                            }
                        }
                    )
                }
            }
        }
    )
}