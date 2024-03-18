package com.farmingdale.stockscreener.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.farmingdale.stockscreener.R
import com.farmingdale.stockscreener.model.local.googlefinance.MarketIndex

@Composable
fun MarketIndices(
    indices: List<MarketIndex>?,
    isLoading: Boolean,
    refresh: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(id = R.string.market_performance))

        }
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (indices.isNullOrEmpty()) {
            Box(
                modifier = Modifier
                    .size(300.dp, 150.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { refresh() },
                    shape = RoundedCornerShape(10),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFE4E1)
                    )
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = stringResource(id = R.string.notLoaded)
                        )
                        Text(
                            text = stringResource(id = R.string.notLoaded),
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                content = {
                    items(indices) { index ->
                        MarketIndexCard(index = index)
                    }
                }
            )
        }
    }
}

@Composable
fun MarketIndexCard(index: MarketIndex) {
    val color = index.percentChange.let {
        if (it.contains('+')) Color.Green else Color.Red
    }
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        ),
        modifier = Modifier
            .size(250.dp, 150.dp)
            .shadow(1.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        color.copy(alpha = 0.5f),
                        color.copy(alpha = 0.1f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = index.name,
                style = MaterialTheme.typography.headlineMedium,
                maxLines = 1,
                color = Color(0xff147efb)
            )
            Text(
                text = index.score,
                style = MaterialTheme.typography.headlineMedium
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = index.change,
                    style = MaterialTheme.typography.headlineSmall,
                    color = if (index.change.contains('+')) Color.Green else Color.Red
                )
                Text(
                    text = index.percentChange,
                    style = MaterialTheme.typography.headlineSmall,
                    color = if (index.change.contains('+')) Color.Green else Color.Red
                )
            }

        }
    }

}

@Preview
@Composable
fun PreviewMarketIndexCard(){
    Row {
        MarketIndexCard(
            MarketIndex(
                name = "Dow Jones",
                score = "100.0",
                change = "+100.0",
                percentChange = "+100%"
            ),
        )
        MarketIndexCard(
            MarketIndex(
                name = "Dow Jones",
                score = "100.0",
                change = "-100.0",
                percentChange = "-100%"
            )
        )
    }

}
