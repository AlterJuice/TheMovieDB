package com.alterjuice.task.moviedb.feature.movies.ui.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.alterjuice.task.moviedb.core.ui.utils.EffectHandler
import com.alterjuice.task.moviedb.core.ui.utils.rememberEffectHandlerOfType
import com.alterjuice.task.moviedb.feature.movies.model.MoviesEffect

@Composable
fun rememberShareEffectHandler(): EffectHandler<MoviesEffect.ShareMovie> {
    val context = LocalContext.current
    return rememberEffectHandlerOfType { effect ->
        val shareText = "Look what a movie I found:\n'${effect.movieTitle}' - ${effect.movieUrl}"
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        val resolveInfo = context.packageManager.resolveActivity(sendIntent, 0)

        if (resolveInfo != null) {
            val shareIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(shareIntent)
        } else {
            // Using a fallback as copy to clipboard in case there is no handler for our intent
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Copied movie link", shareText)
            clipboard.setPrimaryClip(clip)
        }
    }
}