package com.davioooh.stackexchange.api

import com.davioooh.stackexchange.api.model.Question
import com.davioooh.stackexchange.api.model.ResultWrapper
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.jackson.responseObject

class QuestionsApi(key: String) {
    private val httpClient = FuelManager()

    init {
        httpClient.basePath = API_BASE
        httpClient.baseParams = listOf(
            "key" to key,
            "site" to "stackoverflow",
            "filter" to "!9Z(-x-Ptf"
        )
    }

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