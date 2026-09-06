package com.idontwantcancer.app.domain.repository

import com.idontwantcancer.app.domain.model.*
import kotlinx.coroutines.flow.Flow

/**
 * Repository for patient support and healing intelligence.
 */
interface HealingRepository {
    /**
     * Retrieves all specialized treatment manuals.
     */
    fun getTreatmentManuals(): Flow<List<TreatmentManual>>

    /**
     * Saves foundational treatment manuals.
     */
    suspend fun saveTreatmentManuals(items: List<TreatmentManual>)

    /**
     * Retrieves all symptom management directives.
     */
    fun getSymptomDirectives(): Flow<List<SymptomDirective>>

    /**
     * Saves foundational symptom directives.
     */
    suspend fun saveSymptomDirectives(items: List<SymptomDirective>)

    /**
     * Retrieves all specialized truth checks for patients.
     */
    fun getPatientTruthChecks(): Flow<List<PatientTruthCheck>>

    /**
     * Saves foundational patient truth checks.
     */
    suspend fun savePatientTruthChecks(items: List<PatientTruthCheck>)

    /**
     * Retrieves all emergency red-flag directives.
     */
    fun getRedFlagDirectives(): Flow<List<RedFlagDirective>>

    /**
     * Saves foundational red-flag directives.
     */
    suspend fun saveRedFlagDirectives(items: List<RedFlagDirective>)

    /**
     * Retrieves the permanent record of healing acts.
     */
    fun getHealingLog(): Flow<List<HealingLogEntry>>

    /**
     * Logs a completed healing directive.
     */
    suspend fun logHealingAction(entry: HealingLogEntry)
}
