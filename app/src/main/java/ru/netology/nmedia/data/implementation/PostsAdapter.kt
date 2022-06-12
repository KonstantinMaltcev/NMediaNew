package ru.netology.nmedia.data.implementation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostBinding
import ru.netology.nmedia.dto.Post


class PostsAdapter(
    private val onLikeListener: (Post) -> Unit,
    private val onShareListener: (Post) -> Unit
) : ListAdapter<Post, PostsAdapter.ViewHolder>(DiffCallBack) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DiffCallBack : DiffUtil.ItemCallback<Post>() {

        override fun areItemsTheSame(oldItem: Post, newItem: Post) =
            oldItem.id == newItem.id


        override fun areContentsTheSame(oldItem: Post, newItem: Post) =
            oldItem == newItem

    }


    inner class ViewHolder(
        private val binding: PostBinding
    ) : RecyclerView.ViewHolder(binding.root) {


        fun bind(post: Post) = with(binding) {
            authorName.text = post.author
            published.text = post.published
            postText.text = post.content
            amountLikes.text = reductionNumbers(post.likes)
            amountShare.text = reductionNumbers(post.shares)
            likes.setImageResource(getLikeIconResId(post.likedByMe))
            likes.setOnClickListener {
                onLikeListener(post)
            }
            share.setOnClickListener {
                onShareListener(post)
            }
        }

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

        private fun Int.pow(x: Int): Int = (2..x).fold(this) { R, _ -> R * this }

        @DrawableRes
        private fun getLikeIconResId(liked: Boolean) =
            if (liked) R.drawable.ic_liked_24dp else R.drawable.ic_like_24dp
    }
}

