package com.davioooh.qsearch.caching

import org.ehcache.Cache
import org.ehcache.CacheManager
import org.ehcache.config.builders.CacheConfigurationBuilder
import org.ehcache.config.builders.CacheManagerBuilder
import org.ehcache.config.builders.ResourcePoolsBuilder
import java.util.*
import kotlin.reflect.KClass

abstract class AbstractCacheFacadeImpl<T : Any>(private val valueClass: KClass<T>) :
    CacheFacade<T> {
    private val cacheName = "${UUID.randomUUID()}-${valueClass.simpleName}-cache"

    private val cacheManager: CacheManager = CacheManagerBuilder.newCacheManagerBuilder()
        .withCache(
            cacheName,
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                String::class.java, valueClass.java,
                ResourcePoolsBuilder.heap(100)
            )
        )
        .build(true)

    private val cache: Cache<String, T>
        get() = cacheManager.getCache(cacheName, String::class.java, valueClass.java)

    override fun put(key: String, value: T): T = cache.put(key, value).let { return value }

    override fun get(key: String): T? = if (cache.containsKey(key)) cache.get(key) else null


}