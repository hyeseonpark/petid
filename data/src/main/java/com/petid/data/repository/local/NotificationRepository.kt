package com.petid.data.repository.local

import com.petid.data.source.local.dao.NotificationDao
import com.petid.data.source.local.entity.NotificationEntity
import jakarta.inject.Inject
import javax.inject.Singleton


@Singleton
class NotificationRepository @Inject constructor(
    private val notificationDao: NotificationDao,
) {
    suspend fun getNotifications(): DBResult<List<NotificationEntity>> {
        return try {
            val result = notificationDao.getAllNotifications()
            DBResult.Success(result)
        } catch (e: Exception) {
            DBResult.Error(e)
        }
    }

    suspend fun saveNotification(notificationEntity: NotificationEntity): DBResult<Unit> {
        return try {
            val result = notificationDao.insertNotification(notificationEntity)
            DBResult.Success(result)
        } catch (e: Exception) {
            DBResult.Error(e)
        }
    }

    suspend fun markAsChecked(notificationId: Long) {
        notificationDao.updateNotificationChecked(notificationId)
    }
}

sealed class DBResult<out T> {
    data class Success<T>(val data: T) : DBResult<T>()
    data class Error(val exception: Exception) : DBResult<Nothing>()
}