package com.davioooh.qsearch.services

import com.davioooh.qsearch.authentication.authenticatedUser
import com.davioooh.qsearch.caching.CacheFacade
import com.davioooh.qsearch.caching.QuestionsWrapper
import com.davioooh.qsearch.model.toQuestionDetailsList
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
    private val cache = mockk<CacheFacade<QuestionsWrapper>>(relaxed = true)
    private val searchIndex = mockk<FullTextSearchIndex<QuestionItem, Int>>(relaxed = true)
    private val userFavoritesService = QuestionsService(questionsApi, cache, searchIndex)

    @BeforeEach
    fun init() {
        clearAllMocks()
    }

    @ParameterizedTest
    @MethodSource("itemsConfigurations")
    fun `returns favorites questions filtered by search criteria`(pageConfig: QuestionsPageConfig) {
        val authenticatedUser = authenticatedUser()
        val allItems = questionsList(pageConfig.itemsCount)
        val filteredItems = allItems.subList(0, pageConfig.filteredItemsCount)
        val searchCriteria = SearchCriteria(pageConfig.searchKey)

        every {
            questionsApi.fetchUserFavoriteQuestions(
                authenticatedUser.accessToken,
                authenticatedUser.userId
            )
        } returns ResultWrapper(
            hasMore = false,
            items = allItems,
            quotaMax = 999,
            quotaRemaining = 999
        )

        every { cache.get(any()) } returns null
        every { searchIndex.search(any()) } returns filteredItems.map { it.questionId }


        val result = userFavoritesService.searchUserFavorites(
            authenticatedUser.userId,
            authenticatedUser.accessToken,
            PaginationCriteria(pageConfig.page, pageConfig.pageSize),
            searchCriteria
        )

        assertThat(result).isEqualTo(
            if (pageConfig.itemsCount > 0) {
                SearchPageResult(
                    PageResult(
                        if (filteredItems.isNotEmpty())
                            filteredItems.chunked(pageConfig.pageSize)[pageConfig.page - 1].toQuestionDetailsList()
                        else
                            listOf(),
                        filteredItems.size,
                        PaginationCriteria(pageConfig.page, pageConfig.pageSize)
                    ),
                    pageConfig.itemsCount,
                    searchCriteria
                )
            } else null
        )
    }

    private fun itemsConfigurations() =
        IntRange(0, 100).step(10)
            .flatMap { itemsCount ->
                IntRange(0, itemsCount).step(10)
                    .map { filteredItemsCount ->
                        QuestionsPageConfig(1, 10, itemsCount, filteredItemsCount)
                    }
            }

    data class QuestionsPageConfig(
        val page: Int,
        val pageSize: Int,
        val itemsCount: Int,
        val filteredItemsCount: Int,
        val searchKey: String = "aa"
    )

}