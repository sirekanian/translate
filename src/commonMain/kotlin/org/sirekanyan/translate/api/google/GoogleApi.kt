package org.sirekanyan.translate.api.google

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.sirekanyan.translate.api.TranslateApi
import org.sirekanyan.translate.api.google.model.GoogleRequest
import org.sirekanyan.translate.api.google.model.GoogleResponse
import org.sirekanyan.translate.api.google.model.GoogleResponse.Data.Translation

class GoogleApi(private val httpClient: HttpClient, private val apiKey: String) : TranslateApi {

    override suspend fun translate(sourceLang: String?, targetLang: String, text: String): List<String> {
        return httpClient.post("https://translation.googleapis.com/language/translate/v2") {
            header("x-goog-api-key", apiKey)
            contentType(ContentType.Application.Json)
            setBody(GoogleRequest(text, sourceLang, targetLang))
        }.body<GoogleResponse>().data.translations.map(Translation::translatedText)
    }
}
