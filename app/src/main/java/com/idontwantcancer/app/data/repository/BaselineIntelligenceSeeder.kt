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
            actionType = ActionType.AVOID,
            scope = GeographicScope.NATIONAL,
            targetCountryCode = "US"
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
            actionType = ActionType.MONITOR,
            scope = GeographicScope.GLOBAL
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
            actionType = ActionType.SCREEN,
            scope = GeographicScope.GLOBAL
        )

        // 4. Nutrition Intelligence Baseline (Step 222)
        val nutritionSignal = Signal(
            id = "seed-nutrition-1",
            title = "Evidence-Based Guidance: Whole Grains and Cancer Prevention",
            summary = "Strong evidence indicates that consuming whole grains reduces the risk of colorectal cancer. This is one of the most consistent findings in nutritional oncology.",
            significance = "Unlike many dietary claims, the link between whole grain fiber and reduced cancer risk is supported by a large body of corroborating research from multiple international agencies.",
            significanceLevel = SignificanceOutcome.SIGNIFICANT,
            category = SignalCategory.NUTRITION,
            importance = SignalImportance.MODERATE,
            confidence = SignalConfidence.HIGH,
            detectedAt = now.minusSeconds(3600 * 96),
            publishedAt = now.minusSeconds(3600 * 96),
            recommendedAction = "Integrate whole grains (oats, brown rice, whole wheat) into your daily eating pattern as a sustainable prevention action.",
            source = SignalSource("WCRF / AICR", "https://www.wcrf.org/diet-activity-and-cancer/dietary-patterns/eat-wholegrains-vegetables-fruit-and-beans/"),
            isActionable = true,
            actionType = ActionType.MONITOR,
            scope = GeographicScope.GLOBAL
        )

        // 5. Truth Check Baseline (Step 222)
        val truthCheckSignal = Signal(
            id = "seed-truth-1",
            title = "Claim Check: Aspartame and Cancer Risk",
            summary = "Following a review of available evidence, the IARC classified aspartame as 'possibly carcinogenic to humans' (Group 2B), while the JECFA reaffirmed the acceptable daily intake level.",
            significance = "This update provides clarity on a widely circulated health claim. The 'possibly carcinogenic' label means evidence is limited, and current consumption levels remain within safety limits according to food safety authorities.",
            significanceLevel = SignificanceOutcome.SIGNIFICANT,
            category = SignalCategory.RESEARCH,
            importance = SignalImportance.MODERATE,
            confidence = SignalConfidence.HIGH,
            detectedAt = now.minusSeconds(3600 * 120),
            publishedAt = now.minusSeconds(3600 * 120),
            recommendedAction = "Maintain consumption within the established acceptable daily intake (ADI) of 40 mg/kg of body weight.",
            source = SignalSource("IARC / WHO", "https://www.who.int/news/item/14-07-2023-aspartame-hazard-and-risk-assessment-results-released"),
            verdict = EvidenceVerdict.PARTLY_SUPPORTED,
            scope = GeographicScope.GLOBAL
        )

        // 6. Education Baseline (Step 222)
        val educationSignal = Signal(
            id = "seed-edu-1",
            title = "Intelligence Foundation: What is a Carcinogen?",
            summary = "A carcinogen is any substance or agent that can cause cancer. These are classified by authoritative bodies like the IARC based on the strength of evidence.",
            significance = "Understanding what a carcinogen is—and how they are classified—helps you navigate health claims and understand why certain products are recalled or regulated.",
            significanceLevel = SignificanceOutcome.SIGNIFICANT,
            category = SignalCategory.RESEARCH,
            importance = SignalImportance.LOW,
            confidence = SignalConfidence.VERY_HIGH,
            detectedAt = now.minusSeconds(3600 * 144),
            publishedAt = now.minusSeconds(3600 * 144),
            source = SignalSource("Agency Education", ""),
            isActionable = false,
            scope = GeographicScope.GLOBAL
        )

        memory.saveSignal(fdaRecall)
        memory.saveSignal(iarcSignal)
        memory.saveSignal(screeningSignal)
        memory.saveSignal(nutritionSignal)
        memory.saveSignal(truthCheckSignal)
        memory.saveSignal(educationSignal)

        // Seed corresponding threads to ensure they appear in briefings
        seedThread(fdaRecall)
        seedThread(iarcSignal)
        seedThread(screeningSignal)
        seedThread(nutritionSignal)
        seedThread(truthCheckSignal)
        seedThread(educationSignal)
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
