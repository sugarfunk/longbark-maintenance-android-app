package com.longbark.maintenance.presentation.client

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.longbark.maintenance.domain.model.*
import com.longbark.maintenance.presentation.theme.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientDetailScreen(
    onNavigateBack: () -> Unit,
    onSiteClick: (String) -> Unit,
    viewModel: ClientDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Client Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.loadClientDetails() }) {
                        Icon(Icons.Default.Refresh, "Refresh")
                    }
                }
            )
        }
    ) { padding ->
        when (val state = uiState) {
            is ClientDetailUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is ClientDetailUiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    // Client Header
                    ClientHeader(client = state.client)

                    // Tabs
                    TabRow(selectedTabIndex = selectedTab) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { viewModel.selectTab(0) },
                            text = { Text("Sites") }
                        )
                        if (state.billingData != null) {
                            Tab(
                                selected = selectedTab == 1,
                                onClick = { viewModel.selectTab(1) },
                                text = { Text("Billing") }
                            )
                        }
                        Tab(
                            selected = selectedTab == if (state.billingData != null) 2 else 1,
                            onClick = { viewModel.selectTab(if (state.billingData != null) 2 else 1) },
                            text = { Text("Settings") }
                        )
                    }

                    // Tab Content
                    when (selectedTab) {
                        0 -> SitesTab(sites = state.sites, onSiteClick = onSiteClick)
                        1 -> {
                            if (state.billingData != null) {
                                BillingTab(billingData = state.billingData)
                            } else {
                                SettingsTab(client = state.client)
                            }
                        }
                        2 -> SettingsTab(client = state.client)
                    }
                }
            }
            is ClientDetailUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(state.message, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.loadClientDetails() }) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ClientHeader(client: Client) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = client.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("${client.siteCount} Sites")
                
                val statusColor = when (client.healthStatus) {
                    HealthStatus.HEALTHY -> HealthyGreen
                    HealthStatus.WARNING -> WarningYellow
                    HealthStatus.CRITICAL -> CriticalRed
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }
                
                Surface(
                    color = statusColor.copy(alpha = 0.2f),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = client.healthStatus.name,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = statusColor
                    )
                }
            }
        }
    }
}

@Composable
fun SitesTab(sites: List<Site>, onSiteClick: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(sites) { site ->
            SiteCard(site = site, onClick = { onSiteClick(site.id) })
        }
    }
}

@Composable
fun SiteCard(site: Site, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = site.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = site.url,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text("Uptime: ${String.format("%.1f", site.uptimePercentage)}%")
                Spacer(modifier = Modifier.width(16.dp))
                val statusColor = when (site.healthStatus) {
                    HealthStatus.HEALTHY -> HealthyGreen
                    HealthStatus.WARNING -> WarningYellow
                    HealthStatus.CRITICAL -> CriticalRed
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }
                Surface(
                    color = statusColor.copy(alpha = 0.2f),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = site.healthStatus.name,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = statusColor
                    )
                }
            }
        }
    }
}

@Composable
fun BillingTab(billingData: BillingData) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Billing Overview", style = MaterialTheme.typography.titleLarge)
        }

        item {
            BillingStatsCard(stats = billingData.stats)
        }

        item {
            Text("Recent Invoices", style = MaterialTheme.typography.titleMedium)
        }

        items(billingData.invoices.take(5)) { invoice ->
            InvoiceCard(invoice = invoice)
        }

        item {
            Text("Recent Payments", style = MaterialTheme.typography.titleMedium)
        }

        items(billingData.payments.take(5)) { payment ->
            PaymentCard(payment = payment)
        }
    }
}

@Composable
fun BillingStatsCard(stats: BillingStats) {
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)
    
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            StatRow("Total Revenue", currencyFormat.format(stats.totalRevenue))
            StatRow("Unpaid Invoices", currencyFormat.format(stats.unpaidInvoices))
            StatRow("Overdue", currencyFormat.format(stats.overdueInvoices), isError = stats.overdueInvoices > 0)
            StatRow("Average Invoice", currencyFormat.format(stats.averageInvoice))
            StatRow("Outstanding Quotes", currencyFormat.format(stats.outstandingQuotes))
        }
    }
}

@Composable
fun StatRow(label: String, value: String, isError: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(
            value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = if (isError) CriticalRed else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun InvoiceCard(invoice: Invoice) {
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("#${invoice.number}", fontWeight = FontWeight.Bold)
                Text(currencyFormat.format(invoice.amount))
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Due: ${dateFormat.format(Date(invoice.dueDate))}",
                    style = MaterialTheme.typography.bodySmall
                )
                Surface(
                    color = if (invoice.isPaid) HealthyGreen.copy(alpha = 0.2f) else WarningYellow.copy(alpha = 0.2f),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = invoice.status.name,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (invoice.isPaid) HealthyGreen else WarningYellow
                    )
                }
            }
        }
    }
}

@Composable
fun PaymentCard(payment: Payment) {
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Payment", fontWeight = FontWeight.Bold)
                Text(currencyFormat.format(payment.amount), color = HealthyGreen)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                dateFormat.format(Date(payment.date)),
                style = MaterialTheme.typography.bodySmall
            )
            if (payment.transactionReference != null) {
                Text(
                    "Ref: ${payment.transactionReference}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun SettingsTab(client: Client) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text("Client Settings", style = MaterialTheme.typography.titleLarge)
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            SettingItem(
                title = "Notifications",
                subtitle = "Configure client-specific notifications",
                icon = Icons.Default.Notifications
            )
        }
        item {
            SettingItem(
                title = "Invoice Ninja Integration",
                subtitle = "Link to Invoice Ninja client",
                icon = Icons.Default.AccountBalance
            )
        }
        item {
            SettingItem(
                title = "Check Frequency",
                subtitle = "Configure site check intervals",
                icon = Icons.Default.Schedule
            )
        }
    }
}

@Composable
fun SettingItem(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, fontWeight = FontWeight.SemiBold)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
