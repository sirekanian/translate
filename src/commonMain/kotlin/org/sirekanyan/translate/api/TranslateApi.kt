package org.sirekanyan.translate.api

interface TranslateApi {

    suspend fun translate(sourceLang: String?, targetLang: String, text: String): List<String>
}
