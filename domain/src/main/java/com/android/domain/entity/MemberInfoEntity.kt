package com.android.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class MemberInfoEntity  (
    val memberId: Int,
    val name: String,
    val address: String?,
    val addressDetails: String?,
    val phone: String?,
    var image: String?,
    val petId: Int?,
) : Parcelable