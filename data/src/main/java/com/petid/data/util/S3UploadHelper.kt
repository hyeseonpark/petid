package com.petid.data.util

import android.content.Context
import android.util.Log
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class S3UploadHelper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val transferUtility: TransferUtility,
) {
    suspend fun uploadWithTransferUtility(
        file: File,
        bucketName: String = "petid-bucket",
        keyName: String,
    ): Result<Boolean> {
        return suspendCancellableCoroutine { continuation ->
            try {
                TransferNetworkLossHandler.getInstance(context)

                val uploadObserver = transferUtility.upload(bucketName, keyName, file)

                uploadObserver.setTransferListener(object : TransferListener {
                    override fun onStateChanged(id: Int, state: TransferState?) {
                        if(continuation.isActive) {
                            if (state == TransferState.COMPLETED) {
                                continuation.resume(Result.success(true))
                            } else if (state == TransferState.FAILED) {
                                continuation.resume(Result.failure(Exception("Upload failed")))
                            }
                        }
                    }

                    override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                        val done = ((bytesCurrent / bytesTotal) * 100.0).toInt()
                        Log.d("S3UploadHelper", "UPLOAD - - ID: $id, percent done = $done")
                    }

                    override fun onError(id: Int, ex: Exception?) {
                        continuation.resume(Result.failure(ex ?: Exception("Unknown error")))
                    }
                })

                continuation.invokeOnCancellation {
                    try {
                        transferUtility.cancel(uploadObserver.id)
                    } catch (e: Exception) {
                        Log.e("S3UploadHelper", "Failed to cancel upload", e)
                    }
                }

            } catch (e: Exception) {
                continuation.resume(Result.failure(e))
            }
        }
    }
}
