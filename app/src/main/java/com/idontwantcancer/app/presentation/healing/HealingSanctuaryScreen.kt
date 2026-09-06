package com.idontwantcancer.app.presentation.healing

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
 import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.presentation.components.AgencyLoadingState
import com.idontwantcancer.app.presentation.components.ExecutionStepItem
import com.idontwantcancer.app.presentation.home.DailyPeace

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealingSanctuaryScreen(
    onSettings: () -> Unit,
    viewModel: HealingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "CLINICAL CONTROL",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                },
                actions = {
                    IconButton(onClick = onSettings) {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = null)
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
                is HealingUiState.Success -> ClinicalStreamContent(state, viewModel)
            }
        }
    }
}

@Composable
private fun ClinicalStreamContent(state: HealingUiState.Success, viewModel: HealingViewModel) {
    val spacing = LocalSpacing.current
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(spacing.screenPadding),
        verticalArrangement = Arrangement.spacedBy(spacing.large)
    ) {
        // 1. Agency Operational Status
        item {
            AgencyStatusAnchor()
        }

        // 2. Victory Ledger (Progress)
        if (state.healingLogEntries.isNotEmpty()) {
            item {
                HealingProgressLedger(entries = state.healingLogEntries)
            }
        }

        // 3. Daily Peace (Affirmation)
        item {
            DailyPeaceCard(
                DailyPeace(
                    title = "You are Winning",
                    summary = "Every protective act you complete today strengthens your body's recovery shield.",
                    shield = "Resilience is a pattern."
                )
            )
        }

        // Triage 1: Are you safe? (Red Flags)
        item {
            ClinicalSectionHeader(
                title = "1. SAFETY CHECK: ARE YOU FEELING ANY OF THESE?",
                color = MaterialTheme.colorScheme.error
            )
        }
        
        items(state.redFlagDirectives, key = { "rf-${it.id}" }) { flag ->
            RedFlagDossierCard(flag)
        }

        // Triage 2: Your Protocol Today
        item {
            ClinicalSectionHeader(
                title = "2. YOUR CURRENT TREATMENT PROTOCOL",
                color = MaterialTheme.colorScheme.primary
            )
        }

        items(state.treatmentManuals, key = { "manual-${it.id}" }) { manual ->
            TreatmentDossierCard(
                manual = manual,
                onComplete = { viewModel.logHealingAction(manual.id, manual.title, HealingLogType.MANUAL) }
            )
        }

        // Triage 3: Symptom Toolkit
        item {
            ClinicalSectionHeader(
                title = "3. HELP WITH TREATMENT SIDE-EFFECTS",
                color = MaterialTheme.colorScheme.secondary
            )
        }

        items(state.symptomDirectives, key = { "symptom-${it.id}" }) { symptom ->
            SymptomDossierCard(
                symptom = symptom,
                onComplete = { viewModel.logHealingAction(symptom.id, symptom.name, HealingLogType.SYMPTOM) }
            )
        }

        // Triage 4: Deception Shield
        item {
            ClinicalSectionHeader(
                title = "4. PROTECTION FROM FALSE CURES",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        items(state.patientTruthChecks, key = { "truth-${it.id}" }) { truth ->
            PatientTruthDossierCard(truth)
        }
        
        item {
            Spacer(modifier = Modifier.height(spacing.extraLarge))
        }
    }
}

@Composable
private fun AgencyStatusAnchor() {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        shape = MaterialTheme.shapes.extraSmall,
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
                text = "STATUS: OPERATIONAL - HEALING MODE ACTIVE",
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
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
        shape = MaterialTheme.shapes.extraSmall
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
private fun HealingProgressLedger(entries: List<HealingLogEntry>) {
    val spacing = LocalSpacing.current
    val lastEntries = entries.take(5)

    Surface(
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
        shape = MaterialTheme.shapes.extraSmall,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(spacing.cardPadding)) {
            Text(
                text = "HEALING PROGRESS",
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
        }
    }
}

@Composable
private fun ClinicalSectionHeader(title: String, color: androidx.compose.ui.graphics.Color) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Black,
        color = color,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp).semantics { heading() }
    )
}

@Composable
private fun RedFlagDossierCard(flag: RedFlagDirective) {
    var expanded by remember { mutableStateOf(false) }
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        border = BorderStroke(1.dp, colorScheme.error.copy(alpha = 0.5f)),
        shape = MaterialTheme.shapes.extraSmall
    ) {
        Column(modifier = Modifier.clickable { expanded = !expanded }.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Report, contentDescription = null, tint = colorScheme.error, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(flag.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(flag.theCommand, color = colorScheme.error, fontWeight = FontWeight.Black)
                Spacer(modifier = Modifier.height(12.dp))
                flag.whileYouWait.forEach { step ->
                    Text("• $step", style = MaterialTheme.typography.bodyMedium)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Surface(color = colorScheme.error.copy(alpha = 0.05f), modifier = Modifier.fillMaxWidth()) {
                    Text("\"${flag.handoffScript}\"", modifier = Modifier.padding(8.dp), fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                }
            }
        }
    }
}

@Composable
private fun TreatmentDossierCard(manual: TreatmentManual, onComplete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
        shape = MaterialTheme.shapes.extraSmall
    ) {
        Column(modifier = Modifier.clickable { expanded = !expanded }.padding(16.dp)) {
            Text(manual.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))
                manual.theExecution.forEachIndexed { i, step ->
                    ExecutionStepItem(stepNumber = i + 1, content = step, isLast = i == manual.theExecution.size - 1)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onComplete, modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.extraSmall) {
                    Text("I COMPLETED THIS")
                }
            }
        }
    }
}

@Composable
private fun SymptomDossierCard(symptom: SymptomDirective, onComplete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)),
        shape = MaterialTheme.shapes.extraSmall
    ) {
        Column(modifier = Modifier.clickable { expanded = !expanded }.padding(16.dp)) {
            Text(symptom.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(symptom.theCommand, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.secondary)
                Spacer(modifier = Modifier.height(12.dp))
                symptom.theExecution.forEach { step ->
                    Text("• $step", style = MaterialTheme.typography.bodyMedium)
                }
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = onComplete) { Text("MARK COMPLETED") }
            }
        }
    }
}

@Composable
private fun PatientTruthDossierCard(truth: PatientTruthCheck) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shape = MaterialTheme.shapes.extraSmall
    ) {
        Column(modifier = Modifier.clickable { expanded = !expanded }.padding(16.dp)) {
            Text(truth.claim, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(truth.theTruth, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(12.dp))
                Surface(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)) {
                    Text(truth.socialScript, modifier = Modifier.padding(8.dp), fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                }
            }
        }
    }
}
