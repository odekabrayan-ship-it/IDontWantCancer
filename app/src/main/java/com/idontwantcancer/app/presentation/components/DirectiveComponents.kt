package com.idontwantcancer.app.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GppMaybe
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.idontwantcancer.app.R
import com.idontwantcancer.app.core.ui.theme.LocalSpacing
import com.idontwantcancer.app.domain.model.Signal

@Composable
fun PrimaryDirectiveCard(
    signal: Signal,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = spacing.screenPadding, vertical = spacing.small),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.errorContainer,
            contentColor = colorScheme.onErrorContainer
        ),
        border = BorderStroke(2.dp, colorScheme.error.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(spacing.cardPadding)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.GppMaybe,
                    contentDescription = null,
                    tint = colorScheme.error,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.directive_action_required),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Black,
                    color = colorScheme.error,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(spacing.medium))

            Text(
                text = signal.theCommand ?: signal.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                lineHeight = 34.sp
            )

            if (signal.theCommand != null) {
                Text(
                    text = "Intelligence: ${signal.title}",
                    style = MaterialTheme.typography.labelSmall,
                    color = colorScheme.error.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(spacing.medium))

            Text(
                text = signal.theTruth ?: signal.summary,
                style = MaterialTheme.typography.bodyLarge,
                color = colorScheme.onSurface,
                lineHeight = 24.sp
            )

            if (signal.theShield != null) {
                Spacer(modifier = Modifier.height(spacing.medium))
                Surface(
                    color = colorScheme.onSurface.copy(alpha = 0.05f),
                    shape = MaterialTheme.shapes.small
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = signal.theShield,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
