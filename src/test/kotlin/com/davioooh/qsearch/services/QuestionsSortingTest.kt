package com.davioooh.qsearch.services

import com.davioooh.qsearch.services.SortingCriteria.*
import com.davioooh.qsearch.services.SortingDirection.Desc
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class QuestionsSortingTest {
    private val unsortedQuestions: Questions
    private val activitySortedQuestions: Questions
    private val activitySortedDescQuestions: Questions
    private val voteSortedQuestions: Questions
    private val voteSortedDescQuestions: Questions
    private val askedSortedQuestions: Questions
    private val askedSortedDescQuestions: Questions
    private val viewsSortedQuestions: Questions
    private val viewsSortedDescQuestions: Questions

    init {
        val date = LocalDateTime.of(2019, 2, 3, 10, 0, 0)
        unsortedQuestions = listOf(
            question(1, "Question 1", date, date.plusDays(2), 2, -100),
            question(2, "Question 2", date.plusDays(5), date.plusDays(5), 1, 100),
            question(3, "Question 3", date.plusMonths(6), date.plusDays(3), 0, 1000),
            question(4, "Question 4", date.minusMonths(6), date.minusDays(2), 3, 3),
            question(5, "Question 5", date.plusMonths(3), date.plusDays(2), 6, 10),
            question(6, "Question 6", date.minusWeeks(2), date.minusDays(1), 2, 2000)
        )

        activitySortedQuestions = unsortedQuestions.sortedBy { it.lastActivityDate }
        activitySortedDescQuestions = activitySortedQuestions.reversed()

        voteSortedQuestions = unsortedQuestions.sortedBy { it.score }
        voteSortedDescQuestions = voteSortedQuestions.reversed()

        askedSortedQuestions = unsortedQuestions.sortedBy { it.creationDate }
        askedSortedDescQuestions = askedSortedQuestions.reversed()

        viewsSortedQuestions = unsortedQuestions.sortedBy { it.viewCount }
        viewsSortedDescQuestions = viewsSortedQuestions.reversed()
    }

    @Nested
    inner class ByActivity {

        @Test
        fun `sort by Activity correctly`() {
            val sQst = unsortedQuestions.sortBy(Activity)

            assertThat(sQst).isEqualTo(activitySortedQuestions)
        }

        @Test
        fun `sort desc by Activity correctly`() {
            val sQst = unsortedQuestions.sortBy(Activity, Desc)

            assertThat(sQst).isEqualTo(activitySortedDescQuestions)
        }

    }

    @Nested
    inner class ByVote {

        @Test
        fun `sort by Vote correctly`() {
            val sQst = unsortedQuestions.sortBy(Vote)

            assertThat(sQst).isEqualTo(voteSortedQuestions)
        }

        @Test
        fun `sort desc by Vote correctly`() {
            val sQst = unsortedQuestions.sortBy(Vote, Desc)

            assertThat(sQst).isEqualTo(voteSortedDescQuestions)
        }

    }

    @Nested
    inner class ByAsked {

        @Test
        fun `sort by Asked correctly`() {
            val sQst = unsortedQuestions.sortBy(Asked)

            assertThat(sQst).isEqualTo(askedSortedQuestions)
        }


        @Test
        fun `sort desc by Asked correctly`() {
            val sQst = unsortedQuestions.sortBy(Asked, Desc)

            assertThat(sQst).isEqualTo(askedSortedDescQuestions)
        }

    }

    @Nested
    inner class ByViews {

        @Test
        fun `sort by Views correctly`() {
            val sQst = unsortedQuestions.sortBy(Views)

            assertThat(sQst).isEqualTo(viewsSortedQuestions)
        }

        @Test
        fun `sort desc by Views correctly`() {
            val sQst = unsortedQuestions.sortBy(Views, Desc)

            assertThat(sQst).isEqualTo(viewsSortedDescQuestions)
        }

    }

}