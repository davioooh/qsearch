package com.davioooh

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get

fun main(args: Array<String>) {
    Javalin.create()
        .start()
        .routes {
            get("/") { it.render("/templates/index.html") }
        }
}

