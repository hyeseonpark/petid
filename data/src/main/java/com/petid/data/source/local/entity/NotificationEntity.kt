package com.petid.data.source.local.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *
 * [펫아이디 알림 정의](https://petid.atlassian.net/wiki/x/MIBLAQ)
 *
 * @param category 분류 (booking : 관리자 예약 확인, order : 방문 전후 알림, reminder: 리마인더 (status: null))
 * @param desc 내용
 * @param status 상태 (PENDING, CONFIRMED, CANCELED, COMPLETED)
 * @param detailId 예약 또는 컨텐츠 id
 * @param isChecked 읽음 여부
 */
@Parcelize
@Entity(tableName = "notifications")
data class NotificationEntity(
    val category: String,
    val desc: String,
    val status: String?,
    val detailId: Int?,
    val isChecked: Boolean = false,
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}