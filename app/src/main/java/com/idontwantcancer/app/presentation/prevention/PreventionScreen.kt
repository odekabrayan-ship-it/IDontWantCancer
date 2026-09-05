package com.idontwantcancer.app.presentation.prevention

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.idontwantcancer.app.domain.model.EducationLesson
import com.idontwantcancer.app.domain.model.NutritionIntelligence
import com.idontwantcancer.app.domain.model.PreventionAction
import com.idontwantcancer.app.domain.model.Signal
import com.idontwantcancer.app.presentation.components.AgencyLoadingState
import com.idontwantcancer.app.presentation.components.SignalCard

@Composable
fun PreventionScreen(
    viewModel: PreventionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            PreventionHeader()
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = uiState) {
                is PreventionUiState.Loading -> AgencyLoadingState()
                is PreventionUiState.Success -> PreventionContent(state, viewModel)
            }
        }
    }
}

@Composable
private fun PreventionHeader() {
    val spacing = LocalSpacing.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = spacing.screenPadding, vertical = spacing.large)
            .semantics { heading() }
    ) {
        Text(
            text = stringResource(R.string.prevention_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.prevention_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun PreventionContent(state: PreventionUiState.Success, viewModel: PreventionViewModel) {
    val spacing = LocalSpacing.current
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = spacing.medium)
    ) {
        item {
            PreventionSectionHeader(
                title = stringResource(R.string.prevention_section_actions),
                subtitle = stringResource(R.string.prevention_section_actions_desc),
                icon = Icons.Default.TrackChanges
            )
        }

        items(state.preventionActions, key = { it.id }) { action ->
            PreventionActionItem(
                action = action,
                onToggle = { viewModel.toggleActionAdoption(action.id) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(spacing.large))
            PreventionSectionHeader(
                title = stringResource(R.string.prevention_section_eat_smarter),
                subtitle = stringResource(R.string.prevention_section_eat_smarter_desc)
            )
        }

        items(state.nutritionTruths, key = { it.id }) { truth ->
            NutritionTruthItem(truth)
        }

        item {
            Spacer(modifier = Modifier.height(spacing.large))
            PreventionSectionHeader(
                title = stringResource(R.string.prevention_section_environment),
                subtitle = stringResource(R.string.prevention_section_environment_desc),
                icon = Icons.Default.Public
            )
        }

        items(state.environmentalSignals, key = { it.id }) { signal ->
            SignalCard(
                signal = signal,
                isSelected = false,
                onClick = { /* Step 223: Detail navigation */ }
            )
        }

        item {
            Spacer(modifier = Modifier.height(spacing.large))
            PreventionSectionHeader(
                title = stringResource(R.string.prevention_section_education),
                subtitle = stringResource(R.string.prevention_section_education_desc),
                icon = Icons.Default.School
            )
        }

        items(state.educationLessons, key = { it.id }) { lesson ->
            EducationLessonItem(lesson)
        }
        
        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun PreventionSectionHeader(
    title: String, 
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector = Icons.Default.Restaurant
) {
    val spacing = LocalSpacing.current
    Column(modifier = Modifier.padding(horizontal = spacing.screenPadding, vertical = spacing.medium)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun PreventionActionItem(
    action: PreventionAction,
    onToggle: () -> Unit
) {
    val spacing = LocalSpacing.current
    ListItem(
        headlineContent = {
            Text(
                text = action.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        },
        supportingContent = {
            Text(
                text = action.description,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        trailingContent = {
            Checkbox(
                checked = action.isAdopted,
                onCheckedChange = { onToggle() }
            )
        },
        leadingContent = {
            val icon = when (action.iconName) {
                "smoke_free" -> Icons.Default.SmokeFree
                "sunny" -> Icons.Default.WbSunny
                "no_drinks" -> Icons.Default.NoDrinks
                "directions_run" -> Icons.Default.DirectionsRun
                "grass" -> Icons.Default.Grass
                "restaurant" -> Icons.Default.Restaurant
                else -> Icons.Default.Verified
            }
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (action.isAdopted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        modifier = Modifier.clickable { onToggle() }
    )
}

@Composable
private fun NutritionTruthItem(truth: NutritionIntelligence) {
    var expanded by remember { mutableStateOf(false) }
    val spacing = LocalSpacing.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spacing.screenPadding, vertical = spacing.extraSmall),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
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
                    text = truth.title,
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
                text = truth.summary,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp))
                    
                    TruthDetailSection(
                        label = stringResource(R.string.truth_evidence_label),
                        content = truth.evidenceLevel.name.replace("_", " "),
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    TruthDetailSection(
                        label = stringResource(R.string.truth_reality_label),
                        content = truth.reality
                    )
                    
                    TruthDetailSection(
                        label = stringResource(R.string.truth_recommendation_label),
                        content = truth.recommendation,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Surface(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = stringResource(R.string.truth_no_fear_clause),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EducationLessonItem(lesson: EducationLesson) {
    var expanded by remember { mutableStateOf(false) }
    val spacing = LocalSpacing.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spacing.screenPadding, vertical = spacing.extraSmall),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
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
                    text = lesson.title,
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
                text = lesson.summary,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp))
                    
                    Text(
                        text = lesson.content,
                        style = MaterialTheme.typography.bodyLarge,
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Surface(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "KEY TAKEAWAY",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = lesson.keyTakeaway,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Source: ${lesson.source}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun TruthDetailSection(
    label: String,
    content: String,
    color: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface,
    fontWeight: FontWeight = FontWeight.Normal
) {
    Column(modifier = Modifier.padding(bottom = 12.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = content,
            style = MaterialTheme.typography.bodyLarge,
            color = color,
            fontWeight = fontWeight,
            lineHeight = 24.sp
        )
    }
}
