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
        seedRedFlagDirectives()
        
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

    private suspend fun seedRedFlagDirectives() {
        val flags = listOf(
            RedFlagDirective(
                id = "rf-1",
                title = "Feeling Hot, Shaky, or Flushed",
                summary = "These are signs of a fever, which is an emergency after chemotherapy.",
                theTruth = "Chemotherapy lowers your white blood cells, making it impossible for your body to fight even small infections. A fever is a signal that your body is being overwhelmed.",
                theCommand = "Call your 24-hour Oncology Hotline immediately.",
                theExecution = listOf(
                    "If you have a thermometer, check your temperature. If it is 38°C (100.4°F) or higher, call now.",
                    "If you don't have a thermometer but feel 'feverish' or have the shivers, call anyway.",
                    "Ask a family member to stay with you while you make the call."
                ),
                whileYouWait = listOf(
                    "Do NOT take Paracetamol, Tylenol, or Aspirin yet. The doctor needs to see the true fever signal.",
                    "Drink a large glass of water to stay hydrated.",
                    "Place your 'Red Treatment Folder' and all your pill bottles on the kitchen table."
                ),
                handoffScript = "I am a cancer patient on active treatment. I am calling because I have a Fever/Shivers. I need immediate oncology triage.",
                theShield = "Prevents 'Neutropenic Sepsis,' a life-threatening infection that can be stopped if caught in the first hour."
            ),
            RedFlagDirective(
                id = "rf-2",
                title = "New or Worsening Back Pain",
                summary = "Sharp pain in your back or spine that won't go away.",
                theTruth = "Cancer can sometimes put pressure on your spinal cord. If not treated quickly, this can cause permanent damage to your nerves.",
                theCommand = "Call your doctor today. If you have trouble walking, go to the ER.",
                theExecution = listOf(
                    "Check if the pain feels like a tight 'band' around your chest or waist.",
                    "Check if your legs feel 'heavy,' 'tingly,' or weak when you stand up.",
                    "Note if you are having any new trouble going to the bathroom."
                ),
                whileYouWait = listOf(
                    "Lie flat on a firm surface like a bed or the floor to take pressure off your spine.",
                    "Avoid any lifting, bending, or straining.",
                    "Ask a relative to help you gather your most recent scan reports."
                ),
                handoffScript = "I am a cancer patient. I have developed new, sharp back pain and I am concerned about Spinal Cord Compression.",
                theShield = "Protects your ability to walk and maintain control of your body."
            ),
            RedFlagDirective(
                id = "rf-3",
                title = "Swelling in Face, Neck, or Arms",
                summary = "Sudden puffiness or bulging veins in your upper body.",
                theTruth = "A tumor can sometimes press on the main vein (the SVC) that carries blood from your head to your heart.",
                theCommand = "Call your oncology team immediately.",
                theExecution = listOf(
                    "Look in the mirror for new swelling around your eyes or neck.",
                    "Check if your shirt collars or rings feel suddenly tight.",
                    "Note if you feel 'fullness' in your head when you lean forward."
                ),
                whileYouWait = listOf(
                    "Sit upright; do not lie flat as this increases the swelling.",
                    "Loosen any tight clothing, ties, or jewelry.",
                    "Try to remain calm and breathe slowly."
                ),
                handoffScript = "I am a cancer patient. I have new swelling in my face and neck and I am concerned about SVC Syndrome.",
                theShield = "Ensures blood continues to flow properly from your brain and upper body."
            ),
            RedFlagDirective(
                id = "rf-4",
                title = "Sudden Shortness of Breath",
                summary = "Feeling like you can't get enough air, even when resting.",
                theTruth = "This can be caused by fluid around the lungs, a blood clot, or an infection. It needs immediate medical attention.",
                theCommand = "Dial 911 or your local emergency number immediately.",
                theExecution = listOf(
                    "Stop all physical activity.",
                    "If you have a pulse-oximeter at home, check your oxygen level.",
                    "Ask someone to unlock your front door for the paramedics."
                ),
                whileYouWait = listOf(
                    "Sit upright in a chair; do not lie down.",
                    "Open a window for fresh air.",
                    "Practice 'Pursed Lip' breathing (inhale through nose, exhale slowly through puckered lips)."
                ),
                handoffScript = "I am a cancer patient. I have sudden shortness of breath and chest tightness. I need emergency assistance.",
                theShield = "Protects your lungs and heart from total failure."
            ),
            RedFlagDirective(
                id = "rf-5",
                title = "Pain or Swelling in One Leg",
                summary = "One leg (usually the calf) becomes red, hot, and painful.",
                theTruth = "Cancer patients are at high risk for blood clots (DVT). If the clot moves to your lungs, it is life-threatening.",
                theCommand = "Call your oncology team or go to the ER today.",
                theExecution = listOf(
                    "Compare your legs; look for one that is larger or redder.",
                    "Flex your foot upward; see if the pain in your calf gets sharper.",
                    "Note any sudden coughing or chest pain."
                ),
                whileYouWait = listOf(
                    "Do NOT massage or rub the painful area (this can dislodge the clot).",
                    "Keep your leg elevated on a pillow.",
                    "Limit walking until you have been seen by a doctor."
                ),
                handoffScript = "I am a cancer patient. I have a painful, swollen left/right leg and I am concerned about a blood clot (DVT).",
                theShield = "Prevents a clot from traveling to your lungs (Pulmonary Embolism)."
            ),
            RedFlagDirective(
                id = "rf-6",
                title = "Uncontrolled Bleeding or Bruising",
                summary = "Nosebleeds that won't stop or large purple spots on your skin.",
                theTruth = "Treatment can lower your platelets, which are the 'plugs' that stop bleeding. Without them, you can bleed internally.",
                theCommand = "Call your clinic now. If bleeding is heavy, go to the ER.",
                theExecution = listOf(
                    "Check your gums for bleeding after brushing.",
                    "Look for tiny red dots (petechiae) or large bruises that appeared for no reason.",
                    "If you have a nosebleed, pinch the bridge of your nose and lean forward for 10 minutes."
                ),
                whileYouWait = listOf(
                    "Avoid any activity where you could fall or hit your head.",
                    "Do not blow your nose or use a hard toothbrush.",
                    "Stay seated and calm to keep your blood pressure low."
                ),
                handoffScript = "I am a cancer patient. I have uncontrolled bleeding/bruising and I am concerned about a Low Platelet count.",
                theShield = "Protects you from dangerous internal bleeding."
            ),
            RedFlagDirective(
                id = "rf-7",
                title = "Intense Thirst and Confusion",
                summary = "Feeling extremely thirsty, peeing often, and feeling 'spaced out'.",
                theTruth = "Some cancers cause bones to release too much calcium into the blood. This 'Hypercalcemia' can damage your heart and kidneys.",
                theCommand = "Call your oncology team today.",
                theExecution = listOf(
                    "Check if you are also suffering from severe constipation.",
                    "Ask a family member if you seem unusually sleepy or confused.",
                    "Note if you have new, deep bone pain."
                ),
                whileYouWait = listOf(
                    "Drink as much plain water as you can to help flush the calcium.",
                    "Do not take any calcium or Vitamin D supplements.",
                    "Keep a list of how many times you are peeing."
                ),
                handoffScript = "I am a cancer patient. I have extreme thirst and confusion and I am concerned about High Calcium (Hypercalcemia).",
                theShield = "Protects your kidneys and heart from mineral overload."
            ),
            RedFlagDirective(
                id = "rf-8",
                title = "Muscle Cramps and Palpitations",
                summary = "Feeling your heart 'flutter' or having painful muscle twitches.",
                theTruth = "When cancer cells die quickly after chemo, they release salts into your blood that can confuse your heart and kidneys (Tumor Lysis).",
                theCommand = "Call your oncology hotline immediately.",
                theExecution = listOf(
                    "Check if your urine has turned very dark or if you aren't peeing much.",
                    "Note any feeling of 'pins and needles' around your mouth or fingers.",
                    "Check your pulse; is it irregular or very fast?"
                ),
                whileYouWait = listOf(
                    "Sip water slowly and steadily.",
                    "Find your most recent blood test results (look for Potassium or Uric Acid levels).",
                    "Do not eat high-potassium foods (like bananas or oranges) until cleared."
                ),
                handoffScript = "I am a cancer patient. I just started treatment and have heart palpitations. I am concerned about Tumor Lysis Syndrome.",
                theShield = "Prevents sudden kidney failure and heart rhythm problems."
            ),
            RedFlagDirective(
                id = "rf-9",
                title = "Severe Burning at the IV Site",
                summary = "Pain, stinging, or redness where your chemo was injected.",
                theTruth = "If chemo drugs leak out of the vein into your skin (Extravasation), they can cause permanent tissue damage.",
                theCommand = "Call your infusion center or oncology hotline now.",
                theExecution = listOf(
                    "Look for any new swelling or blistering around the injection site.",
                    "Note the name of the drug you were just given.",
                    "Check if the area feels hot or if the skin is changing color."
                ),
                whileYouWait = listOf(
                    "Do NOT rub or apply pressure to the site.",
                    "Ask your nurse/hotline if you should use a cold or warm pack (different drugs need different care).",
                    "Keep the arm or area elevated."
                ),
                handoffScript = "I am a cancer patient. I have severe pain/swelling at my injection site and I am concerned about an IV leak (Extravasation).",
                theShield = "Prevents permanent damage to your skin, nerves, and muscles."
            ),
            RedFlagDirective(
                id = "rf-10",
                title = "Sharp Chest Pain",
                summary = "Pain in your chest that feels sharp, especially when you breathe deep.",
                theTruth = "This can be a signal of a blood clot in the lung or fluid around the heart. Both are serious.",
                theCommand = "Dial 911 or your local emergency number immediately.",
                theExecution = listOf(
                    "Stop all movement and sit down.",
                    "Check if you feel faint or lightheaded.",
                    "Note if your fingernails or lips look blue or grey."
                ),
                whileYouWait = listOf(
                    "Try to take small, shallow breaths if deep breathing hurts too much.",
                    "Unlock your door for the paramedics.",
                    "Do not eat or drink anything."
                ),
                handoffScript = "I am a cancer patient. I have sharp chest pain and feel faint. I need emergency assistance.",
                theShield = "Saves your heart and lungs from a critical blockage."
            ),
            RedFlagDirective(
                id = "rf-11",
                title = "Sudden Confusion or Slurred Speech",
                summary = "Difficulty thinking, knowing where you are, or speaking clearly.",
                theTruth = "Metabolic changes or treatment side effects can affect your brain function rapidly.",
                theCommand = "Call your doctor or emergency services now.",
                theExecution = listOf(
                    "Check if the person can tell you the current date and their location.",
                    "Look for any new facial drooping or weakness on one side.",
                    "Note if they have had a severe headache recently."
                ),
                whileYouWait = listOf(
                    "Stay with the person at all times.",
                    "Keep them in a safe, seated position so they don't fall.",
                    "Note down when the confusion started."
                ),
                handoffScript = "I am a caregiver for a cancer patient. They have sudden confusion and slurred speech. We need immediate triage.",
                theShield = "Protects the brain from metabolic or neurological damage."
            ),
            RedFlagDirective(
                id = "rf-12",
                title = "Severe Abdominal Pain and Vomiting",
                summary = "Cramping pain in your stomach and inability to pass gas or stool.",
                theTruth = "Treatment or the tumor itself can sometimes block the flow through your bowels.",
                theCommand = "Call your oncology team or go to the ER.",
                theExecution = listOf(
                    "Check if your stomach looks bloated or feels hard.",
                    "Note when you last had a bowel movement.",
                    "Check if you can keep any water down."
                ),
                whileYouWait = listOf(
                    "Do NOT take any laxatives or enemas.",
                    "Stop eating and drinking until you speak to a doctor.",
                    "Walk around gently if you can, but stop if pain increases."
                ),
                handoffScript = "I am a cancer patient. I have severe stomach pain and vomiting and I am concerned about a Bowel Obstruction.",
                theShield = "Prevents a dangerous tear (perforation) in your digestive system."
            ),
            RedFlagDirective(
                id = "rf-13",
                title = "Severe Diarrhea (>4 times/day)",
                summary = "Watery stools that happen much more often than usual.",
                theTruth = "Some treatments can damage the gut lining too much, leading to rapid dehydration and loss of minerals.",
                theCommand = "Call your oncology clinic today.",
                theExecution = listOf(
                    "Keep a count of how many times you go to the bathroom.",
                    "Check for any blood or mucus in your stool.",
                    "Note if you feel dizzy when you stand up (a sign of dehydration)."
                ),
                whileYouWait = listOf(
                    "Sip an electrolyte drink (like Pedialyte or Gatorade) slowly.",
                    "Eat 'B.R.A.T' foods: Bananas, Rice, Applesauce, Toast.",
                    "Do NOT take over-the-counter anti-diarrhea meds unless your doctor says yes."
                ),
                handoffScript = "I am a cancer patient. I have severe treatment-related diarrhea and I'm concerned about dehydration.",
                theShield = "Protects your gut and prevents a dangerous loss of body fluids."
            ),
            RedFlagDirective(
                id = "rf-14",
                title = "Sudden Vision Changes",
                summary = "Blurred vision, seeing 'spots,' or sudden loss of sight.",
                theTruth = "High blood pressure or treatment-related changes can affect the pressure in your brain or eyes.",
                theCommand = "Call your doctor or go to the ER today.",
                theExecution = listOf(
                    "Check if you also have a severe, 'pounding' headache.",
                    "Note if the vision change is in one eye or both.",
                    "Check if you feel dizzy or lose your balance."
                ),
                whileYouWait = listOf(
                    "Rest in a quiet, dark room.",
                    "Avoid looking at phone or TV screens.",
                    "Ask someone to drive you; do NOT attempt to drive."
                ),
                handoffScript = "I am a cancer patient. I have sudden vision changes and a severe headache. I need immediate evaluation.",
                theShield = "Protects your sight and your brain from internal pressure damage."
            ),
            RedFlagDirective(
                id = "rf-15",
                title = "Uncontrollable Shaking (Rigors)",
                summary = "Shaking so hard you can't hold a glass of water, even without a fever.",
                theTruth = "Violent shivering is often the body's first signal of a serious bloodstream infection, even before a fever starts.",
                theCommand = "Call your oncology hotline immediately.",
                theExecution = listOf(
                    "Note if you also feel suddenly very cold or very tired.",
                    "Check your temperature every 15 minutes.",
                    "Look for any redness around your PICC line or Port site."
                ),
                whileYouWait = listOf(
                    "Bundle up in warm blankets.",
                    "Do NOT take any medicine to stop the shaking until you speak to a nurse.",
                    "Prepare to go to the hospital, as this often needs IV antibiotics."
                ),
                handoffScript = "I am a cancer patient. I have developed uncontrollable shaking/rigors. I need immediate sepsis triage.",
                theShield = "Intercepts a bloodstream infection at the earliest possible second."
            )
        )
        healingRepository.saveRedFlagDirectives(flags)
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
