package com.idontwantcancer.app.domain.repository

import com.idontwantcancer.app.domain.model.Signal
import com.idontwantcancer.app.domain.model.SignalCategory
import kotlinx.coroutines.flow.Flow

interface SignalRepository {
    /**
     * Retrieves the latest evaluated cancer-intelligence signals.
     */
    suspend fun getLatestSignals(): List<Signal>

    /**
     * Retrieves intelligence signals that require immediate user attention.
     */
    suspend fun getAttentionSignals(): List<Signal>

    /**
     * Searches the available intelligence signals for a specific query.
     */
    suspend fun searchSignals(query: String): List<Signal>

    /**
     * Retrieves signals by their category.
     */
    fun getSignalsByCategories(categories: List<SignalCategory>): Flow<List<Signal>>

    /**
     * Retrieves a specific intelligence signal by its unique identifier.
     */
    suspend fun getSignalById(signalId: String): Signal?

    /**
     * Updates the user's action taken status for a specific signal.
     */
    suspend fun updateActionTakenStatus(signalId: String, isTaken: Boolean)
}
