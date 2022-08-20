package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.hideKeyboard
import ru.netology.nmedia.utils.showKeyboard

class PostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        val newCurrentPost = MutableLiveData<Post?>(null)

        val binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
            // пока не надо
//        fun onEditButtonClicked(content: String) {
//            if (content.isBlank()) return
//
//            val editPost = newCurrentPost.value?.copy(
//                content = content
//            ) ?: Post(
//                id = PostRepository.NEW_POST_ID,
//                author = "Konstantin",
//                content = content,
//                published = "18/06/2022"
//            )
//            repository.save(editPost)
//            newCurrentPost.value = null
//        }
//

        binding.editPost.requestFocus()
        binding.editPost.setOnClickListener {
            with(binding.editPost) {
                val content = newCurrentPost.value?.content
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
        binding.ok.setOnClickListener {
            onOkButtonClicked(binding.editPost.text?.toString())
        }
//        val editActivityLauncher = registerForActivityResult(EditResultContract())
//        {
//            it?.let(::onEditButtonClicked)
//        }
//        binding.editPost.setOnClickListener {
//            editActivityLauncher.launch(Unit)
//        }
    }

    private fun onOkButtonClicked(postContent: String?) {
        val outIntent = Intent()
        if (postContent.isNullOrBlank()) {
            setResult(Activity.RESULT_CANCELED, outIntent)
        } else {
            outIntent.putExtra(POST_CONTENT_EXTRA_KEY, postContent)
            setResult(Activity.RESULT_OK, outIntent)
        }
        finish()
    }

    companion object {
        const val POST_CONTENT_EXTRA_KEY = "postContent"
    }
}