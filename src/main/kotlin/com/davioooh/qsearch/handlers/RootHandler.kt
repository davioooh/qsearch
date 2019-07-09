package com.davioooh.qsearch.handlers

import com.davioooh.qsearch.authentication.AuthenticationInfoHolder
import com.davioooh.qsearch.services.QuestionsService
import com.davioooh.qsearch.services.SortingCriteria.Activity
import com.davioooh.qsearch.services.SortingDirection.Desc
import com.davioooh.qsearch.utils.enumValueOrDefault
import io.javalin.http.Context
import io.javalin.http.Handler
import kotlin.math.ceil

class RootHandler(
    private val questionsService: QuestionsService
) : Handler {
    override fun handle(ctx: Context) {
        val page = ctx.queryParam("page")?.toInt()?.let { if (it > 0) it else 1 } ?: 1
        val pageSize = DEFAULT_PAGE_SIZE // ctx.queryParam("size")?.toInt()?.let { if (it in 25..100) it else 25 } ?: 25
        val sortBy = ctx.queryParam("sortBy")?.let { enumValueOrDefault(it, Activity) } ?: Activity
        val sortDir = ctx.queryParam("sortDir")?.let { enumValueOrDefault(it, Desc) } ?: Desc

        val favResult =
            questionsService.getUserFavorites(
                AuthenticationInfoHolder.currentUser.userId,
                page,
                pageSize,
                sortBy,
                sortDir
            )
        ctx.render(
            "/templates/index.html",
            mapOf(
                "questions" to favResult.items,
                "currentPage" to favResult.page,
                "pages" to 1..ceil(favResult.total / pageSize.toFloat()).toInt(),
                "pageSize" to pageSize,
                "totalItems" to favResult.total,
                "sortBy" to sortBy.toString(),
                "sortDir" to sortDir.toString()
            )
        )
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 25
    }
}