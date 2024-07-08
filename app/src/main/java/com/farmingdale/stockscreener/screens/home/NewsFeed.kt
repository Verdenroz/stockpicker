package com.farmingdale.stockscreener.screens.home

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.farmingdale.stockscreener.R
import com.farmingdale.stockscreener.model.local.News
import com.farmingdale.stockscreener.utils.DataError
import com.farmingdale.stockscreener.utils.Resource
import com.farmingdale.stockscreener.utils.UiText
import kotlinx.collections.immutable.ImmutableList

@Composable
fun NewsFeed(
    news: Resource<ImmutableList<News>, DataError.Network>,
    snackbarHost: SnackbarHostState,
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = R.string.news),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.fillMaxWidth()
        )
        when (news) {
            is Resource.Loading -> {
                NewsFeedSkeleton()
            }

            is Resource.Error -> {
                NewsFeedSkeleton()

                LaunchedEffect(news.error) {
                    snackbarHost.showSnackbar(
                        message = news.error.asUiText().asString(context),
                        actionLabel = UiText.StringResource(R.string.dismiss).asString(context),
                        duration = SnackbarDuration.Short
                    )
                }
            }

            is Resource.Success -> {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    items(
                        items = news.data,
                        key = { news -> news.link }
                    ) { article ->
                        ContentCard(article = article)
                    }
                }
            }
        }
    }
}

@Composable
fun ContentCard(
    article: News?,
) {
    val context = LocalContext.current
    if (article == null) {
        return
    }
    Card(
        modifier = Modifier
            .sizeIn(
                minWidth = 200.dp,
                maxWidth = 300.dp,
                maxHeight = 250.dp
            )
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.link))
                context.startActivity(intent)
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(context)
                    .data(article.img)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(id = R.string.news_image),
                loading = {
                    Card(
                        modifier = Modifier.size(250.dp, 150.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
                    ) {}
                },
                error = {
                    Card(
                        modifier = Modifier.size(250.dp, 150.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
                    ) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = stringResource(id = R.string.error_loading_data)
                        )
                    }
                },
                imageLoader = ImageLoader(context),
            )
            Text(
                text = article.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(.9f)
            )
            Row(
                modifier = Modifier.fillMaxWidth(.75f),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = article.source,
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = article.time,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewContentCard() {
    ContentCard(
        article = News(
            title = "Title",
            source = "Source",
            time = "Time",
            img = "https://www.google.com",
            link = "https://www.google.com"
        ),
    )
}

@Composable
fun NewsFeedSkeleton(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primaryContainer
) {
    LazyRow(
        modifier = Modifier
            .height(250.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(5) {
            item(key = it) {
                Card(
                    modifier = modifier.size(300.dp, 250.dp),
                    colors = CardDefaults.cardColors(containerColor = color)
                ) {
                    // skeleton
                }
            }
        }
    }
}