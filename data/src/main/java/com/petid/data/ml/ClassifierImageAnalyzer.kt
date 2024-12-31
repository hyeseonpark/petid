package com.petid.data.ml

import android.graphics.Bitmap
import com.petid.data.ml.ImageClassifierHelper.ResultBundle

interface ClassifierImageAnalyzer {
    suspend fun analyzeImage(bitmap: Bitmap): ClassifierResult<ResultBundle>
}