package com.davioooh.qsearch.services

import kotlin.math.ceil

data class PaginationCriteria(
    val page: Int,
    val pageSize: Int,
    val sortingCriteria: SortingCriteria = SortingCriteria.Activity,
    val sortingDirection: SortingDirection = SortingDirection.Asc
)

data class PageableResult<T>(
    val items: List<T>,
    val page: Int,
    val pageSize: Int,
    val total: Int
)

fun <T> paginate(items: List<T>, page: Int, pageSize: Int): PageableResult<T> {
    require(items.isNotEmpty()) { "items can't be an empty list" }
    require(page > 0) { "page must be greater than 0" }
    require(pageSize > 0) { "pageSize must be greater than 0" }
    val lastPage = calculateLastPage(items.size, pageSize)
    require(page <= lastPage) { "page can't be greater than $lastPage" }

    return PageableResult(
        items.chunked(pageSize)[page - 1],
        page,
        pageSize,
        items.size
    )
}

fun calculateLastPage(itemsCount: Int, pageSize: Int) = ceil(itemsCount / pageSize.toFloat()).toInt()
