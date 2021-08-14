package dev.unusedvariable.vlr.utils

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.ui.unit.IntOffset


private val tween: FiniteAnimationSpec<IntOffset> = tween(800)
private val tweenF: FiniteAnimationSpec<Float> = tween(400)

val slideInFromLeft = slideInHorizontally(
    animationSpec = tween,
    initialOffsetX = { -it })

val slideInFromRight = slideInHorizontally(
    animationSpec = tween,
    initialOffsetX = { it })

val slideInFromBottom =
    slideInVertically(
        animationSpec = tween,
        initialOffsetY = { it })

val slideInFromTop =
    slideInVertically(
        animationSpec = tween,
        initialOffsetY = { -it })

val fadeIn = fadeIn(animationSpec = tweenF)
val fadeOut = fadeOut(animationSpec = tweenF)