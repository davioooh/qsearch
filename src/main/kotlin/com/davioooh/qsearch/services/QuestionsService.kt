package com.davioooh.qsearch.services

import com.davioooh.qsearch.model.PageableResult
import com.davioooh.qsearch.model.PaginationCriteria
import com.davioooh.qsearch.model.paginate
import com.davioooh.qsearch.stackexchange.api.QuestionsApi
import com.davioooh.qsearch.stackexchange.api.model.Question
import org.slf4j.LoggerFactory

class QuestionsService(
    private val questionsApi: QuestionsApi
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun getUserFavorites(
        userId: Int,
        accessToken: String,
        paginationCriteria: PaginationCriteria
    ): PageableResult<Question> {
        val questions = mutableListOf<Question>()
        var i = 1
        do {
            val res =
                questionsApi.fetchUserFavoriteQuestions(
                    accessToken = accessToken,
                    userId = userId,
                    page = i,
                    pageSize = 100
                )
            questions.addAll(res.items)
            logger.debug("Request $i")
            i++
        } while (questions.size < res.total ?: 0)

        val sQuestions = questions.sortBy(
            paginationCriteria.sortingCriteria,
            paginationCriteria.sortingDirection
        )

        logger.debug("Sort by ${paginationCriteria.sortingCriteria} (${paginationCriteria.sortingDirection})")

        return paginate(
            sQuestions,
            paginationCriteria.page,
            paginationCriteria.pageSize
        )
    }
}