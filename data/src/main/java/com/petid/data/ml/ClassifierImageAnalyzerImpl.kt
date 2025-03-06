package com.petid.data.ml

import android.content.Context
import android.graphics.Bitmap
import com.petid.data.ml.ImageClassifierHelper.ResultBundle
import com.petid.data.util.sendCrashlytics

class ClassifierImageAnalyzerImpl(
    private val context: Context,
) : ClassifierImageAnalyzer {
    private var imageClassifier: ImageClassifierHelper? = null

    override suspend fun analyzeImage(bitmap: Bitmap): ClassifierResult<ResultBundle> {
        return runCatching {
            imageClassifier = ImageClassifierHelper(context = context)
            imageClassifier?.classifyImage(bitmap)
        }.fold(
            onSuccess = { result ->
                when (result) {
                    is ClassifierResult.Success -> {
                        imageClassifier?.clearImageClassifier()
                        result
                    }
                    is ClassifierResult.Error -> result
                    null -> ClassifierResult.Error("Result is null")
                }
            },
            onFailure = {
                it.sendCrashlytics()
                ClassifierResult.Error("Error occurred: ${it.message}")
            }
        )

//        return when(val result = imageClassifier?.classifyImage(bitmap)) {
//            is ClassifierResult.Success -> {
//                imageClassifier?.clearImageClassifier()
//                result
//            }
//            is ClassifierResult.Error -> result
//            null -> ClassifierResult.Error("is null") //TODO
//        }
    }
}