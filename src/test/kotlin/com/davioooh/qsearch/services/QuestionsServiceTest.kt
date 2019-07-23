package com.davioooh.qsearch.services

import com.davioooh.qsearch.authentication.authenticatedUser
import com.davioooh.qsearch.caching.CacheFacade
import com.davioooh.qsearch.caching.QuestionsWrapper
import com.davioooh.qsearch.stackexchange.api.QuestionsApi
import com.davioooh.qsearch.stackexchange.api.model.ResultWrapper
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

internal class QuestionsServiceTest {
    private val questionsApi = mockk<QuestionsApi>(relaxed = true)
    private val userFavoritesService = QuestionsService(questionsApi, NoopCache(), NoopSearchIndex())

    @BeforeEach
    fun init() {
        clearAllMocks()
    }

    @ParameterizedTest
    @MethodSource("itemsConfigurations")
    fun `returns ALL favorites questions correctly`(pageConfig: QuestionsPageConfig) {
        val authenticatedUser = authenticatedUser()
        val items = questionsList(pageConfig.itemsCount)

        every {
            questionsApi.fetchUserFavoriteQuestions(
                authenticatedUser.accessToken,
                authenticatedUser.userId
            )
        } returns ResultWrapper(
            hasMore = false,
            items = items,
            quotaMax = 999,
            quotaRemaining = 999
        )

        val result = userFavoritesService.getUserFavorites(
            authenticatedUser.userId,
            authenticatedUser.accessToken,
            PaginationCriteria(pageConfig.page, pageConfig.pageSize)
        )

        assertThat(result).isEqualTo(
            if (pageConfig.itemsCount > 0) {
                PageResult(
                    items.chunked(pageConfig.pageSize)[pageConfig.page - 1],
                    pageConfig.page,
                    pageConfig.pageSize,
                    items.size
                )
            } else null
        )
    }

    private fun itemsConfigurationsZeroItems() =
        IntRange(1, 25).filter { it % 3 == 0 } // step 3
            .map { pageSize ->
                QuestionsPageConfig(1, pageSize, 0)
            }

    private fun itemsConfigurations() =
        IntRange(1, 100).filter { it % 5 == 0 } // step 5
            .flatMap { itemsCount ->
                IntRange(1, 25).filter { it % 3 == 0 } // step 3
                    .flatMap { pageSize ->
                        IntRange(1, calculateLastPage(itemsCount, pageSize)).filter { it % 2 == 0 } // step 2
                            .map { currentPage -> QuestionsPageConfig(currentPage, pageSize, itemsCount) }
                    }
            } + itemsConfigurationsZeroItems()

    data class QuestionsPageConfig(val page: Int, val pageSize: Int, val itemsCount: Int)

    class NoopCache : CacheFacade<QuestionsWrapper> {
        override fun put(key: String, value: QuestionsWrapper): QuestionsWrapper {
            return value
        }

        override fun get(key: String): QuestionsWrapper? {
            return null
        }

    }

    class NoopSearchIndex : FullTextSearchIndex<QuestionItem, Int> {
        override fun addToIndex(vararg item: QuestionItem) {
        }

        override fun search(searchKey: String): List<Int> {
            return listOf()
        }

    }
}