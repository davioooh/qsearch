package com.davioooh.qsearch.model

import com.davioooh.qsearch.services.Questions
import com.davioooh.qsearch.services.toLocalDateTime
import com.davioooh.qsearch.services.toNiceString
import com.davioooh.qsearch.stackexchange.api.model.Question
import java.time.LocalDateTime

data class QuestionDetails(
    val answerCount: Int,
    val creationDate: LocalDateTime,
    val answered: Boolean,
    val lastActivityDate: LocalDateTime,
    val link: String,
    val score: Int,
    val tags: List<String>,
    val title: String,
    val viewCount: Int
) {
    val formattedCreationDate: String = this.creationDate.toNiceString()
    val formattedLastActivityDate: String = this.lastActivityDate.toNiceString()

    val answerCountNice = answerCount.toNiceString()
    val viewCountNice = viewCount.toNiceString()
    val scoreNice = score.toNiceString()
}

fun Question.toQuestionDetails() = QuestionDetails(
    this.answerCount,
    this.creationDate.toLocalDateTime(),
    this.answered,
    this.lastActivityDate.toLocalDateTime(),
    this.link,
    this.score,
    this.tags,
    this.title,
    this.viewCount
)

fun Questions.toQuestionDetailsList() = this.map { it.toQuestionDetails() }