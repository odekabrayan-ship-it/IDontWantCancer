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
 * Every signal follows the 4-Point Directive Protocol: Truth, Command, Execution, Shield.
 * Updated for Retail Sentinel (Action 2 Overhaul): Includes safe alternatives for products.
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
        seedEducationLessons()
        seedPreventionActions()
        
        val now = Instant.now()
        
        // 1. FDA Food Recall Baseline
        val fdaRecall = Signal(
            id = "seed-fda-1",
            title = "Class I Recall: Listeria in Frozen Vegetables",
            summary = "The FDA has issued a Class I recall for several brands of frozen organic vegetables due to potential contamination with Listeria monocytogenes.",
            theTruth = "Testing confirmed Listeria monocytogenes in specific batches of frozen organic peas and corn. Listeria is a resilient bacteria that can survive freezing and cause serious infection.",
            theCommand = "Check your freezer for the affected products immediately and do not consume them.",
            theExecution = listOf(
                "Locate any frozen organic vegetable bags in your freezer.",
                "Check the 'Best By' dates and 'Lot Codes' against the official FDA list (linked in source).",
                "If matched, dispose of the product in a sealed bag or return it to the place of purchase.",
                "Sanitize any freezer drawers or counters that touched the packaging."
            ),
            theShield = "Listeriosis can cause severe illness in pregnant women, newborns, and the elderly. Immediate avoidance prevents potential systemic infection.",
            significanceLevel = SignificanceOutcome.CRITICAL,
            category = SignalCategory.FOOD,
            importance = SignalImportance.CRITICAL,
            confidence = SignalConfidence.VERY_HIGH,
            detectedAt = now.minusSeconds(3600 * 24),
            publishedAt = now.minusSeconds(3600 * 24),
            source = SignalSource("U.S. FDA", "https://www.fda.gov/safety/recalls-market-withdrawals-safety-alerts"),
            isActionable = true,
            actionType = ActionType.AVOID,
            scope = GeographicScope.NATIONAL,
            targetCountryCode = "US",
            safetyLevel = SafetyLevel.DANGER
        )

        // 2. IARC Classification Baseline
        val iarcSignal = Signal(
            id = "seed-iarc-1",
            title = "IARC Monograph: Processed Meat a Group 1 Carcinogen",
            summary = "The IARC has classified processed meat as carcinogenic to humans (Group 1), linked primarily to colorectal cancer.",
            theTruth = "Strong evidence from over 800 studies confirms that regular consumption of processed meat causes colorectal cancer. Chemicals used in processing (like nitrates) form carcinogenic compounds in the body.",
            theCommand = "Minimize or eliminate processed meats from your daily eating pattern.",
            theExecution = listOf(
                "Identify processed meats in your diet: bacon, sausages, ham, deli meats, and hot dogs.",
                "Replace these with fresh proteins: chicken breast, fish, beans, or lentils.",
                "Reserve processed meats for very occasional use rather than daily consumption."
            ),
            theShield = "Reducing processed meat intake directly lowers the exposure of the colon lining to DNA-damaging N-nitroso compounds.",
            significanceLevel = SignificanceOutcome.HIGH_SIGNIFICANCE,
            category = SignalCategory.RESEARCH,
            importance = SignalImportance.HIGH,
            confidence = SignalConfidence.VERY_HIGH,
            detectedAt = now.minusSeconds(3600 * 48),
            publishedAt = now.minusSeconds(3600 * 48),
            source = SignalSource("IARC / WHO", "https://www.iarc.who.int/news-events/iarc-monographs-evaluate-consumption-of-red-meat-and-processed-meat/"),
            isActionable = true,
            actionType = ActionType.MONITOR,
            scope = GeographicScope.GLOBAL,
            safetyLevel = SafetyLevel.CAUTION
        )

        // 3. Screening Guideline Baseline
        val screeningSignal = Signal(
            id = "seed-screening-1",
            title = "Colorectal Cancer Screening Starting Age: 45",
            summary = "Authoritative clinical guidelines now recommend that colorectal cancer screening for average-risk individuals should begin at age 45.",
            theTruth = "Incidence of 'early-onset' colorectal cancer in adults under 50 has risen sharply. Screening at 45 catches precancerous polyps before they turn into cancer.",
            theCommand = "If you are 45 or older, schedule a consultation for colorectal cancer screening.",
            theExecution = listOf(
                "Confirm your age and risk factors (family history, previous polyps).",
                "Contact your primary care provider to discuss screening options: Colonoscopy or stool-based tests (FIT/Cologuard).",
                "Schedule the procedure and follow the preparation instructions precisely."
            ),
            theShield = "Early screening allows for the removal of polyps, preventing cancer from ever starting. It is one of the most effective prevention actions available.",
            significanceLevel = SignificanceOutcome.SIGNIFICANT,
            category = SignalCategory.SCREENING,
            importance = SignalImportance.HIGH,
            confidence = SignalConfidence.HIGH,
            detectedAt = now.minusSeconds(3600 * 72),
            publishedAt = now.minusSeconds(3600 * 72),
            source = SignalSource("USPSTF", "https://www.uspreventiveservicestaskforce.org/"),
            isActionable = true,
            actionType = ActionType.SCREEN,
            scope = GeographicScope.GLOBAL,
            safetyLevel = SafetyLevel.MONITOR
        )

        // 4. Nutrition Intelligence Baseline
        val nutritionSignal = Signal(
            id = "seed-nutrition-1",
            title = "Evidence-Based Guidance: Whole Grains and Prevention",
            summary = "Strong evidence indicates that consuming whole grains reduces the risk of colorectal cancer.",
            theTruth = "Whole grains are rich in dietary fiber and bioactive compounds. Fiber dilutes carcinogens and speeds their transit through the colon.",
            theCommand = "Shift your starch intake to at least 90% whole grain sources.",
            theExecution = listOf(
                "Check labels for '100% Whole Wheat' or 'Whole Grain'.",
                "Replace white rice with brown or wild rice.",
                "Incorporate oats, quinoa, or barley into at least one meal per day."
            ),
            theShield = "High fiber intake maintains gut health and reduces the time your digestive tract is exposed to potential dietary carcinogens.",
            significanceLevel = SignificanceOutcome.SIGNIFICANT,
            category = SignalCategory.NUTRITION,
            importance = SignalImportance.MODERATE,
            confidence = SignalConfidence.HIGH,
            detectedAt = now.minusSeconds(3600 * 96),
            publishedAt = now.minusSeconds(3600 * 96),
            source = SignalSource("WCRF / AICR", "https://www.wcrf.org/diet-activity-and-cancer/dietary-patterns/eat-wholegrains-vegetables-fruit-and-beans/"),
            isActionable = true,
            actionType = ActionType.MONITOR,
            scope = GeographicScope.GLOBAL,
            safetyLevel = SafetyLevel.VERIFIED_SAFE
        )

        // 5. Truth Check Baseline
        val truthCheckSignal = Signal(
            id = "seed-truth-1",
            title = "Claim Check: Aspartame and Cancer Risk",
            summary = "IARC classified aspartame as 'possibly carcinogenic' (2B), but safety limits remain unchanged.",
            theTruth = "The classification is based on 'limited' evidence. Authorities like JECFA maintain that the acceptable daily intake (40mg/kg) is safe.",
            theCommand = "Maintain current consumption levels if within safety limits, or opt for water as a superior alternative.",
            theExecution = listOf(
                "Estimate your intake: A typical diet soda contains ~200mg. A 70kg adult would need 14 cans daily to exceed the limit.",
                "If you consume large amounts, consider replacing some with sparkling water or tea.",
                "Do not switch back to sugar-sweetened drinks, as they carry higher obesity-related risks."
            ),
            theShield = "This intelligence prevents unnecessary alarm while encouraging a shift toward safer, non-additive beverages.",
            significanceLevel = SignificanceOutcome.SIGNIFICANT,
            category = SignalCategory.RESEARCH,
            importance = SignalImportance.MODERATE,
            confidence = SignalConfidence.HIGH,
            detectedAt = now.minusSeconds(3600 * 120),
            publishedAt = now.minusSeconds(3600 * 120),
            source = SignalSource("IARC / WHO", "https://www.who.int/news/item/14-07-2023-aspartame-hazard-and-risk-assessment-results-released"),
            verdict = EvidenceVerdict.PARTLY_SUPPORTED,
            investigatedClaim = "Aspartame in diet soda causes cancer and should be banned.",
            scope = GeographicScope.GLOBAL,
            safetyLevel = SafetyLevel.MONITOR
        )

        // 6. Benzene in Aerosols
        val benzeneSignal = Signal(
            id = "seed-benzene-1",
            title = "Safety Alert: Benzene in Aerosol Products",
            summary = "Benzene contamination has been identified in various aerosol sunscreens and dry shampoos.",
            theTruth = "Benzene is a human carcinogen linked to leukemia. It was found as a contaminant in the propellant used for spray products.",
            theCommand = "Stop using aerosolized sunscreens and dry shampoos until you verify they are benzene-free.",
            theExecution = listOf(
                "Check your bathroom and gym bag for aerosol spray cans.",
                "Compare the brand and lot number against the Valisure or FDA recall lists.",
                "Switch to lotion-based sunscreens or powder/non-aerosol dry shampoos."
            ),
            theShield = "Eliminating aerosol use prevents the inhalation of benzene-contaminated particles, removing a direct path to the bloodstream.",
            significanceLevel = SignificanceOutcome.HIGH_SIGNIFICANCE,
            category = SignalCategory.CONSUMER_PRODUCTS,
            importance = SignalImportance.HIGH,
            confidence = SignalConfidence.VERY_HIGH,
            detectedAt = now.minusSeconds(3600 * 168),
            publishedAt = now.minusSeconds(3600 * 168),
            source = SignalSource("Consumer Intelligence", ""),
            isActionable = true,
            actionType = ActionType.AVOID,
            scope = GeographicScope.GLOBAL,
            affectedIngredients = listOf("Benzene", "Aerosol", "Propellant"),
            safetyLevel = SafetyLevel.DANGER,
            safeAlternatives = listOf("Mineral lotions", "Mechanical pump sprays", "Non-aerosol dry shampoos")
        )

        // 7. Asbestos in Talc
        val talcSignal = Signal(
            id = "seed-talc-1",
            title = "Protective Watch: Asbestos in Talc-Based Powders",
            summary = "Talc can be naturally contaminated with asbestos, a Group 1 carcinogen.",
            theTruth = "Asbestos and talc often occur together in the earth. Contaminated talc used in body powders is linked to mesothelioma and ovarian cancer.",
            theCommand = "Discontinue use of talc-based body powders, especially for personal hygiene.",
            theExecution = listOf(
                "Check the ingredient list for 'Talc' or 'Talcum Powder'.",
                "Switch to cornstarch-based alternatives.",
                "Ensure any cosmetic powders are explicitly labeled as 'Talc-Free'."
            ),
            theShield = "Avoiding talc removes the risk of inhaling or absorbing microscopic asbestos fibers that cause permanent DNA damage.",
            significanceLevel = SignificanceOutcome.CRITICAL,
            category = SignalCategory.CONSUMER_PRODUCTS,
            importance = SignalImportance.CRITICAL,
            confidence = SignalConfidence.VERY_HIGH,
            detectedAt = now.minusSeconds(3600 * 200),
            publishedAt = now.minusSeconds(3600 * 200),
            source = SignalSource("Legal & Regulatory Intelligence", ""),
            isActionable = true,
            actionType = ActionType.AVOID,
            scope = GeographicScope.GLOBAL,
            affectedIngredients = listOf("Talc", "Asbestos", "Baby Powder"),
            safetyLevel = SafetyLevel.DANGER,
            safeAlternatives = listOf("Zea Mays (Corn) Starch", "Arrowroot Powder", "Kaolin Clay")
        )

        // 8. PFAS (Forever Chemicals)
        val pfasSignal = Signal(
            id = "seed-pfas-1",
            title = "Environmental Intelligence: PFAS Exposure",
            summary = "PFAS chemicals used in non-stick and water-repellent goods persist in the body.",
            theTruth = "PFAS exposure is linked to kidney and testicular cancers. These 'forever chemicals' are found in many household products and water supplies.",
            theCommand = "Reduce your household exposure to PFAS by phasing out specific products.",
            theExecution = listOf(
                "Replace old non-stick (Teflon) cookware with stainless steel or cast iron.",
                "Avoid grease-resistant fast-food packaging and microwave popcorn bags.",
                "Use water filtration (carbon or reverse osmosis) if local supplies are known to contain PFAS."
            ),
            theShield = "Lowering PFAS levels in your environment reduces the cumulative toxic load on your organs over time.",
            significanceLevel = SignificanceOutcome.SIGNIFICANT,
            category = SignalCategory.ENVIRONMENT,
            importance = SignalImportance.MODERATE,
            confidence = SignalConfidence.HIGH,
            detectedAt = now.minusSeconds(3600 * 240),
            publishedAt = now.minusSeconds(3600 * 240),
            source = SignalSource("Environmental Intelligence", ""),
            isActionable = true,
            actionType = ActionType.MONITOR,
            scope = GeographicScope.GLOBAL,
            affectedIngredients = listOf("PFAS", "PFOA", "PFOS", "Non-stick"),
            safetyLevel = SafetyLevel.CAUTION,
            safeAlternatives = listOf("Cast Iron", "Stainless Steel", "Ceramic Coating", "Glass")
        )

        // 9. Cell Phones & 5G
        val cellPhoneSignal = Signal(
            id = "seed-cellphone-1",
            title = "Truth Check: Cell Phones and Brain Cancer",
            summary = "No consistent evidence links non-ionizing radiation from phones to brain tumors.",
            theTruth = "Radiofrequency (RF) waves are non-ionizing and do not have enough energy to damage DNA directly. Large population studies show no correlation with tumor rates.",
            theCommand = "You do not need to avoid cell phone use for cancer prevention reasons.",
            theExecution = listOf(
                "Use your device as normal.",
                "If you are concerned about heat or minor RF exposure, use speakerphone or wired headsets.",
                "Ignore viral claims about 5G radiation, as they lack scientific basis."
            ),
            theShield = "This intelligence protects you from unnecessary anxiety and from investing in fraudulent 'anti-radiation' stickers or devices.",
            significanceLevel = SignificanceOutcome.SIGNIFICANT,
            category = SignalCategory.ENVIRONMENT,
            importance = SignalImportance.LOW,
            confidence = SignalConfidence.VERY_HIGH,
            detectedAt = now.minusSeconds(3600 * 300),
            publishedAt = now.minusSeconds(3600 * 300),
            source = SignalSource("Agency Truth Check", ""),
            verdict = EvidenceVerdict.NOT_SUPPORTED,
            investigatedClaim = "Cell phones and 5G networks cause brain tumors.",
            scope = GeographicScope.GLOBAL,
            safetyLevel = SafetyLevel.VERIFIED_SAFE
        )

        // 10. Air Pollution (PM2.5)
        val airPollutionSignal = Signal(
            id = "seed-env-1",
            title = "WHO: Outdoor Air Pollution a Group 1 Carcinogen",
            summary = "Outdoor air pollution is officially classified as carcinogenic to humans.",
            theTruth = "Fine particulate matter (PM2.5) enters the lungs and causes chronic inflammation, a primary driver of lung cancer.",
            theCommand = "Take protective measures during high-pollution events.",
            theExecution = listOf(
                "Install a reliable Air Quality Index (AQI) app or check local weather reports.",
                "When AQI is above 100, keep windows closed and avoid jogging near busy roads.",
                "Use an air purifier with a HEPA filter in bedrooms."
            ),
            theShield = "Reducing PM2.5 inhalation protects lung tissue from the cellular stress that leads to malignant mutations.",
            significanceLevel = SignificanceOutcome.HIGH_SIGNIFICANCE,
            category = SignalCategory.ENVIRONMENT,
            importance = SignalImportance.HIGH,
            confidence = SignalConfidence.VERY_HIGH,
            detectedAt = now.minusSeconds(3600 * 400),
            publishedAt = now.minusSeconds(3600 * 400),
            source = SignalSource("WHO / IARC", "https://www.iarc.who.int/news-events/iarc-outdoor-air-pollution-a-leading-environmental-cause-of-cancer-deaths/"),
            scope = GeographicScope.GLOBAL,
            safetyLevel = SafetyLevel.CAUTION
        )

        // 11. Radon Gas
        val radonSignal = Signal(
            id = "seed-env-2",
            title = "Invisible Risk: Radon Gas in Homes",
            summary = "Radon is the leading cause of lung cancer among non-smokers.",
            theTruth = "Radon gas comes from the natural breakdown of uranium in soil and enters homes through cracks in foundations. Long-term exposure damages lung DNA.",
            theCommand = "Test your home for radon every 2–5 years.",
            theExecution = listOf(
                "Purchase a low-cost radon test kit from a hardware store or health department.",
                "Follow the instructions for a 48-hour or long-term test in the lowest living level of your home.",
                "If levels exceed 4 pCi/L, contact a certified radon mitigation professional."
            ),
            theShield = "Detection and mitigation can reduce radon levels by up to 99%, virtually eliminating this specific lung cancer risk.",
            significanceLevel = SignificanceOutcome.HIGH_SIGNIFICANCE,
            category = SignalCategory.ENVIRONMENT,
            importance = SignalImportance.HIGH,
            confidence = SignalConfidence.VERY_HIGH,
            detectedAt = now.minusSeconds(3600 * 450),
            publishedAt = now.minusSeconds(3600 * 450),
            source = SignalSource("National Health Authorities", ""),
            scope = GeographicScope.GLOBAL,
            isActionable = true,
            actionType = ActionType.MONITOR,
            safetyLevel = SafetyLevel.CAUTION
        )

        // 12. UV Radiation
        val uvSignal = Signal(
            id = "seed-env-3",
            title = "Protective Intelligence: UV Radiation",
            summary = "UV radiation from the sun is a proven carcinogen linked to skin cancer.",
            theTruth = "UV rays penetrate skin and cause mutations. Melanoma, the most dangerous skin cancer, is directly tied to intense, intermittent sun exposure.",
            theCommand = "Adopt a daily 'Sun Defense' habit.",
            theExecution = listOf(
                "Apply SPF 30+ broad-spectrum sunscreen to exposed skin every morning.",
                "Wear wide-brimmed hats and UV-rated sunglasses when outdoors.",
                "Seek shade between 10 AM and 4 PM when UV intensity is highest."
            ),
            theShield = "Physical and chemical barriers block UV energy before it can reach and damage your cellular DNA.",
            significanceLevel = SignificanceOutcome.SIGNIFICANT,
            category = SignalCategory.ENVIRONMENT,
            importance = SignalImportance.MODERATE,
            confidence = SignalConfidence.VERY_HIGH,
            detectedAt = now.minusSeconds(3600 * 500),
            publishedAt = now.minusSeconds(3600 * 500),
            source = SignalSource("Skin Cancer Foundations", ""),
            scope = GeographicScope.GLOBAL,
            isActionable = true,
            actionType = ActionType.AVOID,
            safetyLevel = SafetyLevel.CAUTION
        )

        // 13. Night Shift Work
        val shiftWorkSignal = Signal(
            id = "seed-env-4",
            title = "Occupational Intelligence: Night Shift Work",
            summary = "Night shift work is classified as 'probably carcinogenic' due to circadian disruption.",
            theTruth = "Artificial light at night suppresses melatonin production and disrupts the biological clock, which regulates cell repair and immune response.",
            theCommand = "If working nights, implement strict circadian recovery protocols.",
            theExecution = listOf(
                "Use blackout curtains and a cool, quiet room for daytime sleep.",
                "Wear blue-light blocking glasses during the second half of your shift.",
                "Maintain a consistent sleep-wake schedule, even on days off, to minimize 'social jetlag'."
            ),
            theShield = "Optimizing sleep hygiene helps preserve the body's natural anti-tumor surveillance and repair mechanisms.",
            significanceLevel = SignificanceOutcome.SIGNIFICANT,
            category = SignalCategory.OCCUPATIONAL,
            importance = SignalImportance.MODERATE,
            confidence = SignalConfidence.HIGH,
            detectedAt = now.minusSeconds(3600 * 600),
            publishedAt = now.minusSeconds(3600 * 600),
            source = SignalSource("IARC", "https://www.iarc.who.int/news-events/iarc-monographs-evaluate-night-shift-work/"),
            scope = GeographicScope.GLOBAL,
            isActionable = true,
            actionType = ActionType.MONITOR,
            safetyLevel = SafetyLevel.CAUTION
        )

        val signals = listOf(
            fdaRecall, iarcSignal, screeningSignal, nutritionSignal, 
            truthCheckSignal, benzeneSignal, talcSignal, pfasSignal, 
            cellPhoneSignal, airPollutionSignal, radonSignal, uvSignal, shiftWorkSignal
        )

        signals.forEach { signal ->
            memory.saveSignal(signal)
            seedThread(signal)
        }
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

    private suspend fun seedEducationLessons() {
        val lessons = listOf(
            EducationLesson(
                id = "edu-1",
                title = "What is a Carcinogen?",
                summary = "Learn how authoritative bodies classify cancer-causing substances.",
                content = "A carcinogen is any agent—substance, radiation, or organism—that can cause cancer. The IARC (WHO) classifies these into groups: Group 1 (Known), Group 2A (Probable), and Group 2B (Possible).",
                keyTakeaway = "Group 1 means there is sufficient evidence of harm. Group 2A/2B means evidence is still developing.",
                source = "IARC / WHO"
            ),
            EducationLesson(
                id = "edu-2",
                title = "Risk vs. Hazard",
                summary = "Understanding why the amount of exposure is critical.",
                content = "A 'hazard' is something that has the potential to cause harm. 'Risk' is the likelihood of that harm happening. Sun radiation is a hazard, but your risk depends on how long you stay in the sun without protection.",
                keyTakeaway = "Being near a hazard doesn't always mean you are at high risk. Exposure level matters.",
                source = "Agency Literacy"
            ),
            EducationLesson(
                id = "edu-3",
                title = "The 40% Prevention Rule",
                summary = "Why your everyday choices are a powerful shield.",
                content = "Research indicates that approximately 40% of all cancer cases are preventable through lifestyle choices, including avoiding tobacco, maintaining a healthy weight, and limiting UV exposure.",
                keyTakeaway = "Cancer is not entirely up to chance; many significant risks are within your control.",
                source = "Cancer Research UK / AICR"
            ),
            EducationLesson(
                id = "edu-4",
                title = "Why Science Changes",
                summary = "Understanding the evolution of health recommendations.",
                content = "As technology improves and more data is collected over decades, scientific consensus evolves. This is why a substance once thought 'safe' may be re-evaluated as evidence of long-term harm emerges.",
                keyTakeaway = "Changing advice is a sign of a working intelligence system, not a failure of science.",
                source = "Agency Intelligence"
            ),
            EducationLesson(
                id = "edu-5",
                title = "The Prevention Mindset",
                summary = "Moving from fear to consistent, calm protection.",
                content = "Prevention is not about one-time miracle foods or living in fear. It is about identifying known high-priority risks and consistently reducing your exposure to them over years and decades.",
                keyTakeaway = "Think in patterns, not in poisons. Consistency is your strongest defense.",
                source = "Agency Philosophy"
            )
        )
        preventionRepository.saveEducationLessons(lessons)
    }

    private suspend fun seedPreventionActions() {
        val actions = listOf(
            PreventionAction(
                id = "action-1",
                title = "Avoid Tobacco",
                description = "The most significant avoidable risk factor for cancer.",
                iconName = "smoke_free"
            ),
            PreventionAction(
                id = "action-2",
                title = "Protect from UV",
                description = "Consistent use of sunscreen and shade during peak hours.",
                iconName = "sunny"
            ),
            PreventionAction(
                id = "action-3",
                title = "Limit Alcohol",
                description = "Reducing consumption directly impacts multiple cancer risks.",
                iconName = "no_drinks"
            ),
            PreventionAction(
                id = "action-4",
                title = "Move More",
                description = "Daily physical activity reduces risk for 13 types of cancer.",
                iconName = "directions_run"
            ),
            PreventionAction(
                id = "action-5",
                title = "Whole Grains First",
                description = "Choosing whole grains over refined ones for colorectal health.",
                iconName = "grass"
            ),
            PreventionAction(
                id = "action-6",
                title = "Limit Red Meat",
                description = "Keep consumption below 500g per week to reduce risk.",
                iconName = "restaurant"
            )
        )
        preventionRepository.savePreventionActions(actions)
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
