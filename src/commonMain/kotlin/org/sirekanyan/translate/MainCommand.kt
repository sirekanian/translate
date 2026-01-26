package org.sirekanyan.translate

import com.github.ajalt.clikt.core.CoreCliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import kotlinx.coroutines.runBlocking
import org.sirekanyan.translate.api.createTranslateApi

class MainCommand : CoreCliktCommand("translate") {

    private val sourceLang: String? by option("-s", "--source", metavar = "lang").help("Source language")
    private val targetLang: String by option("-t", "--target", metavar = "lang").required().help("Target language")
    private val text by argument().multiple()

    override fun run() {
        runBlocking {
            try {
                translate()
            } catch (exception: TranslateException) {
                println(exception.message)
            }
        }
    }

    private suspend fun translate() {
        createTranslateApi()
            .translate(sourceLang, targetLang, text.joinToString(" "))
            .forEach(::println)
    }
}
