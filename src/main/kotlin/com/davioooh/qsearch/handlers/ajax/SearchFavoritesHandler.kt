package com.davioooh.qsearch.handlers.ajax

import com.davioooh.qsearch.authentication.AuthenticationInfoHolder
import com.davioooh.qsearch.model.PaginationBar
import com.davioooh.qsearch.services.PaginationCriteria
import com.davioooh.qsearch.services.QuestionsService
import com.davioooh.qsearch.services.SearchCriteria
import com.davioooh.qsearch.services.calculateLastPage
import io.javalin.http.Context
import io.javalin.http.Handler

class SearchFavoritesHandler(
    private val questionsService: QuestionsService
) : Handler {
    override fun handle(ctx: Context) {
        val page = ctx.getPage()
        val pageSize = DEFAULT_PAGE_SIZE
        val sortBy = ctx.getSortingCriteria()
        val sortDir = ctx.getSortingDirection()

        val searchKey = ctx.getSearchKey()
        val tags = ctx.getSearchTags()

        val pageResult =
            questionsService.searchUserFavorites(
                AuthenticationInfoHolder.currentUser.userId,
                AuthenticationInfoHolder.currentUser.accessToken,
                PaginationCriteria(page, pageSize, sortBy, sortDir),
                SearchCriteria(searchKey, tags)
            )

        val paginationBar =
            if (pageResult != null && pageResult.filteredItemsCount > 0) {
                PaginationBar.from(
                    pageResult.paginationCriteria.page, calculateLastPage(pageResult.filteredItemsCount, pageSize),
                    baseUrl = PaginationBar.buildUrl(
                        "/",
                        listOf(
                            "sortBy" to sortBy.toString(),
                            "sortDir" to sortDir.toString()
                        )
                    )
                )
            } else null

        ctx.json(
            mapOf(
                "pageResult" to pageResult,
                "paginationBar" to paginationBar
            )
        )
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 25
    }
}