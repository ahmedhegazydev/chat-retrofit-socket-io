package com.kadirkuruca.newsapp.data.model;

import com.nurbk.ps.demochat.model.ApiUser;
import com.nurbk.ps.demochat.model.Links


data class AllUsers(
    val `data`: List<Data>,
    val limit: Int,
    val links: LinksAllUser,
    val page: Int,
    val pageCount: Int,
    val totalCount: Int
) {

    data class Data(
        val createdAt: String,
        val deleted: Boolean,
        val friends: List<Any>,
        val fullname: String,
        val id: Int,
        val img: String,
        val phone: String,
        val updatedAt: String
    )
}

data class LinksAllUser(
    val last: String,
    val next: String,
    val self: String
)