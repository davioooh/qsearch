package com.davioooh.qsearch.services

data class SearchCriteria(val key: String) {
    val isClear
        get() = key.isBlank()
}

data class SearchPageResult<T>(
    private val pageResult: PageResult<T>,
    val totalItemsCount: Int,
    val searchCriteria: SearchCriteria
) {
    val pageItems = pageResult.pageItems
    val page = pageResult.page
    val pageSize = pageResult.pageSize
    val filteredItemsCount = pageResult.totalItemsCount
}