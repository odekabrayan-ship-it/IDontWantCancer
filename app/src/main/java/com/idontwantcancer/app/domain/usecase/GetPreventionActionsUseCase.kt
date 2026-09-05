package com.idontwantcancer.app.domain.usecase

import com.idontwantcancer.app.domain.model.PreventionAction
import com.idontwantcancer.app.domain.repository.PreventionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPreventionActionsUseCase @Inject constructor(
    private val preventionRepository: PreventionRepository
) {
    operator fun invoke(): Flow<List<PreventionAction>> {
        return preventionRepository.getPreventionActions()
    }
}
