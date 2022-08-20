package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.utils.hideKeyboard
import ru.netology.nmedia.utils.showKeyboard
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<PostViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostsAdapter(viewModel)
        binding.list.adapter = adapter

        viewModel.data.observe(this) { posts ->
            posts.map {
                adapter.submitList(posts)
            }
        }

        viewModel.currentPost.observe(this) { currentPost ->
            with(binding.content) {
                val content = currentPost?.content
                setText(content)
                if (content != null) {
                    requestFocus()
                    showKeyboard()
                } else {
                    clearFocus()
                    hideKeyboard()
                }
            }
        }

        viewModel.sharePostContent.observe(this) { post ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, post.content)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(intent, getString(R.string.chooser_share_post))
            startActivity(shareIntent)
        }
        viewModel.sharePostUriContent.observe(this) { post ->
            val intentVideo = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(post.video)
                type = "video/*"
            }
            val shareIntent = Intent.createChooser(intentVideo, getString(R.string.play))
            startActivity(shareIntent)
        }

        val activityLauncher = registerForActivityResult(PostResultContract())
        { postContent: String? ->
            postContent?.let(viewModel::onSaveButtonClicked)
        }
        binding.save.setOnClickListener {
            activityLauncher.launch(Unit)
        }

        val editActivityLauncher = registerForActivityResult(EditResultContract())
        {
            it?.let(viewModel::onEditButtonClicked)
        }
        viewModel.editedPost.observe(this) {
            if (it != null)
                editActivityLauncher.launch(Unit)
        }
    }
}