package com.petid.data.dto.request

import com.petid.domain.entity.FilePath

data class FilePathRequest (
    val filePath: String
) {
    fun toDomain(): FilePath {
        return FilePath(
            filePath = filePath
        )
    }
}

fun FilePath.toDto(): FilePathRequest {
    return FilePathRequest(
        filePath = filePath
    )
}
