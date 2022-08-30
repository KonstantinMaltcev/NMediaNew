//package ru.netology.nmedia.data.implementation
//
//import androidx.lifecycle.MutableLiveData
//import ru.netology.nmedia.dto.Post
//import ru.netology.nmedia.data.PostRepository
//import ru.netology.nmedia.data.impl.Data
//
//class InMemoryPostRepository : PostRepository {
//    private var nextID = GENERATED_POSTS_AMOUNT.toLong()
//    private val someData = Data()
//    override val data = MutableLiveData(
//        List(GENERATED_POSTS_AMOUNT) { index ->
//            Post(
//                id = index + 1L,
//                authorName = "Netology",
//                date = "13/06/2022",
//                text = "#${index + 1} \n" + someData.getRandomContent(),
//                isLiked = false,
//                likesCount = 999,
//                isReposted = false,
//                repostsCount = 9995,
//                viewesCount = 1299999,
//                videoUrl = someData.getRandomURL()
//            )
//        }
//    )
//    override var contentGeneratorButtonWasClicked: Boolean
//        get() = ("Not yet implemented")
//        set(value) {}
//
//    private val posts
//        get() = checkNotNull(data.value) {
//            "Error. Data is null"
//        }
//
//    override fun like(postID: Long) {
//        data.value = posts.map {
//            if (it.id != postID) it
//            else {
//                it.copy(
//                    isLiked = !it.isLiked,
//                    likesCount = if (!it.isLiked) it.likesCount + 1 else it.likesCount - 1,
//                )
//            }
//        }
//    }
//
//    override fun share(postID: Long) {
//
//        data.value = posts.map {
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
//        data.value = posts.filterNot { it.id == postID }
//    }
//
//    override fun save(post: Post) {
//        if (post.id == PostRepository.NEW_POST_ID) insert(post) else update(post)
//    }
//
//    override fun generateContent() {
//
//    }
//
//    private fun update(post: Post) {
//        data.value = posts.map {
//            if (it.id == post.id) post else it
//        }
//    }
//
//    private fun insert(post: Post) {
//        data.value = listOf(post.copy(id = ++nextID)) + posts
//    }
//
//    private companion object {
//        const val GENERATED_POSTS_AMOUNT = 1000
//    }
//
//}