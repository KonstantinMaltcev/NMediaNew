package ru.netology.nmedia.data.implementation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post

class InMemoryPostRepository : PostRepository {

    private var nextId = GENERATED_AMOUNT_POST.toLong()

    override val data = MutableLiveData(
        List(GENERATED_AMOUNT_POST) { index ->
            Post(
                id = index + 1L,
                author = "Константин Мальцев",
                content = "Text $index som content: Какойто текст.......",
                published = "17.06.2022"
            )
        }
    )

    private var posts
        get() =
            checkNotNull(data.value) {
                Log.e("error", "Data value should be not null")
            }
        set(value) {
            data.value = value
        }

    override fun likeById(id: Long) {
        data.value = posts.map {
            if (it.id != id) it else {
                it.copy(
                    likedByMe = !it.likedByMe,
                    likes = countLikeByMe(it.likedByMe, it.likes)
                )
            }
        }
    }

    override fun shareById(id: Long) {
        data.value = posts.map {
            if (it.id != id) it else {
                it.copy(
                    shares = it.shares + 1
                )
            }
        }
    }

    override fun removeById(id: Long) {
        data.value = posts.filter { it.id != id }
    }

    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) insert(post) else update(post)
    }

    private fun insert(post: Post) {
        data.value = listOf(
            post.copy(
                id = ++nextId
            )
        ) + posts
    }

    private fun update(post: Post) {
        val content = posts.map {
            if (post.id == it.id) post else it
        }
        data.value = content
    }


    private fun countLikeByMe(liked: Boolean, like: Int) =
        if (liked) like - 1 else like + 1

    private companion object {
        const val GENERATED_AMOUNT_POST = 1000
    }
}