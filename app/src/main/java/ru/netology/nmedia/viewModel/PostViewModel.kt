package ru.netology.nmedia.viewModel

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.implementation.InMemoryPostRepository
import ru.netology.nmedia.dto.Post

class PostViewModel : ViewModel() {

    private val repository: PostRepository = InMemoryPostRepository()

    val data = repository.data

    fun onLikeClicked(post: Post) = repository.likeById(post.id)

    fun onShareClicked(post: Post) = repository.shareById(post.id)
}