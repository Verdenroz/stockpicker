package com.farmingdale.stockscreener.screens.watchlist

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.farmingdale.stockscreener.R
import com.farmingdale.stockscreener.model.local.SimpleQuoteData
import com.farmingdale.stockscreener.ui.theme.negativeTextColor
import com.farmingdale.stockscreener.ui.theme.positiveTextColor
import kotlinx.collections.immutable.ImmutableList
import java.util.Locale
import kotlin.math.abs
import kotlin.math.pow

@Composable
fun WatchlistScreen(
    navController: NavHostController,
    watchList: ImmutableList<SimpleQuoteData>,
    deleteFromWatchList: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = watchList,
            key = { watchList -> watchList.symbol }
        ) { item ->
            WatchlistQuote(
                quote = item,
                navController = navController,
                deleteFromWatchList = deleteFromWatchList
            )
        }
    }
}


@Composable
fun WatchlistQuote(
    quote: SimpleQuoteData,
    navController: NavHostController,
    deleteFromWatchList: (String) -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    val maxDragDistance = 500f
    val dragProportion = abs(offsetX) / maxDragDistance
    val weight by animateFloatAsState(
        targetValue = (dragProportion.pow(2)).coerceIn(0.00001f, 1f),
        label = "weight"
    )

    Row(
        modifier = Modifier
            .height(60.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        navController.navigate("stock/${quote.symbol}")
                    }
                )
            }
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { _, dragAmount ->
                        // Update offsetX only if dragAmount is positive or already dragging
                        if (dragAmount > 0 || offsetX != 0f) {
                            offsetX += dragAmount
                            offsetX = offsetX.coerceIn(-maxDragDistance, maxDragDistance)
                        }
                    },
                    onDragEnd = {
                        if (abs(offsetX) > maxDragDistance * 0.75) {
                            deleteFromWatchList(quote.symbol)
                        }
                        offsetX = 0f
                    }
                )
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(weight)
                .fillMaxHeight()
                .padding(vertical = 4.dp)
                .clip(RoundedCornerShape(15))
                .background(MaterialTheme.colorScheme.errorContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(id = R.string.delete),
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.alpha(1f - weight)
            )
        }
        Box(
            modifier = Modifier
                .weight((1f - weight).coerceAtLeast(0.01f))
                .fillMaxHeight()
                .padding(8.dp)
                .clip(RoundedCornerShape(15))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = quote.symbol,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    modifier = Modifier
                        .clip(RoundedCornerShape(25))
                        .background(MaterialTheme.colorScheme.inverseSurface)
                        .padding(4.dp)
                        .weight(.2f)
                )
                Text(
                    text = quote.name,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .weight(.6f)
                )
                Text(
                    text = String.format(Locale.US, "%.2f", quote.price),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = if (quote.change.startsWith("-")) negativeTextColor else positiveTextColor,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(.2f)
                        .wrapContentSize()
                        .padding(4.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewWatchlistQuote() {
    WatchlistQuote(
        quote = SimpleQuoteData(
            symbol = "AAPL",
            name = "Apple Inc.",
            price = 145.86,
            change = "-0.12",
            percentChange = "-0.08%"
        ),
        navController = rememberNavController(),
        deleteFromWatchList = {}
    )
}