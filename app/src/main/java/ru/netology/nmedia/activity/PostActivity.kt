package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityPostBinding

class PostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        val binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editPost.requestFocus()
        binding.ok.setOnClickListener {
            onOkButtonClicked(binding.editPost.text?.toString())
        }
    }

    private fun onOkButtonClicked(postContent: String?) {
        val intent = Intent()
        if (postContent.isNullOrBlank()) {
            setResult(Activity.RESULT_CANCELED, intent)
        } else {
            intent.putExtra(POST_CONTENT_EXTRA_KEY, postContent)
            setResult(Activity.RESULT_OK, intent)
        }
        finish()


    }

    companion object {
        const val POST_CONTENT_EXTRA_KEY = "postContent"
    }
}
