package com.android.petid.di

import com.android.data.source.remote.SocialAuthRemoteDataSource
import com.android.data.source.remote.SocialAuthRemoteDataSourceImpl
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

    /*companion object {
        @Provides
        @Singleton
        fun provideSignUpAPI(retrofit: Retrofit): SignUpAPI {
            return retrofit.create(SignUpAPI::class.java)
        }
    }*/

    /*@Binds
    @Singleton
    abstract fun bindLoginRemoteDataSource(
        impl: LoginRemoteDataSourceImpl
    ): LoginRemoteDataSource*/

    /*@Binds
    @Singleton
    abstract fun bindLoginLocalDataSource(
        impl: LoginLocalDataSourceImpl
    ): LoginLocalDataSource*/
}

/*@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Provides
    @Singleton
    fun provideLoginRemoteDataSource(
        signUpAPI: SignUpAPI
    ): LoginRemoteDataSource {
        return LoginRemoteDataSourceImpl(signUpAPI)
    }

    @Binds
    abstract fun bindLoginRemoteDataSource(
        impl: LoginRemoteDataSourceImpl
    ): LoginRemoteDataSource

//    @Provides
//    @Singleton
//    fun provideLoginRepository(
//        loginRemote: LoginDataSource.Remote
//    ): LoginRepository {
//        return LoginRepositoryImpl(loginRemote)
//    }

//    @Provides
//    fun provideGetLoginUseCase(loginRepository: LoginRepository){
//        return GetLoginUseCase(loginRepository)
//    }
}*/