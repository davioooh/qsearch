package com.davioooh.qsearch.utils

interface TokenEncryption {
    fun encrypt(txt: String): String
    fun decrypt(txt: String): String
}