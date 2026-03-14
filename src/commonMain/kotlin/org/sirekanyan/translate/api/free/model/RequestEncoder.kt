package org.sirekanyan.translate.api.free.model

fun encodeFreeGoogleRequest(sourceLang: String?, targetLang: String, text: String): String {
    val sourceLang = sourceLang ?: "auto"
    val request = DefaultJson.encodeToString(listOf(listOf(text, sourceLang, targetLang)))
    return DefaultJson.encodeToString(listOf(listOf(listOf("MkEWBc", request))))
}
