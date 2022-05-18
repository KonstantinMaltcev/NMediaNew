package ru.netology.nmedia

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likes: Int = 9999,
    var reductionLike: String = "9.9K",
    var likedByMe: Boolean = false,
    var shares: Int =999 ,
    var reductionShare: String = "9.9K"
)