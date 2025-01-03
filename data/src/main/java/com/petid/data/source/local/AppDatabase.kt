package com.petid.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.petid.data.source.local.dao.NotificationDao
import com.petid.data.source.local.entity.NotificationEntity


@Database(entities = [NotificationEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationDao
}