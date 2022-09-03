package ru.netology.nmedia.service

import kotlinx.serialization.SerialName

class ActionsTo(
    @SerialName("userId")
    val userId: Long,

    @SerialName("userName")
    val userName: String,

    @SerialName("postId")
    val postId: Long,

    @SerialName("postAuthor")
    val postAuthor: String
)
