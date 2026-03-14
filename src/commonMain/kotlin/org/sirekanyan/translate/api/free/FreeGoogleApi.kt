package org.sirekanyan.translate.api.free

import io.ktor.client.HttpClient
import io.ktor.client.request.forms.submitForm
import io.ktor.client.statement.bodyAsText
import io.ktor.http.parametersOf
import org.sirekanyan.translate.TranslateException
import org.sirekanyan.translate.api.TranslateApi
import org.sirekanyan.translate.api.free.model.decodeFreeGoogleResponse
import org.sirekanyan.translate.api.free.model.encodeFreeGoogleRequest

private const val TranslateFreeUrl = "https://translate.google.com/_/TranslateWebserverUi/data/batchexecute"

class FreeGoogleApi(private val httpClient: HttpClient) : TranslateApi {

    override suspend fun translate(sourceLang: String?, targetLang: String, text: String): List<String> {
        val request = try {
            encodeFreeGoogleRequest(sourceLang, targetLang, text)
        } catch (exception: Exception) {
            throw TranslateException("Cannot encode request", exception)
        }
        val response = httpClient.submitForm(TranslateFreeUrl, parametersOf("f.req", request)).bodyAsText()
        val translation = try {
            decodeFreeGoogleResponse(response)
        } catch (exception: Exception) {
            throw TranslateException("Cannot decode response: $response", exception)
        }
        return listOf(translation)
    }
}
