package com.davioooh.qsearch.stackexchange.api

import com.davioooh.qsearch.stackexchange.api.model.Question
import com.davioooh.qsearch.stackexchange.api.model.ResultWrapper
import com.github.kittinunf.fuel.jackson.responseObject

class QuestionsApi(conf: ApiClientConfig) {
    private val httpClient = buildHttpClient(conf)

    fun fetchUserFavoriteQuestions(
        accessToken: String, userId: Int? = null,
        page: Int = 0, pageSize: Int = 100
    ): ResultWrapper<Question> =
        httpClient.get(
            if (userId == null) "/me/favorites" else "/users/$userId/favorites", listOf(
                "access_token" to accessToken,
                "page" to page,
                "pagesize" to pageSize
            )
        ).responseObject<ResultWrapper<Question>>().third.get()

}