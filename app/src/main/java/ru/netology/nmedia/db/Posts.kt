package ru.netology.nmedia.db

import ru.netology.nmedia.dto.Post


internal fun PostEntity.toModel() = Post(
    id = id,
    author = author,
    content = content,
    published = published,
    likes = likes,
    likedByMe = likedByMe,
    shares = shares,
    isReposted = isReposted,
    viewCount = viewCount,
    video = video
)

internal fun Post.toEntity() = PostEntity(
    id = id,
    author = author,
    content = content,
    published = published,
    likes = likes,
    likedByMe = likedByMe,
    shares = shares,
    isReposted = isReposted,
    viewCount = viewCount,
    video = video
)