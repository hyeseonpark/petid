/*
 * Copyright 2022 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.petid.data.ml

import android.content.Context
import android.graphics.Bitmap
import android.os.SystemClock
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.core.Delegate
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.imageclassifier.ImageClassifier
import com.google.mediapipe.tasks.vision.imageclassifier.ImageClassifierResult

class ImageClassifierHelper(
    private var threshold: Float = THRESHOLD_DEFAULT,
    private var maxResults: Int = MAX_RESULTS_DEFAULT,
    private var currentDelegate: Int = DELEGATE_CPU,
    private var runningMode: RunningMode = RunningMode.IMAGE,
    val context: Context,
) {

    // For this example this needs to be a var so it can be reset on changes. If the ImageClassifier
    // will not change, a lazy val would be preferable.
    private var imageClassifier: ImageClassifier? = null

    init {
        setupImageClassifier()
    }

    // Classifier must be closed when creating a new one to avoid returning results to a
    // non-existent object
    fun clearImageClassifier() {
        imageClassifier?.close()
        imageClassifier = null
    }

    // Return running status of image classifier helper
    fun isClosed(): Boolean {
        return imageClassifier == null
    }

    // Initialize the image classifier using current settings on the
    // thread that is using it. CPU can be used with detectors
    // that are created on the main thread and used on a background thread, but
    // the GPU delegate needs to be used on the thread that initialized the
    // classifier
    private fun setupImageClassifier() {
        val baseOptionsBuilder = BaseOptions.builder()
        when (currentDelegate) {
            DELEGATE_CPU -> {
                baseOptionsBuilder.setDelegate(Delegate.CPU)
            }

            DELEGATE_GPU -> {
                baseOptionsBuilder.setDelegate(Delegate.GPU)
            }
        }

        val modelName ="petid_crop_image_mobilenet_v1_2.tflite"

        baseOptionsBuilder.setModelAssetPath(modelName)

        try {
            val baseOptions = baseOptionsBuilder.build()
            val optionsBuilder =
                ImageClassifier.ImageClassifierOptions.builder()
                    .setScoreThreshold(threshold)
                    .setMaxResults(maxResults)
                    .setRunningMode(runningMode)
                    .setBaseOptions(baseOptions)

            val options = optionsBuilder.build()
            imageClassifier =
                ImageClassifier.createFromOptions(context, options)
        } catch (e: IllegalStateException) {
            ClassifierResult.Error("Image classifier failed to initialize. See error logs for details")
            /*Log.e(
                TAG,
                "Image classifier failed to load model with error: " + e.message
            )*/
        } catch (e: RuntimeException) {
            // This occurs if the model being used does not support GPU
            ClassifierResult.Error(
                "Image classifier failed to initialize. See error logs for " +
                        "details", GPU_ERROR
            )
            /*Log.e(
                TAG,
                "Image classifier failed to load model with error: " + e.message
            )*/
        }
    }

    // Accepted a Bitmap and runs image classification inference on it to
    // return results back to the caller
    fun classifyImage(image: Bitmap): ClassifierResult<ResultBundle> {
        if (runningMode != RunningMode.IMAGE) {
            throw IllegalArgumentException(
                "Attempting to call classifyImage" +
                        " while not using RunningMode.IMAGE"
            )
        }

        if (imageClassifier == null) return ClassifierResult.Error("Image classifier is null.")

        // Inference time is the difference between the system time at the start and finish of the
        // process
        val startTime = SystemClock.uptimeMillis()

        // Convert the input Bitmap object to an MPImage object to run inference
        val mpImage = BitmapImageBuilder(image).build()

        // Run image classification using MediaPipe Image Classifier API
        imageClassifier?.classify(mpImage)?.also { classificationResults ->
            val inferenceTimeMs = SystemClock.uptimeMillis() - startTime
            val result = ResultBundle(listOf(classificationResults), inferenceTimeMs)
            return ClassifierResult.Success(result)
        }

        // If imageClassifier?.classify() returns null, this is likely an error. Returning null
        // to indicate this.
        return ClassifierResult.Error("Image classifier failed to classify.")
        /*imageClassifierListener?.onError(
            "Image classifier failed to classify."
        )
        return null*/
    }

    // Wraps results from inference, the time it takes for inference to be
    // performed.
    data class ResultBundle(
        val results: List<ImageClassifierResult>,
        val inferenceTime: Long,
    )

    companion object {
        const val DELEGATE_CPU = 0
        const val DELEGATE_GPU = 1
        const val MAX_RESULTS_DEFAULT = 1
        const val THRESHOLD_DEFAULT = 0.5F
        const val OTHER_ERROR = 0
        const val GPU_ERROR = 1
    }
}

sealed class ClassifierResult<out T> {
    data class Success<T>(val data: ImageClassifierHelper.ResultBundle) : ClassifierResult<T>()
    data class Error(
        val error: String?,
        val errorCode: Int = ImageClassifierHelper.OTHER_ERROR) : ClassifierResult<Nothing>()
}
