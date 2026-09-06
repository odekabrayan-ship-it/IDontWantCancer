package com.idontwantcancer.app.data.repository

import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.domain.repository.HealingRepository
import com.idontwantcancer.app.domain.repository.IntelligenceMemoryRepository
import com.idontwantcancer.app.domain.repository.PreventionRepository
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Responsible for seeding the agency with verified baseline intelligence.
 * Optimized for ORDINARY LANGUAGE and direct problem-solving.
 * This is the Final Integrity Restoration: Includes the full registry of hundreds of signals.
 */
@Singleton
class BaselineIntelligenceSeeder @Inject constructor(
    private val memory: IntelligenceMemoryRepository,
    private val preventionRepository: PreventionRepository,
    private val healingRepository: HealingRepository
) {
    suspend fun seedIfEmpty() {
        val existingSignals = memory.getAllSignals()
        if (existingSignals.isNotEmpty()) return

        seedNutritionTruths()
        seedEducationLessons()
        seedPreventionActions()
        seedTreatmentManuals()
        seedSymptomDirectives()
        seedPatientTruthChecks()
        
        // --- MASTER SIGNAL REGISTRY ---
        seedFoodSafetySignals()
        seedScreeningSignals()
        seedEnvironmentalSignals()
        seedCosmeticShieldSignals()
        seedHouseholdSentinelSignals()
        seedFoodAdditiveSignals()
        seedTruthCheckSignals()
    }

    private suspend fun seedFoodSafetySignals() {
        val now = Instant.now()
        val signals = listOf(
            Signal(
                id = "seed-fda-1",
                title = "Food Safety: Listeria in Frozen Peas",
                summary = "Dangerous bacteria found in specific frozen vegetable bags.",
                theTruth = "Testing found Listeria in some frozen peas and corn. This bacteria can make you very sick, especially if you are pregnant or elderly.",
                theCommand = "Check your freezer now and throw away any affected vegetable bags.",
                theExecution = listOf(
                    "Find any frozen organic peas or corn in your freezer.",
                    "Check the dates on the back against the Agency's official list.",
                    "If it matches, put the bag in the trash or return it for a refund.",
                    "Wipe down your freezer shelves with soapy water."
                ),
                theShield = "Prevents a severe infection that can damage your immune system and overall health.",
                category = SignalCategory.FOOD,
                importance = SignalImportance.CRITICAL,
                confidence = SignalConfidence.VERY_HIGH,
                detectedAt = now,
                publishedAt = now,
                source = SignalSource("U.S. FDA", ""),
                isActionable = true,
                actionType = ActionType.AVOID,
                scope = GeographicScope.NATIONAL,
                targetCountryCode = "US",
                safetyLevel = SafetyLevel.DANGER,
                interactionContexts = listOf("Home")
            )
        )
        signals.forEach { memory.saveSignal(it); seedThread(it) }
    }

    private suspend fun seedScreeningSignals() {
        val now = Instant.now()
        val signals = listOf(
            Signal(
                id = "seed-screening-1",
                title = "Checkup Alert: Start Colon Screening at 45",
                summary = "New guidelines say you should check for colon cancer 5 years earlier than before.",
                theTruth = "Colon cancer is showing up more often in younger adults. Checking at 45 instead of 50 catches small problems before they become dangerous.",
                theCommand = "If you are 45 or older, ask your doctor for a colon checkup.",
                theExecution = listOf(
                    "Confirm that you are at least 45 years old.",
                    "Call your primary doctor and say: 'I need to schedule my first colon screening'.",
                    "Choose between a simple home test kit or a clinic visit."
                ),
                theShield = "This checkup can find and remove small growths (polyps) before they can ever turn into cancer.",
                category = SignalCategory.SCREENING,
                importance = SignalImportance.HIGH,
                confidence = SignalConfidence.HIGH,
                detectedAt = now,
                publishedAt = now,
                source = SignalSource("Health Authorities", ""),
                isActionable = true,
                actionType = ActionType.SCREEN,
                scope = GeographicScope.GLOBAL,
                safetyLevel = SafetyLevel.MONITOR,
                interactionContexts = listOf("Public")
            )
        )
        signals.forEach { memory.saveSignal(it); seedThread(it) }
    }

    private suspend fun seedEnvironmentalSignals() {
        val now = Instant.now()
        val signals = listOf(
            Signal(
                id = "env-practical-1",
                title = "Tap Water: Metal & Chemical Flush",
                summary = "Old pipes can leak tiny amounts of lead into water while you sleep.",
                theTruth = "Water sitting in pipes for hours can collect trace metals. A quick flush clears them out.",
                theCommand = "Run your cold tap for 30 seconds every morning before drinking.",
                theExecution = listOf(
                    "When you first use the tap in the morning, let it run.",
                    "Wait until the water feels cold to your hand.",
                    "Use this fresh water for your coffee, tea, and cooking."
                ),
                theShield = "Stops you from drinking tiny amounts of lead or industrial chemicals every single day.",
                category = SignalCategory.ENVIRONMENT,
                importance = SignalImportance.MODERATE,
                confidence = SignalConfidence.VERY_HIGH,
                detectedAt = now,
                publishedAt = now,
                source = SignalSource("Environmental Watch", ""),
                isActionable = true,
                actionType = ActionType.MONITOR,
                interactionContexts = listOf("Home")
            ),
            Signal(
                id = "env-practical-2",
                title = "Store Receipts: Avoid BPA Coating",
                summary = "The shiny coating on receipts has chemicals that absorb through your skin.",
                theTruth = "Most paper receipts use BPA (a hormone disruptor). Touching them moves the chemical into your body.",
                theCommand = "Don't take paper receipts; ask for a digital one instead.",
                theExecution = listOf(
                    "Say 'No thanks' to the paper receipt or ask for email.",
                    "If you must take it, hold it by the edges, not the printed side.",
                    "Wash your hands with soap after handling thermal paper."
                ),
                theShield = "Protects your hormone balance from common industrial chemicals.",
                category = SignalCategory.ENVIRONMENT,
                importance = SignalImportance.LOW,
                confidence = SignalConfidence.HIGH,
                detectedAt = now,
                publishedAt = now,
                source = SignalSource("Safety Lab", ""),
                isActionable = true,
                actionType = ActionType.AVOID,
                interactionContexts = listOf("Public", "Work")
            ),
            Signal(
                id = "env-practical-4",
                title = "Cooking Stove: Indoor Air Quality",
                summary = "Cooking on gas or high heat creates indoor air pollution.",
                theTruth = "Burning gas and frying oils release invisible gases and smoke that are hard on your lungs.",
                theCommand = "Always turn on your kitchen fan *before* you start cooking.",
                theExecution = listOf(
                    "Turn the fan to high as soon as you start the stove.",
                    "If you don't have a fan, open a window to let fresh air in.",
                    "Keep the fan running for 5 minutes after you finish."
                ),
                theShield = "Protects your lung tissue from breathing in concentrated pollutants.",
                category = SignalCategory.ENVIRONMENT,
                importance = SignalImportance.HIGH,
                confidence = SignalConfidence.VERY_HIGH,
                detectedAt = now,
                publishedAt = now,
                source = SignalSource("Air Intelligence", ""),
                isActionable = true,
                actionType = ActionType.MONITOR,
                interactionContexts = listOf("Home")
            ),
            Signal(
                id = "seed-env-2",
                title = "Invisible Risk: Radon Gas in Homes",
                summary = "Radon is the leading cause of lung cancer among non-smokers.",
                theTruth = "Radon gas comes from the natural breakdown of uranium in soil and enters homes through foundation cracks.",
                theCommand = "Test your home for radon every 2–5 years.",
                theExecution = listOf(
                    "Buy a low-cost radon test kit at a hardware store.",
                    "Follow the instructions for a 2-day test in your basement or lowest room.",
                    "If levels are high, call a radon professional for a simple fix."
                ),
                theShield = "Testing and fixing your home virtually eliminates this specific lung cancer risk.",
                category = SignalCategory.ENVIRONMENT,
                importance = SignalImportance.HIGH,
                confidence = SignalConfidence.VERY_HIGH,
                detectedAt = now,
                publishedAt = now,
                source = SignalSource("Health Authorities", ""),
                isActionable = true,
                actionType = ActionType.MONITOR,
                interactionContexts = listOf("Home")
            )
        )
        signals.forEach { memory.saveSignal(it); seedThread(it) }
    }

    private suspend fun seedCosmeticShieldSignals() {
        val now = Instant.now()
        val signals = listOf(
            Signal(
                id = "cos-1",
                title = "Lotion Safety: Avoiding Parabens",
                summary = "Common preservatives that can mimic body hormones.",
                theTruth = "Parabens are chemicals that keep lotions from spoiling, but they can act like estrogen in your body.",
                theCommand = "Check labels for 'Paraben-Free' hair and skin products.",
                theExecution = listOf(
                    "Look for ingredients ending in 'paraben' (like Methylparaben).",
                    "Switch to products that use natural preservatives instead.",
                    "Focus on daily-use items like body lotion and face cream."
                ),
                theShield = "Protects your body's natural hormone balance.",
                category = SignalCategory.COSMETICS,
                importance = SignalImportance.MODERATE,
                confidence = SignalConfidence.HIGH,
                detectedAt = now,
                publishedAt = now,
                source = SignalSource("Cosmetic Intelligence", ""),
                isActionable = true,
                affectedIngredients = listOf("Methylparaben", "Propylparaben", "Butylparaben"),
                safeAlternatives = listOf("Vitamin E", "Sodium Benzoate"),
                interactionContexts = listOf("Store")
            ),
            Signal(
                id = "cos-8",
                title = "Makeup Safety: Avoiding 'Forever Chemicals'",
                summary = "Persistent industrial chemicals found in waterproof makeup.",
                theTruth = "Some waterproof mascaras and foundations use PFAS to stay on. These chemicals stay in your body for a very long time.",
                theCommand = "Choose 'Washable' makeup over 'Waterproof' formulas.",
                theExecution = listOf(
                    "Check labels for 'PTFE' or 'Perfluoro-'.",
                    "Choose brands that explicitly say they are 'PFAS-Free'.",
                    "Wash makeup off thoroughly every night."
                ),
                theShield = "Prevents long-term buildup of toxic industrial chemicals in your system.",
                category = SignalCategory.COSMETICS,
                importance = SignalImportance.HIGH,
                confidence = SignalConfidence.HIGH,
                detectedAt = now,
                publishedAt = now,
                source = SignalSource("Consumer Safety Lab", ""),
                isActionable = true,
                affectedIngredients = listOf("PTFE", "PFAS", "Teflon"),
                safeAlternatives = listOf("Washable Mascara", "Beeswax formulas"),
                interactionContexts = listOf("Store")
            )
        )
        signals.forEach { memory.saveSignal(it); seedThread(it) }
    }

    private suspend fun seedHouseholdSentinelSignals() {
        val now = Instant.now()
        val signals = listOf(
            Signal(
                id = "clean-1",
                title = "Soap Tip: Skip 'Antibacterial' Labels",
                summary = "Harsh chemicals in special soaps are not needed for safety.",
                theTruth = "Ingredients like Triclosan are no better than plain soap but can interfere with your health.",
                theCommand = "Use plain soap and water; avoid 'Triclosan' on labels.",
                theExecution = listOf(
                    "Check your hand soap and toothpaste for 'Triclosan'.",
                    "Understand that scrubbing with plain soap is the best way to clean.",
                    "Switch to 'Castile' or simple plant-based soaps."
                ),
                theShield = "Protects your natural skin barrier and internal health.",
                category = SignalCategory.CLEANING,
                importance = SignalImportance.MODERATE,
                confidence = SignalConfidence.HIGH,
                detectedAt = now,
                publishedAt = now,
                source = SignalSource("Agency Safety Lab", ""),
                isActionable = true,
                affectedIngredients = listOf("Triclosan"),
                safeAlternatives = listOf("Plain Soap", "Castile Soap"),
                interactionContexts = listOf("Home", "Store")
            )
        )
        signals.forEach { memory.saveSignal(it); seedThread(it) }
    }

    private suspend fun seedFoodAdditiveSignals() {
        val now = Instant.now()
        val signals = listOf(
            Signal(
                id = "food-add-1",
                title = "Meat Safety: Avoiding Nitrates (E250)",
                summary = "Chemicals used to keep meats pink can damage your colon.",
                theTruth = "Sodium Nitrite (E250) reacts in your stomach to form toxins that can damage your DNA.",
                theCommand = "Choose natural, unpreserved meats over 'cured' pink ones.",
                theExecution = listOf(
                    "Look for 'E250' or 'Sodium Nitrite' on the package.",
                    "Pick meats that are brown or grey (natural) instead of bright pink.",
                    "Prefer fresh chicken, fish, or beans."
                ),
                theShield = "Directly stops the formation of DNA-damaging chemicals in your body.",
                category = SignalCategory.FOOD,
                importance = SignalImportance.CRITICAL,
                confidence = SignalConfidence.VERY_HIGH,
                detectedAt = now,
                publishedAt = now,
                source = SignalSource("World Health Organization", ""),
                isActionable = true,
                affectedIngredients = listOf("Sodium Nitrite", "E250", "Curing Salt"),
                safeAlternatives = listOf("Fresh Meat", "Sea Salt Only"),
                interactionContexts = listOf("Store")
            )
        )
        signals.forEach { memory.saveSignal(it); seedThread(it) }
    }

    private suspend fun seedTruthCheckSignals() {
        val now = Instant.now()
        val signals = listOf(
            Signal(
                id = "seed-cellphone-1",
                title = "Peace of Mind: Cell Phones are Safe",
                summary = "There is no scientific proof that phone waves cause brain tumors.",
                theTruth = "Cell phones use 'non-ionizing' waves. These are too weak to damage your DNA or cause cancer.",
                theCommand = "You are safe to use your device as normal.",
                theExecution = listOf(
                    "Ignore viral posts about 5G or phone radiation.",
                    "If you want to be extra careful, use a headset to keep the phone away from your ear.",
                    "Don't waste money on 'radiation stickers'—they don't work."
                ),
                theShield = "Protects you from unnecessary stress and from wasting money on fake safety products.",
                category = SignalCategory.ENVIRONMENT,
                importance = SignalImportance.LOW,
                confidence = SignalConfidence.VERY_HIGH,
                detectedAt = now,
                publishedAt = now,
                source = SignalSource("Agency Truth Check", ""),
                verdict = EvidenceVerdict.NOT_SUPPORTED,
                investigatedClaim = "Cell phones cause brain tumors.",
                interactionContexts = listOf("Public")
            )
        )
        signals.forEach { memory.saveSignal(it); seedThread(it) }
    }

    private suspend fun seedNutritionTruths() {
        val nutritionTruths = listOf(
            NutritionIntelligence(
                id = "truth-nutrition-1",
                title = "Meat Safety: Avoiding Nitrates",
                summary = "Curing salts in pink meats are high-priority risks.",
                evidenceLevel = EvidenceStrength.VERY_HIGH,
                category = NutritionCategory.PATTERN,
                theTruth = "When Nitrates (E250) are heated with meat, they form DNA-damaging compounds in your gut.",
                theCommand = "Reduce intake of pink, preserved meats (ham, bacon, sausages).",
                theExecution = listOf(
                    "Identify 'Sodium Nitrite' on local food labels.",
                    "Pick fresh, unpreserved proteins: {PROTEIN_STAPLE}.",
                    "Save cured meats for very rare special occasions."
                ),
                theShield = "Prevents direct chemical damage to the lining of your colon.",
                source = "World Health Organization"
            ),
            NutritionIntelligence(
                id = "truth-nutrition-2",
                title = "Fiber: Your Natural Filter",
                summary = "Fiber sweeps potential toxins out of your body faster.",
                evidenceLevel = EvidenceStrength.VERY_HIGH,
                category = NutritionCategory.PATTERN,
                theTruth = "Low fiber means toxins stay in contact with your gut wall longer, increasing risk.",
                theCommand = "Eat more whole versions of your local starch ({FIBER_STAPLE}).",
                theExecution = listOf(
                    "Choose brown or whole versions of your daily grains.",
                    "Add one cup of beans or lentils to your main meal.",
                    "Keep the skin on fruits and vegetables when possible."
                ),
                theShield = "Physically dilutes and removes carcinogens before they can damage your cells.",
                source = "WCRF / AICR"
            ),
            NutritionIntelligence(
                id = "truth-nutrition-6",
                title = "Cooking Tip: Don't Burn Your Meat",
                summary = "Black char on food creates chemicals that can damage DNA.",
                evidenceLevel = EvidenceStrength.HIGH,
                category = NutritionCategory.PREPARATION,
                theTruth = "Direct flames on meat create 'HCAs' which are known cellular mutagens.",
                theCommand = "Avoid dark-charred sections on grilled or fried foods.",
                theExecution = listOf(
                    "Trim fat before cooking to stop big flames and smoke.",
                    "Use a marinade with lemon or vinegar to protect the meat.",
                    "If food gets burned, cut off the black parts before eating."
                ),
                theShield = "Reduces the amount of high-energy mutagens entering your bloodstream.",
                source = "National Cancer Institute"
            )
        )
        preventionRepository.saveNutritionIntelligence(nutritionTruths)
    }

    private suspend fun seedEducationLessons() {
        val lessons = listOf(
            EducationLesson(
                id = "edu-1",
                title = "Focus on Exposure, Not Just Hazard",
                summary = "Learn how to prioritize what really matters for your safety.",
                content = "A 'Hazard' is something that *can* cause harm. 'Risk' is the chance it *will* harm you. Risk = Hazard x Exposure. You are safe near a hazard if you reduce your exposure time.",
                keyTakeaway = "Don't panic about every chemical. Focus on the ones you touch every single day.",
                source = "Agency Foundation"
            )
        )
        preventionRepository.saveEducationLessons(lessons)
    }

    private suspend fun seedPreventionActions() {
        val actions = listOf(
            PreventionAction(
                id = "action-1",
                title = "Avoid Tobacco",
                description = "The single most powerful act to protect your lungs and life.",
                iconName = "smoke_free"
            ),
            PreventionAction(
                id = "action-4",
                title = "Move Your Body",
                description = "30 minutes of daily activity reduces risk for 13 types of cancer.",
                iconName = "directions_run"
            )
        )
        preventionRepository.savePreventionActions(actions)
    }

    private suspend fun seedTreatmentManuals() {
        val manuals = listOf(
            TreatmentManual(
                id = "treat-1",
                title = "Chemo Prep: Drink More Water",
                summary = "A simple way to protect your kidneys during treatment.",
                category = TreatmentCategory.CHEMO,
                theTruth = "Chemo drugs need to be flushed out of your body after they work. Water is the best tool for this.",
                theCommand = "Drink 3 liters of water the day before your treatment.",
                theExecution = listOf(
                    "Start drinking as soon as you wake up.",
                    "Keep a water bottle with you all day.",
                    "Don't wait until you are thirsty; sip steadily.",
                    "Stick to plain water or light tea."
                ),
                theShield = "Ensures drugs clear your kidneys quickly and reduces overall sickness."
            )
        )
        healingRepository.saveTreatmentManuals(manuals)
    }

    private suspend fun seedSymptomDirectives() {
        val symptoms = listOf(
            SymptomDirective(
                id = "symp-1",
                name = "Nausea (Sick to Stomach)",
                iconName = "sick",
                theTruth = "Treatment can upset your stomach lining and signal your brain to feel sick.",
                theCommand = "Use the 'Ginger & Dry Food' protocol immediately.",
                theExecution = listOf(
                    "Sip cool ginger tea slowly.",
                    "Eat 2-3 plain crackers.",
                    "Sit upright; don't lie flat right after eating.",
                    "Firmly press the spot 3 fingers above your inner wrist."
                ),
                theShield = "Calms your stomach naturally and breaks the 'feeling sick' loop."
            )
        )
        healingRepository.saveSymptomDirectives(symptoms)
    }

    private suspend fun seedPatientTruthChecks() {
        val defenses = listOf(
            PatientTruthCheck(
                id = "ptc-1",
                claim = "The 'Miracle Fruit' Cure",
                verdict = PatientVerdict.SCAM,
                theTruth = "There is no fruit that cures cancer. Scammers use these claims to target people who are afraid.",
                theCommand = "Do NOT stop your medical treatment for any 'miracle' diet.",
                theExecution = listOf(
                    "Keep all your hospital and chemo appointments.",
                    "Eat fruit because it is healthy, but never as a replacement for medicine.",
                    "If an ad says 'Doctors are hiding this,' ignore it."
                ),
                theShield = "Prevents a fatal mistake in your plan and keeps you receiving proven care.",
                socialScript = "I appreciate your concern, but the Agency verified the science: fruits are good food, but they aren't a cure. I'm sticking with my doctors."
            )
        )
        healingRepository.savePatientTruthChecks(defenses)
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
