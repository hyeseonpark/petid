package com.petid.data.di

import android.content.Context
import com.petid.data.ml.ClassifierImageAnalyzer
import com.petid.data.ml.ClassifierImageAnalyzerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MLModule {
    @Provides
    @Singleton
    fun provideImageAnalyzer(
        @ApplicationContext context: Context
    ): ClassifierImageAnalyzer {
        return ClassifierImageAnalyzerImpl(context)
    }
}