package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.databinding.ActivityShareBinding

class VideoPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding
            .inflate(layoutInflater)
        setContentView(binding.root)

        val intentUri = intent ?: return
        if (intentUri.action != Intent.ACTION_VIEW) return
        if (intentUri.type != "video/*") {
            Snackbar.make(binding.root, R.string.unknown_type, Snackbar.LENGTH_INDEFINITE)
                .setAction(android.R.string.ok) {
                    finish()
                }.show()
        }
        val uriText = intent.getStringExtra(Intent.EXTRA_TEXT)
        if (uriText.isNullOrBlank()) {
            uriText?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok) {
                        finish()
                    }.show()
            }
        } else {
            binding.content
                .setText(uriText)
        }
    }
}