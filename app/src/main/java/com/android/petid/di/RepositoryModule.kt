package com.android.petid.di

import com.android.data.repository.HomeMainRepositoryImpl
import com.android.data.repository.HospitalMainRepositoryImpl
import com.android.data.repository.ReservationCalendarRepositoryImpl
import com.android.data.repository.ReservationHistoryInfoRepositoryImpl
import com.android.data.repository.SocialAuthRepositoryImpl
import com.android.data.repository.TermsRepositoryImpl
import com.android.domain.repository.HomeMainRepository
import com.android.domain.repository.HospitalMainRepository
import com.android.domain.repository.ReservationCalendarRepository
import com.android.domain.repository.ReservationHistoryInfoRepository
import com.android.domain.repository.SocialAuthRepository
import com.android.domain.repository.TermsRepository
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



    /*@Binds
    @Singleton
    abstract fun bindLoginRemoteDataSource(
        loginRemoteDataSource: LoginRemoteDataSource
    ): LoginDataSource.Remote*/
}
