package ru.netology.nmedia.data.implementation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post

class InMemoryPostRepository : PostRepository {

    override val data = MutableLiveData(
        List(100) { index ->
            Post(
                id = index + 1L,
                author = "Константин Мальцев",
                content = "Text $index som content: Главным методом обогащения сульфидных цинковых руд является флотация – способ, основанный на различии в смачиваемости поверхности частичек рудного минерала и частичек пустой породы. \n" +
                        "Сульфиды металлов относятся к категории плохо смачиваемых водой – к гидрофобным материалам. Минералы пустой породы (силикаты, оксиды, карбонаты) хорошо смачиваются водой – являются гидрофильными. \n",
                published = "$index мая в 19:22"
            )
        }
    )

    private val posts
        get() =
            checkNotNull(data.value) {
                Log.e("error", "Data value should be not null")
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
                it.copy(shares = +1)
            }
        }
    }


    private fun countLikeByMe(liked: Boolean, like: Int) =
        if (liked) like + 1 else like - 1

}
