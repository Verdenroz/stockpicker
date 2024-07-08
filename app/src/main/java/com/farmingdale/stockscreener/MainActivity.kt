package com.farmingdale.stockscreener

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.farmingdale.stockscreener.ui.theme.StockScreenerTheme
import com.farmingdale.stockscreener.screens.main.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            StockScreenerTheme {
                MainScreen()
            }
        }
    }
}
