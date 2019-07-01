package com.davioooh.stackexchange.api.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Question(
    @JsonProperty("tags") val tags: List<String>,
    @JsonProperty("owner") val owner: ShallowUser,
    @JsonProperty("is_answered") val answered: Boolean,
    @JsonProperty("answer_count") val answerCount: Int,
    @JsonProperty("accepted_answer_id") val acceptedAnswerId: Int,
    @JsonProperty("view_count") val viewCount: Int,
    @JsonProperty("favorite_count") val favoriteCount: Int?,
    @JsonProperty("down_vote_count") val downVoteCount: Int?,
    @JsonProperty("up_vote_count") val upVoteCount: Int?,
    @JsonProperty("score") val score: Int,
    @JsonProperty("last_activity_date") val lastActivityDate: Int,
    @JsonProperty("creation_date") val creationDate: Int,
    @JsonProperty("last_edit_date") val lastEditDate: Int,
    @JsonProperty("question_id") val questionId: Int,
    @JsonProperty("link") val link: String,
    @JsonProperty("title") val title: String,
    @JsonProperty("body") val body: String?
)