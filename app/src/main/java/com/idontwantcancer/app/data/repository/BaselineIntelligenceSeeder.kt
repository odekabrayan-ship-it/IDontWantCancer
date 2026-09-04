package com.idontwantcancer.app.data.repository

import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.domain.repository.IntelligenceMemoryRepository
import com.idontwantcancer.app.domain.repository.PreventionRepository
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
    private val memory: IntelligenceMemoryRepository,
    private val preventionRepository: PreventionRepository
) {
    suspend fun seedIfEmpty() {
        val existingSignals = memory.getAllSignals()
        if (existingSignals.isNotEmpty()) return

        seedNutritionTruths()
        
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

        // --- NEW CONSUMER SAFETY REGISTRY SEEDS (Feature 2) ---

        // 7. Benzene in Aerosols
        val benzeneSignal = Signal(
            id = "seed-benzene-1",
            title = "Safety Alert: Benzene Contamination in Aerosol Products",
            summary = "Internal and independent testing has identified the presence of benzene, a known human carcinogen, in various brands of aerosol sunscreens and dry shampoos.",
            significance = "Benzene is not an intended ingredient but a contaminant from the propellant process. Long-term exposure to benzene is linked to leukemia and other blood disorders.",
            significanceLevel = SignificanceOutcome.HIGH_SIGNIFICANCE,
            category = SignalCategory.CONSUMER_PRODUCTS,
            importance = SignalImportance.HIGH,
            confidence = SignalConfidence.VERY_HIGH,
            detectedAt = now.minusSeconds(3600 * 168),
            publishedAt = now.minusSeconds(3600 * 168),
            recommendedAction = "Check your aerosol sunscreens and dry shampoos for manufacturer recall notices. Switch to lotion or pump-spray alternatives when possible.",
            source = SignalSource("Consumer Intelligence", ""),
            isActionable = true,
            actionType = ActionType.AVOID,
            scope = GeographicScope.GLOBAL,
            affectedIngredients = listOf("Benzene", "Aerosol", "Propellant"),
            safetyLevel = SafetyLevel.DANGER
        )

        // 8. Asbestos in Talc
        val talcSignal = Signal(
            id = "seed-talc-1",
            title = "Protective Watch: Asbestos Risks in Talc-Based Powders",
            summary = "Talc mines can be naturally contaminated with asbestos. Authoritative investigations have led to multi-billion dollar settlements and product reformulations for major baby powder brands.",
            significance = "Asbestos is a Group 1 carcinogen with no safe level of exposure. Inhalation or topical application of contaminated talc increases risk for mesothelioma and ovarian cancer.",
            significanceLevel = SignificanceOutcome.CRITICAL,
            category = SignalCategory.CONSUMER_PRODUCTS,
            importance = SignalImportance.CRITICAL,
            confidence = SignalConfidence.VERY_HIGH,
            detectedAt = now.minusSeconds(3600 * 200),
            publishedAt = now.minusSeconds(3600 * 200),
            recommendedAction = "The agency recommends switching to cornstarch-based body powders and verifying that cosmetic products are labeled as 'talc-free'.",
            source = SignalSource("Legal & Regulatory Intelligence", ""),
            isActionable = true,
            actionType = ActionType.AVOID,
            scope = GeographicScope.GLOBAL,
            affectedIngredients = listOf("Talc", "Asbestos", "Baby Powder", "Cosmetics"),
            safetyLevel = SafetyLevel.DANGER
        )

        // 9. PFAS (Forever Chemicals)
        val pfasSignal = Signal(
            id = "seed-pfas-1",
            title = "Environmental Intelligence: PFAS Exposure in Daily Goods",
            summary = "Per- and polyfluoroalkyl substances (PFAS) are used in non-stick cookware, water-repellent clothing, and food packaging. They persist in the human body and environment almost indefinitely.",
            significance = "New evidence increasingly links high PFAS exposure to kidney and testicular cancers, as well as immune system suppression.",
            significanceLevel = SignificanceOutcome.SIGNIFICANT,
            category = SignalCategory.ENVIRONMENT,
            importance = SignalImportance.MODERATE,
            confidence = SignalConfidence.HIGH,
            detectedAt = now.minusSeconds(3600 * 240),
            publishedAt = now.minusSeconds(3600 * 240),
            recommendedAction = "Avoid non-stick cookware with PFOA/PFOS. Opt for stainless steel or cast iron. Look for 'PFAS-free' certifications in clothing and dental floss.",
            source = SignalSource("Environmental Intelligence", ""),
            isActionable = true,
            actionType = ActionType.MONITOR,
            scope = GeographicScope.GLOBAL,
            affectedIngredients = listOf("PFAS", "PFOA", "PFOS", "Non-stick", "Forever Chemicals"),
            safetyLevel = SafetyLevel.CAUTION
        )

        memory.saveSignal(benzeneSignal)
        memory.saveSignal(talcSignal)
        memory.saveSignal(pfasSignal)
        
        seedThread(benzeneSignal)
        seedThread(talcSignal)
        seedThread(pfasSignal)

        // --- TRUTH CHECK REGISTRY SEEDS (Feature 4) ---

        // 10. Cell Phones & 5G
        val cellPhoneSignal = Signal(
            id = "seed-cellphone-1",
            title = "Truth Check: Do Cell Phones Cause Brain Cancer?",
            summary = "Decades of research and large-scale population studies have found no consistent evidence that the non-ionizing radiation used by cell phones increases the risk of brain tumors.",
            significance = "This address a common environmental health concern. Scientific consensus from WHO and major cancer institutes indicates that current exposure levels are safe.",
            significanceLevel = SignificanceOutcome.SIGNIFICANT,
            category = SignalCategory.ENVIRONMENT,
            importance = SignalImportance.LOW,
            confidence = SignalConfidence.VERY_HIGH,
            detectedAt = now.minusSeconds(3600 * 300),
            publishedAt = now.minusSeconds(3600 * 300),
            recommendedAction = "Follow standard manufacturer safety guidelines. Use hands-free options if you wish to further reduce exposure, though not required for cancer prevention.",
            source = SignalSource("Agency Truth Check", ""),
            verdict = EvidenceVerdict.NOT_SUPPORTED,
            investigatedClaim = "Cell phones and 5G networks cause brain tumors.",
            scope = GeographicScope.GLOBAL
        )

        // 11. Sugar "Feeds" Cancer
        val sugarMythSignal = Signal(
            id = "seed-sugar-1",
            title = "Truth Check: Does Sugar 'Feed' Cancer Specifically?",
            summary = "While all cells (including cancer cells) consume sugar (glucose) for energy, there is no evidence that eating sugar makes cancer grow faster or that avoiding sugar stops it.",
            significance = "The primary link between sugar and cancer is indirect: high sugar consumption can lead to obesity, which is a known risk factor for 13 types of cancer.",
            significanceLevel = SignificanceOutcome.SIGNIFICANT,
            category = SignalCategory.NUTRITION,
            importance = SignalImportance.MODERATE,
            confidence = SignalConfidence.HIGH,
            detectedAt = now.minusSeconds(3600 * 350),
            publishedAt = now.minusSeconds(3600 * 350),
            recommendedAction = "Focus on a balanced eating pattern. Limiting added sugars is recommended for weight management and general health, rather than starving individual cells.",
            source = SignalSource("Agency Truth Check", ""),
            verdict = EvidenceVerdict.MISLEADING,
            investigatedClaim = "Sugar feeds cancer cells and makes them grow faster than healthy cells.",
            scope = GeographicScope.GLOBAL
        )

        memory.saveSignal(cellPhoneSignal)
        memory.saveSignal(sugarMythSignal)
        
        seedThread(cellPhoneSignal)
        seedThread(sugarMythSignal)

        // --- ENVIRONMENTAL WATCH REGISTRY SEEDS (Feature 5) ---

        // 12. Air Pollution (PM2.5)
        val airPollutionSignal = Signal(
            id = "seed-env-1",
            title = "WHO Intelligence: Outdoor Air Pollution a Group 1 Carcinogen",
            summary = "The World Health Organization has classified outdoor air pollution and particulate matter (PM) as carcinogenic to humans. PM2.5 is specifically linked to lung cancer.",
            significance = "This is a major environmental health truth. Fine particulates can penetrate deep into the lungs and enter the bloodstream, causing systemic inflammation and DNA damage.",
            significanceLevel = SignificanceOutcome.HIGH_SIGNIFICANCE,
            category = SignalCategory.ENVIRONMENT,
            importance = SignalImportance.HIGH,
            confidence = SignalConfidence.VERY_HIGH,
            detectedAt = now.minusSeconds(3600 * 400),
            publishedAt = now.minusSeconds(3600 * 400),
            recommendedAction = "Monitor your local Air Quality Index (AQI). On high-pollution days, limit heavy outdoor exertion and use HEPA air filtration indoors where possible.",
            source = SignalSource("WHO / IARC", "https://www.iarc.who.int/news-events/iarc-outdoor-air-pollution-a-leading-environmental-cause-of-cancer-deaths/"),
            scope = GeographicScope.GLOBAL
        )

        // 13. Radon Gas
        val radonSignal = Signal(
            id = "seed-env-2",
            title = "Invisible Risk: Radon Gas in Homes",
            summary = "Radon is a naturally occurring radioactive gas that can accumulate in homes. It is the second leading cause of lung cancer globally, and the leading cause among non-smokers.",
            significance = "Unlike outdoor pollution, radon levels vary by individual building. It is colorless and odorless, making detection impossible without specialized testing.",
            significanceLevel = SignificanceOutcome.HIGH_SIGNIFICANCE,
            category = SignalCategory.ENVIRONMENT,
            importance = SignalImportance.HIGH,
            confidence = SignalConfidence.VERY_HIGH,
            detectedAt = now.minusSeconds(3600 * 450),
            publishedAt = now.minusSeconds(3600 * 450),
            recommendedAction = "The agency recommends testing your home for radon. If levels are high (above 4 pCi/L or 148 Bq/m³), professional mitigation is highly effective.",
            source = SignalSource("National Health Authorities", ""),
            scope = GeographicScope.GLOBAL, // Keeping global for general awareness, though levels are local
            isActionable = true,
            actionType = ActionType.MONITOR
        )

        // 14. UV Radiation
        val uvSignal = Signal(
            id = "seed-env-3",
            title = "Protective Intelligence: UV Radiation and Skin Cancer",
            summary = "Ultraviolet (UV) radiation from the sun and tanning beds is a proven carcinogen. It causes DNA damage in skin cells that can lead to melanoma and other skin cancers.",
            significance = "Skin cancer is one of the most preventable forms of cancer. UV exposure is cumulative, meaning protection at every age reduces long-term risk.",
            significanceLevel = SignificanceOutcome.SIGNIFICANT,
            category = SignalCategory.ENVIRONMENT,
            importance = SignalImportance.MODERATE,
            confidence = SignalConfidence.VERY_HIGH,
            detectedAt = now.minusSeconds(3600 * 500),
            publishedAt = now.minusSeconds(3600 * 500),
            recommendedAction = "Use broad-spectrum sunscreen (SPF 30+), wear protective clothing, and seek shade during peak sun hours. Avoid indoor tanning entirely.",
            source = SignalSource("Skin Cancer Foundations", ""),
            scope = GeographicScope.GLOBAL,
            isActionable = true,
            actionType = ActionType.AVOID
        )

        // 15. Night Shift Work
        val shiftWorkSignal = Signal(
            id = "seed-env-4",
            title = "Occupational Intelligence: Night Shift Work Risks",
            summary = "The IARC has classified night shift work as 'probably carcinogenic to humans' (Group 2A) due to its disruption of the circadian rhythm.",
            significance = "Circadian disruption affects hormone levels and immune function. Evidence is strongest for links to breast, prostate, and colorectal cancers.",
            significanceLevel = SignificanceOutcome.SIGNIFICANT,
            category = SignalCategory.OCCUPATIONAL,
            importance = SignalImportance.MODERATE,
            confidence = SignalConfidence.HIGH,
            detectedAt = now.minusSeconds(3600 * 600),
            publishedAt = now.minusSeconds(3600 * 600),
            recommendedAction = "If you work nights, prioritize 'sleep hygiene' and healthy eating patterns. Discuss screening timing with your doctor, as risk profiles may differ.",
            source = SignalSource("IARC", "https://www.iarc.who.int/news-events/iarc-monographs-evaluate-night-shift-work/"),
            scope = GeographicScope.GLOBAL,
            isActionable = true,
            actionType = ActionType.MONITOR
        )

        memory.saveSignal(airPollutionSignal)
        memory.saveSignal(radonSignal)
        memory.saveSignal(uvSignal)
        memory.saveSignal(shiftWorkSignal)
        
        seedThread(airPollutionSignal)
        seedThread(radonSignal)
        seedThread(uvSignal)
        seedThread(shiftWorkSignal)
    }

    private suspend fun seedNutritionTruths() {
        val nutritionTruths = listOf(
            NutritionIntelligence(
                id = "truth-nutrition-1",
                title = "Processed Meat and Colorectal Cancer",
                summary = "Processed meats like bacon, sausages, and ham are classified by IARC as Group 1 carcinogens.",
                evidenceLevel = EvidenceStrength.VERY_HIGH,
                reality = "There is convincing evidence that processed meat causes colorectal cancer. Each 50-gram portion consumed daily increases the risk by about 18%.",
                recommendation = "Avoid processed meat where possible. Choose fresh poultry, fish, or plant-based proteins (legumes, lentils) as healthy alternatives.",
                source = "IARC / World Health Organization",
                sourceUrl = "https://www.iarc.who.int/wp-content/uploads/2018/07/pr240_E.pdf"
            ),
            NutritionIntelligence(
                id = "truth-nutrition-2",
                title = "Alcohol and Multiple Cancer Types",
                summary = "Alcohol consumption is a known cause of at least seven types of cancer, including breast, liver, and esophageal cancer.",
                evidenceLevel = EvidenceStrength.VERY_HIGH,
                reality = "When it comes to cancer prevention, there is no safe level of alcohol consumption. Risk increases with the amount consumed.",
                recommendation = "For cancer prevention, it is best not to drink alcohol. If you do, limit consumption to national guidelines (e.g., no more than 2 drinks a day for men, 1 for women).",
                source = "IARC / WCRF",
                sourceUrl = "https://www.wcrf.org/diet-activity-and-cancer/risk-factors/alcoholic-drinks-and-cancer-risk/"
            ),
            NutritionIntelligence(
                id = "truth-nutrition-3",
                title = "Dietary Fiber and Colon Protection",
                summary = "Consuming foods high in dietary fiber, particularly whole grains, strongly reduces the risk of colorectal cancer.",
                evidenceLevel = EvidenceStrength.VERY_HIGH,
                reality = "Fiber increases stool bulk and dilutes potential carcinogens in the colon, while whole grains contain various bioactive compounds with anti-cancer properties.",
                recommendation = "Aim for at least 30g of fiber daily. Switch white bread and pasta for whole-grain versions and include beans and lentils in your meals.",
                source = "WCRF / AICR",
                sourceUrl = "https://www.wcrf.org/diet-activity-and-cancer/risk-factors/wholegrains-veg-fruit-beans-and-cancer-risk/"
            ),
            NutritionIntelligence(
                id = "truth-nutrition-4",
                title = "Red Meat: Probable Carcinogenicity",
                summary = "Red meat (beef, lamb, pork) is classified as 'probably carcinogenic to humans' (Group 2A).",
                evidenceLevel = EvidenceStrength.HIGH,
                reality = "The evidence for red meat is strong but not as absolute as processed meat. High consumption is linked to colorectal, pancreatic, and prostate cancers.",
                recommendation = "If you eat red meat, limit it to no more than about 3 portions (350–500g cooked weight) per week.",
                source = "WCRF / IARC",
                sourceUrl = "https://www.wcrf.org/diet-activity-and-cancer/risk-factors/meat-fish-dairy-and-cancer-risk/"
            ),
            NutritionIntelligence(
                id = "truth-nutrition-5",
                title = "Sugar-Sweetened Drinks and Weight-Mediated Risk",
                summary = "Sugary drinks are a cause of weight gain, overweight, and obesity, which in turn increase the risk of 13 types of cancer.",
                evidenceLevel = EvidenceStrength.HIGH,
                reality = "While sugar doesn't 'feed' cancer directly in a unique way, the metabolic changes caused by obesity (like inflammation and insulin levels) are major cancer drivers.",
                recommendation = "Avoid sugar-sweetened drinks. Choose water or unsweetened tea/coffee instead.",
                source = "AICR / WCRF",
                sourceUrl = "https://www.aicr.org/cancer-prevention/recommendations/limit-consumption-of-sugar-sweetened-drinks/"
            )
        )
        preventionRepository.saveNutritionIntelligence(nutritionTruths)
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
