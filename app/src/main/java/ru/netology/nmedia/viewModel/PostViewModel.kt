package ru.netology.nmedia.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.implementation.InMemoryPostRepository
import ru.netology.nmedia.dto.Post

class PostViewModel : ViewModel(), PostInteractionListener {

    private val repository: PostRepository = InMemoryPostRepository()

    val data get() = repository.data


    val currentPost = MutableLiveData<Post?>(null)
    private val editPosts = MutableLiveData<Post?>(null)

    fun onSaveButtonClicked(content: String) {
        if (content.isBlank()) return

        val editPost = currentPost.value?.copy(
            content = content
        ) ?: Post(
            id = PostRepository.NEW_POST_ID,
            author = "Konstantin",
            content = content,
            published = "18/06/2022"
        )
        repository.save(editPost)
        currentPost.value = null
    }

    // region PostInteractionListener

    override fun onLikeClicked(post: Post) = repository.likeById(post.id)

    override fun onShareClicked(post: Post) = repository.shareById(post.id)

    override fun onRemoveClicked(post: Post) = repository.removeById(post.id)

    override fun onEditClicked(post: Post) {
        currentPost.value = post
        editPosts.value = post
    }

    // endregion PostInteractionListener

}