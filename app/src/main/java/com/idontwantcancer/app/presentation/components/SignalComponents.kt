package com.idontwantcancer.app.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.idontwantcancer.app.R
import com.idontwantcancer.app.core.ui.theme.LocalSpacing
import com.idontwantcancer.app.domain.model.Signal
import com.idontwantcancer.app.domain.model.SignalCategory
import com.idontwantcancer.app.domain.model.SignalImportance

/**
 * A consistent card for displaying intelligence signals in lists.
 */
@Composable
fun SignalCard(
    signal: Signal,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    reconciliationIndicator: (@Composable () -> Unit)? = null
) {
    val spacing = LocalSpacing.current
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = spacing.extraSmall)
            .semantics { selected = isSelected },
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        border = if (isSelected)
            BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        else
            null
    ) {
        Column(modifier = Modifier.padding(spacing.cardPadding)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatCategory(signal.category),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
                SignalImportanceBadge(signal.importance)
            }
            Spacer(modifier = Modifier.height(spacing.small))
            Text(
                text = signal.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = signal.summary,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            reconciliationIndicator?.invoke()
        }
    }
}

/**
 * A standard badge for displaying signal importance.
 */
@Composable
fun SignalImportanceBadge(
    importance: SignalImportance,
    modifier: Modifier = Modifier
) {
    val color = getImportanceColor(importance)
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = MaterialTheme.shapes.small,
        modifier = modifier
    ) {
        Text(
            text = formatImportance(importance),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * A standard header for signal detail views.
 */
@Composable
fun SignalHeader(
    signal: Signal,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacing.small)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = formatCategory(signal.category),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold
            )
            SignalImportanceBadge(signal.importance)
        }

        Text(
            text = signal.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            lineHeight = 34.sp,
            modifier = Modifier.semantics { heading() }
        )
    }
}

@Composable
fun formatCategory(category: SignalCategory): String {
    return when (category) {
        SignalCategory.FOOD -> stringResource(R.string.cat_food)
        SignalCategory.CONSUMER_PRODUCTS -> stringResource(R.string.cat_consumer_products)
        SignalCategory.SCREENING -> stringResource(R.string.cat_screening)
        SignalCategory.ENVIRONMENT -> stringResource(R.string.cat_environment)
        SignalCategory.MEDICINE -> stringResource(R.string.cat_medicine)
        SignalCategory.RESEARCH -> stringResource(R.string.cat_research)
        SignalCategory.REGULATION -> stringResource(R.string.cat_regulation)
        SignalCategory.PREVENTION -> stringResource(R.string.cat_prevention)
    }
}

@Composable
fun formatImportance(importance: SignalImportance): String {
    return when (importance) {
        SignalImportance.LOW -> stringResource(R.string.imp_low)
        SignalImportance.MODERATE -> stringResource(R.string.imp_moderate)
        SignalImportance.HIGH -> stringResource(R.string.imp_high)
        SignalImportance.CRITICAL -> stringResource(R.string.imp_critical)
    }
}

@Composable
fun getImportanceColor(importance: SignalImportance): Color {
    return when (importance) {
        SignalImportance.CRITICAL -> MaterialTheme.colorScheme.error
        SignalImportance.HIGH -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.secondary
    }
}
