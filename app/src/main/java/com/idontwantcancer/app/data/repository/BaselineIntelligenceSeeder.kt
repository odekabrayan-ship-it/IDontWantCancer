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
 * Every signal follows the 4-Point Directive Protocol: Truth, Command, Execution, Shield.
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
        seedCosmeticShield()
        seedHouseholdSentinel()
        seedFoodAdditiveRegistry()
        
        val now = Instant.now()
        
        // --- 1. FOOD SAFETY DIRECTIVES ---
        val fdaRecall = Signal(
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
            significanceLevel = SignificanceOutcome.CRITICAL,
            category = SignalCategory.FOOD,
            importance = SignalImportance.CRITICAL,
            confidence = SignalConfidence.VERY_HIGH,
            detectedAt = now.minusSeconds(3600 * 24),
            publishedAt = now.minusSeconds(3600 * 24),
            source = SignalSource("U.S. FDA", ""),
            isActionable = true,
            actionType = ActionType.AVOID,
            scope = GeographicScope.NATIONAL,
            targetCountryCode = "US",
            safetyLevel = SafetyLevel.DANGER
        )

        // --- 2. SCREENING DIRECTIVES ---
        val screeningSignal = Signal(
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
            significanceLevel = SignificanceOutcome.SIGNIFICANT,
            category = SignalCategory.SCREENING,
            importance = SignalImportance.HIGH,
            confidence = SignalConfidence.HIGH,
            detectedAt = now.minusSeconds(3600 * 72),
            publishedAt = now.minusSeconds(3600 * 72),
            source = SignalSource("Health Authorities", ""),
            isActionable = true,
            actionType = ActionType.SCREEN,
            scope = GeographicScope.GLOBAL,
            safetyLevel = SafetyLevel.MONITOR
        )

        // --- 3. ENVIRONMENTAL SECURITY (Practical Daily Interactions) ---
        val waterTapSignal = Signal(
            id = "env-practical-1",
            title = "Tap Water: Metal & Chemical Flush",
            summary = "Old pipes can leak tiny amounts of lead into water while you sleep.",
            theTruth = "Water sitting in pipes for hours can collect trace metals. A quick flush clears them out.",
            theCommand = "Run your cold tap for 30 seconds every morning before drinking.",
            theExecution = listOf(
                "When you first use the tap in the morning, let it run.",
                "Wait until the water feels cold to your hand.",
                "Use this fresh water for your coffee, tea, and cooking.",
                "The first 'sitting' water is still fine for washing or watering plants."
            ),
            theShield = "Stops you from drinking tiny amounts of lead or industrial chemicals every single day.",
            significanceLevel = SignificanceOutcome.SIGNIFICANT,
            category = SignalCategory.ENVIRONMENT,
            importance = SignalImportance.MODERATE,
            confidence = SignalConfidence.VERY_HIGH,
            detectedAt = now.minusSeconds(3600),
            publishedAt = now.minusSeconds(3600),
            source = SignalSource("Environmental Watch", ""),
            isActionable = true,
            actionType = ActionType.MONITOR,
            interactionContexts = listOf("Home")
        )

        val receiptSignal = Signal(
            id = "env-practical-2",
            title = "Store Receipts: Avoid BPA Coating",
            summary = "The shiny coating on receipts has chemicals that absorb through your skin.",
            theTruth = "Most paper receipts use BPA (a hormone disruptor). Touching them—especially with wet hands—moves the chemical into your body.",
            theCommand = "Don't take paper receipts; ask for a digital one instead.",
            theExecution = listOf(
                "Say 'No thanks' to the paper receipt or ask for email.",
                "If you must take it, hold it by the white edges, not the printed side.",
                "Wash your hands with soap after handling thermal paper.",
                "Don't let children play with receipts or keep them in your pockets."
            ),
            theShield = "Protects your hormone balance from common industrial chemicals.",
            significanceLevel = SignificanceOutcome.SIGNIFICANT,
            category = SignalCategory.ENVIRONMENT,
            importance = SignalImportance.LOW,
            confidence = SignalConfidence.HIGH,
            detectedAt = now.minusSeconds(7200),
            publishedAt = now.minusSeconds(7200),
            source = SignalSource("Agency Safety Lab", ""),
            isActionable = true,
            actionType = ActionType.AVOID,
            interactionContexts = listOf("Public", "Work")
        )

        val stoveSignal = Signal(
            id = "env-practical-4",
            title = "Cooking Stove: Indoor Air Quality",
            summary = "Cooking on gas or high heat creates indoor air pollution.",
            theTruth = "Burning gas and frying oils release invisible gases and smoke that are hard on your lungs.",
            theCommand = "Always turn on your kitchen fan *before* you start cooking.",
            theExecution = listOf(
                "Turn the fan to high as soon as you start the stove.",
                "If you don't have a fan, open a window to let fresh air in.",
                "Keep the air moving for 5 minutes after you finish cooking.",
                "Use the back burners when possible—the fan catches that smoke better."
            ),
            theShield = "Protects your lung tissue from breathing in the same pollutants found in heavy city smog.",
            significanceLevel = SignificanceOutcome.HIGH_SIGNIFICANCE,
            category = SignalCategory.ENVIRONMENT,
            importance = SignalImportance.HIGH,
            confidence = SignalConfidence.VERY_HIGH,
            detectedAt = now.minusSeconds(14400),
            publishedAt = now.minusSeconds(14400),
            source = SignalSource("Indoor Air Intelligence", ""),
            isActionable = true,
            actionType = ActionType.MONITOR,
            interactionContexts = listOf("Home")
        )

        val signals = listOf(fdaRecall, screeningSignal, waterTapSignal, receiptSignal, stoveSignal)

        signals.forEach { signal ->
            memory.saveSignal(signal)
            seedThread(signal)
        }
    }

    private suspend fun seedNutritionTruths() {
        val nutritionTruths = listOf(
            NutritionIntelligence(
                id = "truth-nutrition-1",
                title = "Meat Safety: Avoiding Nitrates",
                summary = "Certain pink meats have chemicals that damage your colon.",
                evidenceLevel = EvidenceStrength.VERY_HIGH,
                category = NutritionCategory.PATTERN,
                theTruth = "Meats like ham, bacon, and sausages use 'Nitrates' to stay pink. In your stomach, these turn into DNA-damaging compounds.",
                theCommand = "Choose natural, unpreserved meats over 'cured' pink ones.",
                theExecution = listOf(
                    "Look for 'Sodium Nitrite' or 'E250' on the back of the package.",
                    "Pick meats that look grey or brown (natural) instead of bright pink.",
                    "Prefer fresh chicken, fish, or beans as your main protein."
                ),
                theShield = "Directly stops the formation of DNA-mutating chemicals in your digestive system.",
                source = "World Health Organization"
            ),
            NutritionIntelligence(
                id = "truth-nutrition-6",
                title = "Cooking Tip: Don't Burn Your Meat",
                summary = "Dark charring on meat creates chemicals linked to cancer.",
                evidenceLevel = EvidenceStrength.HIGH,
                category = NutritionCategory.PREPARATION,
                theTruth = "Direct flames and black char on meat create 'mutagens' that can damage your cells.",
                theCommand = "Avoid dark charring; cook meat until just done.",
                theExecution = listOf(
                    "Trim fat before grilling to prevent big flames and smoke.",
                    "Use a marinade with lemon or vinegar—this helps protect the meat.",
                    "If a piece gets burned or black, cut that part off before eating.",
                    "Use lower heat and turn the meat often."
                ),
                theShield = "Reduces the amount of high-energy toxins that enter your body through your food.",
                source = "Cancer Intelligence Lab"
            )
        )
        preventionRepository.saveNutritionIntelligence(nutritionTruths)
    }

    private suspend fun seedEducationLessons() {
        val lessons = listOf(
            EducationLesson(
                id = "edu-1",
                title = "How the Agency Finds Risks",
                summary = "Learn why we focus on 'Exposure' rather than 'Poison'.",
                content = "A substance might be a 'Hazard' (can cause harm), but your 'Risk' depends on how much and how often you touch it. Living near a hazard doesn't mean you are at risk if you have a shield.",
                keyTakeaway = "Focus your energy on things you touch or breathe every single day.",
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
                theTruth = "Chemotherapy drugs need to be flushed out of your system after they do their work. Water is the best way to do this.",
                theCommand = "Drink 3 liters of water the day before your treatment.",
                theExecution = listOf(
                    "Start drinking water as soon as you wake up.",
                    "Keep a bottle with you at all times.",
                    "Don't wait until you are thirsty—sip steadily all day.",
                    "Avoid sugary sodas; stick to plain water or light tea."
                ),
                theShield = "Ensures the drugs don't sit in your kidneys too long and helps reduce overall sickness."
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
                    "Sip cool ginger tea or suck on a piece of fresh ginger.",
                    "Eat 2-3 plain crackers very slowly.",
                    "Sit upright; do not lie flat right after eating.",
                    "Press firmly on your inner wrist (3 fingers up from the hand)."
                ),
                theShield = "Calms your stomach naturally and breaks the 'feeling sick' loop in your brain."
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
                theTruth = "There is no fruit that cures cancer. Scammers use these claims to sell expensive supplements to people who are afraid.",
                theCommand = "Do NOT stop your medical treatment for any 'miracle' diet.",
                theExecution = listOf(
                    "Keep all your hospital appointments.",
                    "Eat fruit because it is healthy, but not as a replacement for medicine.",
                    "If an ad says 'Doctors are hiding this,' it is a scam."
                ),
                theShield = "Prevents a fatal mistake in your treatment plan and keeps your body strong for the fight.",
                socialScript = "I appreciate you wanting to help, but the Agency verified the science: fruits are good food, but they aren't a cure. I'm sticking with my doctors."
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

    private suspend fun seedCosmeticShield() {}
    private suspend fun seedHouseholdSentinel() {}
    private suspend fun seedFoodAdditiveRegistry() {}
}
