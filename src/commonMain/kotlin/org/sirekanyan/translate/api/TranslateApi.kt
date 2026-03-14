package org.sirekanyan.translate.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.curl.Curl
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.sirekanyan.translate.TranslateException
import org.sirekanyan.translate.api.deepl.DeeplApi
import org.sirekanyan.translate.api.free.FreeGoogleApi
import org.sirekanyan.translate.api.google.GoogleApi
import org.sirekanyan.translate.getEnv

private const val DeeplEnvKey = "DEEPL_TRANSLATE_API_KEY"
private const val GoogleEnvKey = "GOOGLE_TRANSLATE_API_KEY"

fun createTranslateApi(): TranslateApi {
    val httpClient = HttpClient(Curl) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        HttpResponseValidator {
            validateResponse { response ->
                if (!response.status.isSuccess()) {
                    throw TranslateException("Error ${response.status}: ${response.bodyAsText()}", cause = null)
                }
            }
        }
    }
    val deeplToken = getEnv(DeeplEnvKey)
    val googleToken = getEnv(GoogleEnvKey)
    return when {
        deeplToken.isNotBlank() -> DeeplApi(httpClient, deeplToken)
        googleToken.isNotBlank() -> GoogleApi(httpClient, googleToken)
        else -> FreeGoogleApi(httpClient)
    }
}

interface TranslateApi {

    suspend fun translate(sourceLang: String?, targetLang: String, text: String): List<String>
}
