package org.sirekanyan.translate.api.deepl

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.sirekanyan.translate.api.TranslateApi
import org.sirekanyan.translate.api.deepl.model.DeeplRequest
import org.sirekanyan.translate.api.deepl.model.DeeplResponse
import org.sirekanyan.translate.api.deepl.model.DeeplResponse.Translation

class DeeplApi(private val httpClient: HttpClient, private val apiKey: String) : TranslateApi {

    override suspend fun translate(sourceLang: String?, targetLang: String, text: String): List<String> {
        return httpClient.post("https://api-free.deepl.com/v2/translate") {
            header("Authorization", "DeepL-Auth-Key $apiKey")
            contentType(ContentType.Application.Json)
            setBody(DeeplRequest(listOf(text), sourceLang, targetLang))
        }.body<DeeplResponse>().translations.map(Translation::text)
    }
}
