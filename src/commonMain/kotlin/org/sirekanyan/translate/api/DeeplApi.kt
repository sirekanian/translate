package org.sirekanyan.translate.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.curl.Curl
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.sirekanyan.translate.api.model.DeeplRequest
import org.sirekanyan.translate.api.model.DeeplResponse
import org.sirekanyan.translate.api.model.DeeplResponse.Translation

class DeeplApi(private val apiKey: String) {

    private val httpClient = HttpClient(Curl) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    suspend fun translate(sourceLang: String?, targetLang: String, text: String): List<String> {
        return httpClient.post("https://api-free.deepl.com/v2/translate") {
            header("Authorization", "DeepL-Auth-Key $apiKey")
            contentType(ContentType.Application.Json)
            setBody(DeeplRequest(listOf(text), sourceLang, targetLang))
        }.body<DeeplResponse>().translations.map(Translation::text)
    }
}
