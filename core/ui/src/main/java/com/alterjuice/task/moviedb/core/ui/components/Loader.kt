package com.alterjuice.task.moviedb.core.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun Loader(
    modifier: Modifier = Modifier,
    options: LoaderOptions = rememberLoaderOptions(),
) {
    val infiniteTransition = rememberInfiniteTransition(label = "LoaderInfiniteTransition")
    val spinnerRotation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(500, easing = LinearEasing), RepeatMode.Restart),
        label = "RotationAnimation"
    )
    val spinnerSweep = infiniteTransition.animateFloat(
        initialValue = 10f,
        targetValue = 270f,
        animationSpec = infiniteRepeatable(tween(700, easing = LinearEasing), RepeatMode.Reverse),
        label = "SweepSpinnerAnimation"
    )
    val spinnerColor = when (val color = options.color) {
        Color.Unspecified -> LocalContentColor.current
        else -> color
    }
    Spacer(
        modifier = modifier
            .size(options.size)
            .padding(options.padding)
            .graphicsLayer {
                rotationZ = spinnerRotation.value
            }
            .drawWithCache {
                val style = Stroke(options.strokeWidth.toPx())
                onDrawBehind {
                    drawArc(
                        color = spinnerColor,
                        startAngle = 0f,
                        sweepAngle = spinnerSweep.value,
                        useCenter = false,
                        style = style
                    )
                }
            }
    )
}


@Stable
@Immutable
data class LoaderOptions(
    val padding: PaddingValues,
    val color: Color,
    val strokeWidth: Dp,
    val size: Dp,
)

@Composable
fun rememberLoaderOptions(
    padding: PaddingValues = PaddingValues(0.dp),
    color: Color = MaterialTheme.colorScheme.primary,
    strokeWidth: Dp = 4.dp,
    size: Dp = 50.dp,
) = remember {
    LoaderOptions(
        padding = padding,
        color = color,
        strokeWidth = strokeWidth,
        size = size
    )
}

@Preview
@Composable
private fun LoaderPreview() {
    Loader(
        modifier = Modifier,
        options = rememberLoaderOptions()
    )
}
