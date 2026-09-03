package com.idontwantcancer.app.data.repository

import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.domain.repository.IntelligenceMemoryRepository
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Responsible for seeding the agency with verified baseline intelligence 
 * to ensure a professional first-run experience.
 */
@Singleton
class BaselineIntelligenceSeeder @Inject constructor(
    private val memory: IntelligenceMemoryRepository
) {
    suspend fun seedIfEmpty() {
        val existingSignals = memory.getAllSignals()
        if (existingSignals.isNotEmpty()) return

        val now = Instant.now()
        
        // 1. FDA Food Recall Baseline
        val fdaRecall = Signal(
            id = "seed-fda-1",
            title = "Class I Recall: Listeria monocytogenes in Frozen Vegetables",
            summary = "The FDA has issued a Class I recall for several brands of frozen organic vegetables due to potential contamination with Listeria monocytogenes. Class I is the most urgent recall category.",
            significance = "Listeria contamination in ready-to-eat vegetables represents an immediate health risk, particularly for immunocompromised individuals and pregnant women.",
            significanceLevel = SignificanceOutcome.CRITICAL,
            category = SignalCategory.FOOD,
            importance = SignalImportance.CRITICAL,
            confidence = SignalConfidence.VERY_HIGH,
            detectedAt = now.minusSeconds(3600 * 24),
            publishedAt = now.minusSeconds(3600 * 24),
            recommendedAction = "Immediately check your freezer for the affected brand names listed in the full briefing and return them for a refund.",
            source = SignalSource("U.S. FDA", "https://www.fda.gov/safety/recalls-market-withdrawals-safety-alerts"),
            isActionable = true,
            actionType = ActionType.AVOID
        )

        // 2. IARC Classification Baseline
        val iarcSignal = Signal(
            id = "seed-iarc-1",
            title = "IARC Monograph: Processed Meat Classified as Group 1 Carcinogen",
            summary = "The International Agency for Research on Cancer (IARC) has classified processed meat as carcinogenic to humans (Group 1), based on sufficient evidence that it causes colorectal cancer.",
            significance = "This classification places processed meat in the same category as tobacco and asbestos in terms of the strength of evidence for carcinogenicity.",
            significanceLevel = SignificanceOutcome.HIGH_SIGNIFICANCE,
            category = SignalCategory.RESEARCH,
            importance = SignalImportance.HIGH,
            confidence = SignalConfidence.VERY_HIGH,
            detectedAt = now.minusSeconds(3600 * 48),
            publishedAt = now.minusSeconds(3600 * 48),
            recommendedAction = "Consider limiting consumption of processed meats (like sausages, ham, and bacon) as part of a long-term cancer prevention strategy.",
            source = SignalSource("IARC / WHO", "https://www.iarc.who.int/news-events/iarc-monographs-evaluate-consumption-of-red-meat-and-processed-meat/"),
            isActionable = true,
            actionType = ActionType.MONITOR
        )

        // 3. Screening Guideline Baseline
        val screeningSignal = Signal(
            id = "seed-screening-1",
            title = "Colorectal Cancer Screening Starting Age Lowered to 45",
            summary = "Authoritative clinical guidelines now recommend that colorectal cancer screening for individuals at average risk should begin at age 45, rather than 50.",
            significance = "This change follows a significant increase in colorectal cancer rates among younger adults and is expected to save lives through earlier detection.",
            significanceLevel = SignificanceOutcome.SIGNIFICANT,
            category = SignalCategory.SCREENING,
            importance = SignalImportance.HIGH,
            confidence = SignalConfidence.HIGH,
            detectedAt = now.minusSeconds(3600 * 72),
            publishedAt = now.minusSeconds(3600 * 72),
            recommendedAction = "If you are 45 or older, discuss colorectal cancer screening options with your primary care provider.",
            source = SignalSource("USPSTF", "https://www.uspreventiveservicestaskforce.org/"),
            isActionable = true,
            actionType = ActionType.SCREEN
        )

        memory.saveSignal(fdaRecall)
        memory.saveSignal(iarcSignal)
        memory.saveSignal(screeningSignal)

        // Seed corresponding threads to ensure they appear in briefings
        seedThread(fdaRecall)
        seedThread(iarcSignal)
        seedThread(screeningSignal)
    }

    private suspend fun seedThread(signal: Signal) {
        val thread = IntelligenceThread(
            id = UUID.randomUUID().toString(),
            topicIdentifier = signal.title,
            firstDetectedAt = signal.detectedAt,
            lastUpdatedAt = signal.detectedAt,
            currentStatus = signal.summary,
            signalIds = listOf(signal.id)
        )
        memory.saveThread(thread)

        // Create a timeline entry so reconstruction works
        memory.saveTimelineEntry(
            TimelineEntry(
                id = UUID.randomUUID().toString(),
                threadId = thread.id,
                type = TimelineEntryType.INITIAL_OBSERVATION,
                description = signal.title,
                ingestionTime = signal.detectedAt,
                signalId = signal.id
            )
        )
    }
}
