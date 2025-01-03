package com.petid.petid.di

import com.petid.data.repository.local.NotificationRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AppFirebaseMessageServiceEntryPoint {
    fun notificationRepository(): NotificationRepository
}