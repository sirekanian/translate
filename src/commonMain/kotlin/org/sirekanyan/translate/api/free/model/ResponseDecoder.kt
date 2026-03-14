package org.sirekanyan.translate.api.free.model

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive

fun decodeFreeGoogleResponse(response: String): String {
    val response = response.removePrefix(")]}'")
    val jsonArrayString = DefaultJson.decodeFromString<JsonArray>(response)[0].jsonArray[2].jsonPrimitive.content
    val jsonArray = DefaultJson.decodeFromString<JsonArray>(jsonArrayString)
    return convertToFlatArrays(jsonArray)
        .maxBy(JsonArray::size)
        .filterIsInstance<JsonPrimitive>()
        .first(JsonPrimitive::isString)
        .content
}

private fun convertToFlatArrays(jsonArray: JsonArray): List<JsonArray> {
    if (jsonArray.all { it is JsonPrimitive }) {
        return listOf(jsonArray)
    }
    return jsonArray
        .filterIsInstance<JsonArray>()
        .flatMap(::convertToFlatArrays)
}
