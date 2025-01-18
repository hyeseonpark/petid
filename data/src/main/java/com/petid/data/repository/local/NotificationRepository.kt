package com.petid.data.repository.local

import com.petid.data.source.local.dao.NotificationDao
import com.petid.data.source.local.entity.NotificationEntity
import com.petid.data.util.mapDBResult
import jakarta.inject.Inject
import javax.inject.Singleton


@Singleton
class NotificationRepository @Inject constructor(
    private val notificationDao: NotificationDao,
) {
    suspend fun getNotifications(): DBResult<List<NotificationEntity>> =
        runCatching {
            notificationDao.getAllNotifications()
        }.mapDBResult { DBResult.Success(it) }

    suspend fun saveNotification(notificationEntity: NotificationEntity): DBResult<Unit> =
        runCatching {
            notificationDao.insertNotification(notificationEntity)
        }.mapDBResult { DBResult.Success(it) }

    suspend fun markAsChecked(notificationId: Long): DBResult<List<NotificationEntity>> =
        runCatching {
            notificationDao.updateAndFetchNotifications(notificationId)
        }.mapDBResult { DBResult.Success(it) }

    suspend fun hasUncheckedNotification(): DBResult<Boolean> =
        runCatching {
            notificationDao.hasUncheckedNotification()
        }.mapDBResult { DBResult.Success(it) }

}

sealed class DBResult<out T> {
    data class Success<T>(val data: T) : DBResult<T>()
    data class Error(val exception: Throwable) : DBResult<Nothing>()
}
