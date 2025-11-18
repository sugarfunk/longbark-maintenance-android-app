package com.longbark.maintenance.presentation.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.longbark.maintenance.domain.model.AppNotification
import com.longbark.maintenance.domain.model.DashboardStats
import com.longbark.maintenance.presentation.theme.CriticalRed
import com.longbark.maintenance.presentation.theme.HealthyGreen
import com.longbark.maintenance.presentation.theme.WarningYellow
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard") },
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Dashboard") },
                    selected = true,
                    onClick = { }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.People, contentDescription = null) },
                    label = { Text("Clients") },
                    selected = false,
                    onClick = { navController.navigate("clients") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Article, contentDescription = null) },
                    label = { Text("Reports") },
                    selected = false,
                    onClick = { navController.navigate("reports") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Notifications, contentDescription = null) },
                    label = { Text("Alerts") },
                    selected = false,
                    onClick = { navController.navigate("notifications") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    label = { Text("Settings") },
                    selected = false,
                    onClick = { navController.navigate("settings") }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is DashboardUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is DashboardUiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.loadDashboard() }) {
                            Text("Retry")
                        }
                    }
                }
                is DashboardUiState.Success -> {
                    DashboardContent(stats = state.stats)
                }
            }
        }
    }
}

@Composable
fun DashboardContent(stats: DashboardStats) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Overview",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatsCard(
                    title = "Total",
                    value = stats.totalSites.toString(),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                StatsCard(
                    title = "Healthy",
                    value = stats.healthySites.toString(),
                    color = HealthyGreen,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatsCard(
                    title = "Warnings",
                    value = stats.warningSites.toString(),
                    color = WarningYellow,
                    modifier = Modifier.weight(1f)
                )
                StatsCard(
                    title = "Critical",
                    value = stats.criticalSites.toString(),
                    color = CriticalRed,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Text(
                text = "Recent Alerts",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        }

        if (stats.recentAlerts.isEmpty()) {
            item {
                Text(
                    text = "No recent alerts",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(stats.recentAlerts) { notification ->
                NotificationCard(notification = notification)
            }
        }
    }
}

@Composable
fun StatsCard(
    title: String,
    value: String,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun NotificationCard(notification: AppNotification) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = notification.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = notification.message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                val severityColor = when (notification.severity.name) {
                    "CRITICAL" -> CriticalRed
                    "WARNING" -> WarningYellow
                    else -> MaterialTheme.colorScheme.primary
                }

                Surface(
                    color = severityColor.copy(alpha = 0.2f),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = notification.severity.name,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = severityColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = formatTimestamp(notification.timestamp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
