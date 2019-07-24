package com.davioooh.qsearch.services

import com.davioooh.qsearch.stackexchange.api.model.Question
import java.time.LocalDateTime
import java.time.ZoneOffset

fun question(
    questionId: Int,
    title: String,
    creationDate: LocalDateTime = LocalDateTime.of(2019, 1, 2, 10, 0),
    lastActivityDate: LocalDateTime = LocalDateTime.of(2019, 3, 12, 23, 10),
    vote: Int = 0,
    views: Int = 0
) = Question(
    answerCount = 5,
    creationDate = creationDate.atZone(ZoneOffset.UTC).toInstant().toEpochMilli(),
    answered = false,
    lastActivityDate = lastActivityDate.atZone(ZoneOffset.UTC).toInstant().toEpochMilli(),
    lastEditDate = 0,
    link = "/q-link/$questionId",
    questionId = questionId,
    score = vote,
    tags = listOf(),
    title = title,
    viewCount = views
)

fun questionsList(size: Int) = IntRange(1, size).map { question(it, "Q$it") }