package com.farmingdale.stockscreener.screens

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector
import com.farmingdale.stockscreener.R

enum class Screen(val route: String, @StringRes val titleResource: Int, val icon: ImageVector) {
    Home("home", R.string.home, Icons.Default.Home),
    Watchlist("watchlist", R.string.watchlist, Icons.AutoMirrored.Default.List),
    Simulate("simulate", R.string.simulate, Icons.Default.Create),
    Alerts("alerts", R.string.alerts, Icons.Default.Warning)
}