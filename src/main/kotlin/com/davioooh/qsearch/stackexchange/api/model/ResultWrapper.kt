package com.davioooh.qsearch.stackexchange.api.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ResultWrapper<T>(
    @JsonProperty("backoff") val backoff: Int? = null,
    @JsonProperty("error_id") val errorId: Int? = null,
    @JsonProperty("error_message") val errorMessage: String? = null,
    @JsonProperty("error_name") val errorName: String? = null,
    @JsonProperty("has_more") val hasMore: Boolean,
    @JsonProperty("items") val items: List<T>,
    @JsonProperty("page") val page: Int? = null,
    @JsonProperty("page_size") val pageSize: Int? = null,
    @JsonProperty("quota_max") val quotaMax: Int,
    @JsonProperty("quota_remaining") val quotaRemaining: Int,
    @JsonProperty("total") val total: Int? = null,
    @JsonProperty("type") val type: String? = null
)