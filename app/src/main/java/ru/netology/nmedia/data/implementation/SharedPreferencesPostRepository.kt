package ru.netology.nmedia.data.implementation//package ru.netology.nmedia.data.implementation
//
//import android.app.Application
//import android.content.Context
//import androidx.core.content.edit
//import androidx.lifecycle.MutableLiveData
//import kotlinx.serialization.decodeFromString
//import kotlinx.serialization.encodeToString
//import kotlinx.serialization.json.Json
//import ru.netology.nmedia.dto.Post
//import ru.netology.nmedia.data.PostRepository
//import ru.netology.nmedia.data.impl.Data
//import kotlin.properties.Delegates
//
//class SharedPrefsPostRepository(
//    application: Application
//) : PostRepository {
//
//    private val prefs = application.getSharedPreferences(
//        "repo", Context.MODE_PRIVATE
//    )
//
//    private var nextID: Long by Delegates.observable(
//        prefs.getLong(NEXT_ID_PREF_KEY, 0L)
//    ) {_,_, newValue ->
//        prefs.edit { putLong(NEXT_ID_PREF_KEY, newValue) }
//    }
//
//    private val someData = Data()
//
//    override val data: MutableLiveData<List<Post>>
//    override var contentGeneratorButtonWasClicked: Boolean
//        get() = ("Not yet implemented")
//        set(value) {}
//
//    private var posts
//        get() = checkNotNull(data.value) {
//            "Error. Data is null"
//        }
//        set(value) {
//            prefs.edit {
//                val serializedPosts = Json.encodeToString(value)
//                putString(POSTS_PREF_KEY, serializedPosts)
//            }
//            data.value = value
//        }
//
//    init {
//        val serializedPosts = prefs.getString(POSTS_PREF_KEY, null)
//        val posts: List<Post> = if (serializedPosts != null) {
//            Json.decodeFromString(serializedPosts)
//        } else {
//            emptyList()
//        }
//        data = MutableLiveData(posts)
//    }
//
//    override fun like(postID: Long) {
//        posts = posts.map {
//            if (it.id != postID) it
//            else {
//                it.copy(
//                    isLiked = !it.isLiked,
//                    likesCount = it.likesCount + if (!it.isLiked) + 1 else - 1,
//                )
//            }
//        }
//    }
//
//    override fun share(postID: Long) {
//
//        posts = posts.map {
////            print("0")
//            if (it.id != postID) it
//            else {
//                it.copy(
//                    repostsCount = it.repostsCount + 1
//                )
//            }
//        }
//    }
//
//    override fun delete(postID: Long) {
//        posts = posts.filterNot { it.id == postID }
//    }
//
//    override fun save(post: Post) {
//        if (post.id == PostRepository.NEW_POST_ID) insert(post) else update(post)
//    }
//
//    override fun generateContent() {
//        ("Not yet implemented")
//    }
//
//    private fun update(post: Post) {
//        posts = posts.map {
//            if (it.id == post.id) post else it
//        }
//    }
//
//    private fun insert(post: Post) {
//        posts = listOf(post.copy(id = ++nextID)) + posts
//    }
//
//    private companion object {
//        const val GENERATED_POSTS_AMOUNT = 1000
//        const val POSTS_PREF_KEY = "posts"
//        const val NEXT_ID_PREF_KEY = "nextID"
//    }
//
//}