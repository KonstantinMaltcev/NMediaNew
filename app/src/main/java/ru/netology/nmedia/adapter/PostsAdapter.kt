package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.databinding.PostBinding
import ru.netology.nmedia.utils.ViewsUtils


internal class PostsAdapter(

    private val interActionListener: PostInterActionListener

) : ListAdapter<Post, PostsAdapter.PostViewHolder>(DiffCallBack) {

    inner class PostViewHolder(
        private val binding: PostBinding,
        listener: PostInterActionListener

    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post

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

        init {
            binding.likes.setOnClickListener {
                listener.onLikeClicked(post)
            }
            binding.share.setOnClickListener {
                listener.onShareClicked(post)
            }

            binding.videoFrameInPost.setOnClickListener {
                listener.onVideoClicked(post)
            }

            binding.menu.setOnClickListener { popupMenu.show() }

            binding.root.setOnClickListener{listener.viewPostDetails(post)}
        }

        fun bind(post: Post) {
            this.post = post
            val resources = binding.root.resources
            with(binding) {
                avatarImage.setImageResource(R.drawable.ic_launcher_foreground)
                authorName.text = post.author
                postText.text = post.content
                published.text = post.published
                likes.text = ViewsUtils.countFormatter(resources, post.likes)
                likes.isChecked = post.likedByMe
                share.text = ViewsUtils.countFormatter(resources, post.shares)
                viewsIcon.text = ViewsUtils.countFormatter(resources, post.viewCount)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostBinding.inflate(inflater, parent, false)
        return PostViewHolder(binding, interActionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private object DiffCallBack : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
            oldItem == newItem
    }

}