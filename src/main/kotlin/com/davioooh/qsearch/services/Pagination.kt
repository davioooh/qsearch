package com.davioooh.qsearch.services

import com.davioooh.qsearch.model.QuestionDetails
import com.davioooh.qsearch.model.toQuestionDetailsList
import kotlin.math.ceil

data class PaginationCriteria(
    val page: Int,
    val pageSize: Int,
    val sortingCriteria: SortingCriteria = SortingCriteria.Activity,
    val sortingDirection: SortingDirection = SortingDirection.Asc
)

data class PageResult<T>(
    val pageItems: List<T>,
    val totalItemsCount: Int,
    val paginationCriteria: PaginationCriteria
)

fun buildPage(
    questions: Questions,
    paginationCriteria: PaginationCriteria
): PageResult<QuestionDetails> {
    require(questions.isNotEmpty()) { "questions can't be an empty list" }
    require(paginationCriteria.page > 0) { "paginationCriteria.page must be greater than 0" }
    require(paginationCriteria.pageSize > 0) { "paginationCriteria.pageSize must be greater than 0" }
    val lastPage = calculateLastPage(questions.size, paginationCriteria.pageSize)
    require(paginationCriteria.page <= lastPage) { "paginationCriteria.page can't be greater than $lastPage" }

    val sQuestions = questions.sortBy(
        paginationCriteria.sortingCriteria,
        paginationCriteria.sortingDirection
    )

    return PageResult(
        sQuestions.chunked(paginationCriteria.pageSize)[paginationCriteria.page - 1].toQuestionDetailsList(),
        sQuestions.size,
        paginationCriteria
    )
}

fun calculateLastPage(itemsCount: Int, pageSize: Int) = ceil(itemsCount / pageSize.toFloat()).toInt()
