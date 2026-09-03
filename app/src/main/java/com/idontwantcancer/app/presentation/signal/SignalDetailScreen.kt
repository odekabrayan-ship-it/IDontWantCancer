package com.idontwantcancer.app.presentation.signal

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
            SignalHeader(signal = signal)
        }

        item {
            IntelligenceSection(
                title = stringResource(R.string.detail_section_summary),
                content = signal.summary
            )
        }

        signal.significance?.let { significance ->
            item {
                IntelligenceSection(
                    title = stringResource(R.string.detail_section_significance),
                    content = significance
                )
            }
        }

        signal.explanation?.let { explanation ->
            item {
                IntelligenceSection(
                    title = stringResource(R.string.detail_section_explanation),
                    content = explanation
                )
            }
        }

        signal.recommendedAction?.let { action ->
            item {
                IntelligenceSection(
                    title = stringResource(R.string.detail_section_action),
                    content = action
                )
            }
        }

        item {
            SourceSection(sourceName = signal.source.name)
        }
        
        item {
            Spacer(modifier = Modifier.height(16.dp))
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
