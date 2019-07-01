package com.davioooh.utils

import java.net.URLDecoder
import java.net.URLEncoder
import java.util.*

typealias Parameters = List<Parameter>
typealias Parameter = Pair<String, String?>

fun Parameters.toUrl(): String =
    joinToString("&") { it.first + it.second?.let { v -> "=$v" }.orEmpty() }

fun String.toParameters() = if (isNotEmpty()) split("&").map(String::toParameter) else listOf()

private fun String.toParameter(): Parameter =
    split("=").map(String::fromFormEncoded).let { l -> l.elementAt(0) to l.elementAtOrNull(1) }

internal fun String.fromFormEncoded() = URLDecoder.decode(this, "UTF-8")

internal fun String.toFormEncoded() = URLEncoder.encode(this, "UTF-8")

internal fun String.toBase64Url() = Base64.getUrlEncoder().encodeToString(this.toByteArray()).toFormEncoded()

internal fun String.fromBase64Url() = String(Base64.getDecoder().decode(this.fromFormEncoded()))
