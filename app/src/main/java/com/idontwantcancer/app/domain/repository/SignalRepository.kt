package com.idontwantcancer.app.domain.repository

import com.idontwantcancer.app.domain.model.Signal

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
     * Retrieves a specific intelligence signal by its unique identifier.
     */
    suspend fun getSignalById(signalId: String): Signal?
}
