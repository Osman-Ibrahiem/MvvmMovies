package com.data.remote.model

import com.google.gson.annotations.SerializedName

data class RemoteResponse<T>(
    @SerializedName("page")
    var page: Int? = null,
    @SerializedName("total_pages")
    var totalPages: Int? = null,
    @SerializedName("total_results")
    var totalResults: Int? = null,
    @SerializedName("errors")
    var errors: List<String>? = null,
    @SerializedName("results")
    var results: T? = null,
)