package com.davioooh.qsearch.model

class PaginationBar(
    val buttonsBar: Array<PaginationButton>,
    val currentPage: Int,
    val firstButton: PaginationButton? = null,
    val lastButton: PaginationButton? = null,
    val previousButton: PaginationButton? = null,
    val nextButton: PaginationButton? = null
) {
    companion object {
        fun from(currentPage: Int, lastPage: Int, baseUrl: String = "/"): PaginationBar {
            require(currentPage >= 1) { "currentPage can't be lower than 1" }
            require(currentPage <= lastPage) { "currentPage ($currentPage) can't be greater than lastPage ($lastPage)" }

            val btnUrlTemplate = "$baseUrl?page=%s"

            val prevButton = when (currentPage) {
                1 -> PaginationButton("#", "Prev", false)
                else -> PaginationButton(btnUrlTemplate.format(currentPage - 1), "Prev")
            }
            val nextButton = when (currentPage) {
                lastPage -> PaginationButton("#", "Next", false)
                else -> PaginationButton(btnUrlTemplate.format(currentPage + 1), "Next")
            }

            val btnBarRange =
                if (lastPage in 1..10) 1..lastPage
                else when (currentPage) {
                    in 1..4 -> 1..5
                    in (lastPage - 3)..lastPage -> (lastPage - 4)..lastPage
                    else -> IntRange(
                        if (currentPage - 2 >= 1) currentPage - 2 else 1,
                        if (currentPage + 2 <= lastPage) currentPage + 2 else lastPage
                    )
                }

            val firstButton =
                when (lastPage) {
                    in 1..10 -> null
                    else -> when (currentPage) {
                        in 1..4 -> null
                        else -> PaginationButton(btnUrlTemplate.format(1), 1.toString())
                    }
                }

            val lastButton =
                when (lastPage) {
                    in 1..10 -> null
                    else -> when (currentPage) {
                        in (lastPage - 3)..lastPage -> null
                        else -> PaginationButton(btnUrlTemplate.format(lastPage), lastPage.toString())
                    }
                }

            return PaginationBar(
                btnBarRange.map { PaginationButton(btnUrlTemplate.format(it), it.toString()) }.toTypedArray(),
                currentPage,
                firstButton = firstButton,
                lastButton = lastButton,
                previousButton = prevButton,
                nextButton = nextButton
            )
        }
    }
}

data class PaginationButton(
    val url: String,
    val text: String,
    val enabled: Boolean = true
    //, val visible: Boolean = true
)