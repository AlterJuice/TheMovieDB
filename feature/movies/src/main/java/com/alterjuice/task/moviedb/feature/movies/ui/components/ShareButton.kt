package com.alterjuice.task.moviedb.feature.movies.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ShareButton(
    modifier: Modifier = Modifier,
    onShare: () -> Unit,
) {
    IconButton(modifier = modifier, onClick = onShare) {
        Icon(
            imageVector = Icons.Rounded.Share,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
@Preview
private fun ShareButtonPreview() {
    ShareButton(
        modifier = Modifier,
        onShare = { }
    )
}