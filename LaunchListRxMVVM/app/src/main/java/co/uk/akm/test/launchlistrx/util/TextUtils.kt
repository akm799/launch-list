package co.uk.akm.test.launchlistrx.util

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import androidx.annotation.StringRes

/**
 * This will only work with arguments that are formatted with the toString() method or an equivalent one.
 */
fun Context.getStringWithArgsInBold(@StringRes resId: Int, vararg args: Any?): Spannable {
    val formatted = getString(resId, *args)

    return formatWithArgsInBold(formatted, *args)
}

private fun formatWithArgsInBold(formatted: String, vararg args: Any?): Spannable {
    return SpannableStringBuilder(formatted).apply {
        args.map { it.toString() }.forEach { arg ->
            formatArgInBold(formatted, arg, this)
        }
    }
}

private fun formatArgInBold(formatted: String, arg: String, spannable: Spannable) {
    val startIndex = formatted.indexOf(arg)
    val endIndex = startIndex + arg.length
    if (startIndex >= 0) {
        spannable.setSpan(StyleSpan(Typeface.BOLD), startIndex, endIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    }
}