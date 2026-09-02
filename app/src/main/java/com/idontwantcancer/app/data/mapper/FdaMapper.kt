package com.idontwantcancer.app.data.mapper

import com.idontwantcancer.app.data.remote.model.fda.OpenFdaRecallDto
import com.idontwantcancer.app.domain.model.SourceMaterial
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private val fdaDateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")

/**
 * Maps openFDA Recall DTOs to normalized [SourceMaterial].
 */
fun OpenFdaRecallDto.toSourceMaterial(): SourceMaterial {
    val normalizedContent = """
        Product: $productDescription
        Status: $status
        Classification: $classification
        Reason: $reasonForRecall
    """.trimIndent()

    // Identity strategy: Use the official recall_number.
    // Fingerprint strategy: Hash the reason and status.
    val fingerprint = (reasonForRecall + status).hashCode().toString()

    return SourceMaterial(
        sourceId = "fda_food",
        contentId = recallNumber,
        title = productDescription.take(120),
        content = normalizedContent,
        publishedAt = try {
            LocalDate.parse(reportDate, fdaDateFormatter).atStartOfDay().toInstant(ZoneOffset.UTC)
        } catch (e: Exception) {
            Instant.now()
        },
        updatedAt = null,
        canonicalUrl = "https://open.fda.gov/apis/food/enforcement/",
        contentHash = fingerprint
    )
}
