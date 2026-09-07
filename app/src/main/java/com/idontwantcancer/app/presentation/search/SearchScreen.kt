package com.idontwantcancer.app.presentation.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
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
import com.idontwantcancer.app.presentation.signal.SignalDetailView
import com.idontwantcancer.app.presentation.signal.SignalDetailViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun SearchScreen(
    initialStoreMode: Boolean = false,
    onInteraction: (IntelligenceUiInteraction) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var query by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val navigator = rememberListDetailPaneScaffoldNavigator<String>()
    val scope = rememberCoroutineScope()
    
    // Context filter state initialized with parameter
    var storeFilterActive by rememberSaveable { mutableStateOf(initialStoreMode) }

    BackHandler(navigator.canNavigateBack()) {
        scope.launch {
            if (navigator.navigateBack()) {
                viewModel.onInteraction(IntelligenceUiInteraction.ClearSelection)
            }
        }
    }

    val selectedId = (uiState as? SearchUiState.Success)?.selectedSignalId
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
                Scaffold { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(LocalSpacing.current.screenPadding)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.HealthAndSafety,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "VERIFICATION LAB",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.semantics { heading() }
                            )
                        }
                        Text(
                            text = "Verify label ingredients and health claims instantly",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                        )
                        
                        FilterChip(
                            selected = storeFilterActive,
                            onClick = { storeFilterActive = !storeFilterActive },
                            label = { Text("🛒 AT THE STORE") },
                            leadingIcon = if (storeFilterActive) {
                                { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                            } else null,
                            modifier = Modifier.padding(bottom = 16.dp),
                            shape = MaterialTheme.shapes.extraSmall
                        )

                        OutlinedTextField(
                            value = query,
                            onValueChange = {
                                query = it
                                if (it.isBlank()) {
                                    onInteraction(IntelligenceUiInteraction.ClearSearch)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester),
                            placeholder = { Text("Enter ingredient name or E-Number...") },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                            trailingIcon = {
                                if (query.isNotEmpty()) {
                                    IconButton(onClick = {
                                        query = ""
                                        onInteraction(IntelligenceUiInteraction.ClearSearch)
                                        focusRequester.requestFocus()
                                    }) {
                                        Icon(Icons.Default.Clear, contentDescription = null)
                                    }
                                }
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    onInteraction(IntelligenceUiInteraction.PerformSearch(query))
                                    focusManager.clearFocus()
                                    keyboardController?.hide()
                                }
                            ),
                            shape = MaterialTheme.shapes.extraSmall
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Box(modifier = Modifier.fillMaxSize().imePadding()) {
                            when (val state = uiState) {
                                is SearchUiState.Idle -> LabelScanDirectory(onChipClick = {
                                    query = it
                                    onInteraction(IntelligenceUiInteraction.PerformSearch(it))
                                })
                                is SearchUiState.Searching -> SearchingState()
                                is SearchUiState.Success -> {
                                    val filteredSignals = if (storeFilterActive) {
                                        state.signals.filter { "Store" in it.interactionContexts }
                                    } else {
                                        state.signals
                                    }
                                    SearchResultsList(
                                        signals = filteredSignals,
                                        verdict = state.verdict,
                                        reconciliations = state.reconciliations,
                                        finality = state.finality,
                                        selectedSignalId = state.selectedSignalId,
                                        onInteraction = onInteraction,
                                        onScroll = {
                                            focusManager.clearFocus()
                                            keyboardController?.hide()
                                        }
                                    )
                                }
                                is SearchUiState.Empty -> AgencyEmptyState(
                                    title = "NO REGISTRY MATCH",
                                    description = "This ingredient is not flagged in the Agency's high-priority carcinogen database."
                                )
                                is SearchUiState.Error -> AgencyErrorState(
                                    message = state.message,
                                    onRetry = { onInteraction(IntelligenceUiInteraction.RetryOperation) }
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
                    val detailViewModel: SignalDetailViewModel = hiltViewModel(key = selectedId)
                    LaunchedEffect(selectedId) { detailViewModel.loadSignal(selectedId) }
                    val detailUiState by detailViewModel.uiState.collectAsStateWithLifecycle()

                    Scaffold { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            when (val state = detailUiState) {
                                is com.idontwantcancer.app.presentation.signal.SignalDetailUiState.Loading -> AgencyLoadingState()
                                is com.idontwantcancer.app.presentation.signal.SignalDetailUiState.Success -> {
                                    SignalDetailView(
                                        signal = state.signal,
                                        reconciliation = state.reconciliation,
                                        finality = state.finality
                                    )
                                }
                                is com.idontwantcancer.app.presentation.signal.SignalDetailUiState.Error -> AgencyErrorState(message = state.message, onRetry = { detailViewModel.loadSignal(selectedId) })
                                is com.idontwantcancer.app.presentation.signal.SignalDetailUiState.NotFound -> AgencyEmptyState(title = "NOT FOUND", description = "The requested intelligence could not be located.")
                            }
                        }
                    }
                } else {
                    AgencyEmptyState(title = "SELECT A RESULT", description = "Choose an item from the list to view its complete security dossier.")
                }
            }
        }
    )
}

