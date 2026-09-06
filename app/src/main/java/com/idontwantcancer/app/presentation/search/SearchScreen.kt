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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Verified
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.HealthAndSafety,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = stringResource(R.string.search_title),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.semantics { heading() }
                            )
                        }
                        Text(
                            text = stringResource(R.string.search_subtitle),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
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
                            placeholder = { Text(stringResource(R.string.search_placeholder)) },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                            trailingIcon = {
                                if (query.isNotEmpty()) {
                                    IconButton(onClick = {
                                        query = ""
                                        onInteraction(IntelligenceUiInteraction.ClearSearch)
                                        focusRequester.requestFocus()
                                    }) {
                                        Icon(Icons.Default.Clear, contentDescription = stringResource(R.string.search_clear_content_desc))
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
                                is SearchUiState.Idle -> SearchIdleState(
                                    onChipClick = {
                                        query = it
                                        onInteraction(IntelligenceUiInteraction.PerformSearch(it))
                                    }
                                )
                                is SearchUiState.Searching -> {
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
                                    title = stringResource(R.string.search_empty_title),
                                    description = stringResource(R.string.search_empty_desc)
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
                                        title = stringResource(R.string.not_found_title),
                                        description = stringResource(R.string.not_found_desc)
                                    )
                                }
                            }
                        }
                    }
                } else {
                    AgencyEmptyState(
                        title = stringResource(R.string.search_select_prompt),
                        description = stringResource(R.string.search_select_desc)
                    )
                }
            }
        }
    )
}

@Composable
private fun SearchIdleState(onChipClick: (String) -> Unit) {
    val spacing = LocalSpacing.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
            shape = MaterialTheme.shapes.medium,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.search_idle_prompt),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(spacing.medium))
                
                FlowRow(
                    horizontalArrangement = Arrangement.Center,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val chips = listOf(
                        "💄 " + stringResource(R.string.cat_cosmetics),
                        "🧪 " + stringResource(R.string.cat_cleaning),
                        "🍎 " + stringResource(R.string.cat_food),
                        "🧬 " + stringResource(R.string.cat_nutrition),
                        "🩺 " + stringResource(R.string.cat_screening),
                        "🏭 " + stringResource(R.string.cat_occupational)
                    )
                    
                    chips.forEach { label ->
                        val queryText = label.substring(2)
                        SuggestionChip(
                            onClick = { onChipClick(queryText) },
                            label = { 
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                ) 
                            },
                            modifier = Modifier.padding(horizontal = 4.dp),
                            shape = MaterialTheme.shapes.large
                        )
                    }
                }
            }
        }
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
            Surface(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                shape = MaterialTheme.shapes.extraSmall,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Verified, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "SECURITY AUDIT: ${signals.size} MATCHES FOUND",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
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
