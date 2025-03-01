package com.petid.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.petid.data.source.local.entity.NotificationEntity

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY id DESC")
    suspend fun getAllNotifications(): List<NotificationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(user: NotificationEntity)

    @Query("UPDATE notifications SET isChecked = 1 WHERE id = :notificationId")
    suspend fun updateNotificationChecked(notificationId: Long)

    @Transaction
    suspend fun updateAndFetchNotifications(notificationId: Long): List<NotificationEntity> {
        updateNotificationChecked(notificationId)
        return getAllNotifications()
    }


    @Query("SELECT EXISTS(SELECT 1 FROM notifications WHERE isChecked = 0 LIMIT 1)")
    suspend fun hasUncheckedNotification(): Boolean
}
