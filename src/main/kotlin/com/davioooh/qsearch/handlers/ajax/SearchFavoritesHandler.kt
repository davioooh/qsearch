package com.davioooh.qsearch.handlers.ajax

import com.davioooh.qsearch.authentication.AuthenticationInfoHolder
import com.davioooh.qsearch.model.PaginationBar
import com.davioooh.qsearch.services.*
import com.davioooh.qsearch.utils.enumValueOrDefault
import io.javalin.http.Context
import io.javalin.http.Handler

class SearchFavoritesHandler(
    private val questionsService: QuestionsService
) : Handler {
    override fun handle(ctx: Context) {
        val page = ctx.queryParam("page")?.toInt()?.let { if (it > 0) it else 1 } ?: 1
        val pageSize = DEFAULT_PAGE_SIZE
        val sortBy = ctx.queryParam("sortBy")?.let { enumValueOrDefault(it, SortingCriteria.Activity) }
            ?: SortingCriteria.Activity
        val sortDir =
            ctx.queryParam("sortDir")?.let { enumValueOrDefault(it, SortingDirection.Desc) } ?: SortingDirection.Desc
        val query = ctx.queryParam("query") ?: ""

        val pageResult =
            questionsService.searchUserFavorites(
                AuthenticationInfoHolder.currentUser.userId,
                AuthenticationInfoHolder.currentUser.accessToken,
                PaginationCriteria(page, pageSize, sortBy, sortDir),
                SearchCriteria(query)
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