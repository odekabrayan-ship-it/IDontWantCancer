package com.idontwantcancer.app.presentation.home

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.idontwantcancer.app.R
import com.idontwantcancer.app.core.ui.adaptive.AdaptiveLayout
import com.idontwantcancer.app.core.ui.theme.LocalSpacing
import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.presentation.components.*
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

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { }
    )

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    Scaffold(
        topBar = {
            HomeHeader(
                mission = (uiState as? HomeUiState.Success)?.userMission ?: UserMission.UNDEFINED,
                onRefresh = { onInteraction(IntelligenceUiInteraction.RetryOperation) },
                onSettings = { onInteraction(IntelligenceUiInteraction.EnterSettings) }
            )
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
                    DashboardContent(state, onInteraction)
                }
            }
        }
    }
}

@Composable
private fun HomeHeader(
    mission: UserMission,
    onRefresh: () -> Unit,
    onSettings: () -> Unit
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
        Column {
            Text(
                text = "THE AGENCY",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 2.sp
            )
            Text(
                text = if (mission == UserMission.HEALING) "Healing Support Dashboard" else "Prevention Dashboard",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Row {
            IconButton(onClick = onRefresh) {
                Icon(Icons.Default.Refresh, contentDescription = null)
            }
            IconButton(onClick = onSettings) {
                Icon(Icons.Default.Settings, contentDescription = null)
            }
        }
    }
}

@Composable
private fun DashboardContent(
    state: HomeUiState.Success,
    onInteraction: (IntelligenceUiInteraction) -> Unit
) {
    val spacing = LocalSpacing.current
    
    // Extract signals for primary directives
    val signals = remember(state.briefing) { 
        state.briefing.items.mapNotNull { state.briefing.signals[it.intelligenceId] } 
    }
    val criticalDirective = remember(signals) {
        signals.find { (it.importance == SignalImportance.CRITICAL || it.importance == SignalImportance.HIGH) && !it.isActionTaken }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(spacing.screenPadding),
        horizontalArrangement = Arrangement.spacedBy(spacing.medium),
        verticalArrangement = Arrangement.spacedBy(spacing.medium)
    ) {
        // 1. Operational Status
        item(span = { GridItemSpan(2) }) {
            AgencyStatusAnchor()
        }

        // 2. Daily Peace
        state.dailyPeace?.let { peace ->
            item(span = { GridItemSpan(2) }) {
                DailyPeaceCard(peace)
            }
        }

        // 3. Primary Directive (High Urgency Intercept)
        criticalDirective?.let { directive ->
            item(span = { GridItemSpan(2) }) {
                PrimaryDirectiveCard(
                    signal = directive,
                    onClick = { onInteraction(IntelligenceUiInteraction.ViewSignalDetails(directive.id)) }
                )
            }
        }

        // 4. The 7-Pillar Grid
        item(span = { GridItemSpan(2) }) {
            Text(
                text = "PROTECTIVE SECTORS",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(top = spacing.medium, bottom = spacing.small)
            )
        }

        items(state.pillarStatuses) { pillar ->
            PillarCard(
                pillar = pillar,
                onClick = { 
                    when(pillar.id) {
                        "WATCH" -> onInteraction(IntelligenceUiInteraction.EnterAlerts)
                        "SHOP", "TRUTH", "VERIFY" -> onInteraction(IntelligenceUiInteraction.EnterVerify)
                        "EAT", "HOME", "ACADEMY", "PLAN" -> onInteraction(IntelligenceUiInteraction.EnterPrevention)
                        "TREATMENT", "SYMPTOMS", "DECEPTION", "PROGRESS" -> onInteraction(IntelligenceUiInteraction.EnterHealingSanctuary)
                    }
                }
            )
        }
        
        item(span = { GridItemSpan(2) }) {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun AgencyStatusAnchor() {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "STATUS: OPERATIONAL - YOUR WATCH IS CLEAR",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onPrimary,
                letterSpacing = 1.sp
            )
        }
    }
}

@Composable
private fun DailyPeaceCard(peace: DailyPeace) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "DAILY PEACE",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = peace.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = peace.summary,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun PillarCard(pillar: PillarStatus, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().aspectRatio(1.2f),
        colors = CardDefaults.cardColors(
            containerColor = if (pillar.isAlert) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        border = BorderStroke(1.dp, if (pillar.isAlert) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            val icon = when(pillar.id) {
                "WATCH" -> Icons.Default.NotificationsActive
                "SHOP" -> Icons.Default.ShoppingBag
                "EAT" -> Icons.Default.Restaurant
                "TRUTH" -> Icons.Default.VerifiedUser
                "HOME" -> Icons.Default.HomeWork
                "ACADEMY" -> Icons.Default.School
                "PLAN" -> Icons.Default.TrackChanges
                else -> Icons.Default.GridView
            }
            
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (pillar.isAlert) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
            
            Column {
                Text(
                    text = pillar.title,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Black,
                    maxLines = 1
                )
                Text(
                    text = pillar.status,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (pillar.isAlert) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    lineHeight = 14.sp
                )
            }
        }
    }
}
