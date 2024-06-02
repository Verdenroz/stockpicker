package com.farmingdale.stockscreener.views.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.farmingdale.stockscreener.R
import com.farmingdale.stockscreener.model.local.RegionFilter
import com.farmingdale.stockscreener.model.local.SearchResult
import com.farmingdale.stockscreener.model.local.SimpleQuoteData
import com.farmingdale.stockscreener.model.local.TypeFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    searchResults: List<SearchResult>?,
    watchList: List<SimpleQuoteData>?,
    regionFilter: RegionFilter,
    typeFilters: List<TypeFilter>,
    updateRegionFilter: (RegionFilter) -> Unit,
    toggleTypeFilter: (TypeFilter, Boolean) -> Unit,
    updateQuery: (String) -> Unit,
    addToWatchList: (String) -> Unit,
    deleteFromWatchList: (String) -> Unit,
) {
    var query by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }
    var showFilters by rememberSaveable { mutableStateOf(false) }
    DockedSearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceDim,
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
        placeholder = {
            Text(
                stringResource(id = R.string.search),
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = stringResource(id = R.string.search_description)
            )
        },
        trailingIcon = {
            Box {
                IconButton(onClick = { showFilters = !showFilters }) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = stringResource(id = R.string.filter)
                    )
                }
                DropdownMenu(
                    expanded = showFilters,
                    onDismissRequest = { showFilters = false },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .fillMaxWidth(.75f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween,
                    ) {
                        TypeCheckBoxContainer(
                            typeFilters = typeFilters,
                            toggleTypeFilter = toggleTypeFilter,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth()
                        )
                        RegionFilterContainer(
                            currentRegionFilter = regionFilter,
                            updateRegionFilter = updateRegionFilter,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth()
                        )
                    }

                }
            }
        }
    ) {
        searchResults?.forEach { match ->
            val isInWatchListState = watchList?.any { it.symbol == match.symbol } ?: false
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
                    if (isInWatchListState) {
                        IconButton(onClick = { deleteFromWatchList(match.symbol) }) {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = stringResource(id = R.string.remove_description)
                            )
                        }
                    } else {
                        IconButton(onClick = { addToWatchList(match.symbol) }) {
                            Icon(
                                Icons.Default.AddCircle,
                                contentDescription = stringResource(id = R.string.add_description)
                            )
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun TypeCheckBoxContainer(
    typeFilters: List<TypeFilter>,
    toggleTypeFilter: (TypeFilter, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        TypeCheckBox(
            type = TypeFilter.STOCK,
            checked = TypeFilter.STOCK in typeFilters,
            onCheckedChange = { isChecked -> toggleTypeFilter(TypeFilter.STOCK, isChecked) }
        )
        TypeCheckBox(
            type = TypeFilter.ETF,
            checked = TypeFilter.ETF in typeFilters,
            onCheckedChange = { isChecked -> toggleTypeFilter(TypeFilter.ETF, isChecked) }
        )
        TypeCheckBox(
            type = TypeFilter.TRUST,
            checked = TypeFilter.TRUST in typeFilters,
            onCheckedChange = { isChecked -> toggleTypeFilter(TypeFilter.TRUST, isChecked) }
        )
    }
}

@Composable
fun TypeCheckBox(
    type: TypeFilter,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
        Text(
            text = when (type) {
                TypeFilter.STOCK -> stringResource(id = R.string.stock)
                TypeFilter.ETF -> stringResource(id = R.string.etf)
                TypeFilter.TRUST -> stringResource(id = R.string.trust)
            },
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RegionFilterContainer(
    currentRegionFilter: RegionFilter,
    updateRegionFilter: (RegionFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        RegionFilter.entries.forEach { region ->
            RegionFilterChip(
                region = region,
                currentRegionFilter = currentRegionFilter,
                updateRegionFilter = updateRegionFilter
            )
        }
    }
}

@Composable
fun RegionFilterChip(
    region: RegionFilter,
    currentRegionFilter: RegionFilter,
    updateRegionFilter: (RegionFilter) -> Unit
) {
    FilterChip(
        selected = region == currentRegionFilter,
        onClick = { updateRegionFilter(region) },
        label = {
            Text(
                text = when (region) {
                    RegionFilter.US -> stringResource(id = R.string.US)
                    RegionFilter.NA -> stringResource(id = R.string.NA)
                    RegionFilter.SA -> stringResource(id = R.string.SA)
                    RegionFilter.EU -> stringResource(id = R.string.EU)
                    RegionFilter.AS -> stringResource(id = R.string.AS)
                    RegionFilter.ME -> stringResource(id = R.string.ME)
                    RegionFilter.AF -> stringResource(id = R.string.AF)
                    RegionFilter.AU -> stringResource(id = R.string.AU)
                    RegionFilter.GLOBAL -> stringResource(id = R.string.GLOBAL)
                },
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        trailingIcon = {
            if (region == currentRegionFilter) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = stringResource(id = R.string.filter_selected)
                )
            }
        }
    )
}

@Preview
@Composable
fun PreviewSearchList() {
    val match = SearchResult(
        symbol = "AAPL",
        name = "Apple Inc.",
        exchangeShortName = "NASDAQ",
        exchange = "NASDAQ",
        type = "stock"
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