package com.davioooh.qsearch.model

import com.fasterxml.jackson.annotation.JsonProperty
import kotlin.math.ceil

data class PageableResult<T>(
    @JsonProperty("items") val items: List<T>,
    @JsonProperty("page") val page: Int,
    @JsonProperty("page_size") val pageSize: Int,
    @JsonProperty("total") val total: Int
)

fun <T> paginate(items: List<T>, page: Int, pageSize: Int): PageableResult<T> {
    require(items.isNotEmpty()) { "items can't be an empty list" }
    require(page > 0) { "page must be greater than 0" }
    require(pageSize > 0) { "pageSize must be greater than 0" }
    val lastPage = calculateLastPage(items, pageSize)
    require(page <= lastPage) { "page can't be greater than $lastPage" }

    return PageableResult(
        items.chunked(pageSize)[page - 1],
        page,
        pageSize,
        items.size
    )
}

fun <T> calculateLastPage(items: List<T>, pageSize: Int) = ceil(items.size / pageSize.toFloat()).toInt()
