package com.petid.data.di

import com.petid.data.repository.remote.BlogMainRepositoryImpl
import com.petid.data.repository.remote.ContentDetailRepositoryImpl
import com.petid.data.repository.remote.HomeMainRepositoryImpl
import com.petid.data.repository.remote.HospitalMainRepositoryImpl
import com.petid.data.repository.remote.MyInfoRepositoryImpl
import com.petid.data.repository.remote.PetInfoRepositoryImpl
import com.petid.data.repository.remote.ReservationCalendarRepositoryImpl
import com.petid.data.repository.remote.ReservationHistoryInfoRepositoryImpl
import com.petid.data.repository.remote.S3UploadRepositoryImpl
import com.petid.data.repository.remote.SocialAuthRepositoryImpl
import com.petid.data.repository.remote.TermsRepositoryImpl
import com.petid.domain.repository.BlogMainRepository
import com.petid.domain.repository.ContentDetailRepository
import com.petid.domain.repository.HomeMainRepository
import com.petid.domain.repository.HospitalMainRepository
import com.petid.domain.repository.MyInfoRepository
import com.petid.domain.repository.PetInfoRepository
import com.petid.domain.repository.ReservationCalendarRepository
import com.petid.domain.repository.ReservationHistoryInfoRepository
import com.petid.domain.repository.S3UploadRepository
import com.petid.domain.repository.SocialAuthRepository
import com.petid.domain.repository.TermsRepository
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

    @Binds
    @Singleton
    abstract fun bindTermsRepository(
        termsRepositoryImpl: TermsRepositoryImpl
    ): TermsRepository

    @Binds
    @Singleton
    abstract fun bindHospitalMainRepository(
        hospitalMainRepositoryImpl: HospitalMainRepositoryImpl
    ): HospitalMainRepository

    @Binds
    @Singleton
    abstract fun bindReservationCalendarRepository(
       reservationCalendarRepositoryImpl: ReservationCalendarRepositoryImpl
    ): ReservationCalendarRepository

    @Binds
    @Singleton
    abstract fun bindHomeMainRepository(
        homeMainRepositoryImpl: HomeMainRepositoryImpl
    ): HomeMainRepository

    @Binds
    @Singleton
    abstract fun bindReservationHistoryInfoRepository(
        reservationHistoryInfoRepositoryImpl: ReservationHistoryInfoRepositoryImpl
    ): ReservationHistoryInfoRepository


    @Binds
    @Singleton
    abstract fun bindBlogMainRepository(
        blogMainRepositoryImpl: BlogMainRepositoryImpl
    ): BlogMainRepository


    @Binds
    @Singleton
    abstract fun bindContentDetailRepository(
        contentDetailRepositoryImpl: ContentDetailRepositoryImpl
    ): ContentDetailRepository

    @Binds
    @Singleton
    abstract fun bindMyInfoRepository(
        myInfoRepositoryImpl: MyInfoRepositoryImpl
    ): MyInfoRepository

    @Binds
    @Singleton
    abstract fun bindPetInfoRepository(
        petInfoRepositoryImpl: PetInfoRepositoryImpl
    ): PetInfoRepository

    @Binds
    @Singleton
    abstract fun bindS3UploadRepository(
        s3UploadRepositoryImpl: S3UploadRepositoryImpl,
    ): S3UploadRepository
}
