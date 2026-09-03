package com.idontwantcancer.app.presentation.alerts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.idontwantcancer.app.R
import com.idontwantcancer.app.core.ui.theme.LocalSpacing
import com.idontwantcancer.app.domain.model.Signal
import com.idontwantcancer.app.presentation.components.*
import com.idontwantcancer.app.presentation.model.CommandConsumptionFinalityPresentationContract
import com.idontwantcancer.app.presentation.model.IntelligenceReentryReconciliationPresentationContract
import com.idontwantcancer.app.presentation.model.IntelligenceUiInteraction
import com.idontwantcancer.app.presentation.signal.SignalDetailView
import com.idontwantcancer.app.presentation.signal.SignalDetailViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AlertsScreen(
    onInteraction: (IntelligenceUiInteraction) -> Unit,
    viewModel: AlertsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navigator = rememberListDetailPaneScaffoldNavigator<String>()
    val scope = rememberCoroutineScope()

    BackHandler(navigator.canNavigateBack()) {
        scope.launch {
            if (navigator.navigateBack()) {
                // Step 209: Synchronize navigator back with ViewModel selection
                viewModel.onInteraction(IntelligenceUiInteraction.ClearSelection)
            }
        }
    }

    // Sync ViewModel selection with Navigator
    val selectedId = (uiState as? AlertsUiState.Success)?.selectedSignalId
    LaunchedEffect(selectedId) {
        if (selectedId != null && navigator.currentDestination?.contentKey != selectedId) {
            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, selectedId)
        }
    }

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                Scaffold(
                    topBar = {
                        AlertsHeader()
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(LocalSpacing.current.screenPadding)
                    ) {
                        when (val state = uiState) {
                            is AlertsUiState.Loading -> AgencyLoadingState()
                            is AlertsUiState.Error -> AgencyErrorState(
                                message = state.message,
                                onRetry = { onInteraction(IntelligenceUiInteraction.RetryOperation) }
                            )
                            is AlertsUiState.Success -> {
                                AlertsContent(
                                    signals = state.signals,
                                    reconciliations = state.reconciliations,
                                    finality = state.finality,
                                    selectedSignalId = state.selectedSignalId,
                                    onInteraction = onInteraction
                                )
                            }
                        }
                    }
                }
            }
        },
        detailPane = {
            AnimatedPane {
                val selectedId = navigator.currentDestination?.contentKey
                if (selectedId != null) {
                    val detailViewModel: SignalDetailViewModel = hiltViewModel(
                        key = selectedId
                    )
                    
                    LaunchedEffect(selectedId) {
                        detailViewModel.loadSignal(selectedId)
                    }
                    
                    val detailUiState by detailViewModel.uiState.collectAsStateWithLifecycle()
                    
                    Scaffold { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            when (val state = detailUiState) {
                                is com.idontwantcancer.app.presentation.signal.SignalDetailUiState.Loading -> {
                                    AgencyLoadingState()
                                }
                                is com.idontwantcancer.app.presentation.signal.SignalDetailUiState.Success -> {
                                    SignalDetailView(
                                        signal = state.signal,
                                        reconciliation = state.reconciliation,
                                        finality = state.finality
                                    )
                                }
                                is com.idontwantcancer.app.presentation.signal.SignalDetailUiState.Error -> {
                                    AgencyErrorState(
                                        message = state.message,
                                        onRetry = { detailViewModel.loadSignal(selectedId) }
                                    )
                                }
                                is com.idontwantcancer.app.presentation.signal.SignalDetailUiState.NotFound -> {
                                    AgencyEmptyState(
                                        title = stringResource(R.string.not_found_title),
                                        description = stringResource(R.string.not_found_desc)
                                    )
                                }
                            }
                        }
                    }
                } else {
                    AgencyEmptyState(
                        title = stringResource(R.string.alerts_select_prompt),
                        description = stringResource(R.string.alerts_select_desc)
                    )
                }
            }
        }
    )
}

@Composable
private fun AlertsHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 24.dp)
            .semantics { heading() }
    ) {
        Text(
            text = stringResource(R.string.alerts_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.alerts_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun AlertsContent(
    signals: List<Signal>,
    reconciliations: Map<String, IntelligenceReentryReconciliationPresentationContract>,
    finality: CommandConsumptionFinalityPresentationContract,
    selectedSignalId: String?,
    onInteraction: (IntelligenceUiInteraction) -> Unit
) {
    val spacing = LocalSpacing.current
    if (signals.isEmpty()) {
        AlertsEmptyState()
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = spacing.large),
            verticalArrangement = Arrangement.spacedBy(spacing.small)
        ) {
            item {
                OperationalFinalityIndicator(finality)
            }
            items(
                items = signals,
                key = { it.id }
            ) { signal ->
                val onClick = remember(signal.id, onInteraction) {
                    { onInteraction(IntelligenceUiInteraction.ViewSignalDetails(signal.id)) }
                }
                
                SignalCard(
                    signal = signal,
                    isSelected = signal.id == selectedSignalId,
                    onClick = onClick,
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

@Composable
private fun AlertsEmptyState() {
    AgencyEmptyState(
        title = stringResource(R.string.alerts_empty_title),
        description = stringResource(R.string.alerts_empty_desc),
        icon = Icons.Default.Notifications
    )
}
