package com.petid.petid.di

import com.petid.data.repository.BlogMainRepositoryImpl
import com.petid.data.repository.ContentDetailRepositoryImpl
import com.petid.data.repository.HomeMainRepositoryImpl
import com.petid.data.repository.HospitalMainRepositoryImpl
import com.petid.data.repository.MyInfoRepositoryImpl
import com.petid.data.repository.PetInfoRepositoryImpl
import com.petid.data.repository.ReservationCalendarRepositoryImpl
import com.petid.data.repository.ReservationHistoryInfoRepositoryImpl
import com.petid.data.repository.SocialAuthRepositoryImpl
import com.petid.data.repository.TermsRepositoryImpl
import com.petid.domain.repository.BlogMainRepository
import com.petid.domain.repository.ContentDetailRepository
import com.petid.domain.repository.HomeMainRepository
import com.petid.domain.repository.HospitalMainRepository
import com.petid.domain.repository.MyInfoRepository
import com.petid.domain.repository.PetInfoRepository
import com.petid.domain.repository.ReservationCalendarRepository
import com.petid.domain.repository.ReservationHistoryInfoRepository
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


    /*@Binds
    @Singleton
    abstract fun bindLoginRemoteDataSource(
        loginRemoteDataSource: LoginRemoteDataSource
    ): LoginDataSource.Remote*/
}
