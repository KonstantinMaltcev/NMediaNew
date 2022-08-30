package ru.netology.nmedia.utils

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import ru.netology.nmedia.R

internal fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, /*flags*/ 0)
}

internal fun EditText.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, 0)
}



class ViewsUtils {

    companion object {
        private lateinit var res: Resources
        fun countFormatter(resources: Resources, count: Int): String {

            res = resources

            val templateNoSuf =
                res.getString(R.string.formatted_like_rep_view_count_without_suffix)
            val suffixThousands =
                res.getString(R.string.suffix_thousands)
            val suffixMillions =
                res.getString(R.string.suffix_millions)

            when (count) {
                in (0 until 1000) -> {
                    return String.format(templateNoSuf, count)
                }
                in (1000 until 10_000) -> {
                    val tensOfHundreds = count / 100
                    val hundreds = tensOfHundreds % 10
                    val thousands = tensOfHundreds / 10
                    return stringOfTwoDigits(thousands, hundreds, suffixThousands)
                }
                in (10_000 until 1_000_000) -> {
                    val thousands = count / 1000
                    val hundreds = (count % 1000) / 100
                    return stringOfTwoDigits(thousands, hundreds, suffixThousands)
                }
                else -> {
                    val tensOfThousands = count / 100_000
                    val thousands = tensOfThousands % 10
                    val millions = tensOfThousands / 10
                    return stringOfTwoDigits(millions, thousands, suffixMillions)
                }
            }

        }

        private fun stringOfTwoDigits(firstDigit: Int, secondDigit: Int, suffix: String): String {

            val templateTwoDigSuf =
                res.getString(R.string.formatted_like_rep_view_count_two_dig_suf)
            val templateOneDigSuf =
                res.getString(R.string.formatted_like_rep_view_thousands_one_dig_suf)

            return if (secondDigit != 0) {
                String.format(templateTwoDigSuf, firstDigit, secondDigit, suffix)
            } else {
                String.format(templateOneDigSuf, firstDigit, suffix)
            }
        }
    }


}