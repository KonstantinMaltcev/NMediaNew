//package ru.netology.nmedia.ui
//
//import android.app.Activity
//import android.content.Context
//import android.content.Intent
//import androidx.activity.result.contract.ActivityResultContract
//
//class EditResultContract : ActivityResultContract<Unit, String?>() {
//
//    override fun createIntent(context: Context, input: Unit): Intent =
//        Intent(context, PostContentFragment::class.java)
//
//    override fun parseResult(resultCode: Int, intent: Intent?): String? {
//        if (resultCode != Activity.RESULT_OK) return null
//        intent ?: return null
//        return intent.getStringExtra(PostContentFragment.POST_CONTENT_EXTRA_KEY)
//
//    }
//}