package com.idontwantcancer.app.presentation.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

    BackHandler(navigator.canNavigateBack()) {
        scope.launch {
            if (navigator.navigateBack()) {
                // Step 209: Synchronize navigator back with ViewModel selection
                viewModel.onInteraction(IntelligenceUiInteraction.ClearSelection)
            }
        }
    }

    // Sync ViewModel selection with Navigator
    val selectedId = (uiState as? SearchUiState.Success)?.selectedSignalId
    LaunchedEffect(selectedId) {
        if (selectedId != null && navigator.currentDestination?.contentKey != selectedId) {
            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, selectedId)
        }
    }

    // Request focus on entry if no query exists
    LaunchedEffect(Unit) {
        if (query.isEmpty()) {
            focusRequester.requestFocus()
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
                        Text(
                            text = "Search",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .semantics { heading() }
                        )
                        Text(
                            text = "Ask the Intelligence Agency",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 24.dp)
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
                            placeholder = { Text("What do you want to know about?") },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                            trailingIcon = {
                                if (query.isNotEmpty()) {
                                    IconButton(onClick = {
                                        query = ""
                                        onInteraction(IntelligenceUiInteraction.ClearSearch)
                                        focusRequester.requestFocus()
                                    }) {
                                        Icon(Icons.Default.Clear, contentDescription = "Clear search")
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
                            shape = MaterialTheme.shapes.medium
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Box(modifier = Modifier
                            .fillMaxSize()
                            .imePadding()
                        ) {
                            when (val state = uiState) {
                                is SearchUiState.Idle -> SearchIdleState()
                                is SearchUiState.Searching -> AgencyLoadingState(message = "Checking the intelligence...")
                                is SearchUiState.Success -> SearchResultsList(
                                signals = state.signals,
                                reconciliations = state.reconciliations,
                                finality = state.finality,
                                selectedSignalId = state.selectedSignalId,
                                onInteraction = onInteraction,
                                onScroll = {
                                    focusManager.clearFocus()
                                    keyboardController?.hide()
                                }
                            )
                                is SearchUiState.Empty -> AgencyEmptyState(
                                    title = "No results",
                                    description = "We don't have a meaningful signal on this yet."
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
                                        title = "Not Found",
                                        description = "The requested intelligence could not be located."
                                    )
                                }
                            }
                        }
                    }
                } else {
                    AgencyEmptyState(
                        title = "Select a search result",
                        description = "Choose an item from the list to view its complete intelligence briefing."
                    )
                }
            }
        }
    )
}

@Composable
private fun SearchIdleState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Try searching for:",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(LocalSpacing.current.small))
        Text("food preservatives", style = MaterialTheme.typography.bodyMedium)
        Text("screening changes", style = MaterialTheme.typography.bodyMedium)
        Text("product recalls", style = MaterialTheme.typography.bodyMedium)
        Text("environmental exposure", style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun SearchResultsList(
    signals: List<Signal>,
    reconciliations: Map<String, IntelligenceReentryReconciliationPresentationContract>,
    finality: CommandConsumptionFinalityPresentationContract,
    selectedSignalId: String?,
    onInteraction: (IntelligenceUiInteraction) -> Unit,
    onScroll: () -> Unit
) {
    val spacing = LocalSpacing.current
    val listState = rememberLazyListState()
    
    // Dismiss keyboard on scroll
    LaunchedEffect(listState.isScrollInProgress) {
        if (listState.isScrollInProgress) {
            onScroll()
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = spacing.small),
        verticalArrangement = Arrangement.spacedBy(spacing.small)
    ) {
        item {
            OperationalFinalityIndicator(finality)
        }
        item {
            Text(
                text = "Intelligence found",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
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
