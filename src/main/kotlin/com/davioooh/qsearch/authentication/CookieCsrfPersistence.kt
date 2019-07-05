package com.davioooh.qsearch.authentication

import io.javalin.http.Context

class CookieCsrfPersistence : CsrfPersistence {
    override fun retrieve(ctx: Context): String? = ctx.cookie(CSRF_COOKIE_NAME)


    override fun persist(ctx: Context, csrf: String) {
        ctx.cookie(CSRF_COOKIE_NAME, csrf)
    }

    companion object {
        const val CSRF_COOKIE_NAME = "so_at"
    }
}