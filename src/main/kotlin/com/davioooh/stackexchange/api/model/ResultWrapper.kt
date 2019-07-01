package com.davioooh.stackexchange.api.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ResultWrapper<T>(
    @JsonProperty("backoff") val backoff: Int?,
    @JsonProperty("error_id") val errorId: Int?,
    @JsonProperty("error_message") val errorMessage: String?,
    @JsonProperty("error_name") val errorName: String?,
    @JsonProperty("has_more") val hasMore: Boolean,
    @JsonProperty("items") val items: List<T>,
    @JsonProperty("page") val page: Int,
    @JsonProperty("page_size") val pageSize: Int,
    @JsonProperty("quota_max") val quotaMax: Int,
    @JsonProperty("quota_remaining") val quotaRemaining: Int,
    @JsonProperty("total") val total: Int,
    @JsonProperty("type") val type: String?
)