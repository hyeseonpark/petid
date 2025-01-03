package com.petid.data.source.local.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *
 * [docs](https://petid.atlassian.net/wiki/x/MIBLAQ)
 * @param title 제목
 * @param body 내용
 * @param category 분류 (booking : 관리자 예약 확인, order : 방문 전후 알림, reminder: 리마인더 (status: null))
 * @param status 상태 (PENDING, CONFIRMED, CANCELED, COMPLETED)
 * @param isChecked 읽음 여부
 */
@Parcelize
@Entity(tableName = "notifications")
data class NotificationEntity(
    val title: String,
    val body: String,
    val category: String,
    val status: String?,
    val isChecked: Boolean = false,
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}