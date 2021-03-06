package com.davioooh.qsearch.services

data class SearchCriteria(
    val key: String? = null,
    val tags: List<String> = listOf()
) {
    val hasValues
        get() = (key?.isNotBlank() ?: false) || tags.isNotEmpty()
}

data class SearchPageResult<T>(
    private val pageResult: PageResult<T>,
    val totalItemsCount: Int,
    val searchCriteria: SearchCriteria
) {
    val pageItems = pageResult.pageItems
    val paginationCriteria = pageResult.paginationCriteria
    val filteredItemsCount = pageResult.totalItemsCount
}