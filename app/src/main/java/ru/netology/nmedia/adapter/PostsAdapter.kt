package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostBinding
import ru.netology.nmedia.dto.Post


class PostsAdapter(
    private val interactionListener: PostInteractionListener
) : ListAdapter<Post, PostsAdapter.ViewHolder>(DiffCallBack) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostBinding.inflate(inflater, parent, /*attach to parent*/ false)
        return ViewHolder(binding, interactionListener)
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
        private val binding: PostBinding,
        listener: PostInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post

        init {
            binding.likes.setOnClickListener {
                listener.onLikeClicked(post)
            }
            binding.menu.setOnClickListener { popupMenu.show() }
        }

        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.menu).apply {
                inflate(R.menu.options_post)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.remove -> {
                            listener.onRemoveClicked(post)
                            true
                        }
                        R.id.edit -> {
                            listener.onEditClicked(post)
                            true
                        }
                        else -> false
                    }
                }
            }
        }

        fun bind(post: Post) {
            this.post = post
            with(binding) {
                authorName.text = post.author
                published.text = post.published
                postText.text = post.content
                likes.text = reductionNumbers(post.likes)
                share.text = reductionNumbers(post.shares)
                likes.isChecked = post.likedByMe
//                share.isChecked = true
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
    }
}

