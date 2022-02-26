package com.nurbk.ps.demochat.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class ApiUser(
    var friends: List<Friend>,
    var deleted: Boolean = false,
    var fullname: String = "",
    var phone: String = "",
    var createdAt: String = "",
    var updatedAt: String = "",
    var id: String = "",
) : Parcelable {

    companion object {
    }

}

@Parcelize
data class Friend(
     val data: @RawValue List<Any>,
    val limit: Int,
    val links: Links,
    val page: Int,
    val pageCount: Int,
    val totalCount: Int
): Parcelable {

    companion object {
    }

}

@Parcelize
data class Links(
    val self: String
)
    : Parcelable {

    companion object {
    }

}

