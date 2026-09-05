package com.idontwantcancer.app.data.local.mapper

import com.idontwantcancer.app.data.local.entity.PreventionActionEntity
import com.idontwantcancer.app.domain.model.PreventionAction

fun PreventionActionEntity.toDomain(): PreventionAction {
    return PreventionAction(
        id = id,
        title = title,
        description = description,
        iconName = iconName,
        isAdopted = isAdopted
    )
}

fun PreventionAction.toEntity(): PreventionActionEntity {
    return PreventionActionEntity(
        id = id,
        title = title,
        description = description,
        iconName = iconName,
        isAdopted = isAdopted
    )
}
