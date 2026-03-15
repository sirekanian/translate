package org.sirekanyan.translate

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.getenv

fun getEnv(key: String): String {
    @OptIn(ExperimentalForeignApi::class)
    return getenv(key)?.toKString().orEmpty()
}
