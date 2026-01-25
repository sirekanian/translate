package org.sirekanyan.translate.api.deepl.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class DeeplResponse(
    @SerialName("translations") val translations: List<Translation>,
) {

    @Serializable
    class Translation(
        @SerialName("text") val text: String,
    )
}
