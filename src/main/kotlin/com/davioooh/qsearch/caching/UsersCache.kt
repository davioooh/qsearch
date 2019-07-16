package com.davioooh.qsearch.caching

import com.davioooh.qsearch.stackexchange.api.model.User
import org.ehcache.Cache
import org.ehcache.CacheManager
import org.ehcache.config.builders.CacheConfigurationBuilder
import org.ehcache.config.builders.CacheManagerBuilder
import org.ehcache.config.builders.ResourcePoolsBuilder

object UsersCache : CacheFacade<User> {
    private val cacheManager: CacheManager = CacheManagerBuilder.newCacheManagerBuilder()
        .withCache(
            "usersCache",
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                String::class.java, User::class.java,
                ResourcePoolsBuilder.heap(100)
            )
        )
        .build(true)

    private val cache: Cache<String, User>
        get() = cacheManager.getCache("usersCache", String::class.java, User::class.java)

    override fun put(key: String, value: User): User = cache.put(key, value).let { return value }

    override fun get(key: String): User? = if (cache.containsKey(key)) cache.get(key) else null

}