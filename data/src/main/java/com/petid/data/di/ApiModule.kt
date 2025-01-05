package com.petid.data.di

import com.petid.data.api.AuthAPI
import com.petid.data.api.AuthAuthenticator
import com.petid.data.api.AuthInterceptor
import com.petid.data.api.BannerAPI
import com.petid.data.api.ContentAPI
import com.petid.data.api.HosptialAPI
import com.petid.data.api.LocationAPI
import com.petid.data.api.LoggingInterceptor
import com.petid.data.api.NullOnEmptyConverterFactory
import com.petid.data.api.PetAPI
import com.petid.data.api.MemberAPI
import com.petid.data.util.PreferencesHelper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.petid.data.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {

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
    fun provideAuthAuthenticator(
        preferencesHelper: PreferencesHelper,
    ): AuthAuthenticator {
        return AuthAuthenticator(preferencesHelper)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        authAuthenticator: AuthAuthenticator,
        loggingInterceptor: LoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .authenticator(authAuthenticator)
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
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(NullOnEmptyConverterFactory())
            .addConverterFactory(ScalarsConverterFactory.create())
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

    @Provides
    @Singleton
    fun provideContentAPI(retrofit: Retrofit): ContentAPI {
        return retrofit.create(ContentAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideUserAPI(retrofit: Retrofit): MemberAPI {
        return retrofit.create(MemberAPI::class.java)
    }

    @Provides
    @Singleton
    fun providePetAPI(retrofit: Retrofit): PetAPI {
        return retrofit.create(PetAPI::class.java)
    }
}