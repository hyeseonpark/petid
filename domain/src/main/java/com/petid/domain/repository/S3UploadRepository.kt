package com.petid.domain.repository

import kotlinx.coroutines.flow.Flow

interface S3UploadRepository {
    suspend fun getUrlForUploadImage(imagePath: String): Flow<String>

    fun uploadImageResource(
        url: String,
        byteArray: ByteArray,
    ): Flow<Unit>
}