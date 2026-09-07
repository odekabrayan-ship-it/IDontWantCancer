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
        
        // --- THE TRUTH SENTINEL (Action 4) ---
        seedTruthCheckSignals()
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
        val nutritionTruths = mutableListOf<NutritionIntelligence>()

        // --- THE DEFENSE (What to Avoid) ---
        nutritionTruths.addAll(listOf(
            NutritionIntelligence(
                id = "truth-def-1",
                title = "Processed Meat: Group 1 Carcinogen",
                summary = "Sausages, bacon, and ham are direct causes of colorectal cancer.",
                evidenceLevel = EvidenceStrength.VERY_HIGH,
                category = NutritionCategory.DEFENSE,
                theTruth = "Strong evidence from 800+ studies confirms that chemicals used in meat processing (like nitrates) form carcinogenic nitrosamines in your body.",
                theCommand = "Eliminate or minimize processed meats to almost zero.",
                theExecution = listOf("Remove bacon, hot dogs, salami, and deli meats from your weekly shopping list.", "Ask for 'Uncured' versions if you must consume them.", "Replace morning bacon with eggs, beans, or avocado."),
                theShield = "Blocks the direct delivery of industrial mutagens to your colon lining.",
                switchThisForThat = "Switch morning bacon or ham for avocado, eggs, or beans.",
                source = "IARC / World Health Organization"
            ),
            NutritionIntelligence(
                id = "truth-def-2",
                title = "Red Meat: Limit Weekly Intake",
                summary = "Beef, pork, and lamb increase cancer risk in high amounts.",
                evidenceLevel = EvidenceStrength.HIGH,
                category = NutritionCategory.DEFENSE,
                theTruth = "Heme iron and high-heat cooking of red meat can damage DNA and promote tumor growth.",
                theCommand = "Limit red meat to no more than 3 portions (12-18 oz) per week.",
                theExecution = listOf("Measure your portions: one portion is about the size of a deck of cards.", "Substitute red meat with poultry, fish, or plant-based proteins 4 days a week.", "Never use red meat as a 'daily staple'."),
                theShield = "Reduces the systemic oxidative stress caused by excess heme iron processing.",
                source = "WCRF / AICR"
            ),
            NutritionIntelligence(
                id = "truth-def-3",
                title = "Sugary Drinks: The body fat link",
                summary = "Sodas and sweetened juices cause cancer through body fat pathways.",
                evidenceLevel = EvidenceStrength.VERY_HIGH,
                category = NutritionCategory.DEFENSE,
                theTruth = "Sugar itself doesn't cause cancer directly, but sugary drinks cause weight gain. Body fat is a cause of 13 different types of cancer.",
                theCommand = "Switch to water, unsweetened tea, or coffee.",
                theExecution = listOf("Stop buying sodas, energy drinks, and 'fruit' drinks with added sugar.", "Dilute juice with 50% sparkling water if you are transitioning.", "Always carry a reusable water bottle to avoid 'impulse' soda purchases."),
                theShield = "Maintains a healthy weight and normalizes insulin signaling, removing 'fuel' for tumors.",
                switchThisForThat = "Switch sugary sodas for sparkling water with a squeeze of fresh lemon or lime.",
                source = "WCRF / AICR"
            ),
            NutritionIntelligence(
                id = "truth-def-4",
                title = "Alcohol: Multi-System Carcinogen",
                summary = "Alcohol breaks DNA strands in seven different organ systems.",
                evidenceLevel = EvidenceStrength.VERY_HIGH,
                category = NutritionCategory.DEFENSE,
                theTruth = "When your body breaks down alcohol, it creates acetaldehyde—a toxin that breaks DNA and prevents your cells from repairing the damage.",
                theCommand = "Minimize or eliminate alcohol for maximum protection.",
                theExecution = listOf("Understand there is no 'safe amount' for breast cancer prevention.", "Switch to 'Mocktails' or non-alcoholic beers during social events.", "If you drink, limit to 1 (women) or 2 (men) standard drinks per day maximum."),
                theShield = "Prevents the systematic flooding of your internal organs with DNA-breaking toxins.",
                source = "IARC / WCRF"
            ),
            NutritionIntelligence(
                id = "truth-def-5",
                title = "Ultra-Processed 'Fast' Foods",
                summary = "Energy-dense snacks and commercial baked goods drive cancer risk.",
                evidenceLevel = EvidenceStrength.HIGH,
                category = NutritionCategory.DEFENSE,
                theTruth = "These foods are engineered to be 'over-consumed.' They are high in fats, starches, and sugars that promote obesity and inflammation.",
                theCommand = "Limit intake of 'convenience' foods; prioritize home-cooked meals.",
                theExecution = listOf("Avoid the 'middle aisles' of the store where packaged snacks live.", "Check labels for more than 5 ingredients; if it's long, it's processed.", "Batch-cook meals on weekends to avoid the 'fast-food' trap during the work week."),
                theShield = "Stops chronic systemic inflammation caused by industrial food additives and excess calories.",
                source = "Agency Intelligence"
            ),
            NutritionIntelligence(
                id = "truth-def-6",
                title = "Salt-Preserved Foods: Stomach Watch",
                summary = "Excess salt and pickled/salted foods increase stomach cancer risk.",
                evidenceLevel = EvidenceStrength.HIGH,
                category = NutritionCategory.DEFENSE,
                theTruth = "High salt intake can damage the stomach lining and encourage the growth of H. pylori bacteria, a major cancer trigger.",
                theCommand = "Limit salt intake to less than 5g (one teaspoon) per day.",
                theExecution = listOf("Avoid traditional salted fish or heavily pickled vegetables.", "Taste food before adding salt; use herbs and spices for flavor instead.", "Check 'Sodium' content on bread and cereal labels—hidden salt is high there."),
                theShield = "Protects your stomach lining from abrasive and inflammatory damage.",
                source = "WCRF / WHO"
            ),
            NutritionIntelligence(
                id = "truth-def-7",
                title = "High-Dose Supplements: Risk of Overload",
                summary = "Unverified supplements can have unexpected harmful effects.",
                evidenceLevel = EvidenceStrength.HIGH,
                category = NutritionCategory.DEFENSE,
                theTruth = "Research shows that high-dose supplements (like Beta-Carotene) can actually increase risk in some groups. Nutrients are safest from whole foods.",
                theCommand = "Meet your nutritional needs through food alone; do not rely on supplements.",
                theExecution = listOf("Consult your doctor before starting any high-dose vitamins.", "Focus on a varied diet to get a natural spectrum of vitamins.", "Be wary of 'miracle pill' marketing for cancer prevention."),
                theShield = "Ensures your biological systems aren't overloaded by concentrated synthetic nutrients.",
                source = "AICR / Agency Truth Check"
            )
        ))

        // --- THE REPAIR (What to Prioritize) ---
        nutritionTruths.addAll(listOf(
            NutritionIntelligence(
                id = "truth-rep-1",
                title = "Whole Grains: Your Fiber Shield",
                summary = "Oats, brown rice, and quinoa reduce colorectal cancer risk.",
                evidenceLevel = EvidenceStrength.VERY_HIGH,
                category = NutritionCategory.REPAIR,
                theTruth = "Dietary fiber dilutes carcinogens in the gut and speeds up their exit from the body.",
                theCommand = "Make at least 90% of your grain intake 'Whole Grain'.",
                theExecution = listOf("Switch white bread for 100% Whole Wheat.", "Use brown rice instead of white rice for all meals.", "Add oats or barley to your breakfast routine."),
                theShield = "Physically sweeps potential cancer-causers out of your system.",
                switchThisForThat = "Switch white rice or white bread for brown rice, quinoa, or whole-grain bread.",
                source = "WCRF / AICR"
            ),
            NutritionIntelligence(
                id = "truth-rep-2",
                title = "Pulses & Legumes: Plant Protein",
                summary = "Beans, lentils, and chickpeas are high-standard protective fuels.",
                evidenceLevel = EvidenceStrength.HIGH,
                category = NutritionCategory.REPAIR,
                theTruth = "Legumes provide high fiber and phytochemicals that assist in cellular repair.",
                theCommand = "Include pulses (beans) in at least one meal every day.",
                theExecution = listOf("Add a cup of lentils to your soups or stews.", "Use chickpeas (garbanzo beans) as a base for salads.", "Replace half the meat in your recipes with beans."),
                theShield = "Provides the biological building blocks for DNA repair and maintenance.",
                source = "Agency Blueprint"
            ),
            NutritionIntelligence(
                id = "truth-rep-3",
                title = "Non-Starchy Veggies: Leafy Greens",
                summary = "Spinach, Kale, and Broccoli provide a total body shield.",
                evidenceLevel = EvidenceStrength.HIGH,
                category = NutritionCategory.REPAIR,
                theTruth = "These vegetables are rich in carotenoids and vitamins that neutralize free radicals.",
                theCommand = "Eat at least 400g (5 portions) of varied vegetables and fruits daily.",
                theExecution = listOf("Fill half your plate with leafy greens at lunch and dinner.", "Snack on raw carrots or peppers instead of chips.", "Try to eat the 'rainbow'—use different colors every day."),
                theShield = "Neutralizes the oxidative stress that leads to cell mutations.",
                source = "WHO / AICR"
            ),
            NutritionIntelligence(
                id = "truth-rep-4",
                title = "Garlic & Alliums: Stomach Defense",
                summary = "Onions, garlic, and leeks contain protective sulfur compounds.",
                evidenceLevel = EvidenceStrength.MODERATE,
                category = NutritionCategory.REPAIR,
                theTruth = "Sulfur compounds in garlic can inhibit the activation of carcinogens in the stomach.",
                theCommand = "Incorporate fresh garlic into your cooking 3+ times per week.",
                theExecution = listOf("Crush or chop garlic and let it sit for 10 minutes before cooking to activate the Allicin.", "Use leeks and onions as the flavor base for all home-cooked meals.", "Prioritize fresh garlic over processed garlic powders."),
                theShield = "Creates a chemical barrier in the stomach against food-borne carcinogens.",
                source = "AICR / Agency Intelligence"
            ),
            NutritionIntelligence(
                id = "truth-rep-5",
                title = "Coffee: Liver & Endometrial Protection",
                summary = "Daily coffee intake is linked to lower risk in specific organs.",
                evidenceLevel = EvidenceStrength.HIGH,
                category = NutritionCategory.REPAIR,
                theTruth = "Coffee contains phytochemicals that help the liver detoxify harmful substances.",
                theCommand = "Enjoy 1-3 cups of unsweetened coffee daily if you tolerate it.",
                theExecution = listOf("Drink it black or with minimal dairy; avoid sugary syrups.", "Filter your coffee to remove oils (cafestol) that can raise cholesterol.", "Stop drinking coffee by 2 PM to protect your sleep/repair cycle."),
                theShield = "Assists the liver's natural metabolic defense system.",
                source = "IARC / WCRF"
            ),
            NutritionIntelligence(
                id = "truth-rep-6",
                title = "Cruciferous Veggies: Sulforaphane Shield",
                summary = "Broccoli and Brussels sprouts boost detoxification enzymes.",
                evidenceLevel = EvidenceStrength.HIGH,
                category = NutritionCategory.REPAIR,
                theTruth = "Sulforaphane in these veggies triggers the body's natural defense against DNA damage.",
                theCommand = "Consume cruciferous vegetables 3+ times per week.",
                theExecution = listOf("Steam broccoli lightly (2-4 mins) to retain the maximum sulforaphane.", "Add cauliflower to your rice or mash.", "Roast Brussels sprouts with a small amount of olive oil."),
                theShield = "Upregulates your internal 'Detox Phase II' enzymes.",
                source = "Agency Biological Blueprint"
            ),
            NutritionIntelligence(
                id = "truth-rep-7",
                title = "Flaxseeds: The Lignan Layer",
                summary = "Tiny seeds that provide high fiber and protective lignans.",
                evidenceLevel = EvidenceStrength.MODERATE,
                category = NutritionCategory.REPAIR,
                theTruth = "Flaxseeds are the richest source of lignans, which can interfere with estrogen-driven tumor growth.",
                theCommand = "Add 1-2 tablespoons of ground flaxseeds to your daily intake.",
                theExecution = listOf("Mix ground flaxseeds into your oatmeal or yogurt.", "Add them to smoothies or home-baked whole-grain muffins.", "Always use ground flax; whole seeds often pass through the body undigested."),
                theShield = "Provides a dual layer of fiber and hormonal modulation.",
                source = "Agency Intelligence"
            )
        ))

        // --- THE PROTOCOL (Preparation) ---
        nutritionTruths.addAll(listOf(
            NutritionIntelligence(
                id = "truth-pro-1",
                title = "Charring Control: Grill Safety",
                summary = "Direct flame on protein creates mutagenic HCAs and PAHs.",
                evidenceLevel = EvidenceStrength.HIGH,
                category = NutritionCategory.PROTOCOL,
                theTruth = "Black char on meat is not 'flavor'—it is a concentrated high-energy carcinogen.",
                theCommand = "Avoid direct flame contact and black charring on all meats.",
                theExecution = listOf("Trim visible fat before grilling to reduce smoke and flare-ups.", "Use acidic marinades (lemon/vinegar) to reduce HCA formation by up to 90%.", "If food gets burned, cut off the black parts before eating.", "Pre-cook meat in a microwave for 2 minutes to reduce time on the high-heat grill."),
                theShield = "Reduces the ingestion of high-energy chemical mutagens.",
                source = "NCI / Agency Protocol"
            ),
            NutritionIntelligence(
                id = "truth-pro-2",
                title = "Acrylamide: Stop at Golden Yellow",
                summary = "Overheating starches (potatoes/bread) creates a probable carcinogen.",
                evidenceLevel = EvidenceStrength.HIGH,
                category = NutritionCategory.PROTOCOL,
                theTruth = "Acrylamide forms when starchy foods are cooked at high temps (frying/roasting) for too long.",
                theCommand = "Cook starches to a light golden yellow, never dark brown or black.",
                theExecution = listOf("Toast your bread to the lightest possible setting.", "Soak potato slices in water for 20 minutes before roasting to lower starch levels.", "Store potatoes in a cool, dark cupboard—NOT the fridge (cold increases sugar/acrylamide risk)."),
                theShield = "Reduces your systemic load of a known neurotoxin and DNA-damaging compound.",
                source = "EFSA / FDA"
            ),
            NutritionIntelligence(
                id = "truth-pro-3",
                title = "Plastic Leaching Protocol",
                summary = "Heat causes plastic to shed hormone disruptors into your food.",
                evidenceLevel = EvidenceStrength.HIGH,
                category = NutritionCategory.PROTOCOL,
                theTruth = "Even 'microwave-safe' plastic can release phthalates when heated. These interfere with hormone balance.",
                theCommand = "Never heat food or drinks in plastic containers.",
                theExecution = listOf("Transfer all leftovers to glass or ceramic before microwaving.", "Use a paper towel or glass lid to cover food instead of plastic wrap.", "Hand-wash plastic items; the high heat of a dishwasher speeds up leaching."),
                theShield = "Eliminates a primary route for endocrine-disrupting chemicals into your warm meals.",
                source = "Agency Consumer Safety"
            ),
            NutritionIntelligence(
                id = "truth-pro-4",
                title = "Aflatoxin Defense: Grain Storage",
                summary = "Mould on stored nuts and grains creates a potent liver toxin.",
                evidenceLevel = EvidenceStrength.VERY_HIGH,
                category = NutritionCategory.PROTOCOL,
                theTruth = "Aflatoxins are produced by fungi in warm, humid storage. It is one of the most potent naturally occurring carcinogens.",
                theCommand = "Ensure grains and nuts are stored in bone-dry, airtight conditions.",
                theExecution = listOf("Store flour, grains, and nuts in airtight glass or high-quality plastic jars.", "Visually inspect nuts for shriveling or discoloration before eating.", "Buy from reputable sources that follow standard drying protocols."),
                theShield = "Prevents chronic systemic poisoning of your liver tissue.",
                source = "WHO / IARC"
            )
        ))

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
                whileYouWait = listOf("Do NOT take Paracetamol, Tylenol, or Aspirer yet. The doctor needs to see the true fever signal.", "Drink a large glass of water to stay hydrated.", "Place your 'Red Treatment Folder' and all your pill bottles on the kitchen table."),
                handoffScript = "I am a cancer patient on active treatment. I am calling because I have a Fever/Shivers. I need immediate oncology triage.",
                theShield = "Prevents 'Neutropenic Sepsis,' a life-threatening infection that can be stopped if caught in the first hour."
            )
        )
        healingRepository.saveRedFlagDirectives(flags)
    }

    private suspend fun seedTruthCheckSignals() {
        val now = Instant.now()
        val signals = listOf(
            Signal(
                id = "truth-sugar-feeds",
                title = "Myth Check: Sugar Feeds Cancer",
                summary = "All cells use sugar, but eating sugar doesn't make cancer grow faster.",
                theTruth = "Every cell in your body uses glucose (sugar) for energy. While cancer cells use it faster, there is no evidence that eating sugar directly feeds tumors. However, too much sugar leads to obesity, which IS a cancer risk.",
                theCommand = "Manage sugar for weight control, not to 'starve' cancer.",
                theExecution = listOf(
                    "Limit sugary sodas and sweets to maintain a healthy weight.",
                    "Don't panic about natural sugars in fruit.",
                    "Focus on a balanced diet rather than extreme sugar-cutting."
                ),
                theShield = "Protects you from the stress of an impossible diet while keeping your weight in a safe range.",
                category = SignalCategory.RESEARCH,
                importance = SignalImportance.LOW,
                confidence = SignalConfidence.VERY_HIGH,
                detectedAt = now,
                publishedAt = now,
                source = SignalSource("Mayo Clinic / Agency Truth Check", ""),
                verdict = EvidenceVerdict.MISLEADING,
                investigatedClaim = "Sugar feeds cancer and should be zeroed.",
                interactionContexts = listOf("Home")
            ),
            Signal(
                id = "truth-alkaline-diet",
                title = "Myth Check: Alkaline Diet Cures Cancer",
                summary = "Your body's pH is tightly controlled and cannot be changed by what you eat.",
                theTruth = "Cancer cannot survive in an alkaline lab dish, but your blood pH is strictly kept at 7.4 by your lungs and kidneys. Eating alkaline foods (like lemons or greens) won't change your body's internal chemistry.",
                theCommand = "Eat greens for their nutrients, not to change your pH.",
                theExecution = listOf(
                    "Ignore claims that 'cancer cannot live in an alkaline body'.",
                    "Maintain a balanced diet rich in varied vegetables.",
                    "Don't waste money on expensive alkaline water machines."
                ),
                theShield = "Protects you from predatory 'cure' scams and biologically useless devices.",
                category = SignalCategory.RESEARCH,
                importance = SignalImportance.MODERATE,
                confidence = SignalConfidence.VERY_HIGH,
                detectedAt = now,
                publishedAt = now,
                source = SignalSource("American Cancer Society / Agency Truth Check", ""),
                verdict = EvidenceVerdict.NOT_SUPPORTED,
                investigatedClaim = "An alkaline diet can cure or treat cancer.",
                interactionContexts = listOf("Home")
            ),
            Signal(
                id = "truth-cell-phones",
                title = "Myth Check: Cell Phones and 5G",
                summary = "Phone waves are non-ionizing and too weak to damage your DNA.",
                theTruth = "Cell phones and 5G networks use radiofrequency (RF) waves. Unlike X-rays, these are 'non-ionizing'—they don't have enough energy to break DNA strands or cause cancer.",
                theCommand = "You are safe to use your devices as normal.",
                theExecution = listOf(
                    "Ignore viral posts about '5G radiation' causing brain tumors.",
                    "If you want extra comfort, use a speakerphone or wired headset.",
                    "Don't waste money on 'EMF protection' stickers."
                ),
                theShield = "Protects you from unnecessary radiation anxiety and financial scams.",
                category = SignalCategory.RESEARCH,
                importance = SignalImportance.LOW,
                confidence = SignalConfidence.VERY_HIGH,
                detectedAt = now,
                publishedAt = now,
                source = SignalSource("WHO / Agency Truth Check", ""),
                verdict = EvidenceVerdict.NOT_SUPPORTED,
                investigatedClaim = "Cell phones and 5G networks cause brain tumors.",
                interactionContexts = listOf("Public", "Work")
            ),
            Signal(
                id = "truth-biopsy-spread",
                title = "Myth Check: Biopsies Spread Cancer",
                summary = "Medical procedures follow strict rules to prevent cancer from moving.",
                theTruth = "It is extremely rare for a biopsy or surgery to cause cancer to spread. Doctors use special techniques and tools to ensure any cancer cells stay contained during the test.",
                theCommand = "Do not skip your biopsy; it is the only way to get the right treatment.",
                theExecution = listOf(
                    "Trust that your surgical team is trained to prevent 'seeding'.",
                    "Understand that finding cancer during surgery usually means it was already there.",
                    "Get the diagnostic test your doctor recommends immediately."
                ),
                theShield = "Ensures you get an accurate diagnosis in time to save your life.",
                category = SignalCategory.RESEARCH,
                importance = SignalImportance.HIGH,
                confidence = SignalConfidence.VERY_HIGH,
                detectedAt = now,
                publishedAt = now,
                source = SignalSource("NCI / Agency Truth Check", ""),
                verdict = EvidenceVerdict.NOT_SUPPORTED,
                investigatedClaim = "Biopsies or surgery cause cancer to spread.",
                interactionContexts = listOf("Public")
            ),
            Signal(
                id = "truth-big-pharma",
                title = "Myth Check: Hidden Cancer Cures",
                summary = "There is no single 'secret cure' being withheld by companies.",
                theTruth = "Cancer is not one disease; it is hundreds of different types. Developing treatments is incredibly complex, and there is more profit in a cure than in temporary treatments.",
                theCommand = "Be wary of anyone claiming to have a 'secret cure' doctors won't tell you about.",
                theExecution = listOf(
                    "Look for clinical trial evidence for any treatment claim.",
                    "Ask: 'If there was a cure, why would researchers' own families still die of cancer?'",
                    "Stick to treatments verified by global medical communities."
                ),
                theShield = "Protects you from delaying life-saving care while chasing fraudulent promises.",
                category = SignalCategory.RESEARCH,
                importance = SignalImportance.MODERATE,
                confidence = SignalConfidence.HIGH,
                detectedAt = now,
                publishedAt = now,
                source = SignalSource("Agency Intelligence Foundation", ""),
                verdict = EvidenceVerdict.NOT_SUPPORTED,
                investigatedClaim = "A simple cancer cure exists but is being hidden for profit. Big Pharma Cures.",
                interactionContexts = listOf("Home")
            ),
            Signal(
                id = "truth-antiperspirant",
                title = "Myth Check: Deodorant and Breast Cancer",
                summary = "No scientific evidence links aluminum in deodorant to tumors.",
                theTruth = "Many people fear that aluminum or parabens in antiperspirants are absorbed and cause cancer. However, large studies have found no consistent link between these products and breast cancer.",
                theCommand = "Use your preferred deodorant with confidence.",
                theExecution = listOf(
                    "Switch to aluminum-free versions only if you have a skin sensitivity.",
                    "Ignore viral emails about 'sweating out toxins'.",
                    "Focus on known breast cancer risks like exercise and alcohol reduction."
                ),
                theShield = "Reduces unnecessary daily worry about a common personal care item.",
                category = SignalCategory.RESEARCH,
                importance = SignalImportance.LOW,
                confidence = SignalConfidence.HIGH,
                detectedAt = now,
                publishedAt = now,
                source = SignalSource("National Cancer Institute", ""),
                verdict = EvidenceVerdict.NOT_SUPPORTED,
                investigatedClaim = "Aluminum in antiperspirants causes breast cancer. Deodorant risk.",
                interactionContexts = listOf("Home")
            ),
            Signal(
                id = "truth-microwaves",
                title = "Myth Check: Microwave Radiation",
                summary = "Microwaves do not make food radioactive or cause cancer.",
                theTruth = "Microwaves use non-ionizing radiation to vibrate water molecules in food, creating heat. This is not the same as the ionizing radiation from X-rays that damages DNA. Your food does not become 'radioactive'.",
                theCommand = "Use your microwave for heating; use glass containers for safety.",
                theExecution = listOf(
                    "Ignore claims that microwaving kills 'life energy' in food.",
                    "Always use microwave-safe glass or ceramic, not plastic.",
                    "Ensure the door seal on your microwave is clean and tight."
                ),
                theShield = "Protects you from appliance-fear while ensuring you avoid plastic chemical leaching.",
                category = SignalCategory.RESEARCH,
                importance = SignalImportance.LOW,
                confidence = SignalConfidence.VERY_HIGH,
                detectedAt = now,
                publishedAt = now,
                source = SignalSource("FDA / Agency Truth Check", ""),
                verdict = EvidenceVerdict.NOT_SUPPORTED,
                investigatedClaim = "Microwaves cause cancer and make food toxic.",
                interactionContexts = listOf("Home")
            ),
            Signal(
                id = "truth-contagious",
                title = "Myth Check: Is Cancer Contagious?",
                summary = "You cannot catch cancer like a cold or flu.",
                theTruth = "Cancer is not a contagious disease. You cannot 'catch' it from someone else. However, some viruses (like HPV or Hepatitis) are contagious and can increase cancer risk years later.",
                theCommand = "Support loved ones with cancer without fear of catching it.",
                theExecution = listOf(
                    "Feel safe to hug and spend time with cancer patients.",
                    "Focus on vaccinations (like HPV) to prevent the viruses that can lead to cancer.",
                    "Practice standard hygiene for overall health, not because cancer is catching."
                ),
                theShield = "Protects your relationships and mental health from unnecessary isolation.",
                category = SignalCategory.RESEARCH,
                importance = SignalImportance.LOW,
                confidence = SignalConfidence.VERY_HIGH,
                detectedAt = now,
                publishedAt = now,
                source = SignalSource("Mayo Clinic", ""),
                verdict = EvidenceVerdict.NOT_SUPPORTED,
                investigatedClaim = "Cancer is contagious and can be caught from others.",
                interactionContexts = listOf("Public")
            ),
            Signal(
                id = "truth-positive-thinking",
                title = "Myth Check: Positive Thinking Cures",
                summary = "Attitude is great for quality of life, but it doesn't shrink tumors.",
                theTruth = "While a positive outlook helps you cope with treatment stress, there is no scientific evidence that 'thinking positive' can cure cancer on its own. This myth often places an unfair emotional burden on patients.",
                theCommand = "Allow yourself to feel all emotions; do not feel guilty for being sad.",
                theExecution = listOf(
                    "Use positive thinking as a tool for mental health, not a medical cure.",
                    "Seek support groups or therapy if you feel overwhelmed.",
                    "Focus on your medical protocol as the primary path to physical healing."
                ),
                theShield = "Protects you from emotional exhaustion and the 'blaming the victim' trap.",
                category = SignalCategory.RESEARCH,
                importance = SignalImportance.LOW,
                confidence = SignalConfidence.HIGH,
                detectedAt = now,
                publishedAt = now,
                source = SignalSource("Cancer Research UK", ""),
                verdict = EvidenceVerdict.MISLEADING,
                investigatedClaim = "A positive attitude can cure or treat cancer.",
                interactionContexts = listOf("Home")
            ),
            Signal(
                id = "truth-alternative-cures",
                title = "Myth Check: Alternative Cures",
                summary = "Cannabis oil, Ivermectin, and massive Vitamin C are not cures.",
                theTruth = "While some natural products help with side effects (like ginger for nausea), they cannot shrink tumors or replace medical treatment. Patients who choose alternative medicine *instead* of conventional care have a much higher risk of death.",
                theCommand = "Use natural products only for comfort; never as a replacement for treatment.",
                theExecution = listOf(
                    "Always tell your oncologist about any supplements you are taking.",
                    "Be wary of expensive 'natural' clinics that promise 100% success.",
                    "Verify any 'unheard of' cure with the Agency before investing money."
                ),
                theShield = "Protects you from predatory financial exploitation and from the danger of stopping proven care.",
                category = SignalCategory.RESEARCH,
                importance = SignalImportance.CRITICAL,
                confidence = SignalConfidence.VERY_HIGH,
                detectedAt = now,
                publishedAt = now,
                source = SignalSource("Huntsman Cancer Institute / Agency Truth Check", ""),
                verdict = EvidenceVerdict.MISLEADING,
                investigatedClaim = "Alternative cures like Cannabis oil or Ivermectin are better than chemo. Detox.",
                interactionContexts = listOf("Home", "Public")
            )
        )
        signals.forEach { memory.saveSignal(it); seedThread(it) }
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
