package com.longbark.maintenance.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.longbark.maintenance.presentation.auth.LoginScreen
import com.longbark.maintenance.presentation.dashboard.DashboardScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    isLoggedIn: Boolean,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Screen.Dashboard.route else Screen.Login.route,
        modifier = modifier
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(navController = navController)
        }

        // TODO: Add more screens (Clients, Sites, Reports, Notifications, Settings)
    }
}

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Dashboard : Screen("dashboard")
    data object Clients : Screen("clients")
    data object ClientDetail : Screen("client/{clientId}") {
        fun createRoute(clientId: String) = "client/$clientId"
    }
    data object SiteDetail : Screen("site/{siteId}") {
        fun createRoute(siteId: String) = "site/$siteId"
    }
    data object Reports : Screen("reports")
    data object Notifications : Screen("notifications")
    data object Settings : Screen("settings")
}
