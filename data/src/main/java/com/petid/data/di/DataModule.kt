package com.petid.data.di

import com.petid.data.source.remote.BlogMainRemoteDataSource
import com.petid.data.source.remote.BlogMainRemoteDataSourceImpl
import com.petid.data.source.remote.ContentDetailRemoteDataSource
import com.petid.data.source.remote.ContentDetailRemoteRemoteDataSourceImpl
import com.petid.data.source.remote.HomeMainRemoteDataSource
import com.petid.data.source.remote.HomeMainRemoteDataSourceImpl
import com.petid.data.source.remote.HospitalMainRemoteDataSource
import com.petid.data.source.remote.HospitalMainRemoteDataSourceImpl
import com.petid.data.source.remote.MyInfoRemoteDataSource
import com.petid.data.source.remote.MyInfoRemoteDataSourceImpl
import com.petid.data.source.remote.PetInfoDataSource
import com.petid.data.source.remote.PetInfoDataSourceImpl
import com.petid.data.source.remote.ReservationCalendarRemoteDataSource
import com.petid.data.source.remote.ReservationCalendarRemoteDataSourceImpl
import com.petid.data.source.remote.ReservationHistoryInfoRemoteDataSource
import com.petid.data.source.remote.ReservationHistoryInfoRemoteDataSourceImpl
import com.petid.data.source.remote.SocialAuthRemoteDataSource
import com.petid.data.source.remote.SocialAuthRemoteDataSourceImpl
import com.petid.data.source.remote.TermsRemoteDataSource
import com.petid.data.source.remote.TermsRemoteDataSourceImpl
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

    @Binds
    @Singleton
    abstract fun bindReservationCalendarRemoteDataSource(
        impl: ReservationCalendarRemoteDataSourceImpl
    ): ReservationCalendarRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindReservationHistoryInfoRemoteDataSource(
        impl: ReservationHistoryInfoRemoteDataSourceImpl
    ): ReservationHistoryInfoRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindBlogMainRemoteDataSource(
        impl: BlogMainRemoteDataSourceImpl
    ): BlogMainRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindContentDetailRemoteDataSource(
        impl: ContentDetailRemoteRemoteDataSourceImpl
    ): ContentDetailRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindMyInfoRemoteDataSource(
        impl: MyInfoRemoteDataSourceImpl
    ): MyInfoRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindPetInfoRemoteDataSource(
        impl: PetInfoDataSourceImpl
    ): PetInfoDataSource
}
