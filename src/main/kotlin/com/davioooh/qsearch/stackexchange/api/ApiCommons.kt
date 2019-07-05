package com.davioooh.qsearch.stackexchange.api

import com.github.kittinunf.fuel.core.FuelManager

const val AUTH_BASE = "https://stackoverflow.com/oauth"
const val API_BASE = "https://api.stackexchange.com/2.2"

data class ApiClientConfig(val site: String, val key: String, val filter: String? = null)

fun buildHttpClient(conf: ApiClientConfig) = FuelManager()
    .apply {
        basePath = API_BASE
        baseParams = listOf(
            "key" to conf.key,
            "site" to conf.site,
            "filter" to (conf.filter ?: "default")//"!9Z(-x-Ptf"
        )
    }