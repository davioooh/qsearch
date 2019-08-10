package com.davioooh.qsearch.services

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

internal class SearchCriteriaTest {

    @ParameterizedTest
    @MethodSource("testCases")
    fun `verifies correctly if search criteria has valid values`(testCase: TestCase) {
        assertThat(testCase.toSearchCriteria().hasValues).isEqualTo(testCase.expectedResult)
    }

    private fun testCases() = listOf(
        TestCase(null, expectedResult = false),
        TestCase("", expectedResult = false),
        TestCase("key", expectedResult = true),
        TestCase(null, expectedResult = false),
        TestCase(null, listOf("a", "b"), true),
        TestCase("key", listOf("a", "b"), true)
    )

    data class TestCase(val query: String?, val tags: List<String> = listOf(), val expectedResult: Boolean) {
        fun toSearchCriteria() = SearchCriteria(query, tags)
    }
}