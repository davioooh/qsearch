package com.davioooh.qsearch.handlers.ajax

import com.davioooh.qsearch.services.SortingCriteria.Activity
import com.davioooh.qsearch.services.SortingDirection.Desc
import com.davioooh.qsearch.utils.enumValueOrDefault
import io.javalin.http.Context

fun Context.getPage(): Int =
    this.queryParam("page")?.takeIf { it.isNotBlank() }?.toIntOrNull()?.let { if (it > 0) it else 1 } ?: 1

fun Context.getSortingCriteria() =
    this.queryParam("sortBy")?.let { enumValueOrDefault(it, Activity) } ?: Activity

fun Context.getSortingDirection() =
    this.queryParam("sortDir")?.let { enumValueOrDefault(it, Desc) } ?: Desc

fun Context.getSearchKey() =
    this.queryParam("query")?.trim()

fun Context.getSearchTags() =
    this.queryParam("tags")?.takeIf { it.isNotBlank() }?.split(",")?.map { it.trim() } ?: listOf()