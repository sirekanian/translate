package org.sirekanyan.translate.api.google.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class GoogleRequest(
    @SerialName("q") val text: String,
    @SerialName("source") val sourceLang: String?,
    @SerialName("target") val targetLang: String,
    @SerialName("format") val format: String = "text",
)
