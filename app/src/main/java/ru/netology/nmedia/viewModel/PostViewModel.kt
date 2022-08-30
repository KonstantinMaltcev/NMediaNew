package ru.netology.nmedia.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.adapter.PostInterActionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.implementation.FilePostRepository
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.SingleLiveEvent

class PostViewModel(
    application: Application
) : AndroidViewModel(application), PostInterActionListener {

    private val repository: PostRepository = FilePostRepository(application)

    val data by repository::data

//    var contentGeneratorButtonVisibility = !repository.contentGeneratorButtonWasClicked

    val sharePostContent = SingleLiveEvent<String>()

    val navigateToPostContentScreenEvent = SingleLiveEvent<String>()

    val navigateToPostDetails = SingleLiveEvent<Long>()

    val currentPost = MutableLiveData<Post?>(null)

    val playVideoContent = SingleLiveEvent<String>()

    fun onSaveButtonClicked(content: String) {

        if (content.isBlank()) return

        val post = currentPost.value?.copy(
            content = content
        ) ?: Post(
            id = PostRepository.NEW_POST_ID,
            author = "Konstantin",
            published = "30/08/2022",
            content = content,
            likedByMe = false,
            likes = 0,
            isReposted = false,
            shares = 0,
            viewCount = 0,
            video = null
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
        if (post.content.isBlank()) return
       print("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
        val editPost = currentPost.value?.copy(
            content = post.content
        )
        print(post.content)
        if (editPost != null) {
            repository.save(editPost)
        }
        currentPost.value = null
    }

    override fun onVideoClicked(post: Post) {
        post.video?.let {
            playVideoContent.value = it
        }
    }

    override fun viewPostDetails(post: Post) {
        currentPost.value = post
        navigateToPostDetails.value = post.id
    }


    //endregion
}