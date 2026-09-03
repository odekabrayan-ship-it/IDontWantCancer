package com.idontwantcancer.app.presentation.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.idontwantcancer.app.R
import com.idontwantcancer.app.core.ui.theme.LocalSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    var showClearDialog by remember { mutableStateOf(false) }
    val spacing = LocalSpacing.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = stringResource(R.string.settings_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(vertical = spacing.medium)
        ) {
            item {
                SettingsSectionHeader(title = stringResource(R.string.settings_section_notifications))
            }
            item {
                ListItem(
                    headlineContent = { Text(stringResource(R.string.settings_notifications_frequency)) },
                    trailingContent = {
                        Text(
                            text = "Daily",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    leadingContent = { Icon(Icons.Default.Notifications, contentDescription = null) }
                )
            }

            item { HorizontalDivider(modifier = Modifier.padding(vertical = spacing.small)) }

            item {
                SettingsSectionHeader(title = stringResource(R.string.settings_section_sources))
            }
            item {
                ListItem(
                    headlineContent = { Text(stringResource(R.string.settings_source_fda)) },
                    supportingContent = { Text("Enabled") },
                    trailingContent = { Switch(checked = true, onCheckedChange = { }) },
                    leadingContent = { Icon(Icons.Default.Source, contentDescription = null) }
                )
            }

            item { HorizontalDivider(modifier = Modifier.padding(vertical = spacing.small)) }

            item {
                SettingsSectionHeader(title = stringResource(R.string.settings_section_data))
            }
            item {
                ListItem(
                    headlineContent = { 
                        Text(
                            text = stringResource(R.string.settings_data_clear),
                            color = MaterialTheme.colorScheme.error
                        ) 
                    },
                    supportingContent = { Text(stringResource(R.string.settings_data_clear_desc)) },
                    modifier = Modifier.clickable { showClearDialog = true },
                    leadingContent = { 
                        Icon(
                            imageVector = Icons.Default.Delete, 
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        ) 
                    }
                )
            }

            item { HorizontalDivider(modifier = Modifier.padding(vertical = spacing.small)) }

            item {
                SettingsSectionHeader(title = stringResource(R.string.settings_section_about))
            }
            item {
                ListItem(
                    headlineContent = { Text(stringResource(R.string.settings_about_version)) },
                    trailingContent = { Text("1.0.0") },
                    leadingContent = { Icon(Icons.Default.Info, contentDescription = null) }
                )
            }
            item {
                ListItem(
                    headlineContent = { Text(stringResource(R.string.settings_about_privacy)) },
                    modifier = Modifier.clickable { /* TODO: Open URL */ },
                    leadingContent = { Icon(Icons.Default.PrivacyTip, contentDescription = null) }
                )
            }
            item {
                ListItem(
                    headlineContent = { Text(stringResource(R.string.settings_about_legal)) },
                    modifier = Modifier.clickable { /* TODO: Show legal info */ },
                    leadingContent = { Icon(Icons.Default.Gavel, contentDescription = null) }
                )
            }
        }
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Clear Memory") },
            text = { Text("This will permanently delete all intelligence signals and briefings from this device. Are you sure?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearIntelligenceMemory()
                        showClearDialog = false
                    }
                ) {
                    Text("Clear", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    val spacing = LocalSpacing.current
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.secondary,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(horizontal = spacing.screenPadding, vertical = spacing.small)
            .semantics { heading() }
    )
}
