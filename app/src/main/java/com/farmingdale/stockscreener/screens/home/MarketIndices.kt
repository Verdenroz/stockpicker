package com.farmingdale.stockscreener.views.home

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.farmingdale.stockscreener.R
import com.farmingdale.stockscreener.model.local.MarketIndex
import com.farmingdale.stockscreener.ui.theme.indexColor
import com.farmingdale.stockscreener.ui.theme.negativeTextColor
import com.farmingdale.stockscreener.ui.theme.positiveTextColor
import com.farmingdale.stockscreener.utils.DataError
import com.farmingdale.stockscreener.utils.Resource
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun MarketIndices(
    indices: Resource<ImmutableList<MarketIndex>, DataError>,
    snackbarHost: SnackbarHostState,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = R.string.market_performance),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.fillMaxWidth()
        )
        when (indices) {
            is Resource.Loading -> {
                CircularProgressIndicator()
            }

            is Resource.Error -> {
                var error = stringResource(id = R.string.error_loading_data)
                when (indices.error) {
                    DataError.Network.NO_INTERNET -> error = "No internet connection"
                    DataError.Network.TIMEOUT -> error = "Timeout error"
                    DataError.Network.BAD_REQUEST -> error = "Bad request"
                    DataError.Network.DENIED -> error = "Access denied"
                    DataError.Network.NOT_FOUND -> error = "Not found"
                    DataError.Network.THROTTLED -> error = "Throttled"
                    DataError.Network.SERVER_DOWN -> error = "Server down"
                    DataError.Network.SERIALIZATION -> error = "Serialization error"
                    DataError.Network.UNKNOWN -> error = "Unknown error"
                }
                ErrorCard(refresh = refresh, message = error)
            }

            is Resource.Success -> {
                if (indices.data.isEmpty()) {
                    ErrorCard(refresh = refresh)
                } else {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = indices.data,
                            key = { index -> index.name }
                        ) { index ->
                            MarketIndexCard(index = index)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketIndexCard(index: MarketIndex) {
    val tooltipState = rememberTooltipState()
    val scope = rememberCoroutineScope()
    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = {
            PlainTooltip {
                Text(index.name)
            }
        },
        state = tooltipState
    ) {
        Card(
            modifier = Modifier
                .size(125.dp, 75.dp)
                .clickable { scope.launch { tooltipState.show() } }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = index.name,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = indexColor
                )
                Text(
                    text = String.format(Locale.US, "%.2f", index.value.toDouble()),
                    style = MaterialTheme.typography.titleSmall
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = index.change,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (index.change.contains('+')) positiveTextColor else negativeTextColor
                    )
                    Text(
                        text = index.percentChange,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (index.change.contains('+')) positiveTextColor else negativeTextColor
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewMarketIndexCard() {
    Row {
        MarketIndexCard(
            MarketIndex(
                name = "Dow Jones",
                value = "100.0",
                change = "+100.0",
                percentChange = "+100%"
            ),
        )
        MarketIndexCard(
            MarketIndex(
                name = "Dow Jones",
                value = "100.0",
                change = "-100.0",
                percentChange = "-100%"
            )
        )
    }
}
