package com.petid.data.repository.remote

import com.petid.data.api.MemberAPI
import com.petid.data.api.S3UploadAPI
import com.petid.data.di.S3Retrofit
import com.petid.domain.repository.S3UploadRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class S3UploadRepositoryImpl
@Inject
constructor(
    @S3Retrofit private val s3UploadAPI: S3UploadAPI,
    private val memberAPI: MemberAPI,
) : S3UploadRepository {
    override suspend fun getUrlForUploadImage(imagePath: String): Flow<String> =
        flow {
            val result = memberAPI.uploadProfileImage(imagePath)
            emit(result)
        }.flowOn(Dispatchers.IO)

    override fun uploadImageResource(
        url: String,
        byteArray: ByteArray,
    ): Flow<Unit> =
        flow {
            val requestBody = byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull())
            val res = s3UploadAPI.uploadImage(url = url, body = requestBody)
            emit(res)
        }.flowOn(Dispatchers.IO)
}
