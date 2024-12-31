package com.petid.data.ml

import android.content.Context
import android.graphics.Bitmap
import com.petid.data.ml.ImageClassifierHelper.ResultBundle

class ClassifierImageAnalyzerImpl(
    private val context: Context,
) : ClassifierImageAnalyzer {
    private var imageClassifier: ImageClassifierHelper? = null

    override suspend fun analyzeImage(bitmap: Bitmap): ClassifierResult<ResultBundle> {
        imageClassifier = ImageClassifierHelper(context = context)
        return when(val result = imageClassifier?.classifyImage(bitmap)) {
            is ClassifierResult.Success -> {
                imageClassifier?.clearImageClassifier()
                result
            }
            is ClassifierResult.Error -> result
            null -> ClassifierResult.Error("is null") //TODO
        }
    }
}