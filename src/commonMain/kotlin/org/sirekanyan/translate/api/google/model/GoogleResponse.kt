package org.sirekanyan.translate.api.google.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class GoogleResponse(
    @SerialName("data") val data: Data,
) {

    @Serializable
    class Data(
        @SerialName("translations") val translations: List<Translation>,
    ) {

        @Serializable
        class Translation(
            @SerialName("translatedText") val translatedText: String,
        )
    }
}
