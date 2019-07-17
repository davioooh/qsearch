package com.davioooh.qsearch.caching

import com.davioooh.qsearch.services.Questions
import com.davioooh.qsearch.stackexchange.api.model.User

object UsersCache : AbstractCacheFacadeImpl<User>(User::class)

object QuestionsCache : AbstractCacheFacadeImpl<QuestionsWrapper>(QuestionsWrapper::class)

class QuestionsWrapper(val questions: Questions) // WORKAROUND