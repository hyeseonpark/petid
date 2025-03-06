package com.petid.domain.usecase

import com.petid.domain.repository.S3UploadRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class UploadImageUseCase @Inject constructor(
    private val s3UploadRepository: S3UploadRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(
        imagePath: String,
        profileImage: ByteArray,
    ) =
        s3UploadRepository
            .getUrlForUploadImage(imagePath)
            .flatMapLatest { url ->
                s3UploadRepository
                    .uploadImageResource(
                        url = url,
                        byteArray = profileImage,
                    )
            }
}