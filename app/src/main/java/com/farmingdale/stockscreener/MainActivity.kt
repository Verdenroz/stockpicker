package com.farmingdale.stockscreener

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.farmingdale.stockscreener.ui.theme.StockScreenerTheme
import com.farmingdale.stockscreener.views.main.MainView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StockScreenerTheme {
                MainView()
            }
        }
    }
}
