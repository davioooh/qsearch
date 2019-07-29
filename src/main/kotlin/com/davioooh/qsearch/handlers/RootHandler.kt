package com.davioooh.qsearch.handlers

import com.davioooh.qsearch.authentication.AuthenticationInfoHolder
import com.davioooh.qsearch.model.PaginationBar
import com.davioooh.qsearch.services.PaginationCriteria
import com.davioooh.qsearch.services.QuestionsService
import com.davioooh.qsearch.services.SearchCriteria
import com.davioooh.qsearch.services.SortingCriteria.Activity
import com.davioooh.qsearch.services.SortingDirection.Desc
import com.davioooh.qsearch.services.calculateLastPage
import com.davioooh.qsearch.utils.enumValueOrDefault
import io.javalin.http.Context
import io.javalin.http.Handler

class RootHandler(
    private val questionsService: QuestionsService
) : Handler {
    override fun handle(ctx: Context) {
        val page = ctx.queryParam("page")?.toInt()?.let { if (it > 0) it else 1 } ?: 1
        val pageSize = DEFAULT_PAGE_SIZE
        val sortBy = ctx.queryParam("sortBy")?.let { enumValueOrDefault(it, Activity) } ?: Activity
        val sortDir = ctx.queryParam("sortDir")?.let { enumValueOrDefault(it, Desc) } ?: Desc
        val query = ctx.queryParam("q") ?: ""

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

        ctx.render(
            "/templates/index.html",
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