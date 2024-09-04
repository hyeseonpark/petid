package com.android.petid.di

import android.content.Context
import com.android.data.util.PreferencesHelper
import com.android.petid.util.PreferencesControl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Provides
    @Singleton
    fun providePreferencesHelper(@ApplicationContext context: Context): PreferencesHelper {
        return PreferencesControl(context)
    }

    @Provides
    @Singleton
    fun providePreferencesControl(@ApplicationContext context: Context): PreferencesControl {
        return PreferencesControl(context)
    }
}