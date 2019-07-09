package com.davioooh.qsearch.services

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

//val Question.answerCountNice: String
//    get() = this.answerCount.toNiceString()
//val Question.viewCountNice: String
//    get() = this.viewCount.toNiceString()
//val Question.scoreNice: String
//    get() = this.score.toNiceString()

val questionsDecimalFormat = DecimalFormat("#.#", DecimalFormatSymbols(Locale.US))

fun Int.toNiceString(): String {
    return when {
        this >= 1_000_000 -> questionsDecimalFormat.format(this / 1_000_000.0) + "M"
        this >= 1_000 -> questionsDecimalFormat.format(this / 1000.0) + "K"
        else -> this.toString()
    }
}
