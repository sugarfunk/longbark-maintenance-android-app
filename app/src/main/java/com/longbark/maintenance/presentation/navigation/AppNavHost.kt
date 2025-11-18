package com.longbark.maintenance.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.longbark.maintenance.presentation.auth.LoginScreen
import com.longbark.maintenance.presentation.client.ClientDetailScreen
import com.longbark.maintenance.presentation.client.ClientListScreen
import com.longbark.maintenance.presentation.dashboard.DashboardScreen
import com.longbark.maintenance.presentation.settings.SettingsScreen

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

        composable(
            route = Screen.Dashboard.route,
            deepLinks = listOf(
                navDeepLink { uriPattern = "longbark://dashboard" },
                navDeepLink { uriPattern = "https://longbark.app/app/dashboard" }
            )
        ) {
            DashboardScreen(navController = navController)
        }

        composable(
            route = Screen.Clients.route,
            deepLinks = listOf(
                navDeepLink { uriPattern = "longbark://clients" },
                navDeepLink { uriPattern = "https://longbark.app/app/clients" }
            )
        ) {
            ClientListScreen(
                onClientClick = { clientId ->
                    navController.navigate(Screen.ClientDetail.createRoute(clientId))
                }
            )
        }

        composable(
            route = Screen.ClientDetail.route,
            arguments = listOf(navArgument("clientId") { type = NavType.StringType }),
            deepLinks = listOf(
                navDeepLink { uriPattern = "longbark://client/{clientId}" },
                navDeepLink { uriPattern = "https://longbark.app/app/client/{clientId}" }
            )
        ) {
            ClientDetailScreen(
                onNavigateBack = { navController.popBackStack() },
                onSiteClick = { siteId ->
                    navController.navigate(Screen.SiteDetail.createRoute(siteId))
                }
            )
        }

        composable(
            route = Screen.SiteDetail.route,
            arguments = listOf(navArgument("siteId") { type = NavType.StringType }),
            deepLinks = listOf(
                navDeepLink { uriPattern = "longbark://site/{siteId}" },
                navDeepLink { uriPattern = "https://longbark.app/app/site/{siteId}" }
            )
        ) {
            // SiteDetailScreen - TODO: Create full implementation
        }

        composable(
            route = Screen.Settings.route,
            deepLinks = listOf(
                navDeepLink { uriPattern = "longbark://settings" },
                navDeepLink { uriPattern = "https://longbark.app/app/settings" }
            )
        ) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.Reports.route,
            deepLinks = listOf(
                navDeepLink { uriPattern = "longbark://reports" }
            )
        ) {
            // ReportsScreen - TODO: Create full implementation
        }

        composable(
            route = Screen.Notifications.route,
            deepLinks = listOf(
                navDeepLink { uriPattern = "longbark://notifications" }
            )
        ) {
            // NotificationsScreen - TODO: Create full implementation
        }
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
