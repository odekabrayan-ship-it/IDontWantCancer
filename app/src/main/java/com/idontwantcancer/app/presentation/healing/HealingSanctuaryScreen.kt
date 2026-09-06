package com.idontwantcancer.app.presentation.healing

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.idontwantcancer.app.core.ui.theme.LocalSpacing
import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.presentation.components.AgencyLoadingState
import com.idontwantcancer.app.presentation.components.ExecutionStepItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealingSanctuaryScreen(
    onBack: () -> Unit,
    viewModel: HealingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.sanctuary_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.nav_back_content_desc)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary
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
                is HealingUiState.Loading -> AgencyLoadingState()
                is HealingUiState.Success -> SanctuaryContent(state, viewModel)
            }
        }
    }
}

@Composable
private fun SanctuaryContent(state: HealingUiState.Success, viewModel: HealingViewModel) {
    val spacing = LocalSpacing.current
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(spacing.screenPadding),
        horizontalArrangement = Arrangement.spacedBy(spacing.medium),
        verticalArrangement = Arrangement.spacedBy(spacing.medium)
    ) {
        item(span = { GridItemSpan(2) }) {
            SanctuaryWelcomeCard()
        }

        if (state.healingLogEntries.isNotEmpty()) {
            item(span = { GridItemSpan(2) }) {
                HealingProgressLedger(entries = state.healingLogEntries)
            }
        }

        item(span = { GridItemSpan(2) }) {
            Column(modifier = Modifier.padding(top = spacing.medium)) {
                Text(
                    text = "WATCH FOR THESE SIGNS",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.error
                )
                Text(
                    text = "High-priority red flags that need your attention",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        items(state.redFlagDirectives, key = { "rf-${it.id}" }, span = { GridItemSpan(2) }) { flag ->
            RedFlagItem(flag)
        }

        item(span = { GridItemSpan(2) }) {
            Text(
                text = "YOUR TREATMENT STEP-BY-STEP",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = spacing.large)
            )
        }

        items(state.treatmentManuals, key = { "manual-${it.id}" }, span = { GridItemSpan(2) }) { manual ->
            TreatmentManualItem(
                manual = manual,
                onComplete = { viewModel.logHealingAction(manual.id, manual.title, HealingLogType.MANUAL) }
            )
        }

        item(span = { GridItemSpan(2) }) {
            Column(modifier = Modifier.padding(top = spacing.large)) {
                Text(
                    text = "IF YOU ARE FEELING UNWELL",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Immediate simple steps for treatment side-effects",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        items(state.symptomDirectives, key = { "symptom-${it.id}" }) { symptom ->
            SymptomSentinelCard(
                symptom = symptom,
                onComplete = { viewModel.logHealingAction(symptom.id, symptom.name, HealingLogType.SYMPTOM) }
            )
        }

        item(span = { GridItemSpan(2) }) {
            Column(modifier = Modifier.padding(top = spacing.large)) {
                Text(
                    text = "PROTECTION FROM FALSE CURES",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.error
                )
                Text(
                    text = "Agency verification of common patient myths",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        items(state.patientTruthChecks, key = { "truth-${it.id}" }, span = { GridItemSpan(2) }) { truth ->
            PatientTruthCard(truth)
        }
        
        item(span = { GridItemSpan(2) }) {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun HealingProgressLedger(entries: List<HealingLogEntry>) {
    val spacing = LocalSpacing.current
    val lastEntries = entries.take(5)

    Surface(
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(spacing.cardPadding)) {
            Text(
                text = stringResource(R.string.sanctuary_section_progress),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                lastEntries.forEach { _ ->
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.sanctuary_winning_affirmation),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun SanctuaryWelcomeCard() {
    val spacing = LocalSpacing.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Healing,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(64.dp)
        )
        
        Spacer(modifier = Modifier.height(spacing.medium))

        Text(
            text = stringResource(R.string.sanctuary_welcome_headline),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.semantics { heading() }
        )
        
        Text(
            text = stringResource(R.string.sanctuary_welcome_desc),
            style = MaterialTheme.typography.bodyLarge,
            lineHeight = 26.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
private fun RedFlagItem(flag: RedFlagDirective) {
    var expanded by remember { mutableStateOf(false) }
    val spacing = LocalSpacing.current
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.errorContainer.copy(alpha = 0.1f)
        ),
        border = BorderStroke(1.dp, colorScheme.error.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .padding(spacing.cardPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "WATCH FOR",
                        style = MaterialTheme.typography.labelSmall,
                        color = colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = flag.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = colorScheme.error
                )
            }
            
            Text(
                text = flag.summary,
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )

            if (expanded) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    HorizontalDivider(color = colorScheme.error.copy(alpha = 0.2f), modifier = Modifier.padding(bottom = 16.dp))
                    
                    Text(
                        text = "THE CALM COMMAND",
                        style = MaterialTheme.typography.labelLarge,
                        color = colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = flag.theCommand,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Black,
                        color = colorScheme.onSurface,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "WHILE YOU WAIT FOR THE CALLBACK",
                        style = MaterialTheme.typography.labelLarge,
                        color = colorScheme.secondary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    flag.whileYouWait.forEachIndexed { index, step ->
                        ExecutionStepItem(
                            stepNumber = index + 1,
                            content = step,
                            isLast = index == flag.whileYouWait.size - 1
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Surface(
                        color = colorScheme.secondary.copy(alpha = 0.05f),
                        shape = MaterialTheme.shapes.medium,
                        border = BorderStroke(1.dp, colorScheme.secondary.copy(alpha = 0.2f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "WHAT TO TELL THE NURSE",
                                style = MaterialTheme.typography.labelLarge,
                                color = colorScheme.secondary,
                                fontWeight = FontWeight.Black
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "\"${flag.handoffScript}\"",
                                style = MaterialTheme.typography.bodyMedium,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TreatmentManualItem(manual: TreatmentManual, onComplete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val spacing = LocalSpacing.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .padding(spacing.cardPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = manual.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null
                )
            }
            
            Text(
                text = manual.summary,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )

            if (expanded) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp), color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                    
                    Text(
                        text = "THE COMMAND",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = manual.theCommand,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "THE EXECUTION",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    manual.theExecution.forEachIndexed { index, step ->
                        ExecutionStepItem(
                            stepNumber = index + 1,
                            content = step,
                            isLast = index == manual.theExecution.size - 1
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = onComplete,
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(R.string.sanctuary_action_completed), fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Surface(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = MaterialTheme.shapes.medium
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
                                    text = "PROTECTION VERIFIED",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = manual.theShield,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SymptomSentinelCard(symptom: SymptomDirective, onComplete: () -> Unit) {
    var showDetails by remember { mutableStateOf(false) }
    val colorScheme = MaterialTheme.colorScheme
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        onClick = { showDetails = true },
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        border = BorderStroke(1.dp, colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val icon = when (symptom.iconName) {
                "sick" -> Icons.Default.Sick
                "battery_alert" -> Icons.Default.BatteryAlert
                "no_food" -> Icons.Default.NoFood
                else -> Icons.Default.Info
            }
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = symptom.name.uppercase(),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                color = colorScheme.onSurface
            )
        }
    }

    if (showDetails) {
        AlertDialog(
            onDismissRequest = { showDetails = false },
            confirmButton = {
                TextButton(onClick = { 
                    onComplete()
                    showDetails = false 
                }) {
                    Text(stringResource(R.string.sanctuary_action_completed), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDetails = false }) {
                    Text("CLOSE")
                }
            },
            title = {
                Text(
                    text = symptom.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = symptom.theCommand,
                        style = MaterialTheme.typography.titleMedium,
                        color = colorScheme.primary,
                        fontWeight = FontWeight.ExtraBold
                    )
                    
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        symptom.theExecution.forEachIndexed { index, step ->
                            Row {
                                Text(
                                    text = "${index + 1}.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = colorScheme.primary,
                                    modifier = Modifier.width(24.dp)
                                )
                                Text(
                                    text = step,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                    
                    Surface(
                        color = colorScheme.primary.copy(alpha = 0.05f),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = symptom.theShield,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(8.dp),
                            color = colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        )
    }
}

@Composable
private fun PatientTruthCard(truth: PatientTruthCheck) {
    var expanded by remember { mutableStateOf(false) }
    val colorScheme = MaterialTheme.colorScheme
    val spacing = LocalSpacing.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.errorContainer.copy(alpha = 0.1f)
        ),
        border = BorderStroke(1.dp, colorScheme.error.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .padding(spacing.cardPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "THE CLAIM",
                        style = MaterialTheme.typography.labelSmall,
                        color = colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = truth.claim,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black
                    )
                }
                Surface(
                    color = colorScheme.error,
                    shape = MaterialTheme.shapes.extraSmall
                ) {
                    Text(
                        text = truth.verdict.name,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = colorScheme.onError,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            if (expanded) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    HorizontalDivider(color = colorScheme.error.copy(alpha = 0.2f), modifier = Modifier.padding(bottom = 16.dp))
                    
                    Text(
                        text = "THE TRUTH",
                        style = MaterialTheme.typography.labelLarge,
                        color = colorScheme.secondary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = truth.theTruth,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "THE COMMAND",
                        style = MaterialTheme.typography.labelLarge,
                        color = colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = truth.theCommand,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = colorScheme.error,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Surface(
                        color = colorScheme.primary.copy(alpha = 0.05f),
                        shape = MaterialTheme.shapes.medium,
                        border = BorderStroke(1.dp, colorScheme.primary.copy(alpha = 0.2f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "WHAT TO SAY TO FRIENDS",
                                style = MaterialTheme.typography.labelLarge,
                                color = colorScheme.primary,
                                fontWeight = FontWeight.Black
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "\"${truth.socialScript}\"",
                                style = MaterialTheme.typography.bodyMedium,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
