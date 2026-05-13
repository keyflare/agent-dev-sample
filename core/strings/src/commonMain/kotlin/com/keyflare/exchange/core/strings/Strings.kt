package com.keyflare.exchange.core.strings

import exchange.core.strings.generated.resources.Res
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString

typealias Strings = Res.string

suspend fun StringResource.value(): String =
    getString(resource =  this)

fun StringResource.valueSync(): String =
    runBlocking { getString(resource = this@valueSync) }
