package com.idontwantcancer.app.presentation.home

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.idontwantcancer.app.R
import com.idontwantcancer.app.core.ui.adaptive.AdaptiveLayout
import com.idontwantcancer.app.core.ui.adaptive.AdaptiveLayoutType
import com.idontwantcancer.app.core.ui.theme.LocalSpacing
import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.presentation.boundary.IntelligenceInteractionBoundary
import com.idontwantcancer.app.presentation.components.*
import com.idontwantcancer.app.presentation.model.CommandConsumptionFinalityPresentationContract
import com.idontwantcancer.app.presentation.model.IntelligenceReentryReconciliationPresentationContract
import com.idontwantcancer.app.presentation.model.IntelligenceUiInteraction
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@Composable
fun HomeScreen(
    onInteraction: (IntelligenceUiInteraction) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Step 222: Request Notification Permission on launch
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { /* No action needed, preference stored by system */ }
    )

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    Scaffold(
        topBar = {
            HomeHeader(onRefresh = { onInteraction(IntelligenceUiInteraction.RetryOperation) })
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = uiState) {
                is HomeUiState.Loading -> AgencyLoadingState()
                is HomeUiState.Error -> AgencyErrorState(
                    message = state.message,
                    onRetry = { onInteraction(IntelligenceUiInteraction.RetryOperation) }
                )
                is HomeUiState.Success -> {
                    HomeContent(
                        briefing = state.briefing,
                        userCountry = state.userCountry,
                        adoptedActions = state.adoptedActions,
                        reconciliations = state.reconciliations,
                        finality = state.finality,
                        onInteraction = onInteraction
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeHeader(
    onRefresh: () -> Unit
) {
    val spacing = LocalSpacing.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = spacing.screenPadding, vertical = spacing.large),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.semantics { heading() }
            )
            Text(
                text = stringResource(R.string.app_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        IconButton(onClick = onRefresh) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = stringResource(R.string.action_refresh_content_desc)
            )
        }
    }
}

@Composable
private fun HomeContent(
    briefing: IntelligenceBriefing,
    userCountry: String,
    adoptedActions: List<PreventionAction>,
    reconciliations: Map<String, IntelligenceReentryReconciliationPresentationContract>,
    finality: CommandConsumptionFinalityPresentationContract,
    onInteraction: (IntelligenceUiInteraction) -> Unit
) {
    val layout = AdaptiveLayout.current
    
    // Step 213: Remember expensive transformations
    val signals = remember(briefing) { 
        briefing.items.mapNotNull { briefing.signals[it.intelligenceId] } 
    }
    
    // Stage 2 Overhaul: Identify Critical Directives (Importance >= HIGH)
    val primaryDirectives = remember(signals) {
        signals.filter { it.importance == SignalImportance.CRITICAL || it.importance == SignalImportance.HIGH }
    }
    val otherSignals = remember(signals, primaryDirectives) {
        signals.filter { it !in primaryDirectives }
    }
    
    val isClear = remember(briefing, signals) { 
        briefing.status == BriefingStatus.NO_MAJOR_CHANGES && signals.isEmpty() 
    }

    if (isClear) {
        HomeClearState(lastUpdated = briefing.generatedAt, userCountry = userCountry)
    } else {
        val spacing = LocalSpacing.current
        LazyColumn(
            modifier = Modifier.fillMaxSize().animateContentSize(animationSpec = tween(500)),
            contentPadding = PaddingValues(bottom = spacing.extraLarge)
        ) {
            item {
                BriefingStatusSection(status = briefing.status)
            }
            
            item {
                OperationalFinalityIndicator(finality)
            }

            if (primaryDirectives.isNotEmpty()) {
                item {
                    SectionHeader(title = stringResource(R.string.section_directives))
                }
                items(primaryDirectives, key = { "directive-${it.id}" }) { signal ->
                    PrimaryDirectiveCard(
                        signal = signal,
                        onClick = { onInteraction(IntelligenceUiInteraction.ViewSignalDetails(signal.id)) }
                    )
                }
            }

            if (adoptedActions.isNotEmpty()) {
                item {
                    SectionHeader(title = stringResource(R.string.section_prevention_focus))
                }
                items(adoptedActions, key = { "adopted-${it.id}" }) { action ->
                    AdoptedActionCard(action)
                }
            }

            if (otherSignals.isNotEmpty()) {
                item {
                    SectionHeader(title = stringResource(R.string.section_additional_intelligence))
                }
                
                items(
                    items = otherSignals,
                    key = { it.id }
                ) { signal ->
                    SignalCard(
                        signal = signal,
                        isSelected = false,
                        onClick = { onInteraction(IntelligenceUiInteraction.ViewSignalDetails(signal.id)) },
                        reconciliationIndicator = {
                            if (reconciliations[signal.id] is IntelligenceReentryReconciliationPresentationContract.InconsistentMismatch) {
                                Spacer(modifier = Modifier.height(spacing.small))
                                CompactReconciliationIndicator()
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun AdoptedActionCard(action: PreventionAction) {
    val spacing = LocalSpacing.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spacing.screenPadding, vertical = spacing.extraSmall),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.padding(spacing.cardPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val icon = when (action.iconName) {
                "smoke_free" -> Icons.Default.SmokeFree
                "sunny" -> Icons.Default.WbSunny
                "no_drinks" -> Icons.Default.NoDrinks
                "directions_run" -> Icons.Default.DirectionsRun
                "grass" -> Icons.Default.Grass
                "restaurant" -> Icons.Default.Restaurant
                else -> Icons.Default.Verified
            }
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = action.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun BriefingStatusSection(status: BriefingStatus) {
    val (text, icon, color) = when (status) {
        BriefingStatus.NO_MAJOR_CHANGES -> Triple(
            stringResource(R.string.status_no_changes),
            Icons.Default.CheckCircle,
            MaterialTheme.colorScheme.primary
        )
        BriefingStatus.READY -> Triple(
            stringResource(R.string.status_ready),
            Icons.Default.Info,
            MaterialTheme.colorScheme.secondary
        )
        BriefingStatus.ATTENTION_REQUIRED -> Triple(
            stringResource(R.string.status_attention),
            Icons.Default.Warning,
            MaterialTheme.colorScheme.error
        )
    }

    Surface(
        color = color.copy(alpha = 0.1f),
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(LocalSpacing.current.screenPadding)
    ) {
        val spacing = LocalSpacing.current
        Row(
            modifier = Modifier.padding(spacing.cardPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = color
            )
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    val spacing = LocalSpacing.current
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.secondary,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(start = spacing.screenPadding, end = spacing.screenPadding, top = spacing.large, bottom = spacing.small)
            .semantics { heading() }
    )
}

@Composable
private fun HomeClearState(lastUpdated: java.time.Instant, userCountry: String) {
    val formatter = remember {
        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
            .withLocale(Locale.getDefault())
            .withZone(ZoneId.systemDefault())
    }
    
    val timeString = remember(lastUpdated) { formatter.format(lastUpdated) }
    val countryName = remember(userCountry) { Locale("", userCountry).displayCountry }

    AgencyEmptyState(
        title = stringResource(R.string.briefing_clear_title),
        description = stringResource(R.string.briefing_clear_desc),
        icon = Icons.Default.CheckCircle
    )
    
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            Text(
                text = stringResource(R.string.briefing_monitoring_reassurance, countryName),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.briefing_last_update, timeString),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        }
    }
}
