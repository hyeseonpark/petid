package com.petid.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *
 * [docs](https://petid.atlassian.net/wiki/x/MIBLAQ)
 * @param type 알람 타입
 * @param desc 내용
 * @param category 분류 (booking : 관리자 예약 확인, order : 방문 전후 알림, reminder: 리마인더 (status: null))
 * @param status 상태 (PENDING, CONFIRMED, CANCELED, COMPLETED)
 * @param isChecked 읽음 여부
 */
@Entity(tableName = "notifications")
data class NotificationEntity(
    val type: String,
    val desc: String,
    val category: String,
    val status: String,
    val isChecked: Boolean = false,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}