package ru.netology.nmedia.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
class PostEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "author")
    val author: String,
    @ColumnInfo(name = "content")
    val content: String,
    @ColumnInfo(name = "published")
    val published: String,
    @ColumnInfo(name = "likes")
    var likes: Int = 0,
    @ColumnInfo(name = "like_by_me")
    var likedByMe: Boolean = false,
    @ColumnInfo(name = "shares")
    var shares: Int = 0,
    @ColumnInfo(name = "is_reposted")
    val isReposted: Boolean,
    @ColumnInfo(name = "view_count")
    val viewCount: Int,
    @ColumnInfo(name = "video")
    var video: String?
)

