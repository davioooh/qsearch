package com.davioooh.qsearch.handlers.ajax

import com.davioooh.qsearch.services.SortingCriteria.Activity
import com.davioooh.qsearch.services.SortingCriteria.Vote
import com.davioooh.qsearch.services.SortingDirection.Asc
import com.davioooh.qsearch.services.SortingDirection.Desc
import io.javalin.http.Context
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class HandlerCommonsTest {
    private val ctx = mockk<Context>(relaxed = true)

    @BeforeEach
    fun init() {
        clearAllMocks()
    }

    @Nested
    inner class PageParam {

        @Test
        fun `should return 1 when page param is not present`() {
            every { ctx.queryParam("page") } returns null

            val page = ctx.getPage()

            assertThat(page).isEqualTo(1)
        }

        @Test
        fun `should return 1 when page param is empty`() {
            every { ctx.queryParam("page") } returns ""

            val page = ctx.getPage()

            assertThat(page).isEqualTo(1)
        }

        @Test
        fun `should return 1 when page param is not valid`() {
            every { ctx.queryParam("page") } returns "uno"

            val page = ctx.getPage()

            assertThat(page).isEqualTo(1)
        }

        @Test
        fun `should return 3 when page param is '3'`() {
            every { ctx.queryParam("page") } returns "3"

            val page = ctx.getPage()

            assertThat(page).isEqualTo(3)
        }
    }

    @Nested
    inner class SortByParam {

        @Test
        fun `should return Activity when sortBy param is not present`() {
            every { ctx.queryParam("sortBy") } returns null

            val sortBy = ctx.getSortingCriteria()

            assertThat(sortBy).isEqualTo(Activity)
        }

        @Test
        fun `should return Activity when sortBy param is empty`() {
            every { ctx.queryParam("sortBy") } returns ""

            val sortBy = ctx.getSortingCriteria()

            assertThat(sortBy).isEqualTo(Activity)
        }

        @Test
        fun `should return Activity when sortBy param is not valid`() {
            every { ctx.queryParam("sortBy") } returns "Foo"

            val sortBy = ctx.getSortingCriteria()

            assertThat(sortBy).isEqualTo(Activity)
        }

        @Test
        fun `should return Vote when sortBy param is 'Vote'`() {
            every { ctx.queryParam("sortBy") } returns "Vote"

            val sortBy = ctx.getSortingCriteria()

            assertThat(sortBy).isEqualTo(Vote)
        }
    }

    @Nested
    inner class SortDirParam {

        @Test
        fun `should return Desc when sortDir param is not present`() {
            every { ctx.queryParam("sortDir") } returns null

            val sortDir = ctx.getSortingDirection()

            assertThat(sortDir).isEqualTo(Desc)
        }

        @Test
        fun `should return Desc when sortDir param is empty`() {
            every { ctx.queryParam("sortDir") } returns ""

            val sortDir = ctx.getSortingDirection()

            assertThat(sortDir).isEqualTo(Desc)
        }

        @Test
        fun `should return Desc when sortDir param is not valid`() {
            every { ctx.queryParam("sortDir") } returns "Foo"

            val sortDir = ctx.getSortingDirection()

            assertThat(sortDir).isEqualTo(Desc)
        }

        @Test
        fun `should return Asc when sortDir param is 'Asc'`() {
            every { ctx.queryParam("sortDir") } returns "Asc"

            val sortDir = ctx.getSortingDirection()

            assertThat(sortDir).isEqualTo(Asc)
        }
    }

    @Nested
    inner class SearchKeyParam {

        @Test
        fun `should return null when query param is not present`() {
            every { ctx.queryParam("query") } returns null

            val tags = ctx.getSearchKey()

            assertThat(tags).isNull()
        }

        @Test
        fun `should return correct value when query param is present`() {
            every { ctx.queryParam("query") } returns " yeah        "

            val tags = ctx.getSearchKey()

            assertThat(tags).isEqualTo("yeah")
        }
    }

    @Nested
    inner class SearchTagsParam {

        @Test
        fun `should return empty list when tags param is not present`() {
            every { ctx.queryParam("tags") } returns null

            val tags = ctx.getSearchTags()

            assertThat(tags).isEqualTo(listOf<String>())
        }

        @Test
        fun `should return empty list when tags param is empty`() {
            every { ctx.queryParam("tags") } returns ""

            val tags = ctx.getSearchTags()

            assertThat(tags).isEqualTo(listOf<String>())
        }

        @Test
        fun `should return populated list when tags param is present and has value`() {
            every { ctx.queryParam("tags") } returns "a,b  , c"

            val tags = ctx.getSearchTags()

            assertThat(tags).isEqualTo(listOf("a", "b", "c"))
        }
    }
}