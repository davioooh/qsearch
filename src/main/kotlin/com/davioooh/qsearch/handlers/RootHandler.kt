package com.davioooh.qsearch.handlers

import io.javalin.http.Context
import io.javalin.http.Handler

class RootHandler : Handler {
    override fun handle(ctx: Context) {
        ctx.render("/templates/index.html")
    }
}