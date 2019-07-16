package com.davioooh.qsearch.caching

interface CacheFacade<T> {
    fun put(key: String, value: T): T
    fun get(key: String): T?
}