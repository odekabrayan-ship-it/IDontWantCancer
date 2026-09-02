package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceCommunicationHandoff

/**
 * Interface defining the deterministic ordering contract for validated 
 * intelligence handoffs crossing into the communication layer.
 */
interface IntelligenceCommunicationOrderingContract {
    /**
     * Authoritative comparator for ordering communication handoffs.
     */
    val handoffComparator: Comparator<IntelligenceCommunicationHandoff>
}
