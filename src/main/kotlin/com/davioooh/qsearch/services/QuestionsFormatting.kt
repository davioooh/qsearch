package com.davioooh.qsearch.services

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

val questionsDecimalFormat = DecimalFormat("#.#", DecimalFormatSymbols(Locale.US))
val questionsDateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, ''yy 'at' HH:mm 'UTC'")

fun Int.toNiceString(): String {
    return when {
        this >= 1_000_000 -> questionsDecimalFormat.format(this / 1_000_000.0) + "M"
        this >= 1_000 -> questionsDecimalFormat.format(this / 1000.0) + "K"
        else -> this.toString()
    }
}

fun Long.toLocalDateTime(): LocalDateTime =
    LocalDateTime.ofInstant(Instant.ofEpochSecond(this), ZoneId.of("UTC"))

fun LocalDateTime.toNiceString(): String = questionsDateFormat.format(this)
