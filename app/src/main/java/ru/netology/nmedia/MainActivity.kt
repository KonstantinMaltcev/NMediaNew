package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<PostViewModel>()

    private val tag by lazy { "ActivityMain" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.data.observe(this) {
            binding.render(it)
        }
        binding.root.setOnClickListener {
            Log.i(tag, "Поехалиииии")
        }

        binding.avatarImage.setOnClickListener {
            Log.i(tag, "Аватарка")
        }

        binding.likes.setOnClickListener {
            viewModel.onLikeClicked()
            Log.i(tag, "Клик по лайку")
        }

        binding.share.setOnClickListener {
            viewModel.onShareClicked()
            Log.i(tag, "Клик по шаре")
        }
    }

    private fun ActivityMainBinding.render(post: Post) {
        authorName.text = post.author
        date.text = post.published
        postText.text = post.content
        amountLikes.text = reductionNumbers(post.likes)
        likes.setImageResource(getLikeIconResId(post.likedByMe))
        amountShare.text = reductionNumbers(post.shares)
    }

    //
    @DrawableRes
    private fun getLikeIconResId(liked: Boolean) =
        if (liked) R.drawable.ic_liked_24dp else R.drawable.ic_like_24dp

    private fun reductionNumbers(count: Int): String {
        return when (count) {
            in 0..999 -> count.toString()
            in 1000..1099 -> "${count / 1000}K"
            in 1100..9999 -> "${count / 1000}.${count % 10}K"
            in 10_000..999_999 -> "${count / 1000}K"
            in 1_000_000..1_099_999 -> "${count / 1_000_000}M"
            else -> "${(count / 10.pow(2)) / 10_000}.${((count / 1000) / 10.pow(2)) % 10}M"
        }
    }
}

private fun Int.pow(x: Int): Int = (2..x).fold(this) { R, _ -> R * this }

