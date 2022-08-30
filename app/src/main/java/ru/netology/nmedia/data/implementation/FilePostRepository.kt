package ru.netology.nmedia.data.implementation

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.data.PostRepository
import kotlin.properties.Delegates

class FilePostRepository(
    private val application: Application
) : PostRepository {

    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type

    private val prefs = application.getSharedPreferences(
        "repo", Context.MODE_PRIVATE
    )

    private var nextID: Long by Delegates.observable(
        prefs.getLong(NEXT_ID_PREF_KEY, 0L)
    ) { _, _, newValue ->
        prefs.edit { putLong(NEXT_ID_PREF_KEY, newValue) }
    }

    override val data: MutableLiveData<List<Post>>


    private var posts
        get() = checkNotNull(data.value) {
            "Error. Data is null"
        }
        set(value) {
            application.openFileOutput(
                FILE_NAME, Context.MODE_PRIVATE
            ).bufferedWriter().use { it.write(gson.toJson(value)) }
            data.value = value
        }

    init {
        val postFile = application.filesDir.resolve(FILE_NAME)
        val posts: List<Post> = if (postFile.exists()) {
            val inputStream = application.openFileInput(FILE_NAME)
            val reader = inputStream.bufferedReader()
            reader.use {
                gson.fromJson(it, type)
            }
        } else {
            emptyList()
        }
        data = MutableLiveData(posts)
    }

    override fun like(postID: Long) {
        posts = posts.map {
            if (it.id != postID) it
            else {
                it.copy(
                    likedByMe = !it.likedByMe,
                    likes = it.likes + if (!it.likedByMe) +1 else -1,
                )
            }
        }
    }

    override fun share(postID: Long) {
        posts = posts.map {
            if (it.id != postID) it
            else {
                it.copy(
                    shares = it.shares + 1
                )
            }
        }
    }

    override fun delete(postID: Long) {
        posts = posts.filter { it.id != postID }
    }

    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) insert(post) else update(post)
    }

    private fun update(post: Post) {
        posts = posts.map {
            if (it.id == post.id) post else it
        }
    }

    private fun insert(post: Post) {
        posts = listOf(
            post.copy(
                id = ++nextID
            )
        ) + posts
    }

    private companion object {
        const val NEXT_ID_PREF_KEY = "nextID"
        const val FILE_NAME = "posts.json"
    }

}