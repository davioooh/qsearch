package com.davioooh.qsearch.services

import com.davioooh.qsearch.caching.CacheFacade
import com.davioooh.qsearch.caching.QuestionsWrapper
import com.davioooh.qsearch.stackexchange.api.QuestionsApi
import com.davioooh.qsearch.stackexchange.api.model.Question
import org.slf4j.LoggerFactory

class QuestionsService(
    private val questionsApi: QuestionsApi,
    private val cache: CacheFacade<QuestionsWrapper>
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun getUserFavorites(
        userId: Int,
        accessToken: String,
        paginationCriteria: PaginationCriteria,
        searchCriteria: SearchCriteria? = null
    ): PageableResult<Question> {
        var questions = fetchAllFavorites(accessToken, userId)

        if (searchCriteria?.isClear == false) {
            questions = filterQuestionsByCriteria(questions, searchCriteria)
        }

        val sQuestions = questions.sortBy(
            paginationCriteria.sortingCriteria,
            paginationCriteria.sortingDirection
        )

        return paginate(sQuestions, paginationCriteria.page, paginationCriteria.pageSize)
    }

    private fun fetchAllFavorites(accessToken: String, userId: Int): Questions {
        var questions = cache.get(accessToken)?.questions ?: listOf()
        if (questions.isEmpty()) {
            questions = fetchFavoritesFromApi(userId, accessToken)
                .also { if (it.isNotEmpty()) cache.put(accessToken, QuestionsWrapper(it)) }
                .also { q ->
                    q.toQuestionItemArray()
                        .let { QuestionsSearchIndex.addToIndex(*it) }
                    logger.debug("Favorite questions for user ($userId) indexed for search")
                }
        } else {
            logger.debug("Favorite questions for user ($userId) found in QuestionsCache")
        }
        return questions
    }

    private fun fetchFavoritesFromApi(userId: Int, accessToken: String): Questions {
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
        return questions
    }

    private fun filterQuestionsByCriteria(questions: Questions, searchCriteria: SearchCriteria): Questions {
        val qids = QuestionsSearchIndex.search(searchCriteria.key)
        return questions.filter { qids.contains(it.questionId) }
        // TODO add other search criteria
    }
}