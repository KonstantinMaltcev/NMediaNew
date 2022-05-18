package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.DrawableRes
import ru.netology.nmedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
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
            published = "12 мая в 19:22",
//            likedByMe = false
        )
        binding.likes.setOnClickListener {
            post.likedByMe = !post.likedByMe
            val imageResLiked =
                if (post.likedByMe) R.drawable.ic_liked_24dp else R.drawable.ic_like_24dp
            binding.likes.setImageResource(imageResLiked)
        }


        binding.render(post)
        binding.likes.setOnClickListener {
            post.likedByMe = !post.likedByMe
            binding.likes.setImageResource(getLikeIconResId(post.likedByMe))
            post.likes = countLikeByMe(post.likedByMe, post.likes)
            post.reductionLike = reductionNumbers(post.likes)
            binding.render(post)
        }

        binding.share.setOnClickListener {
            post.shares += 1
            post.reductionShare = reductionNumbers(post.shares)
            binding.render(post)
        }
    }


    private fun ActivityMainBinding.render(post: Post) {
        authorName.text = post.author
        date.text = post.published
        postText.text = post.content
        amountLikes.text = post.reductionLike
        likes.setImageResource(getLikeIconResId(post.likedByMe))
        amountShare.text = post.reductionShare
    }

    @DrawableRes
    private fun getLikeIconResId(liked: Boolean) =
        if (liked) R.drawable.ic_liked_24dp else R.drawable.ic_like_24dp

    private fun countLikeByMe(liked: Boolean, like: Int) =
        if (liked) like + 1 else like - 1

    private fun reductionNumbers(count: Int): String {
        return if (count in 0..999) count.toString()
        else {
            val stepCount = count.toString().length
            val member: Int
            if (stepCount in 4..6) {
                member = count / 10.pow(2)
                if (member % 10 == 0) "${member / 10}K"
                else "${member / 10}.${member % 10}K"
            } else {
                member = (count / 1000) / 10.pow(2)
                if (member % 10 == 0) "${member / 10}M"
                else "${member / 10}.${member % 10}M"
            }
        }
    }

    private fun Int.pow(x: Int): Int = (2..x).fold(this) { R, _ -> R * this }

}