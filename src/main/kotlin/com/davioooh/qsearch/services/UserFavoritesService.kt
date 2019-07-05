package com.davioooh.qsearch.services

import com.davioooh.qsearch.authentication.AuthenticationInfoHolder
import com.davioooh.qsearch.model.PageableResult
import com.davioooh.qsearch.model.paginate
import com.davioooh.qsearch.services.SortingCriteria.*
import com.davioooh.qsearch.services.SortingDirection.Asc
import com.davioooh.qsearch.services.SortingDirection.Desc
import com.davioooh.qsearch.stackexchange.api.QuestionsApi
import com.davioooh.qsearch.stackexchange.api.model.Question
import org.slf4j.LoggerFactory

class UserFavoritesService(
    private val questionsApi: QuestionsApi
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun getUserFavorites(
        userId: Int,
        page: Int,
        pageSize: Int,
        sortBy: SortingCriteria = Activity,
        sortDirection: SortingDirection = Asc
    ): PageableResult<Question> {
        val questions = mutableListOf<Question>()
        var i = 1
        do {
            val res =
                questionsApi.fetchUserFavoriteQuestions(
                    accessToken = AuthenticationInfoHolder.authenticatedUser.get().accessToken,
                    userId = userId,
                    page = i,
                    pageSize = 100
                )
            questions.addAll(res.items)
            logger.debug("Request $i")
            i++
        } while (questions.size < res.total ?: 0)

        val sQuestions = when (sortBy) {
            Activity -> questions.sortedBy { it.lastActivityDate }
            Vote -> questions.sortedBy { it.score }
            Asked -> questions.sortedBy { it.creationDate }
            Views -> questions.sortedBy { it.viewCount }
        }

        val sdQuestions = if (sortDirection == Desc) sQuestions.reversed() else sQuestions

        logger.debug("Sort by $sortBy ($sortDirection)")

        return paginate(sdQuestions, page, pageSize)
    }

}