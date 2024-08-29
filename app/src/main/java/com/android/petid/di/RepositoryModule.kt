package com.android.petid.di

import com.android.data.repository.SocialAuthRepositoryImpl
import com.android.domain.repository.SocialAuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLoginRepository(
        socialAuthRepositoryImpl: SocialAuthRepositoryImpl
    ): SocialAuthRepository

    /*@Binds
    @Singleton
    abstract fun bindLoginRemoteDataSource(
        loginRemoteDataSource: LoginRemoteDataSource
    ): LoginDataSource.Remote*/
}
