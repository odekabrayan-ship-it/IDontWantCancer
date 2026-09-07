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
import com.idontwantcancer.app.presentation.home.DailyPeace
import java.time.Instant
import java.time.ZoneId

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
        // 1. Status & Peace
        item { AgencyStatusAnchor() }
        item {
            HealingProgressLedger(entries = state.healingLogEntries)
        }
        item { DailyPeaceCard(DailyPeace(title = "You are Winning", summary = "Every protective act you complete today strengthens your body's recovery shield.", shield = "Resilience is a pattern.")) }

        // 2. EMERGENCY SENTINEL (The Sentinel Scan)
        item { 
            Text(
                text = "1. EMERGENCY SCAN: DO YOU FEEL ANY OF THESE?",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.error,
                letterSpacing = 1.sp
            )
        }
        
        items(state.redFlagDirectives, key = { "rf-${it.id}" }) { flag ->
            RedFlagSentinelCard(flag)
        }

        // 3. RAPID SYMPTOM RELIEF (The Sensation Matrix)
        item {
            Text(
                text = "2. RAPID RELIEF: HOW DO YOU FEEL?",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.secondary,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        item {
            SensationReliefMatrix(
                symptoms = state.symptomDirectives,
                onComplete = { id, name -> viewModel.logHealingAction(id, name, HealingLogType.SYMPTOM) }
            )
        }

        // 4. TODAY'S TREATMENT PROTOCOL
        item {
            Text(
                text = "3. YOUR CURRENT TREATMENT PROTOCOL",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        items(state.treatmentManuals, key = { "manual-${it.id}" }) { manual ->
            TreatmentDossierCard(
                manual = manual,
                onComplete = { viewModel.logHealingAction(manual.id, manual.title, HealingLogType.MANUAL) }
            )
        }

        // 5. DECEPTION SHIELD
        item {
            Text(
                text = "4. PROTECTION FROM FALSE CURES",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        items(state.patientTruthChecks, key = { "truth-${it.id}" }) { truth ->
            PatientTruthDossierCard(truth)
        }
        
        item { Spacer(modifier = Modifier.height(spacing.extraLarge)) }
    }
}

@Composable
private fun RedFlagSentinelCard(flag: RedFlagDirective) {
    var isRaised by remember { mutableStateOf(false) }
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isRaised) colorScheme.errorContainer else colorScheme.surface
        ),
        border = BorderStroke(1.dp, if (isRaised) colorScheme.error else colorScheme.outlineVariant),
        shape = MaterialTheme.shapes.extraSmall
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Checkbox(
                    checked = isRaised,
                    onCheckedChange = { isRaised = it },
                    colors = CheckboxDefaults.colors(checkedColor = colorScheme.error)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = flag.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = if (isRaised) colorScheme.error else colorScheme.onSurface
                )
            }
            
            if (isRaised) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "ACTION: ${flag.theCommand}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = colorScheme.error
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text("WHILE YOU WAIT FOR THE DOCTOR:", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                flag.whileYouWait.forEach { step ->
                    Text("• $step", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(vertical = 4.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    color = colorScheme.error.copy(alpha = 0.1f),
                    shape = MaterialTheme.shapes.extraSmall,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("SAY THIS TO THE NURSE:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = colorScheme.error)
                        Text("\"${flag.handoffScript}\"", style = MaterialTheme.typography.bodyMedium, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun SensationReliefMatrix(
    symptoms: List<SymptomDirective>,
    onComplete: (String, String) -> Unit
) {
    var activeSymptom by remember { mutableStateOf<SymptomDirective?>(null) }
    val colorScheme = MaterialTheme.colorScheme

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            symptoms.forEach { symptom ->
                val isSelected = activeSymptom?.id == symptom.id
                FilterChip(
                    selected = isSelected,
                    onClick = { activeSymptom = if (isSelected) null else symptom },
                    label = { Text(symptom.name, fontWeight = FontWeight.Bold) },
                    shape = MaterialTheme.shapes.extraSmall,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = colorScheme.secondary,
                        selectedLabelColor = colorScheme.onSecondary
                    )
                )
            }
        }

        activeSymptom?.let { symptom ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                colors = CardDefaults.cardColors(containerColor = colorScheme.secondaryContainer.copy(alpha = 0.3f)),
                border = BorderStroke(1.dp, colorScheme.secondary),
                shape = MaterialTheme.shapes.extraSmall
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "IMMEDIATE ACTION:", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black, color = colorScheme.secondary)
                    Text(text = symptom.theCommand, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(vertical = 8.dp))
                    symptom.theExecution.forEach { step ->
                        Text("• $step", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(vertical = 4.dp))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { 
                            onComplete(symptom.id, symptom.name)
                            activeSymptom = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.extraSmall,
                        colors = ButtonDefaults.buttonColors(containerColor = colorScheme.secondary)
                    ) {
                        Text("I FEEL BETTER / ACTION TAKEN")
                    }
                }
            }
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
            Icon(Icons.Default.Shield, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "STATUS: OPERATIONAL - HEALING MODE ACTIVE", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onPrimary, letterSpacing = 1.sp)
        }
    }
}

@Composable
private fun DailyPeaceCard(peace: DailyPeace) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
        shape = MaterialTheme.shapes.extraSmall
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "DAILY PEACE", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = peace.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text = peace.summary, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
private fun HealingProgressLedger(entries: List<HealingLogEntry>) {
    val spacing = LocalSpacing.current
    val today = remember { Instant.now().atZone(ZoneId.systemDefault()).toLocalDate() }
    val doneToday = entries.filter { it.timestamp.atZone(ZoneId.systemDefault()).toLocalDate() == today }
    
    // Professional Resilience Shield
    Surface(
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
        shape = MaterialTheme.shapes.extraSmall,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "RESILIENCE SHIELD",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary
                )
                if (doneToday.isNotEmpty()) {
                    Surface(color = MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.extraSmall) {
                        Text(
                            text = "INTEGRITY SECURED",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = if (doneToday.isEmpty()) "Your shield is pending. Complete today's protocol to harden your defense." else "You have logged ${doneToday.size} protective acts today. Your body's environment is currently optimized.",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(5) { i ->
                    val active = i < doneToday.size
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        modifier = Modifier.size(28.dp)
                    )
                }
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
