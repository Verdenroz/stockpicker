package com.farmingdale.stockscreener.views.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.farmingdale.stockscreener.R

@Composable
fun ErrorCard(
    refresh: () -> Unit
) {
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
                containerColor = MaterialTheme.colorScheme.errorContainer
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
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewErrorCard() {
    ErrorCard(
        refresh = {}
    )
}