package com.davioooh.qsearch.utils

inline fun <reified T : Enum<*>> enumValueOrDefault(name: String, defaultValue: T? = null): T? =
    T::class.java.enumConstants.firstOrNull { it.name == name } ?: defaultValue