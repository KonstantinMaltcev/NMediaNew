package ru.netology.nmedia.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostDetailsFragmentBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.ViewsUtils
import ru.netology.nmedia.viewModel.PostViewModel

class PostDetailsFragment : Fragment() {

    private val viewModel by viewModels<PostViewModel>(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = PostDetailsFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        val post = viewModel.currentPost.value

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val sortedPosts = posts.filter { it.id == post?.id }
            if (sortedPosts.isNotEmpty()) {
                binding.render(sortedPosts.first())
            } else {
                findNavController().navigateUp()
            }
        }

        setFragmentResultListener(
            requestKey = PostContentFragment.REQUEST_KEY
        ) { requestKey, bundle ->
            if (requestKey != PostContentFragment.REQUEST_KEY) return@setFragmentResultListener
            val newPostContent = bundle.getString(
                PostContentFragment.RESULT_KEY
            ) ?: return@setFragmentResultListener
            viewModel.onSaveButtonClicked(newPostContent)
        }

        viewModel.sharePostContent.observe(viewLifecycleOwner) { postContent ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, postContent)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(intent, getString(R.string.chooser_share_post))
            startActivity(shareIntent)
        }

        viewModel.playVideoContent.observe(viewLifecycleOwner) { postUrl ->
            val intent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(postUrl)
            }
            startActivity(intent)
        }

        post?.let {
            binding.likesIcon.setOnClickListener {
                viewModel.onLikeClicked(post)
            }

            binding.repostIcon.setOnClickListener {
                viewModel.onShareClicked(post)
            }

            binding.videoFrameInPost.videoPoster.setOnClickListener {
                viewModel.onVideoClicked(post)
            }

            val popupMenu by lazy {
                PopupMenu(layoutInflater.context, binding.options).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.remove -> {
                                viewModel.onRemoveClicked(post)
                                true
                            }
                            R.id.edit -> {
                                viewModel.onEditClicked(post)
                                true
                            }
                            else -> false
                        }
                    }
                }
            }
            binding.options.setOnClickListener { popupMenu.show() }
        }

        viewModel.navigateToPostContentScreenEvent.observe(viewLifecycleOwner) { initialContent ->
            val direction = PostDetailsFragmentDirections.toPostContentFragment(initialContent)
            findNavController().navigate(direction)
        }

    }.root

    private fun PostDetailsFragmentBinding.render(post: Post) {
        post.let {
            avatar.setImageResource(R.drawable.ic_launcher_foreground)
            authorName.text = post.author
            postText.text = post.content
            data.text = post.published
            likesIcon.text = ViewsUtils.countFormatter(resources, post.likes)
            likesIcon.isChecked = post.likedByMe
            repostIcon.text = ViewsUtils.countFormatter(resources, post.shares)
            viewsIcon.text = ViewsUtils.countFormatter(resources, post.viewCount)
            if (post.video != null) {
                videoFrameInPost.root.visibility = View.VISIBLE
                videoFrameInPost.videoUrl.text = post.video
            } else {
                videoFrameInPost.root.visibility = View.GONE
            }
        }
    }
}