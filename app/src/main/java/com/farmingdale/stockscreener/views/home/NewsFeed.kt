package com.farmingdale.stockscreener.views.home

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.farmingdale.stockscreener.R
import com.farmingdale.stockscreener.model.local.News

@Composable
fun NewsFeed(
    news: List<News>?,
    refresh: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = R.string.news),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(vertical = 8.dp)
        )
        if (news.isNullOrEmpty()) {
            ErrorCard(refresh = refresh)
        } else {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                content = {
                    news.forEach { article ->
                        item {
                            ContentCard(article = article, refresh = refresh)
                        }
                    }
                }
            )
        }
    }

}

@Preview
@Composable
fun PreviewNewsFeed() {
    NewsFeed(
        news = listOf(
            News(
                title = "Title",
                source = "Source",
                time = "Time",
                link = "Link",
                img = "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png"
            ),
            News(
                title = "Title",
                source = "Source",
                time = "Time",
                link = "Link",
                img = "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png"
            ),
        ),
        refresh = {}
    )
}

@Composable
fun ContentCard(
    article: News?,
    refresh: () -> Unit
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
            }
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
                    CircularProgressIndicator()
                },
                error = {
                        ErrorCard(refresh = refresh)
                },
                imageLoader = ImageLoader(context),
            )
            Text(
                text = article.title,
                style = MaterialTheme.typography.titleSmall,
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