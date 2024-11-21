package com.android.data.util

import android.content.Context
import android.util.Log
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.android.data.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File

class S3UploadHelper {
    fun uploadWithTransferUtility(
        context: Context,
        file: File,
        scope: CoroutineScope,
        bucketName: String = "petid-bucket",
        keyName: String,
        onUploadResult: suspend (Result<Boolean>) -> Unit
    ) {
        scope.launch {
            try {
                val awsCredentials = BasicAWSCredentials(
                    BuildConfig.AWS_ACCESS_KEY,
                    BuildConfig.AWS_SECRET_KEY
                )

                val s3Client = AmazonS3Client(
                    awsCredentials,
                    Region.getRegion(Regions.AP_NORTHEAST_2)
                )

                val transferUtility = TransferUtility.builder()
                    .s3Client(s3Client)
                    .context(context)
                    .build()

                TransferNetworkLossHandler.getInstance(context)

                val uploadObserver = transferUtility.upload(bucketName, keyName, file)

                uploadObserver.setTransferListener(object : TransferListener {
                    override fun onStateChanged(id: Int, state: TransferState?) {
                        scope.launch {
                            if (state == TransferState.COMPLETED) {
                                onUploadResult(Result.success(true))
                            } else if (state == TransferState.FAILED) {
                                onUploadResult(Result.failure(Exception("Upload failed")))
                            }
                        }
                    }

                    override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                        val done = ((bytesCurrent / bytesTotal) * 100.0).toInt()
                        Log.d("S3UploadHelper", "UPLOAD - - ID: $id, percent done = $done")
                    }

                    override fun onError(id: Int, ex: Exception?) {
                        scope.launch {
                            onUploadResult(Result.failure(ex ?: Exception("Unknown error")))
                        }
                    }
                })
            } catch (e: Exception) {
                onUploadResult(Result.failure(e))
            }
        }
    }
}
