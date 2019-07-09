package com.davioooh.qsearch.stackexchange.api.model

import com.davioooh.qsearch.services.toNiceString
import com.fasterxml.jackson.annotation.JsonProperty

data class Question(
    @JsonProperty("accepted_answer_id") val acceptedAnswerId: Int? = null,
    @JsonProperty("answer_count") val answerCount: Int,
    @JsonProperty("body") val body: String? = null,
    @JsonProperty("creation_date") val creationDate: Long,
    @JsonProperty("is_answered") val answered: Boolean,
    @JsonProperty("last_activity_date") val lastActivityDate: Long,
    @JsonProperty("last_edit_date") val lastEditDate: Long,
    @JsonProperty("link") val link: String,
    @JsonProperty("owner") val owner: ShallowUser? = null,
    @JsonProperty("question_id") val questionId: Int,
    @JsonProperty("score") val score: Int,
    @JsonProperty("tags") val tags: List<String>,
    @JsonProperty("title") val title: String,
    @JsonProperty("view_count") val viewCount: Int
) {
    val answerCountNice = answerCount.toNiceString()
    val viewCountNice = viewCount.toNiceString()
    val scoreNice = score.toNiceString()
}