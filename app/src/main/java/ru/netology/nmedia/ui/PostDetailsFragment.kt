package ru.netology.nmedia.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
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

//        val post = viewModel.currentPost.value
        val postDetail = viewModel.data.value?.find { it.id == viewModel.navigateToPostDetails.value }
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val postId = viewModel.navigateToPostDetails.value
            val sortedPosts = posts.find { it.id == postId }
//            if (sortedPosts) {
            if (sortedPosts != null) {
                binding.render(sortedPosts)
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

        postDetail?.let {
            binding.likesIcon.setOnClickListener {
                viewModel.onLikeClicked(postDetail)
            }

            binding.repostIcon.setOnClickListener {
                viewModel.onShareClicked(postDetail)
            }

            binding.videoFrameInPost.videoPoster.setOnClickListener {
                viewModel.onVideoClicked(postDetail)
            }

            val popupMenu by lazy {
                PopupMenu(layoutInflater.context, binding.optionsMenu).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.remove -> {
                                viewModel.onRemoveClicked(postDetail)
                                true
                            }
                            R.id.edit -> {
                                viewModel.onEditClicked(postDetail)
                                true
                            }
                            else -> false
                        }
                    }
                }
            }
            binding.optionsMenu.setOnClickListener { popupMenu.show() }
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
        }
    }
}