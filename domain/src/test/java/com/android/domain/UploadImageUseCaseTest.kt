package com.android.domain

import com.petid.domain.repository.S3UploadRepository
import com.petid.domain.usecase.UploadImageUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test


class UploadImageUseCaseTest {
    private val s3UploadRepository: S3UploadRepository = mockk(relaxed = true)
    private val uploadImageUseCase = UploadImageUseCase(s3UploadRepository)

    @Test
    fun `서버에서_Url을_받아오고_해당_URL에_이미지를_업로드한다`() = runTest {
        // given
        val imagePath = "test/path"
        val profileImage = ByteArray(10) {1}
        val expectedUrl = "https://example.com/upload"

        coEvery { s3UploadRepository.getUrlForUploadImage(imagePath) } returns flowOf(expectedUrl)
        coEvery { s3UploadRepository.uploadImageResource(expectedUrl, profileImage) } returns flowOf(Unit)

        // when
        val result = uploadImageUseCase(imagePath, profileImage).first()

        // then
        assertEquals(result, Unit)
        coVerify { s3UploadRepository.getUrlForUploadImage(imagePath) }
        coVerify { s3UploadRepository.uploadImageResource(expectedUrl, profileImage) }
    }
}