package com.android.data.model

import com.android.domain.entity.AuthEntity
import com.android.domain.entity.ErrorEntity
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

data class ErrorResponse(
    @SerializedName("timestamp")
    val timestamp: Long,

    @SerializedName("status")
    val status: Int,

    @SerializedName("error")
    val error: String,

    @SerializedName("path")
    val path: String
)

fun ErrorResponse.toDomain() = ErrorEntity(
    timestamp = timestamp,
    status = status,
    error = error,
    path = path
)