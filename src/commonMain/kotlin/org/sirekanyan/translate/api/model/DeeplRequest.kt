package org.sirekanyan.translate.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class DeeplRequest(
    @SerialName("text") val text: List<String>,
    @SerialName("source_lang") val sourceLang: String?,
    @SerialName("target_lang") val targetLang: String,
)
