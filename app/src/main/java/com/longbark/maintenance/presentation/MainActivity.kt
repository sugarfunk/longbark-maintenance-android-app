package com.longbark.maintenance.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.longbark.maintenance.presentation.auth.AuthViewModel
import com.longbark.maintenance.presentation.navigation.AppNavHost
import com.longbark.maintenance.presentation.theme.LongBarkTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val authViewModel: AuthViewModel = hiltViewModel()
            val isLoggedIn by authViewModel.isLoggedIn.collectAsStateWithLifecycle(initialValue = false)

            // Keep splash screen visible while checking auth state
            splashScreen.setKeepOnScreenCondition { false }

            LongBarkTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavHost(
                        navController = navController,
                        isLoggedIn = isLoggedIn
                    )
                }
            }
        }
    }
}
