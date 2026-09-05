package com.idontwantcancer.app.presentation.signal

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.idontwantcancer.app.R
import com.idontwantcancer.app.core.ui.theme.LocalSpacing
import com.idontwantcancer.app.domain.model.Signal
import com.idontwantcancer.app.presentation.components.*
import com.idontwantcancer.app.presentation.model.CommandConsumptionFinalityPresentationContract
import com.idontwantcancer.app.presentation.model.IntelligenceReentryReconciliationPresentationContract
import com.idontwantcancer.app.presentation.model.IntelligenceUiInteraction

/**
 * Signal Detail screen displaying the complete intelligence briefing for a specific signal.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignalDetailScreen(
    onInteraction: (IntelligenceUiInteraction) -> Unit,
    viewModel: SignalDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { onInteraction(IntelligenceUiInteraction.NavigateBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.nav_back_content_desc)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = uiState) {
                is SignalDetailUiState.Loading -> AgencyLoadingState()
                is SignalDetailUiState.NotFound -> AgencyEmptyState(
                    title = stringResource(R.string.not_found_title),
                    description = stringResource(R.string.not_found_desc),
                    icon = Icons.AutoMirrored.Filled.ArrowBack
                )
                is SignalDetailUiState.Error -> AgencyErrorState(
                    message = state.message,
                    onRetry = { onInteraction(IntelligenceUiInteraction.RetryOperation) },
                    onBack = { onInteraction(IntelligenceUiInteraction.NavigateBack) }
                )
                is SignalDetailUiState.Success -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    SignalDetailView(
                        signal = state.signal,
                        reconciliation = state.reconciliation,
                        finality = state.finality,
                        modifier = Modifier.widthIn(max = 800.dp)
                    )
                }
            }
        }
    }
}

/**
 * Reusable view for signal details.
 */
@Composable
fun SignalDetailView(
    signal: Signal,
    reconciliation: IntelligenceReentryReconciliationPresentationContract,
    finality: CommandConsumptionFinalityPresentationContract,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(spacing.screenPadding),
        verticalArrangement = Arrangement.spacedBy(spacing.sectionSpacing)
    ) {
        item {
            OperationalFinalityIndicator(finality)
        }

        if (reconciliation is IntelligenceReentryReconciliationPresentationContract.InconsistentMismatch) {
            item {
                ReconciliationWarning(reconciliation)
            }
        }

        item {
            DirectiveHeader(signal = signal)
        }

        if (signal.theExecution.isNotEmpty()) {
            item {
                HowToSection(steps = signal.theExecution)
            }
        }

        if (signal.theShield != null) {
            item {
                ShieldSection(content = signal.theShield)
            }
        }

        if (signal.investigatedClaim != null) {
            item {
                TruthCheckComparisonCard(signal = signal)
            }
        }

        signal.theTruth?.let { truth ->
            item {
                IntelligenceSection(
                    title = "THE TRUTH",
                    content = truth
                )
            }
        }

        // Legacy summary if Truth is missing
        if (signal.theTruth == null) {
            item {
                IntelligenceSection(
                    title = stringResource(R.string.detail_section_summary),
                    content = signal.summary
                )
            }
        }

        item {
            SourceSection(sourceName = signal.source.name)
        }
        
        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun HowToSection(steps: List<String>) {
    val spacing = LocalSpacing.current
    Column(verticalArrangement = Arrangement.spacedBy(spacing.medium)) {
        Text(
            text = stringResource(R.string.detail_how_to_title),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 2.sp,
            modifier = Modifier.semantics { heading() }
        )
        
        steps.forEachIndexed { index, step ->
            ExecutionStepItem(
                stepNumber = index + 1,
                content = step,
                isLast = index == steps.size - 1
            )
        }
    }
}

@Composable
private fun ShieldSection(content: String) {
    val spacing = LocalSpacing.current
    Surface(
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(spacing.cardPadding)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.detail_shield_verified),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Black
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun TruthCheckComparisonCard(signal: Signal) {
    val spacing = LocalSpacing.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(spacing.cardPadding)) {
            Text(
                text = stringResource(R.string.truth_check_claim_label),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = signal.investigatedClaim ?: "",
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.truth_check_verdict_label),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.width(8.dp))
                signal.verdict?.let { TruthCheckBadge(it) }
            }
        }
    }
}

@Composable
private fun IntelligenceSection(title: String, content: String) {
    val spacing = LocalSpacing.current
    Column(verticalArrangement = Arrangement.spacedBy(spacing.small)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.semantics { heading() }
        )
        Text(
            text = content,
            style = MaterialTheme.typography.bodyLarge,
            lineHeight = 26.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun SourceSection(sourceName: String) {
    val spacing = LocalSpacing.current
    Column(verticalArrangement = Arrangement.spacedBy(spacing.small)) {
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        Text(
            text = stringResource(R.string.detail_section_provenance),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.detail_source_label, sourceName),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
