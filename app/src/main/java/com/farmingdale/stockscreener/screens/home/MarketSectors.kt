package com.farmingdale.stockscreener.screens.home

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.farmingdale.stockscreener.R
import com.farmingdale.stockscreener.model.local.MarketSector
import com.farmingdale.stockscreener.ui.theme.indexColor
import com.farmingdale.stockscreener.ui.theme.negativeTextColor
import com.farmingdale.stockscreener.ui.theme.positiveTextColor
import com.farmingdale.stockscreener.utils.DataError
import com.farmingdale.stockscreener.utils.Resource
import com.farmingdale.stockscreener.utils.UiText
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch

@Composable
fun MarketSectors(
    sectors: Resource<ImmutableList<MarketSector>, DataError.Network>,
    snackbarHost: SnackbarHostState,
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = R.string.sector_performance),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.fillMaxWidth()
        )
        when (sectors) {
            is Resource.Loading -> {
                MarketSectorsSkeleton()
            }

            is Resource.Error -> {
                MarketSectorsSkeleton()

                LaunchedEffect(sectors.error) {
                    snackbarHost.showSnackbar(
                        message = sectors.error.asUiText().asString(context),
                        actionLabel = UiText.StringResource(R.string.dismiss).asString(context),
                        duration = SnackbarDuration.Short
                    )
                }
            }

            is Resource.Success -> {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(sectors.data) { sector ->
                        MarketSectorCard(sector = sector)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketSectorCard(sector: MarketSector) {
    val tooltipState = rememberTooltipState()
    val scope = rememberCoroutineScope()
    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = {
            PlainTooltip {
                Text(sector.sector)
            }
        },
        state = tooltipState
    ) {
        Card(
            modifier = Modifier
                .size(175.dp, 100.dp)
                .clickable { scope.launch { tooltipState.show() } }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = sector.sector,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = indexColor,
                )
                Column {
                    PerformanceRow(
                        title = stringResource(id = R.string.day_return),
                        value = sector.dayReturn,
                        color = if (sector.dayReturn.contains("-")) negativeTextColor else positiveTextColor
                    )
                    PerformanceRow(
                        title = stringResource(id = R.string.ytd_return),
                        value = sector.ytdReturn,
                        color = if (sector.dayReturn.contains("-")) negativeTextColor else positiveTextColor
                    )
                    PerformanceRow(
                        title = stringResource(id = R.string.three_year_return),
                        value = sector.threeYearReturn,
                        color = if (sector.dayReturn.contains("-")) negativeTextColor else positiveTextColor
                    )
                }
            }
        }
    }
}

@Composable
fun PerformanceRow(
    title: String,
    value: String,
    color: Color,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = color
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = color
        )
    }
}

@Preview
@Composable
fun PreviewMarketSectors() {
    MarketSectorCard(
        sector = MarketSector(
            sector = "Technology",
            dayReturn = "+1.23%",
            ytdReturn = "+4.56%",
            yearReturn = "+12.34%",
            threeYearReturn = "+56.78%",
            fiveYearReturn = "+90.12%",
        )
    )
}

@Composable
fun MarketSectorsSkeleton(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surfaceContainer
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(5) {
            item(key = it) {
                Card(
                    modifier = modifier.size(175.dp, 100.dp),
                    colors = CardDefaults.cardColors(containerColor = color)
                ) {
                    // skeleton
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewMarketSectorsSkeleton() {
    MarketSectorsSkeleton()
}