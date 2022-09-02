package ru.netology.nmedia.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.netology.nmedia.adapter.PostInterActionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.implementation.PostRepositoryImpl
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.SingleLiveEvent

class PostViewModel(
    application: Application
) : AndroidViewModel(application), PostInterActionListener {

    private val repository: PostRepository =
        PostRepositoryImpl(dao = AppDb.getInstance(context = application).postDao)

    val data by repository::data

    val sharePostContent = SingleLiveEvent<String>()

    val navigateToPostContentScreenEvent = SingleLiveEvent<String>()

    val navigateToPostDetails = SingleLiveEvent<Long>()

    val currentPost = SingleLiveEvent<Post?>()

    val playVideoContent = SingleLiveEvent<String>()

    fun onSaveButtonClicked(content: String) {

        if (content.isBlank()) return

        val post = currentPost.value?.copy(
            content = content
        ) ?: Post(
            id = PostRepository.NEW_POST_ID,
            author = "Konstantin",
            published = "01/09/2022",
            content = content,
            likedByMe = false,
            likes = 12,
            isReposted = false,
            shares = 0,
            viewCount = 0,
            video = "https://lordserial.site/4303-teoriya-bolshogo-vzryva-sezon-34.html"
        )
        repository.save(post)
        currentPost.value = null
    }

    fun onAddClicked() {
        navigateToPostContentScreenEvent.call()
    }

    //region PostInterActionListener

    override fun onLikeClicked(post: Post) = repository.like(post.id)

    override fun onShareClicked(post: Post) {
        repository.share(post.id)
        sharePostContent.value = post.content
    }

    override fun onRemoveClicked(post: Post) = repository.delete(post.id)

    override fun onEditClicked(post: Post) {
        currentPost.value = post
        navigateToPostContentScreenEvent.value = post.content
    }

    override fun onVideoClicked(post: Post) {
        post.video?.let {
            playVideoContent.value = it
        }
    }

    override fun viewPostDetails(post: Post) {
        navigateToPostDetails.value = post.id
    }


    //endregion
}