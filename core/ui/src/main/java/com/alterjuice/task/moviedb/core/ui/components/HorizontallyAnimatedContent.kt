package com.alterjuice.task.moviedb.core.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun <S> HorizontallyAnimatedContent(
    modifier: Modifier = Modifier,
    targetState: S,
    stateToOrderIndex: (S) -> Int,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable() AnimatedContentScope.(targetState: S) -> Unit
) {
    AnimatedContent(
        targetState = targetState,
        modifier = modifier,
        contentAlignment = contentAlignment,
        transitionSpec = {
            val targetIndex = stateToOrderIndex(targetState)
            val initialIndex = stateToOrderIndex(initialState)

            if (targetIndex == initialIndex) {
                ContentTransform(fadeIn(), fadeOut())
            } else {
                val (inMultiplier, outMultiplier) = if (targetIndex > initialIndex) {
                    +1 to -1
                } else {
                    -1 to +1
                }
                ContentTransform(
                    fadeIn(tween(300)) + slideInHorizontally(tween(400)) { inMultiplier * 200 },
                    fadeOut(tween(300)) + slideOutHorizontally(tween(400)) { outMultiplier * 200 }
                )
            }
        },
        content = content
    )
}