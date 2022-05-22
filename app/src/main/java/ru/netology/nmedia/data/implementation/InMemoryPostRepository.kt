package ru.netology.nmedia.data.implementation

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post

class InMemoryPostRepository : PostRepository {
    override val data = MutableLiveData(
        Post(
            id = 1,
            author = "Константин Мальцев",
            content = "Выполнение домашнего задания по обработке событий в Android Studio: \n " +
                    "Что нужно сделать:" +
                    "\n" +
                    "При клике на like должна меняться не только картинка, но и число рядом с ней: лайкаете - увеличивается на 1, дизлайкаете - уменьшается на 1\n" +
                    "При клике на share должно увеличиваться число рядом (10 раз нажали на share - +10)\n" +
                    "Добавить логику с тысячами: если количество лайков, share или просмотров перевалило за 999, то должно отображаться 1K и т.д., а не 1000 (при этом предыдущие функции должны работать: если у поста было 999 лайков и вы нажали like, то должно стать 1К, если убрали лайк, то снова 999)\n" +
                    "Обратите внимание:\n" +
                    "\n" +
                    "1.1К отображается по достижении 1100\n" +
                    "После 10К сотни перестают отображаться\n" +
                    "После 1M сотни тысяч отображаются в формате 1.3M\n" +
                    "Задумайтесь о том, что стоит это вынести в какую-то функцию, а не хранить эту логику в Activity",
            published = "12 мая в 19:22"
        )
    )

    override fun like() {
        val currentPost = checkNotNull(data.value) {
            "Data value should be not null"
        }
        val likedByMe = currentPost.copy(
            likedByMe = !currentPost.likedByMe
        )
        likedByMe.likes = countLikeByMe(
            likedByMe.likedByMe,
            likedByMe.likes
        )
        data.value = likedByMe
    }

    override fun share() {
        val currentPost = checkNotNull(data.value) {
            "Data value should be not null"
        }
        val shared = currentPost.copy(
            shares = +1
        )

        data.value = shared
    }
}

private fun countLikeByMe(liked: Boolean, like: Int) =
    if (liked) like + 1 else like - 1

