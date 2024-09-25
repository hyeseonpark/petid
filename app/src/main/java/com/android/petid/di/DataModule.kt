package com.android.petid.di

import com.android.data.source.remote.HomeMainRemoteDataSource
import com.android.data.source.remote.HomeMainRemoteDataSourceImpl
import com.android.data.source.remote.HospitalMainRemoteDataSource
import com.android.data.source.remote.HospitalMainRemoteDataSourceImpl
import com.android.data.source.remote.SocialAuthRemoteDataSource
import com.android.data.source.remote.SocialAuthRemoteDataSourceImpl
import com.android.data.source.remote.TermsRemoteDataSource
import com.android.data.source.remote.TermsRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindLoginRemoteDataSource(
        impl: SocialAuthRemoteDataSourceImpl
    ): SocialAuthRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindTermsRemoteDataSource(
        impl: TermsRemoteDataSourceImpl
    ): TermsRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindHospitalMainRemoteDataSource(
        impl: HospitalMainRemoteDataSourceImpl
    ): HospitalMainRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindHomeMainRemoteDataSource(
        impl: HomeMainRemoteDataSourceImpl
    ): HomeMainRemoteDataSource

}
