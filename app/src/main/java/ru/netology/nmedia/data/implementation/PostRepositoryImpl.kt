package ru.netology.nmedia.data.implementation

import androidx.lifecycle.map
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity

class PostRepositoryImpl(
    private val dao: PostDao,
) : PostRepository {

    override val data = dao.getAll().map { entities ->
        entities.map { it.toModel() }
    }

    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) dao.insert(post.toEntity()) else dao.updateContentById(post.id, post.content)
    }

    override fun like(postID: Long) = dao.like(postID)

    override fun delete(postID: Long) = dao.delete(postID)

    override fun share(postID: Long) = dao.share(postID)

}

private fun Post.toEntity() = PostEntity(
    id = id,
    author = author,
    published = published,
    content = content,
    likedByMe = likedByMe,
    likes = likes,
    isReposted = isReposted,
    shares = shares,
    viewCount = viewCount,
    video = video
)

private fun PostEntity.toModel() = Post(
    id = id,
    author = author,
    published = published,
    content = content,
    likedByMe = likedByMe,
    likes = likes,
    isReposted = isReposted,
    shares = shares,
    viewCount = viewCount,
    video = video
)