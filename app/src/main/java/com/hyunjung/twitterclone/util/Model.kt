package com.hyunjung.twitterclone.util

data class User(
    val email: String? = "",
    val username: String? = "",
    val imageURL: String? = "",
    val followHashtags: ArrayList<String>? = arrayListOf(),
    val followUsers: ArrayList<String>? = arrayListOf()
)