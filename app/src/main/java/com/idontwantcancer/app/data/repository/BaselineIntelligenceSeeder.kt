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
 * This is the Elite Integrity Registry: 200+ High-Standard Directives.
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
            RegistrySeed("Parabens", listOf("Methylparaben", "Propylparaben", "Butylparaben", "Ethylparaben", "Isobutylparaben", "E216", "E218"), "Endocrine Disruptors common in lotions and makeup. They mimic estrogen and can promote breast cancer cell growth.", listOf("Lotion", "Moisturizer", "Makeup", "Preservative", "Bathroom")),
            RegistrySeed("Formaldehyde Releasers", listOf("DMDM Hydantoin", "Quaternium-15", "Imidazolidinyl Urea", "Diazolidinyl Urea", "2-bromo-2-nitropropane-1,3-diol", "Bronopol"), "Preservatives that slowly release known human carcinogens into your skin and air.", listOf("Shampoo", "Body Wash", "Conditioner", "Bathroom")),
            RegistrySeed("Phthalates", listOf("DEP", "DBP", "DEHP", "Phthalate", "Fragrance", "Parfum", "Synthetic Musk"), "Hormone disruptors that make scents last longer. Linked to reproductive issues and hormone-sensitive cancers.", listOf("Perfume", "Cologne", "Fragrance", "Scent")),
            RegistrySeed("Ethanolamines", listOf("DEA", "TEA", "MEA", "Diethanolamine", "Triethanolamine", "Cocamide DEA", "Lauramide DEA"), "Chemicals used as sudsing agents. Can react with other ingredients to form Nitrosamines, which are powerful carcinogens.", listOf("Soap", "Sudsing", "Foam", "Detergent")),
            RegistrySeed("Coal Tar", listOf("Coal Tar", "CI 77266", "Carbon Black", "P-phenylenediamine", "PPD", "Aminophenol"), "Used in dandruff shampoos and hair dyes. A known human carcinogen (Group 1).", listOf("Hair Dye", "Dandruff", "Scalp", "Black Dye")),
            RegistrySeed("Synthetic Musks", listOf("Galaxolide", "Tonalide", "HHCB", "AHTN", "Musk Xylene"), "Highly persistent chemicals used in perfumes. They build up in human fat tissue and disrupt hormones.", listOf("Perfume", "Laundry", "Dryer Sheet")),
            RegistrySeed("Chemical UV Filters", listOf("Oxybenzone", "Octinoxate", "Benzophenone-3", "Avobenzone", "Homosalate", "Octocrylene"), "Sunscreen chemicals that absorb into the body at high rates and disrupt hormonal balance.", listOf("Sunscreen", "SPF", "Sunblock", "Beach")),
            RegistrySeed("PFAS (Forever Chemicals)", listOf("PTFE", "Perfluoro", "Polyperfluoromethylisopropyl Ether", "Teflon", "Fluorine"), "Used in waterproof mascara and long-wear foundations. Linked to kidney cancer and immune suppression.", listOf("Waterproof", "Makeup", "Mascara", "Eyeliner")),
            RegistrySeed("Lead Acetate", listOf("Lead Acetate"), "Found in some progressive hair dyes. Lead is a neurotoxin and suspected carcinogen.", listOf("Hair Dye", "Metal", "Neurotoxin")),
            RegistrySeed("Talcum Powder", listOf("Talc", "Hydrous Magnesium Silicate", "Magnesium Silicate"), "Can be naturally contaminated with asbestos. Linked to ovarian and lung cancer.", listOf("Body Powder", "Baby Powder", "Asbestos", "Dust")),
            RegistrySeed("Resorcinol", listOf("Resorcinol", "1,3-benzenediol"), "Found in hair dyes and acne treatments. Linked to thyroid disruption and immune system issues.", listOf("Hair Dye", "Acne", "Skin Treatment")),
            RegistrySeed("Carbon Black", listOf("Carbon Black", "D&C Black No. 2", "Acetylene Black"), "Used in eyeliners and mascaras. Linked to cancer and organ system toxicity.", listOf("Mascara", "Eyeliner", "Black Pigment"))
        )

        data.forEach { seed ->
            saveSignal(
                id = "cos-${seed.name.lowercase().replace(" ", "-")}",
                title = "${seed.name}: Label Check",
                summary = "Dangerous chemicals found in cosmetics and personal care products.",
                theTruth = seed.truth,
                theCommand = "Switch to products explicitly labeled '${seed.name}-Free'.",
                theExecution = listOf(
                    "Scan the ingredient list for: ${seed.ingredients.joinToString(", ")}.",
                    "If any match, do not purchase the product.",
                    "Choose 'Clean Beauty' brands that provide a 'Red List' of banned chemicals."
                ),
                theShield = "Protects your skin and endocrine system from cumulative chemical absorption.",
                category = SignalCategory.COSMETICS,
                importance = SignalImportance.HIGH,
                confidence = SignalConfidence.VERY_HIGH,
                affectedIngredients = seed.ingredients,
                safeAlternatives = listOf("Natural Extracts", "Mineral UV Filters", "Beeswax", "Tocopherol"),
                interactionContexts = listOf("Store", "Home"),
                discoveryTags = seed.tags
            )
        }
    }

    private suspend fun seedHouseholdRegistry() {
        val data = listOf(
            RegistrySeed("Triclosan", listOf("Triclosan", "Triclocarban"), "Antibacterial agent that disrupts thyroid hormones and contributes to antibiotic resistance.", listOf("Antibacterial", "Soap", "Toothpaste", "Kitchen")),
            RegistrySeed("1,4-Dioxane", listOf("PEG", "Polyethylene Glycol", "Sodium Laureth Sulfate", "SLES", "Ceteareth", "Polysorbate"), "A probable carcinogen that is a manufacturing byproduct. Found in sudsing products like dish soap and laundry detergent.", listOf("Laundry", "Dish Soap", "Sudsing", "Foam")),
            RegistrySeed("Quats", listOf("Benzalkonium Chloride", "Distearyldimonium Chloride", "Quaternium-18"), "Quaternary Ammonium Compounds used as disinfectants and fabric softeners. Potent lung irritants and endocrine disruptors.", listOf("Fabric Softener", "Disinfectant", "Wipes", "Laundry")),
            RegistrySeed("Ammonia", listOf("Ammonia", "Ammonium Hydroxide"), "Found in window cleaners. Can react with bleach to create deadly Mustard Gas. Chronic exposure is hard on the lungs.", listOf("Window Cleaner", "Glass", "Ammonia", "Janitorial")),
            RegistrySeed("2-Butoxyethanol", listOf("2-Butoxyethanol", "Ethylene Glycol Monobutyl Ether"), "Found in multipurpose and glass cleaners. A known organ toxin linked to blood disorders and potential cancer.", listOf("Multipurpose", "Spray", "Cleaner", "Solvent"))
        )

        data.forEach { seed ->
            saveSignal(
                id = "clean-${seed.name.lowercase().replace(" ", "-")}",
                title = "${seed.name}: Household Registry",
                summary = "Cleaning chemicals that linger in your home's air and surfaces.",
                theTruth = seed.truth,
                theCommand = "Switch to fragrance-free, plant-based cleaning agents.",
                theExecution = listOf(
                    "Identify '${seed.name}' or synonyms: ${seed.ingredients.joinToString(", ")}.",
                    "Ask for digital receipts or hold paper ones by the edges (Receipts contain BPA).",
                    "Use white vinegar and water as a safe, all-purpose alternative."
                ),
                theShield = "Ensures your home sanctuary is free from volatile industrial solvents.",
                category = SignalCategory.CLEANING,
                importance = SignalImportance.MODERATE,
                confidence = SignalConfidence.HIGH,
                affectedIngredients = seed.ingredients,
                safeAlternatives = listOf("White Vinegar", "Castile Soap", "Baking Soda", "Essential Oils"),
                interactionContexts = listOf("Home", "Store"),
                discoveryTags = seed.tags
            )
        }
    }

    private suspend fun seedFoodAdditiveRegistry() {
        val data = listOf(
            RegistrySeed("Sodium Nitrite", listOf("E250", "Sodium Nitrite", "Curing Salt", "Pink Salt", "Prague Powder", "Nitrate"), "Used in bacon and ham. Forms DNA-damaging Nitrosamines in your stomach.", listOf("Bacon", "Ham", "Deli Meat", "Pink Meat")),
            RegistrySeed("Titanium Dioxide", listOf("E171", "Titanium Dioxide", "CI 77891", "Pigment White 6"), "Whitening pigment in candies and pills. Contains nanoparticles that can damage the gut barrier.", listOf("Candy", "Chewing Gum", "Pill Coating", "White Food")),
            RegistrySeed("Potassium Bromate", listOf("E924", "Potassium Bromate", "Bromated Flour", "Enriched Bromated Flour"), "Flour improver that is a known carcinogen. Banned in most countries except the USA.", listOf("Bread", "Flour", "Bakery", "White Bread")),
            RegistrySeed("BHA & BHT", listOf("E320", "E321", "Butylated Hydroxyanisole", "Butylated Hydroxytoluene", "Antioxidant 320"), "Preservatives used in oils and cereals. Linked to hormonal disruption and cancer.", listOf("Cereal", "Oils", "Chips", "Preservative")),
            RegistrySeed("Azo Dyes", listOf("Red 40", "Yellow 5", "Yellow 6", "E129", "E102", "E110", "Red 3", "E127", "Tartrazine", "Allura Red"), "Synthetic food colors derived from petroleum. Suspected carcinogens.", listOf("Coloring", "Cereal", "Candy", "Soda")),
            RegistrySeed("Propyl Gallate", listOf("E310", "Propyl Gallate"), "Antioxidant used to prevent oils from going rancid. Suspected endocrine disruptor.", listOf("Oils", "Mayonnaise", "Lard", "Preservative")),
            RegistrySeed("Carrageenan", listOf("E407", "Carrageenan", "Irish Moss Extract"), "Thickener found in dairy and plant milks. Can cause intense gut inflammation, a precursor to cancer.", listOf("Milk", "Yogurt", "Dairy Free", "Thickener")),
            RegistrySeed("TBHQ", listOf("Tertiary Butylhydroquinone", "E319", "Antioxidant 319"), "Preservative in crackers and frozen foods. Linked to immune system damage.", listOf("Crackers", "Frozen Food", "Chicken Nuggets", "Preservative")),
            RegistrySeed("Potassium Iodate", listOf("E917", "Potassium Iodate"), "Flour treatment agent. Linked to thyroid dysfunction and potential carcinogenic effects.", listOf("Bread", "Bakery", "Flour")),
            RegistrySeed("Aspartame", listOf("E951", "Aspartame", "Equal", "NutraSweet"), "Artificial sweetener classified as 'possibly carcinogenic' by IARC (2B).", listOf("Diet Soda", "Sugar Free", "Sweetener")),
            RegistrySeed("Acesulfame K", listOf("E950", "Acesulfame Potassium", "Ace-K"), "Artificial sweetener. Some studies suggest potential for thyroid disruption and cancer.", listOf("Diet Soda", "Sugar Free", "Sweetener"))
        )

        data.forEach { seed ->
            saveSignal(
                id = "food-add-${seed.name.lowercase().replace(" ", "-")}",
                title = "${seed.name}: Food Registry",
                summary = "Industrial chemicals used to improve shelf-life and appearance of food.",
                theTruth = seed.truth,
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
                affectedIngredients = seed.ingredients,
                safeAlternatives = listOf("Fresh Meat", "Natural Colors (Turmeric, Beet)", "Honey", "Sea Salt"),
                interactionContexts = listOf("Store", "Home"),
                discoveryTags = seed.tags
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
                interactionContexts = listOf("Home"),
                discoveryTags = listOf("Mold", "Humidity", "Basement", "Bathroom")
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
                interactionContexts = listOf("Home", "Public"),
                discoveryTags = listOf("Pesticide", "Garden", "Lawn", "Glyphosate")
            ),
            Signal(
                id = "env-house-dust",
                title = "Household Dust: Flame Retardants",
                summary = "Dust acts as a 'sink' for chemicals shed from furniture.",
                theTruth = "Flame retardants and PFAS shed from electronics and couches settle in house dust. These are endocrine disruptors linked to multiple cancers.",
                theCommand = "Use a damp cloth for all surface cleaning; avoid dry dusting.",
                theExecution = listOf(
                    "Wipe hard surfaces with a wet microfiber rag to trap particles.",
                    "Vacuum with a certified HEPA-filter machine at least once per week.",
                    "Remove shoes at the door to stop tracking in outdoor lead and pesticides."
                ),
                theShield = "Stops the inhalation and accidental hand-to-mouth ingestion of toxic industrial chemicals.",
                category = SignalCategory.ENVIRONMENT,
                importance = SignalImportance.MODERATE,
                confidence = SignalConfidence.VERY_HIGH,
                detectedAt = now,
                publishedAt = now,
                source = SignalSource("Environmental Health Perspectives", ""),
                interactionContexts = listOf("Home"),
                discoveryTags = listOf("Dust", "Furniture", "Electronics", "Cleaning")
            ),
            Signal(
                id = "env-radon",
                title = "Basement Watch: Radon Gas",
                summary = "The #1 cause of lung cancer in non-smokers.",
                theTruth = "Radon is an invisible, odorless radioactive gas that seeps from the soil into homes. Long-term exposure damages lung DNA.",
                theCommand = "Test your home's lowest living level for Radon every 2 years.",
                theExecution = listOf(
                    "Buy a $15-25 short-term radon test kit at a hardware store.",
                    "Place it in your basement or ground-floor room for 48-96 hours.",
                    "Mail the kit to a lab for results. If levels are high (>4 pCi/L), install a mitigation system."
                ),
                theShield = "Detection and simple ventilation fix virtually eliminates this specific lung cancer risk.",
                category = SignalCategory.ENVIRONMENT,
                importance = SignalImportance.HIGH,
                confidence = SignalConfidence.VERY_HIGH,
                detectedAt = now,
                publishedAt = now,
                source = SignalSource("EPA / Agency Intelligence", ""),
                interactionContexts = listOf("Home"),
                discoveryTags = listOf("Radon", "Basement", "Air Quality", "Radioactive")
            ),
            Signal(
                id = "env-cooking-smoke",
                title = "Kitchen Watch: Cooking Smog",
                summary = "High-heat frying and gas stoves create indoor air pollution.",
                theTruth = "Burning gas releases Nitrogen Dioxide, and high-heat oils create particulate matter (PM2.5) that irritates and damages lung tissue.",
                theCommand = "Always turn on your exhaust fan *before* you light the stove.",
                theExecution = listOf(
                    "Start the fan 1 minute before cooking to create a draft.",
                    "Open a window if you don't have an external-venting hood.",
                    "Prefer back burners as they are more effectively captured by hood fans."
                ),
                theShield = "Protects your lungs from concentrated indoor smog that can be 5x worse than outdoor air.",
                category = SignalCategory.ENVIRONMENT,
                importance = SignalImportance.HIGH,
                confidence = SignalConfidence.VERY_HIGH,
                detectedAt = now,
                publishedAt = now,
                source = SignalSource("WHO / Indoor Air Quality", ""),
                interactionContexts = listOf("Home"),
                discoveryTags = listOf("Kitchen", "Smoke", "Gas Stove", "Frying")
            ),
            Signal(
                id = "env-new-furnishings",
                title = "Home Watch: New Product Off-Gassing",
                summary = "New furniture and carpets release industrial glues for weeks.",
                theTruth = "Volatile Organic Compounds (VOCs) like formaldehyde are used in finishes. These 'off-gas' at high rates when an item is brand new.",
                theCommand = "Ventilate any new furniture or carpet for at least 72 hours.",
                theExecution = listOf(
                    "Unbox new items in a garage or outdoors for the first 3 days if possible.",
                    "Keep windows in the affected room open and use a fan to push air outward.",
                    "Choose 'Low-VOC' or 'Formaldehyde-Free' certified items when buying new."
                ),
                theShield = "Prevents the high-concentrate inhalation of industrial solvents in your safe space.",
                category = SignalCategory.ENVIRONMENT,
                importance = SignalImportance.MODERATE,
                confidence = SignalConfidence.HIGH,
                detectedAt = now,
                publishedAt = now,
                source = SignalSource("Agency Indoor Watch", ""),
                interactionContexts = listOf("Home", "Work"),
                discoveryTags = listOf("Furniture", "Carpet", "VOC", "Formaldehyde")
            ),
            Signal(
                id = "env-plastic-leach",
                title = "Kitchen Watch: Plastic Heat Leaching",
                summary = "Heating plastic releases hormone disruptors into your food.",
                theTruth = "Heat breaks the polymer chains in plastic, releasing phthalates and BPA even from 'microwave-safe' containers.",
                theCommand = "Never heat food in plastic; transfer to glass or ceramic.",
                theExecution = listOf(
                    "Transfer all leftovers to a glass bowl before microwaving.",
                    "Never put hot liquids into plastic cups or containers.",
                    "Discard plastic items that are scratched or stained, as they leach more easily."
                ),
                theShield = "Eliminates a primary route for hormone-disrupting chemicals into your bloodstream.",
                category = SignalCategory.ENVIRONMENT,
                importance = SignalImportance.HIGH,
                confidence = SignalConfidence.VERY_HIGH,
                detectedAt = now,
                publishedAt = now,
                source = SignalSource("Consumer Safety Lab", ""),
                interactionContexts = listOf("Home"),
                discoveryTags = listOf("Plastic", "Microwave", "Kitchen", "BPA", "Phthalate")
            ),
            Signal(
                id = "env-dry-cleaning",
                title = "Wardrobe Watch: Dry Cleaning Chemicals",
                summary = "Dry-cleaned clothes often carry toxic 'PERC' residues.",
                theTruth = "Perchloroethylene (PERC) is a solvent used by many dry cleaners and is a known carcinogen.",
                theCommand = "Air out dry-cleaned clothes outdoors for 24 hours.",
                theExecution = listOf(
                    "Remove the plastic bag immediately upon bringing clothes home.",
                    "Hang items in a well-ventilated area or outdoors before putting them in your closet.",
                    "Choose 'Wet Cleaning' or 'CO2 Cleaning' services which are toxin-free."
                ),
                theShield = "Prevents the build-up of industrial solvent vapors in your bedroom and closet.",
                category = SignalCategory.ENVIRONMENT,
                importance = SignalImportance.MODERATE,
                confidence = SignalConfidence.HIGH,
                detectedAt = now,
                publishedAt = now,
                source = SignalSource("Agency Wardrobe Sentinel", ""),
                interactionContexts = listOf("Home"),
                discoveryTags = listOf("Dry Cleaning", "Wardrobe", "Closet", "PERC")
            ),
            Signal(
                id = "env-non-stick",
                title = "Cookware Watch: Overheated PTFE",
                summary = "Non-stick pans release 'Teflon Flu' gases when overheated.",
                theTruth = "PTFE coatings begin to break down at high temperatures, releasing toxic fumes that can cause flu-like symptoms and damage DNA.",
                theCommand = "Never pre-heat an empty non-stick pan; use Cast Iron instead.",
                theExecution = listOf(
                    "Switch to stainless steel or cast iron for high-heat searing.",
                    "If using non-stick, always have food or oil in the pan while heating.",
                    "Discard non-stick pans immediately if the surface is scratched or peeling."
                ),
                theShield = "Stops the inhalation of fluorinated gases in your kitchen.",
                category = SignalCategory.ENVIRONMENT,
                importance = SignalImportance.HIGH,
                confidence = SignalConfidence.VERY_HIGH,
                detectedAt = now,
                publishedAt = now,
                source = SignalSource("Agency Kitchen Protocol", ""),
                interactionContexts = listOf("Home"),
                discoveryTags = listOf("Kitchen", "Teflon", "PTFE", "Cookware", "Pan")
            ),
            Signal(
                id = "env-pesticide-shoes",
                title = "Threshold Watch: Tracking in Toxins",
                summary = "Shoes carry outdoor pesticides and lead into your home.",
                theTruth = "Street dust contains lead, coal tar, and lawn pesticides. These settle into carpets where children and pets are exposed.",
                theCommand = "Implement a strict 'No-Shoes' policy inside your home.",
                theExecution = listOf(
                    "Leave all outdoor footwear at the entry door.",
                    "Use indoor slippers or socks that never touch the pavement.",
                    "Clean your entry mat weekly to prevent 'dust-drag' into the house."
                ),
                theShield = "Reduces the cumulative outdoor toxic load brought into your living space by 60%.",
                category = SignalCategory.ENVIRONMENT,
                importance = SignalImportance.MODERATE,
                confidence = SignalConfidence.VERY_HIGH,
                detectedAt = now,
                publishedAt = now,
                source = SignalSource("Environmental Health Lab", ""),
                interactionContexts = listOf("Home"),
                discoveryTags = listOf("Shoes", "Threshold", "Pesticide", "Lead", "Floor")
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
                interactionContexts = listOf("Home", "Store"),
                discoveryTags = listOf("Aerosol", "Sunscreen", "Dry Shampoo", "Benzene", "Recall")
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
        interactionContexts: List<String>,
        discoveryTags: List<String> = emptyList(),
        investigatedClaim: String? = null
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
            interactionContexts = interactionContexts,
            discoveryTags = discoveryTags,
            verdict = if (importance == SignalImportance.CRITICAL) EvidenceVerdict.MISLEADING else EvidenceVerdict.NOT_SUPPORTED,
            investigatedClaim = investigatedClaim
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
        val data = listOf(
            EducationLesson(
                id = "edu-risk-vs-hazard",
                title = "Strategy: Hazard vs. Risk",
                summary = "Learn why some carcinogens matter more than others.",
                content = "A 'Hazard' is something that can cause harm. 'Risk' is the chance it *will* harm you. Risk = Hazard x Exposure. You are safe near a hazard if you reduce your exposure time.",
                keyTakeaway = "Don't panic about every chemical. Focus on the ones you touch every single day.",
                source = "Agency Strategic Briefing"
            ),
            EducationLesson(
                id = "edu-dose-response",
                title = "Strategy: The Dose-Response Rule",
                summary = "Why frequency is the most important factor in your safety.",
                content = "Your body can repair minor DNA damage from one-off exposures. Cancer risk becomes high when you are exposed to the same carcinogen daily (e.g., in your soap or daily meat).",
                keyTakeaway = "Prioritize removing daily toxins over rare special-occasion treats.",
                source = "Agency Strategic Briefing"
            ),
            EducationLesson(
                id = "edu-detox-phase-2",
                title = "Masterclass: Real Biological Detox",
                summary = "How your body actually cleans itself without 'teas' or 'cleanses'.",
                content = "Your liver uses 'Phase II Detox' enzymes (GST, SULT) to make toxins water-soluble so they can leave your body. Cruciferous veggies (Broccoli) trigger these enzymes.",
                keyTakeaway = "Eat broccoli and cabbage to 'turn on' your body's natural cleaning system.",
                source = "Agency Strategic Briefing"
            ),
            EducationLesson(
                id = "edu-insulin-tumor",
                title = "Masterclass: Tumor Fuel (Insulin)",
                summary = "The link between liquid sugar and cancer growth.",
                content = "Liquid sugar causes a spike in Insulin and IGF-1. These are growth hormones. High levels can act like 'fertilizer' for tiny tumors that your immune system hasn't caught yet.",
                keyTakeaway = "Eliminate sugary drinks to lower your body's growth-hormone levels.",
                source = "Agency Strategic Briefing"
            ),
            EducationLesson(
                id = "edu-label-scan-pro",
                title = "Masterclass: 5-Second Label Scan",
                summary = "A professional protocol for reading ingredients.",
                content = "Don't read the whole label. Look for the top 3 ingredients first (volume) and then skip to the very end for preservatives (Parabens) and colors (Azo Dyes).",
                keyTakeaway = "The end of the label is often where the highest risks are hidden.",
                source = "Agency Strategic Briefing"
            ),
            EducationLesson(
                id = "edu-dna-repair",
                title = "Masterclass: DNA Repair Kit",
                summary = "The nutrients that act as 'mechanics' for your cells.",
                content = "Folate (greens) and Zinc (seeds) are essential for the enzymes that repair broken DNA strands. Without them, cell mutations can go uncorrected and lead to cancer.",
                keyTakeaway = "Greens and seeds are not just 'healthy'—they are the literal repair tools for your DNA.",
                source = "Agency Strategic Briefing"
            )
        )
        preventionRepository.saveEducationLessons(data)
    }

    private suspend fun seedPreventionActions() {
        val data = listOf(
            Triple("Avoid Tobacco", "The most significant avoidable risk factor for cancer.", "smoke_free"),
            Triple("Protect Skin from UV", "Use SPF 30+ daily and seek shade between 10 AM - 4 PM.", "sunny"),
            Triple("Limit Alcohol", "Reducing intake lowers risk for 7 types of cancer.", "no_drinks"),
            Triple("Move Your Body", "Aim for 30 minutes of moderate activity (like brisk walking) daily.", "directions_run"),
            Triple("Whole Grains First", "Choose brown rice and oats over refined white starches.", "grass"),
            Triple("Limit Red Meat", "Keep weekly intake below 500g (cooked weight).", "restaurant"),
            Triple("Healthy Weight", "Maintain a stable BMI through diet and movement.", "monitor_weight"),
            Triple("Zero Sugary Drinks", "Eliminate liquid sugar to normalize insulin signals.", "water_drop"),
            Triple("Screening Checklist", "Complete age-appropriate cancer checkups on time.", "event_available"),
            Triple("Radon Safety", "Verify your home's air quality every 2 years.", "air")
        )

        val actions = data.map { (title, desc, icon) ->
            PreventionAction(
                id = "action-${title.lowercase().replace(" ", "-")}",
                title = title,
                description = desc,
                iconName = icon,
                isAdopted = false
            )
        }
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
            ),
            TreatmentManual(
                id = "treat-mouth-sores",
                title = "Mouth Health: Salt & Soda Protocol",
                summary = "Prevent painful mouth sores during chemo or radiation.",
                category = TreatmentCategory.DAILY_HYGIENE,
                theTruth = "Treatment can cause the lining of your mouth to break down. Salt and Soda neutralizes acid and keeps the area clean.",
                theCommand = "Rinse your mouth every 2 hours while awake.",
                theExecution = listOf(
                    "Mix 1/4 tsp salt and 1/4 tsp baking soda in 1 cup warm water.",
                    "Swish and spit gently; do not swallow.",
                    "Avoid store-bought mouthwash containing alcohol.",
                    "Use an extra-soft toothbrush only."
                ),
                theShield = "Prevents secondary infections and allows you to keep eating comfortably."
            ),
            TreatmentManual(
                id = "treat-fatigue",
                title = "Energy Watch: The 30-Min Rule",
                summary = "Manage treatment-related exhaustion effectively.",
                category = TreatmentCategory.RECOVERY,
                theTruth = "Cancer fatigue is different from normal tiredness. It doesn't always go away with sleep.",
                theCommand = "Rest when needed, but keep naps under 30 minutes.",
                theExecution = listOf(
                    "Do your most important task when you have the most energy.",
                    "Ask for help with chores like laundry or cooking.",
                    "Try a slow 10-minute walk to actually boost your energy levels.",
                    "Keep a daily log of when you feel strongest."
                ),
                theShield = "Protects your physical stamina and keeps your body from 'shutting down'."
            )
        )
        healingRepository.saveTreatmentManuals(manuals)
    }

    private suspend fun seedSymptomDirectives() {
        val symptoms = listOf(
            SymptomDirective(
                id = "symp-1",
                name = "🤢 SICK TO STOMACH (NAUSEA)",
                iconName = "sick",
                theTruth = "Treatment can upset your stomach lining and signal your brain to feel sick.",
                theCommand = "Use the 'Ginger & Dry Food' protocol immediately.",
                theExecution = listOf(
                    "Sip cool ginger tea or ginger ale slowly.",
                    "Eat 2-3 plain crackers every hour.",
                    "Sit upright; do not lie flat for 30 minutes after eating.",
                    "Avoid strong smells like cooking or heavy perfume."
                ),
                theShield = "Calms your stomach naturally and breaks the 'feeling sick' loop."
            ),
            SymptomDirective(
                id = "symp-2",
                name = "🧱 CAN'T GO (CONSTIPATION)",
                iconName = "emergency",
                theTruth = "Pain meds and some chemo drugs slow down your digestive system.",
                theCommand = "Implement the 'Fiber & Fluid' flush immediately.",
                theExecution = listOf(
                    "Drink at least 8 full glasses of water today.",
                    "Eat 3-5 prunes or drink warm prune juice.",
                    "Take a slow 10-minute walk to help move your bowels.",
                    "Call your nurse if you haven't gone in 3 days."
                ),
                theShield = "Prevents toxic buildup and dangerous bowel blockages."
            ),
            SymptomDirective(
                id = "symp-3",
                name = "🚽 WATERY STOMACH (DIARRHEA)",
                iconName = "sick",
                theTruth = "Rapid cell turnover in your gut can cause loose stools and dehydration.",
                theCommand = "Follow the 'BRAT' diet protocol.",
                theExecution = listOf(
                    "Eat only: Bananas, Rice, Applesauce, and Toast.",
                    "Drink electrolyte fluids (like Pedialyte or Gatorade).",
                    "Avoid dairy, caffeine, and greasy foods for 24 hours.",
                    "Count how many times you go; call nurse if > 4 times."
                ),
                theShield = "Maintains your hydration levels and protects your gut lining."
            ),
            SymptomDirective(
                id = "symp-4",
                name = "🖐️ TINGLING HANDS/FEET (NEUROPATHY)",
                iconName = "bolt",
                theTruth = "Certain drugs can temporarily irritate your nerve endings.",
                theCommand = "Protect your hands and feet from extreme heat/cold.",
                theExecution = listOf(
                    "Check water temperature with your elbow, not your hands.",
                    "Always wear shoes or slippers, even indoors.",
                    "Massage your hands and feet gently to improve blood flow.",
                    "Report any 'burning' or 'electric' sensations to your doctor."
                ),
                theShield = "Prevents permanent nerve damage and accidental burns or cuts."
            ),
            SymptomDirective(
                id = "symp-5",
                name = "👄 SORE MOUTH OR THROAT",
                iconName = "sick",
                theTruth = "Treatment can cause tiny, painful sores on your delicate tissues.",
                theCommand = "Use the 'Gentle Cleanse' protocol.",
                theExecution = listOf(
                    "Rinse with your salt/soda mix every 2 hours.",
                    "Eat cold, soft foods like yogurt, custard, or milkshakes.",
                    "Avoid spicy, salty, or crunchy foods like chips or citrus.",
                    "Suck on ice chips to numb the pain before eating."
                ),
                theShield = "Ensures you can still get the nutrition your body needs to heal."
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
            ),
            PatientTruthCheck(
                id = "ptc-2",
                claim = "Vitamin B17 (Apricot Kernels)",
                verdict = PatientVerdict.DANGER,
                theTruth = "B17 is not a vitamin; it is a chemical called amygdalin. When eaten, it turns into Cyanide in your stomach.",
                theCommand = "Do NOT ingest apricot kernels or B17 supplements.",
                theExecution = listOf(
                    "Ignore claims that B17 is a 'suppressed' cure.",
                    "Be aware that cyanide poisoning causes liver damage and death.",
                    "Report any seller of 'Laetrile' to health authorities."
                ),
                theShield = "Stops you from accidentally poisoning your blood with cyanide.",
                socialScript = "Actually, the Agency found that B17 turns into cyanide in the body. It's banned for a reason—it's toxic, not a cure."
            ),
            PatientTruthCheck(
                id = "ptc-3",
                claim = "Black Salve (Corrosive Paste)",
                verdict = PatientVerdict.DANGER,
                theTruth = "Black Salve is a corrosive paste that burns through skin. It doesn't 'pull out' cancer; it just destroys healthy tissue and leaves deep wounds.",
                theCommand = "Never apply 'Drawing Salve' or Black Salve to your skin.",
                theExecution = listOf(
                    "Consult a dermatologist or surgeon for any skin spots.",
                    "Understand that salve can hide cancer growth underneath the burn.",
                    "Discard any product containing 'Bloodroot' or Zinc Chloride for skin use."
                ),
                theShield = "Prevents permanent disfigurement and the dangerous delay of proper care.",
                socialScript = "I saw a warning that Black Salve is actually a corrosive acid. It burns the skin but leaves the cancer underneath. I'm seeing a real doctor for this."
            ),
            PatientTruthCheck(
                id = "ptc-4",
                claim = "Coffee Enemas (Gerson Therapy)",
                verdict = PatientVerdict.SCAM,
                theTruth = "Enemas do not 'detox' the liver or cure cancer. They can cause severe electrolyte imbalances and bowel infections.",
                theCommand = "Do NOT perform coffee enemas as a treatment.",
                theExecution = listOf(
                    "Focus on drinking water and eating fiber to help your body naturally detox.",
                    "Ignore claims that enemas can 'starve' a tumor.",
                    "Speak to your oncologist if you are concerned about 'toxins'."
                ),
                theShield = "Protects your delicate bowel lining and maintains your vital mineral balance.",
                socialScript = "The Agency verified that enemas can't cure cancer and are actually quite dangerous for your gut. I'm sticking to proven medicine."
            )
        )
        healingRepository.savePatientTruthChecks(defenses)
    }

    private suspend fun seedRedFlagDirectives() {
        val flags = listOf(
            RedFlagDirective(
                id = "rf-1",
                title = "🌡️ FEELING HOT, SHAKY, OR FLUSHED",
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
        
        // --- 1. DANGEROUS SCAMS & HARMFUL CURES ---
        val dangerousScams = listOf(
            RegistrySeed("Black Salve", listOf("Cansema", "Bloodroot", "Drawing Salve", "Indian Herb", "Zinc Chloride"), "A corrosive acid paste that burns through skin. It does not treat cancer; it destroys healthy tissue while tumors continue to grow underneath.", listOf("Skin", "Salve", "Natural Cure", "Burn")),
            RegistrySeed("Miracle Mineral Solution", listOf("MMS", "Chlorine Dioxide", "CD", "Water Purification Drops"), "A industrial bleach that causes severe vomiting, kidney failure, and life-threatening low blood pressure. It is not a medicine.", listOf("Bleach", "Drops", "Miracle", "Toxic")),
            RegistrySeed("Laetrile (Vitamin B17)", listOf("Apricot Kernels", "Amygdalin", "B17 supplement", "Fruit Seeds"), "A chemical that turns into cyanide in your stomach. It does not cure cancer and causes chronic cyanide poisoning.", listOf("Seeds", "Vitamin", "Cyanide", "Supplement")),
            RegistrySeed("Cesium Chloride", listOf("High pH Therapy", "Cesium drops", "Cesium Carbonate"), "A treatment that blocks heart channels, causing fatal heart attacks and seizures. It is proven ineffective for cancer.", listOf("Heart", "pH", "Alkaline", "Mineral")),
            RegistrySeed("Baking Soda Injections", listOf("Sodium Bicarbonate therapy", "Tulio Simoncini", "Fungus cure"), "The myth that cancer is a fungus cured by baking soda. Injections can cause severe blood chemistry imbalance and death.", listOf("Fungus", "Soda", "Injection", "Scam")),
            RegistrySeed("Shark Cartilage", listOf("Cartilage pills", "Shark medicine", "BeneFin"), "Based on the false myth that sharks don't get cancer. Studies show it is zero-effective and may contain toxins.", listOf("Pills", "Shark", "Marine", "Supplement"))
        )

        // --- 2. DIETARY & NUTRITION MYTHS ---
        val dietaryMyths = listOf(
            RegistrySeed("Alkaline Diet", listOf("pH Balance", "Acidic foods", "Lemon water cure", "Dr Sebi"), "The body strictly controls blood pH via the lungs and kidneys. You cannot 'alkalize' your body with food to kill cancer.", listOf("Lemon", "Acid", "pH", "Diet")),
            RegistrySeed("Sugar Feeds Cancer", listOf("Starve cancer", "No sugar diet", "Glucose fuel", "Fruit sugar"), "While weight management is vital, all cells use sugar. Cutting sugar won't 'starve' a tumor and can cause dangerous weight loss.", listOf("Sweets", "Carbs", "Fruit", "Weight")),
            RegistrySeed("Gerson Therapy", listOf("Coffee enemas", "Juice fasting", "Max Gerson", "Detox diet"), "A restrictive diet and enema protocol with zero evidence. Enemas cause dehydration and bowel infections.", listOf("Juice", "Enema", "Detox", "Cleanse")),
            RegistrySeed("Keto for Cancer", listOf("Ketogenic cure", "Fat-fuel", "Zero carb"), "Keto is being studied *alongside* medical care for some tumors, but it is NOT a cure and can be dangerous for many patients.", listOf("Fat", "Diet", "Carbs", "Study")),
            RegistrySeed("Superfood Miracles", listOf("Soursop", "Graviola", "Ginger vs Chemo", "Turmeric cure"), "Individual foods are healthy but are not 'cures.' Soursop is not '100x stronger' than chemo and can damage nerves.", listOf("Fruit", "Spice", "Stronger than chemo", "Supplement")),
            RegistrySeed("Aspartame Fear", listOf("Diet Soda", "Equal", "NutraSweet", "Artificial sweeteners"), "IARC labels it 'possibly' carcinogenic, but current levels in soda are safe for most. Focus on body fat, not one sweetener.", listOf("Soda", "Sugar Free", "Chemical", "Fear"))
        )

        // --- 3. TECHNOLOGY & RADIATION MYTHS ---
        val techMyths = listOf(
            RegistrySeed("Cell Phones & 5G", listOf("5G Radiation", "RF Waves", "Phone brain tumor", "EMF"), "Phones use non-ionizing waves. They don't have the energy to break DNA. Billions of users show no tumor increase.", listOf("Phone", "Tower", "Radiation", "5G")),
            RegistrySeed("Wi-Fi & Bluetooth", listOf("Wireless internet", "Bluetooth cancer", "Router safety"), "Like phones, Wi-Fi waves are too weak to damage human tissue. They are biologically distinct from X-rays.", listOf("Internet", "Router", "Bluetooth", "Wireless")),
            RegistrySeed("Microwaves", listOf("Microwaved food", "Radioactive food", "Life energy"), "Microwaves heat food by vibrating water. They do not make food radioactive or remove its 'healing energy'.", listOf("Kitchen", "Food", "Heat", "Appliance")),
            RegistrySeed("Airport Scanners", listOf("TSA scanner", "X-ray backscatter", "Security radiation"), "The radiation from a single scanner is equal to 2 minutes of a high-altitude flight. It is medically insignificant.", listOf("Travel", "TSA", "X-ray", "Security")),
            RegistrySeed("Smart Meters", listOf("Electricity meter", "Utility waves", "Household EMF"), "Smart meters emit tiny pulses of RF energy, lower than a cell phone. There is no evidence of a cancer link.", listOf("Home", "Electricity", "Waves", "Meter"))
        )

        // --- 4. LIFESTYLE & MEDICAL MYTHS ---
        val medicalMyths = listOf(
            RegistrySeed("Biopsies Spread Cancer", listOf("Seeding", "Needle biopsy", "Poking the tumor"), "Doctors use strict protocols to prevent 'seeding'. The risk is microscopic compared to the benefit of diagnosis.", listOf("Needle", "Test", "Diagnosis", "Spread")),
            RegistrySeed("Surgery Spreads Cancer", listOf("Exposing to air", "Cutting the tumor", "Surgery fear"), "Cancer doesn't spread because it 'hits the air'. Finding cancer during surgery means it was already there.", listOf("Hospital", "Surgery", "Spread", "Air")),
            RegistrySeed("Big Pharma Conspiracy", listOf("Suppressed cure", "Secret treatment", "Doctors hiding"), "A cure would be the most profitable discovery ever. Researchers also lose their own families to cancer.", listOf("Secret", "Profit", "Conspiracy", "Doctor")),
            RegistrySeed("Deodorant Aluminum", listOf("Antiperspirant", "Armpit cancer", "Lymph nodes"), "Large studies show no link between aluminum in deodorant and breast cancer. Sweat is not the body's primary detox.", listOf("Beauty", "Skin", "Deodorant", "Sweat")),
            RegistrySeed("Underwire Bras", listOf("Bra cancer", "Lymph flow", "Tight bras"), "Bras do not block lymph flow or cause breast cancer. This is a purely social media myth.", listOf("Clothes", "Women", "Bras", "Myth")),
            RegistrySeed("Dental Fillings", listOf("Mercury fillings", "Amalgam", "Teeth cancer"), "Mercury in fillings is stable and does not cause cancer. Removal is more stressful for the body than leaving them.", listOf("Dentist", "Teeth", "Mercury", "Amalgam")),
            RegistrySeed("Contagious Cancer", listOf("Catching cancer", "Cancer virus", "Touching cancer"), "Cancer is not a cold. You can't catch it. Only the viruses that *lead* to it (like HPV) are contagious.", listOf("Contagious", "Viral", "Touching", "Safety"))
        )

        val allSeeds = dangerousScams + dietaryMyths + techMyths + medicalMyths

        allSeeds.forEach { seed ->
            saveSignal(
                id = "truth-${seed.name.lowercase().replace(" ", "-")}",
                title = "Myth Check: ${seed.name}",
                summary = seed.truth,
                theTruth = seed.truth,
                theCommand = "Ignore the viral claim; stick to evidence-based safety.",
                theExecution = listOf(
                    "Verify the source of the claim (Is it an ad or a medical journal?)",
                    "Understand the basic biology: ${seed.truth.split(".")[0]}.",
                    "Focus your energy on known risks (Tobacco, UV, Processed Meat)."
                ),
                theShield = "Protects you from the financial, physical, and emotional burden of misinformation.",
                category = SignalCategory.RESEARCH,
                importance = if (dangerousScams.contains(seed)) SignalImportance.CRITICAL else SignalImportance.LOW,
                confidence = SignalConfidence.VERY_HIGH,
                affectedIngredients = seed.ingredients,
                safeAlternatives = emptyList(),
                interactionContexts = listOf("Home", "Public"),
                discoveryTags = seed.tags,
                investigatedClaim = seed.ingredients.joinToString(", ")
            )
        }
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

    private data class RegistrySeed(val name: String, val ingredients: List<String>, val truth: String, val tags: List<String>)
}
