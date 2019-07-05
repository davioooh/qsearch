package com.davioooh.qsearch.model

import com.fasterxml.jackson.annotation.JsonProperty

data class PageableResult<T>(
    @JsonProperty("items") val items: List<T>,
    @JsonProperty("page") val page: Int,
    @JsonProperty("page_size") val pageSize: Int,
    @JsonProperty("total") val total: Int
)

fun <T> paginate(items: List<T>, page: Int, pageSize: Int): PageableResult<T> =
    PageableResult(
        items.chunked(pageSize)[page - 1],
        page,
        pageSize,
        items.size
    )
