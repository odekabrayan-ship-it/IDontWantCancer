package com.idontwantcancer.app.domain.repository

import com.idontwantcancer.app.domain.model.*

/**
 * Interface for the component responsible for maintaining the agency's memory.
 */
interface IntelligenceMemoryRepository {
    /**
     * Finds the previously processed version of a piece of source material.
     */
    suspend fun getPreviousMaterial(contentId: String): SourceMaterial?

    /**
     * Retrieves source material by its ID.
     */
    suspend fun getSourceMaterialById(contentId: String): SourceMaterial?

    /**
     * Saves newly processed source material to memory.
     */
    suspend fun saveSourceMaterial(material: SourceMaterial)

    /**
     * Saves a formed signal to memory.
     */
    suspend fun saveSignal(signal: Signal)

    /**
     * Retrieves all signals currently in memory, regardless of lifecycle.
     */
    suspend fun getAllSignals(): List<Signal>

    /**
     * Retrieves a specific signal by its ID.
     */
    suspend fun getSignalById(id: String): Signal?

    /**
     * Finds a thread by its topic identifier or associated content ID.
     */
    suspend fun findThreadByTopic(topicIdentifier: String): IntelligenceThread?

    /**
     * Retrieves a specific thread by its unique ID.
     */
    suspend fun getThreadById(id: String): IntelligenceThread?

    /**
     * Saves or updates an intelligence thread.
     */
    suspend fun saveThread(thread: IntelligenceThread)

    /**
     * Retrieves all consolidated events currently in memory.
     */
    suspend fun getAllConsolidatedEvents(): List<ConsolidatedEvent>

    /**
     * Retrieves a specific consolidated event by its ID.
     */
    suspend fun getConsolidatedEventById(id: String): ConsolidatedEvent?

    /**
     * Saves or updates a consolidated event in memory.
     */
    suspend fun saveConsolidatedEvent(event: ConsolidatedEvent)

    /**
     * Saves a timeline entry to memory.
     */
    suspend fun saveTimelineEntry(entry: TimelineEntry)

    /**
     * Retrieves the complete history of events for a specific thread.
     */
    suspend fun getTimelineEntriesForThread(threadId: String): List<TimelineEntry>

    /**
     * Saves a supersession relationship to memory.
     */
    suspend fun saveSupersessionRelation(relation: SupersessionRelation)

    /**
     * Retrieves all supersession relations for a specific entry.
     */
    suspend fun getSupersessionRelationsForEntry(entryId: String): List<SupersessionRelation>

    /**
     * Retrieves all conflicts associated with a specific topic identifier.
     */
    suspend fun getConflictsForTopic(topicId: String): List<IntelligenceConflict>

    /**
     * Saves a state transition to memory.
     */
    suspend fun saveStateTransition(transition: IntelligenceStateTransition)

    /**
     * Retrieves the transition history for a specific thread.
     */
    suspend fun getStateTransitionsForThread(threadId: String): List<IntelligenceStateTransition>

    /**
     * Saves or updates a re-entry lifecycle record.
     */
    suspend fun saveReentryLifecycle(lifecycle: IntelligenceReentryLifecycle)

    /**
     * Retrieves a re-entry lifecycle record by its unique identity.
     */
    suspend fun getReentryLifecycleByIdentity(identity: String): IntelligenceReentryLifecycle?

    /**
     * Records a re-entry state transition and appends to the audit trail atomically.
     */
    suspend fun recordReentryTransition(
        lifecycle: IntelligenceReentryLifecycle,
        auditEntry: IntelligenceReentryAuditEntry
    )

    /**
     * Retrieves the complete audit history for a re-entry event.
     */
    suspend fun getReentryAuditHistory(identity: String): List<IntelligenceReentryAuditEntry>

    /**
     * Clears all intelligence memory from the local repository.
     */
    suspend fun clearAll()
}
