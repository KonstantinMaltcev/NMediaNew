package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityPostBinding
import ru.netology.nmedia.databinding.ActivityShareBinding


class ShareActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = intent ?: return
        if (intent.action != Intent.ACTION_SEND) return
        if (intent.type != "text/plain") {
            Snackbar.make(binding.root, R.string.unknown_type, Snackbar.LENGTH_INDEFINITE)
                .setAction(android.R.string.ok) {
                    finish()
                }.show()
        }

        val text = intent.getStringExtra(Intent.EXTRA_TEXT)
        if (text.isNullOrBlank()) {
            Snackbar.make(binding.root, R.string.text_is_blank, Snackbar.LENGTH_INDEFINITE)
                .setAction(android.R.string.ok) {
                    finish()
                }.show()
        } else {
            binding.editPost
                .setText(text)
        }
    }
}