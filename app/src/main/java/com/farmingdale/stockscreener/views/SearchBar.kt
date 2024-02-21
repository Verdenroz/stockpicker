package com.farmingdale.stockscreener.views

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.farmingdale.stockscreener.R
import com.farmingdale.stockscreener.model.local.GeneralSearchData
import com.farmingdale.stockscreener.model.local.GeneralSearchMatch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    searchResults: GeneralSearchData?,
    updateQuery: (String) -> Unit,
    addToWatchList: (String) -> Unit,
) {
    var query by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }
    DockedSearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(2.dp, Color.LightGray, RoundedCornerShape(10)),
        colors = SearchBarDefaults.colors(
            containerColor = Color.White,
            dividerColor = Color.Transparent,
        ),
        query = query,
        onQueryChange = {
            query = it
            updateQuery(query)
        },
        onSearch = {
            active = false
        },
        active = active,
        onActiveChange = { active = it },
        placeholder = { Text("Search") },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = stringResource(id = R.string.search_description)
            )
        },
    ) {
        searchResults?.matches?.forEach { match ->
            ListItem(
                modifier = Modifier
                    .clickable {
                        active = false
                    }
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                headlineContent = { Text(match.name) },
                leadingContent = { Text(match.symbol) },
                trailingContent = {
                    IconButton(onClick = { addToWatchList(match.symbol) }) {
                        Icon(
                            Icons.Default.AddCircle,
                            contentDescription = stringResource(id = R.string.add_description)
                        )
                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun PreviewSearchList() {
    val match = GeneralSearchMatch(
        symbol = "AAPL",
        name = "Apple Inc.",
        currency = "USD",
        stockExchange = "NASDAQ",
        exchangeShortName = "NASDAQ"
    )
    ListItem(
        headlineContent = { Text(match.name) },
        leadingContent = { Text(match.symbol) },
        trailingContent = { Icon(Icons.Default.AddCircle, contentDescription = null) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    )
}