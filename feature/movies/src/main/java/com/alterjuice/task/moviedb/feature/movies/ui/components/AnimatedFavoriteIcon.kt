package com.alterjuice.task.moviedb.feature.movies.ui.components

import androidx.compose.animation.core.EaseOutQuad
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview

@Composable
internal fun AnimatedFavoriteIcon(
    modifier: Modifier = Modifier,
    isFavorite: Boolean,
    onToggle: () -> Unit
) {
    IconButton(modifier = modifier, onClick = onToggle) {
        val scale = animateFloatAsState(
            targetValue = if (isFavorite) 1.3f else 1f,
            animationSpec = tween(200, easing = EaseOutQuad),
            label = "HeartScale"
        )
        Icon(
            imageVector = if (isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            }
        )
    }
}

@Composable
@Preview
private fun AnimatedFavoriteIconPreview() {
    val isFavorite = remember { mutableStateOf(false) }
    AnimatedFavoriteIcon(
        isFavorite = isFavorite.value,
        onToggle = { isFavorite.value = !isFavorite.value }
    )
}