package com.android.petid.di

import com.android.data.api.AuthAPI
import com.android.data.api.AuthInterceptor
import com.android.data.api.BannerAPI
import com.android.data.api.HosptialAPI
import com.android.data.api.LocationAPI
import com.android.data.api.LoggingInterceptor
import com.android.data.util.PreferencesHelper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {

    val BASE_URL = "http://yourpet-id.com:8080/"


    @Provides
    @Singleton
    fun provideLoggingInterceptor(): LoggingInterceptor {
        return LoggingInterceptor()
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(preferencesHelper: PreferencesHelper): AuthInterceptor {
        return AuthInterceptor(preferencesHelper)
    }


    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        loggingInterceptor: LoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    @Provides
    @Singleton
    fun provideAuthAPI(retrofit: Retrofit): AuthAPI {
        return retrofit.create(AuthAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideLocationAPI(retrofit: Retrofit): LocationAPI {
        return retrofit.create(LocationAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideHosptialAPI(retrofit: Retrofit): HosptialAPI {
        return retrofit.create(HosptialAPI::class.java)
    }
    @Provides
    @Singleton
    fun provideBannerAPI(retrofit: Retrofit): BannerAPI {
        return retrofit.create(BannerAPI::class.java)
    }
}