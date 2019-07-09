package com.davioooh.qsearch.services

import com.davioooh.qsearch.authentication.AuthenticationInfoHolder
import com.davioooh.qsearch.model.PageableResult
import com.davioooh.qsearch.model.paginate
import com.davioooh.qsearch.services.SortingCriteria.Activity
import com.davioooh.qsearch.services.SortingDirection.Asc
import com.davioooh.qsearch.stackexchange.api.QuestionsApi
import com.davioooh.qsearch.stackexchange.api.model.Question
import org.slf4j.LoggerFactory

class QuestionsService(
    private val questionsApi: QuestionsApi
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun getUserFavorites(
        userId: Int,
        page: Int,
        pageSize: Int,
        sortingCriteria: SortingCriteria = Activity,
        sortingDirection: SortingDirection = Asc
    ): PageableResult<Question> {
        val questions = mutableListOf<Question>()
        var i = 1
        do {
            val res =
                questionsApi.fetchUserFavoriteQuestions(
                    accessToken = AuthenticationInfoHolder.currentUser.accessToken,
                    userId = userId,
                    page = i,
                    pageSize = 100
                )
            questions.addAll(res.items)
            logger.debug("Request $i")
            i++
        } while (questions.size < res.total ?: 0)

        val sQuestions = questions.sortBy(sortingCriteria, sortingDirection)

        logger.debug("Sort by $sortingCriteria ($sortingDirection)")

        return paginate(sQuestions, page, pageSize)
    }
}