package com.longbark.maintenance.presentation.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle(initialValue = "system")
    val biometricEnabled by viewModel.biometricEnabled.collectAsStateWithLifecycle(initialValue = false)
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsStateWithLifecycle(initialValue = true)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text("Appearance", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            item {
                SettingsSwitchItem(
                    title = "Biometric Authentication",
                    subtitle = "Use fingerprint or face unlock",
                    icon = Icons.Default.Fingerprint,
                    checked = biometricEnabled,
                    onCheckedChange = { viewModel.setBiometricEnabled(it) }
                )
            }
            item {
                SettingsSwitchItem(
                    title = "Notifications",
                    subtitle = "Enable notifications",
                    icon = Icons.Default.Notifications,
                    checked = notificationsEnabled,
                    onCheckedChange = { viewModel.setNotificationsEnabled(it) }
                )
            }
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        viewModel.logout()
                        onLogout()
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.ExitToApp, contentDescription = null)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Logout", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsSwitchItem(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.SemiBold)
                Text(subtitle, style = MaterialTheme.typography.bodySmall)
            }
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        }
    }
}
