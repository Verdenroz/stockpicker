package com.farmingdale.stockscreener.views

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.farmingdale.stockscreener.R

@Composable
fun BottomBar() {
    Row(
        modifier = Modifier
            .background(Color.LightGray)
            .padding(8.dp)
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        BottomIcon(stringResource(id = R.string.home), Icons.Default.Home) {}
        BottomIcon(stringResource(id = R.string.watchlist), Icons.AutoMirrored.Filled.List) {}
        BottomIcon(stringResource(id = R.string.simulate), Icons.Default.Create) {}
        BottomIcon(stringResource(id = R.string.alerts), Icons.Default.Warning) {}
    }

}

@Composable
fun BottomIcon(
    text: String,
    icon: ImageVector,
    nav: () -> Unit
) {
    Column(
        modifier = Modifier
            .wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        IconButton(
            onClick = { nav() },
            modifier = Modifier
                .size(48.dp)
        ) {
            Icon(icon, contentDescription = text)
        }
        Text(
            text = text,
        )
    }
}