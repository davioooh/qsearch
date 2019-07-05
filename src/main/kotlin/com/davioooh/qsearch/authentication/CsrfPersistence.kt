package com.davioooh.qsearch.authentication

import io.javalin.http.Context

interface CsrfPersistence {
    fun persist(ctx: Context, csrf: String)
    fun retrieve(ctx: Context): String?
}