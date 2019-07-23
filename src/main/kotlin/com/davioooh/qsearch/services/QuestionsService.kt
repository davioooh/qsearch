package com.davioooh.qsearch.services

import com.davioooh.qsearch.caching.CacheFacade
import com.davioooh.qsearch.caching.QuestionsWrapper
import com.davioooh.qsearch.stackexchange.api.QuestionsApi
import com.davioooh.qsearch.stackexchange.api.model.Question
import org.slf4j.LoggerFactory

class QuestionsService(
    private val questionsApi: QuestionsApi,
    private val cache: CacheFacade<QuestionsWrapper>,
    private val textSearchIndex: FullTextSearchIndex<QuestionItem, Int>
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun getUserFavorites(
        userId: Int,
        accessToken: String,
        paginationCriteria: PaginationCriteria,
        searchCriteria: SearchCriteria? = null
    ): PageResult<Question>? {
        var questions = fetchAllFavorites(accessToken, userId)

        if (questions.isEmpty()) return null

        if (searchCriteria?.isClear == false) {
            questions = filterQuestionsByCriteria(questions, searchCriteria)
            if (questions.isEmpty()) return null
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
                .also {
                    logger.debug("Retrieved ${it.size} questions for user ($userId) from API call")
                }
                .also {
                    if (it.isNotEmpty()) {
                        cache.put(accessToken, QuestionsWrapper(it))
                        logger.debug("Favorite questions for user ($userId) cached in QuestionsCache")
                    }
                }
                .also { q ->
                    if (q.isNotEmpty()) {
                        q.toQuestionItemArray()
                            .let { textSearchIndex.addToIndex(*it) }
                        logger.debug("Favorite questions for user ($userId) indexed for search")
                    }
                }
        } else {
            logger.debug("Retrieved ${questions.size} questions for user ($userId) from QuestionsCache")
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
            logger.trace("Favorite questions for user ($userId): retrieved page $i from API call")
            i++
        } while (questions.size < res.total ?: 0)
        return questions
    }

    private fun filterQuestionsByCriteria(questions: Questions, searchCriteria: SearchCriteria): Questions {
        val qids = textSearchIndex.search(searchCriteria.key)
        return questions.filter { qids.contains(it.questionId) }
        // TODO add other search criteria
    }
}