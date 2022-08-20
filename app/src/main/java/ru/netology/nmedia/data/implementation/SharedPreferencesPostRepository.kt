package ru.netology.nmedia.data.implementation

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post
import kotlin.properties.Delegates

class SharedPreferencesPostRepository(
    application: Application
) : PostRepository {

    val preferences = application.getSharedPreferences(
        "repository", Context.MODE_PRIVATE
    )

    private var nextId: Long by Delegates.observable(
        preferences.getLong(NEXT_ID_PREFERENCES_KEY, 0L)
    ) { _, _, newValue ->
        preferences.edit {
            putLong(NEXT_ID_PREFERENCES_KEY, newValue)
        }
    }

    override val data: MutableLiveData<List<Post>>

    init {
        val serializedPost = preferences.getString(POST_PREFERENCES_KEY, null)
        val posts: List<Post> = if (serializedPost != null) {
            Json.decodeFromString(serializedPost)
        } else emptyList()
        data = MutableLiveData(posts)
    }

    private var posts
        get() =
            checkNotNull(data.value) {
                Log.e("error", "Data value should be not null")
            }
        set(value) {
            preferences.edit {
                val serialized = Json.encodeToString(value)
                putString(POST_PREFERENCES_KEY, serialized)
            }
            data.value = value
        }

    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else {
                it.copy(
                    likedByMe = !it.likedByMe,
                    likes = countLikeByMe(it.likedByMe, it.likes)
                )
            }
        }
    }

    override fun shareById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else {
                it.copy(
                    shares = it.shares + 1
                )
            }
        }
    }

    override fun shareUriById(id: Long) {
        posts = posts.filter { it.id == id }
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
    }

    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) insert(post) else update(post)
    }

    private fun insert(post: Post) {
        posts = listOf(
            post.copy(
                id = ++nextId
            )
        ) + posts
    }

    private fun update(post: Post) {
        val content = posts.map {
            if (post.id == it.id) post else it
        }
        posts = content
    }

    private fun countLikeByMe(liked: Boolean, like: Int) =
        if (liked) like - 1 else like + 1

    private companion object {
        const val POST_PREFERENCES_KEY = "posts"
        const val NEXT_ID_PREFERENCES_KEY = "nextId"

    }
}