package org.sirekanyan.translate

import com.github.ajalt.clikt.core.CoreCliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import kotlinx.coroutines.runBlocking
import org.sirekanyan.translate.api.deepl.DeeplApi
import org.sirekanyan.translate.api.google.GoogleApi

private const val DeeplEnvKey = "DEEPL_TRANSLATE_API_KEY"
private const val GoogleEnvKey = "GOOGLE_TRANSLATE_API_KEY"

class MainCommand : CoreCliktCommand("translate") {

    private val sourceLang: String? by option("-s", "--source", metavar = "lang").help("Source language")
    private val targetLang: String by option("-t", "--target", metavar = "lang").required().help("Target language")
    private val text by argument().multiple()

    override fun run() {
        val deeplApiKey = getEnv(DeeplEnvKey)
        val googleApiKey = getEnv(GoogleEnvKey)
        val api = when {
            deeplApiKey.isNotBlank() -> DeeplApi(deeplApiKey)
            googleApiKey.isNotBlank() -> GoogleApi(googleApiKey)
            else -> error("Please specify $DeeplEnvKey or $GoogleEnvKey environment variable")
        }
        runBlocking { api.translate(sourceLang, targetLang, text.joinToString(" ")) }
            .forEach(::println)
    }
}
