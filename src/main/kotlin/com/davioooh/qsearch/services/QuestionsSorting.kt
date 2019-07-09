package com.davioooh.qsearch.services

import com.davioooh.qsearch.services.SortingCriteria.*
import com.davioooh.qsearch.services.SortingDirection.Asc
import com.davioooh.qsearch.services.SortingDirection.Desc
import com.davioooh.qsearch.stackexchange.api.model.Question

enum class SortingCriteria {
    Vote, Activity, Asked, Views;
}

enum class SortingDirection {
    Asc, Desc
}

typealias Questions = List<Question>

fun Questions.sortBy(
    criteria: SortingCriteria,
    direction: SortingDirection = Asc
): List<Question> = when (criteria) {
    Activity -> this.sortedBy { it.lastActivityDate }
    Vote -> this.sortedBy { it.score }
    Asked -> this.sortedBy { it.creationDate }
    Views -> this.sortedBy { it.viewCount }
}.let {
    if (direction == Desc) it.reversed() else it
}
