package com.idontwantcancer.app.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.idontwantcancer.app.presentation.model.CommandConsumptionFinalityPresentationContract
import com.idontwantcancer.app.presentation.model.IntelligenceReentryReconciliationPresentationContract

/**
 * A standard warning banner for displaying re-entry reconciliation discrepancies.
 */
@Composable
fun ReconciliationWarning(
    reconciliation: IntelligenceReentryReconciliationPresentationContract.InconsistentMismatch,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.errorContainer,
        shape = MaterialTheme.shapes.small,
        modifier = modifier
            .fillMaxWidth()
            .semantics { 
                contentDescription = "System consistency warning: ${reconciliation.details ?: "Inconsistency detected"}"
            }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Processing discrepancy detected",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    fontWeight = FontWeight.Bold
                )
                reconciliation.details?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

/**
 * A compact indicator for displaying re-entry reconciliation discrepancies.
 */
@Composable
fun CompactReconciliationIndicator(
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clearAndSetSemantics {
            contentDescription = "Sync discrepancy detected for this item"
        }
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "Sync discrepancy",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.error
        )
    }
}

/**
 * Renders the finality status of the operational reporting cycle.
 * This is a pure read-only projection of Step 135.
 */
@Composable
fun OperationalFinalityIndicator(
    contract: CommandConsumptionFinalityPresentationContract,
    modifier: Modifier = Modifier
) {
    if (contract is CommandConsumptionFinalityPresentationContract.StatusUnavailable) return

    val text = when (contract) {
        is CommandConsumptionFinalityPresentationContract.Final -> "Operational finality reached"
        is CommandConsumptionFinalityPresentationContract.NonTerminal -> "Syncing operational state..."
        else -> ""
    }

    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontStyle = FontStyle.Italic,
        modifier = modifier
            .padding(16.dp)
            .semantics { 
                contentDescription = "System operational status: $text"
            }
    )
}

@Preview(showBackground = true)
@Composable
fun OperationalFinalityIndicatorPreview_Final() {
    OperationalFinalityIndicator(
        contract = CommandConsumptionFinalityPresentationContract.Final(
            operationId = "op1",
            detail = "Done"
        )
    )
}

@Preview(showBackground = true)
@Composable
fun OperationalFinalityIndicatorPreview_NonTerminal() {
    OperationalFinalityIndicator(
        contract = CommandConsumptionFinalityPresentationContract.NonTerminal(
            operationId = "op2",
            detail = "Syncing"
        )
    )
}