@Composable
private fun LabelScanDirectory(onChipClick: (String) -> Unit) {
    val spacing = LocalSpacing.current
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Text(
                text = "LABEL SCAN GUIDE: THE DIRTY 50",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 1.sp
            )
        }

        item {
            AisleSection(
                title = "💄 COSMETICS & CARE",
                items = listOf("Parabens", "Phthalates", "DMDM Hydantoin", "PFAS", "Talc", "Aluminum"),
                onChipClick = onChipClick
            )
        }

        item {
            AisleSection(
                title = "🧼 CLEANING & LAUNDRY",
                items = listOf("Triclosan", "PEG", "SLES", "1,4-Dioxane", "Quats"),
                onChipClick = onChipClick
            )
        }

        item {
            AisleSection(
                title = "🍎 FOOD ADDITIVES",
                items = listOf("E250", "E171", "Potassium Bromate", "BHA", "Red 40", "Yellow 5"),
                onChipClick = onChipClick
            )
        }
    }
}

@Composable
private fun AisleSection(title: String, items: List<String>, onChipClick: (String) -> Unit) {
    Column {
        Text(text = title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items.forEach { label ->
                SuggestionChip(
                    onClick = { onChipClick(label) },
                    label = { Text(label, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium) },
                    shape = MaterialTheme.shapes.extraSmall
                )
            }
        }
    }
}

@Composable
private fun SearchingState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth().height(2.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "SCANNING REGISTRY...",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.primary,
            letterSpacing = 1.sp
        )
    }
}

@Composable
private fun SearchResultsList(
    signals: List<Signal>,
    verdict: SummaryVerdict,
    reconciliations: Map<String, IntelligenceReentryReconciliationPresentationContract>,
    finality: CommandConsumptionFinalityPresentationContract,
    selectedSignalId: String?,
    onInteraction: (IntelligenceUiInteraction) -> Unit,
    onScroll: () -> Unit
) {
    val spacing = LocalSpacing.current
    val listState = rememberLazyListState()
    
    LaunchedEffect(listState.isScrollInProgress) { if (listState.isScrollInProgress) onScroll() }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(spacing.small)
    ) {
        item { SummaryVerdictHeader(verdict) }
        item { OperationalFinalityIndicator(finality) }
        
        items(items = signals, key = { it.id }) { signal ->
            SignalCard(
                signal = signal,
                isSelected = signal.id == selectedSignalId,
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

@Composable
private fun SummaryVerdictHeader(verdict: SummaryVerdict) {
    val colorScheme = MaterialTheme.colorScheme
    val (title, icon, bgColor, textColor) = when (verdict) {
        is SummaryVerdict.HazardDetected -> Quad(
            "🔴 HAZARD DETECTED: PUT THIS ITEM BACK",
            Icons.Default.Report,
            colorScheme.error,
            colorScheme.onError
        )
        is SummaryVerdict.ScamDetected -> Quad(
            "🔴 FRAUD ALERT: DO NOT TRUST THIS CLAIM",
            Icons.Default.GppBad,
            colorScheme.error,
            colorScheme.onError
        )
        SummaryVerdict.NoHazardMatch -> Quad(
            "🟢 NO REGISTRY MATCH: SAFE FOR USE",
            Icons.Default.Verified,
            colorScheme.primary,
            colorScheme.onPrimary
        )
    }

    Surface(
        color = bgColor,
        shape = MaterialTheme.shapes.extraSmall,
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = textColor, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = title, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = textColor, letterSpacing = 1.sp)
        }
    }
}

private data class Quad<A, B, C, D>(val a: A, val b: B, val c: C, val d: D)
