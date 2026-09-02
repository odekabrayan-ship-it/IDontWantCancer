package com.idontwantcancer.app.data.remote.model.fda

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenFdaRecallDto(
    @SerialName("recall_number") val recallNumber: String,
    @SerialName("reason_for_recall") val reasonForRecall: String,
    @SerialName("product_description") val productDescription: String,
    @SerialName("status") val status: String,
    @SerialName("report_date") val reportDate: String,
    @SerialName("event_id") val eventId: String,
    @SerialName("classification") val classification: String
)
