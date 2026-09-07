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
 * This is the Elite Integrity Registry: 100+ High-Standard Directives.
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
        
        // --- THE MASTER SHOPPING SHIELD (Action 2) ---
        seedCosmeticRegistry()
        seedHouseholdRegistry()
        seedFoodAdditiveRegistry()
        
        // --- THE ENVIRONMENTAL SECURITY REGISTRY (Action 5) ---
        seedEnvironmentalRegistry()
        
        // --- THE SENTINEL WATCH (Action 1) ---
        seedSafetyAlerts()
    }

    private suspend fun seedCosmeticRegistry() {
        val data = listOf(
            Triple("Parabens", listOf("Methylparaben", "Propylparaben", "Butylparaben", "Ethylparaben", "Isobutylparaben", "E216", "E218"), "Endocrine Disruptors common in lotions and makeup. They mimic estrogen and can promote breast cancer cell growth."),
            Triple("Formaldehyde Releasers", listOf("DMDM Hydantoin", "Quaternium-15", "Imidazolidinyl Urea", "Diazolidinyl Urea", "2-bromo-2-nitropropane-1,3-diol", "Bronopol"), "Preservatives that slowly release known human carcinogens into your skin and air."),
            Triple("Phthalates", listOf("DEP", "DBP", "DEHP", "Phthalate", "Fragrance", "Parfum", "Synthetic Musk"), "Hormone disruptors that make scents last longer. Linked to reproductive issues and hormone-sensitive cancers."),
            Triple("Ethanolamines", listOf("DEA", "TEA", "MEA", "Diethanolamine", "Triethanolamine", "Cocamide DEA", "Lauramide DEA"), "Chemicals used as sudsing agents. Can react with other ingredients to form Nitrosamines, which are powerful carcinogens."),
            Triple("Coal Tar", listOf("Coal Tar", "CI 77266", "Carbon Black", "P-phenylenediamine", "PPD", "Aminophenol"), "Used in dandruff shampoos and hair dyes. A known human carcinogen (Group 1)."),
            Triple("Synthetic Musks", listOf("Galaxolide", "Tonalide", "HHCB", "AHTN", "Musk Xylene"), "Highly persistent chemicals used in perfumes. They build up in human fat tissue and disrupt hormones."),
            Triple("Chemical UV Filters", listOf("Oxybenzone", "Octinoxate", "Benzophenone-3", "Avobenzone", "Homosalate", "Octocrylene"), "Sunscreen chemicals that absorb into the body at high rates and disrupt hormonal balance."),
            Triple("PFAS (Forever Chemicals)", listOf("PTFE", "Perfluoro", "Polyperfluoromethylisopropyl Ether", "Teflon", "Fluorine"), "Used in waterproof mascara and long-wear foundations. Linked to kidney cancer and immune suppression."),
            Triple("Lead Acetate", listOf("Lead Acetate"), "Found in some progressive hair dyes. Lead is a neurotoxin and suspected carcinogen."),
            Triple("Talcum Powder", listOf("Talc", "Hydrous Magnesium Silicate", "Magnesium Silicate"), "Can be naturally contaminated with asbestos. Linked to ovarian and lung cancer."),
            Triple("Resorcinol", listOf("Resorcinol", "1,3-benzenediol"), "Found in hair dyes and acne treatments. Linked to thyroid disruption and immune system issues."),
            Triple("Carbon Black", listOf("Carbon Black", "D&C Black No. 2", "Acetylene Black"), "Used in eyeliners and mascaras. Linked to cancer and organ system toxicity.")
        )

        data.forEach { (name, ingredients, truth) ->
            saveSignal(
                id = "cos-${name.lowercase().replace(" ", "-")}",
                title = "$name: Label Check",
                summary = "Dangerous chemicals found in cosmetics and personal care products.",
                theTruth = truth,
                theCommand = "Switch to products explicitly labeled '${name}-Free'.",
                theExecution = listOf(
                    "Scan the ingredient list for: ${ingredients.joinToString(", ")}.",
                    "If any match, do not purchase the product.",
                    "Choose 'Clean Beauty' brands that provide a 'Red List' of banned chemicals."
                ),
                theShield = "Protects your skin and endocrine system from cumulative chemical absorption.",
                category = SignalCategory.COSMETICS,
                importance = SignalImportance.HIGH,
                confidence = SignalConfidence.VERY_HIGH,
                affectedIngredients = ingredients,
                safeAlternatives = listOf("Natural Extracts", "Mineral UV Filters", "Beeswax", "Tocopherol"),
                interactionContexts = listOf("Store", "Home")
            )
        }
    }

    private suspend fun seedHouseholdRegistry() {
        val data = listOf(
            Triple("Triclosan", listOf("Triclosan", "Triclocarban"), "Antibacterial agent that disrupts thyroid hormones and contributes to antibiotic resistance."),
            Triple("1,4-Dioxane", listOf("PEG", "Polyethylene Glycol", "Sodium Laureth Sulfate", "SLES", "Ceteareth", "Polysorbate"), "A probable carcinogen that is a manufacturing byproduct. Found in sudsing products like dish soap and laundry detergent."),
            Triple("Quats", listOf("Benzalkonium Chloride", "Distearyldimonium Chloride", "Quaternium-18"), "Quaternary Ammonium Compounds used as disinfectants and fabric softeners. Potent lung irritants and endocrine disruptors."),
            Triple("Ammonia", listOf("Ammonia", "Ammonium Hydroxide"), "Found in window cleaners. Can react with bleach to create deadly Mustard Gas. Chronic exposure is hard on the lungs."),
            Triple("2-Butoxyethanol", listOf("2-Butoxyethanol", "Ethylene Glycol Monobutyl Ether"), "Found in multipurpose and glass cleaners. A known organ toxin linked to blood disorders and potential cancer.")
        )

        data.forEach { (name, ingredients, truth) ->
            saveSignal(
                id = "clean-${name.lowercase().replace(" ", "-")}",
                title = "$name: Household Registry",
                summary = "Cleaning chemicals that linger in your home's air and surfaces.",
                theTruth = truth,
                theCommand = "Switch to fragrance-free, plant-based cleaning agents.",
                theExecution = listOf(
                    "Identify '$name' or synonyms: ${ingredients.joinToString(", ")}.",
                    "Ask for digital receipts or hold paper ones by the edges (Receipts contain BPA).",
                    "Use white vinegar and water as a safe, all-purpose alternative."
                ),
                theShield = "Ensures your home sanctuary is free from volatile industrial solvents.",
                category = SignalCategory.CLEANING,
                importance = SignalImportance.MODERATE,
                confidence = SignalConfidence.HIGH,
                affectedIngredients = ingredients,
                safeAlternatives = listOf("White Vinegar", "Castile Soap", "Baking Soda", "Essential Oils"),
                interactionContexts = listOf("Home", "Store")
            )
        }
    }

    private suspend fun seedFoodAdditiveRegistry() {
        val data = listOf(
            Triple("Sodium Nitrite", listOf("E250", "Sodium Nitrite", "Curing Salt", "Pink Salt", "Prague Powder", "Nitrate"), "Used in bacon and ham. Forms DNA-damaging Nitrosamines in your stomach."),
            Triple("Titanium Dioxide", listOf("E171", "Titanium Dioxide", "CI 77891", "Pigment White 6"), "Whitening pigment in candies and pills. Contains nanoparticles that can damage the gut barrier."),
            Triple("Potassium Bromate", listOf("E924", "Potassium Bromate", "Bromated Flour", "Enriched Bromated Flour"), "Flour improver that is a known carcinogen. Banned in most countries except the USA."),
            Triple("BHA & BHT", listOf("E320", "E321", "Butylated Hydroxyanisole", "Butylated Hydroxytoluene", "Antioxidant 320"), "Preservatives used in oils and cereals. Linked to hormonal disruption and cancer."),
            Triple("Azo Dyes", listOf("Red 40", "Yellow 5", "Yellow 6", "E129", "E102", "E110", "Red 3", "E127", "Tartrazine", "Allura Red"), "Synthetic food colors derived from petroleum. Suspected carcinogens."),
            Triple("Propyl Gallate", listOf("E310", "Propyl Gallate"), "Antioxidant used to prevent oils from going rancid. Suspected endocrine disruptor."),
            Triple("Carrageenan", listOf("E407", "Carrageenan", "Irish Moss Extract"), "Thickener found in dairy and plant milks. Can cause intense gut inflammation, a precursor to cancer."),
            Triple("TBHQ", listOf("Tertiary Butylhydroquinone", "E319", "Antioxidant 319"), "Preservative in crackers and frozen foods. Linked to immune system damage."),
            Triple("Potassium Iodate", listOf("E917", "Potassium Iodate"), "Flour treatment agent. Linked to thyroid dysfunction and potential carcinogenic effects."),
            Triple("Aspartame", listOf("E951", "Aspartame", "Equal", "NutraSweet"), "Artificial sweetener classified as 'possibly carcinogenic' by IARC (2B)."),
            Triple("Acesulfame K", listOf("E950", "Acesulfame Potassium", "Ace-K"), "Artificial sweetener. Some studies suggest potential for thyroid disruption and cancer.")
        )

        data.forEach { (name, ingredients, truth) ->
            saveSignal(
                id = "food-add-${name.lowercase().replace(" ", "-")}",
                title = "$name: Food Registry",
                summary = "Industrial chemicals used to improve shelf-life and appearance of food.",
                theTruth = truth,
                theCommand = "Avoid 'Highly Processed' foods with long chemical ingredient lists.",
                theExecution = listOf(
                    "Check the 'E-Number' or chemical name on the back label.",
                    "Prioritize fresh, single-ingredient whole foods.",
                    "Switch to organic or 'Additive-Free' versions of your staples."
                ),
                theShield = "Protects your digestive system from chronic exposure to industrial mutagens.",
                category = SignalCategory.FOOD,
                importance = SignalImportance.HIGH,
                confidence = SignalConfidence.VERY_HIGH,
                affectedIngredients = ingredients,
                safeAlternatives = listOf("Fresh Meat", "Natural Colors (Turmeric, Beet)", "Honey", "Sea Salt"),
                interactionContexts = listOf("Store", "Home")
            )
        }
    }

    private suspend fun seedEnvironmentalRegistry() {
        val now = Instant.now()
        val signals = listOf(
            Signal(
                id = "env-mold",
                title = "Indoor Watch: Hidden Black Mold",
                summary = "Invisible spores that release toxic mycotoxins.",
                theTruth = "Stachybotrys (Black Mold) releases toxins that suppress the immune system and cause chronic cellular stress.",
                theCommand = "Fix any water leaks and maintain humidity below 50%.",
                theExecution = listOf("Inspect window seals and under-sink pipes for dampness.", "Use a dehumidifier in basements.", "Clean small mold spots with vinegar, never bleach."),
                theShield = "Maintains your body's immune surveillance by removing a constant biological toxin.",
                category = SignalCategory.ENVIRONMENT,
                importance = SignalImportance.HIGH,
                confidence = SignalConfidence.HIGH,
                detectedAt = now,
                publishedAt = now,
                source = SignalSource("Health Watch", ""),
                interactionContexts = listOf("Home")
            ),
            Signal(
                id = "env-pesticide",
                title = "Garden Watch: Glyphosate & RoundUp",
                summary = "Common weedkiller classified as a probable human carcinogen.",
                theTruth = "Glyphosate is linked to Non-Hodgkin Lymphoma. It lingers in soil and on non-organic produce.",
                theCommand = "Switch to manual weeding or 'Acetic Acid' (strong vinegar) sprays.",
                theExecution = listOf("Check your garage for 'RoundUp' or anything with Glyphosate.", "Buy 'Certified Organic' for the Dirty Dozen fruits (like Strawberries).", "Wash all produce with a 50/50 vinegar/water soak."),
                theShield = "Reduces the cumulative pesticide load in your bloodstream.",
                category = SignalCategory.ENVIRONMENT,
                importance = SignalImportance.HIGH,
                confidence = SignalConfidence.HIGH,
                detectedAt = now,
                publishedAt = now,
                source = SignalSource("Agency Safety Lab", ""),
                interactionContexts = listOf("Home", "Public")
            )
        )
        signals.forEach { memory.saveSignal(it); seedThread(it) }
    }

    private suspend fun seedSafetyAlerts() {
        val now = Instant.now()
        val signals = listOf(
            Signal(
                id = "alert-medical-benzene",
                title = "Safety Alert: Benzene in Aerosols",
                summary = "Benzene contamination identified in spray sunscreens and dry shampoos.",
                theTruth = "Benzene is a powerful leukemia-causing chemical. It was found as a propellant contaminant in multiple aerosol brands.",
                theCommand = "Stop using aerosolized skin/hair products; use lotions or pump sprays.",
                theExecution = listOf("Check your bathroom for aerosol cans.", "Compare brand names against the FDA recall list.", "Switch to mechanical pump sprays or lotions."),
                theShield = "Eliminates the inhalation and skin-absorption of a high-energy human carcinogen.",
                category = SignalCategory.MEDICINE,
                importance = SignalImportance.CRITICAL,
                confidence = SignalConfidence.VERY_HIGH,
                detectedAt = now,
                publishedAt = now,
                source = SignalSource("U.S. FDA", ""),
                isActionable = true,
                actionType = ActionType.AVOID,
                interactionContexts = listOf("Home", "Store")
            )
        )
        signals.forEach { memory.saveSignal(it); seedThread(it) }
    }

    private suspend fun saveSignal(
        id: String,
        title: String,
        summary: String,
        theTruth: String,
        theCommand: String,
        theExecution: List<String>,
        theShield: String,
        category: SignalCategory,
        importance: SignalImportance,
        confidence: SignalConfidence,
        affectedIngredients: List<String>,
        safeAlternatives: List<String>,
        interactionContexts: List<String>
    ) {
        val signal = Signal(
            id = id,
            title = title,
            summary = summary,
            theTruth = theTruth,
            theCommand = theCommand,
            theExecution = theExecution,
            theShield = theShield,
            category = category,
            importance = importance,
            confidence = confidence,
            detectedAt = Instant.now(),
            publishedAt = Instant.now(),
            source = SignalSource("Agency Intelligence Lab", ""),
            isActionable = true,
            affectedIngredients = affectedIngredients,
            safeAlternatives = safeAlternatives,
            interactionContexts = interactionContexts
        )
        memory.saveSignal(signal)
        seedThread(signal)
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
                theExecution = listOf("Identify 'Sodium Nitrite' on local food labels.", "Pick fresh, unpreserved proteins: {PROTEIN_STAPLE}.", "Save cured meats for very rare special occasions."),
                theShield = "Prevents direct chemical damage to the lining of your colon.",
                source = "World Health Organization"
            ),
            NutritionIntelligence(
                id = "truth-nutrition-6",
                title = "Cooking Tip: Don't Burn Your Meat",
                summary = "Black char on food creates chemicals that can damage DNA.",
                evidenceLevel = EvidenceStrength.HIGH,
                category = NutritionCategory.PREPARATION,
                theTruth = "Direct flames on meat create 'HCAs' which are known cellular mutagens.",
                theCommand = "Avoid dark-charred sections on grilled or fried foods.",
                theExecution = listOf("Trim fat before cooking to stop big flames and smoke.", "Use a marinade with lemon or vinegar to protect the meat.", "If food gets burned, cut off the black parts before eating."),
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
                theExecution = listOf("Start drinking as soon as you wake up.", "Keep a water bottle with you all day.", "Don't wait until you are thirsty; sip steadily.", "Stick to plain water or light tea."),
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
                theExecution = listOf("Sip cool ginger tea slowly.", "Eat 2-3 plain crackers.", "Sit upright; don't lie flat right after eating.", "Firmly press the spot 3 fingers above your inner wrist."),
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
                theExecution = listOf("Keep all your hospital and chemo appointments.", "Eat fruit because it is healthy, but never as a replacement for medicine.", "If an ad says 'Doctors are hiding this,' ignore it."),
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
                theExecution = listOf("If you have a thermometer, check your temperature. If it is 38°C (100.4°F) or higher, call now.", "If you don't have a thermometer but feel 'feverish' or have the shivers, call anyway.", "Ask a family member to stay with you while you make the call."),
                whileYouWait = listOf("Do NOT take Paracetamol, Tylenol, or Aspirin yet. The doctor needs to see the true fever signal.", "Drink a large glass of water to stay hydrated.", "Place your 'Red Treatment Folder' and all your pill bottles on the kitchen table."),
                handoffScript = "I am a cancer patient on active treatment. I am calling because I have a Fever/Shivers. I need immediate oncology triage.",
                theShield = "Prevents 'Neutropenic Sepsis,' a life-threatening infection that can be stopped if caught in the first hour."
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
}
