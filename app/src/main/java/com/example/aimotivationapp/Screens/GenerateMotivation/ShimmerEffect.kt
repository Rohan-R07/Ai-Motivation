package com.example.aimotivationapp.Screens.GenerateMotivation

import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// ─── Shimmer Effect ───────────────────────────────────────────────────────────

@Composable
fun ShimmerQuotePlaceholder() {

    // ── Shimmer sweep animation ───────────────────────────────────────────────
    val shimmerTransition = rememberInfiniteTransition(label = "shimmer")
    val shimmerOffset by shimmerTransition.animateFloat(
        initialValue  = -1000f,
        targetValue   = 1000f,
        animationSpec = infiniteRepeatable(
            animation  = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerOffset"
    )

    // ── Pulse opacity animation ───────────────────────────────────────────────
    val pulseAlpha by shimmerTransition.animateFloat(
        initialValue  = 0.4f,
        targetValue   = 0.9f,
        animationSpec = infiniteRepeatable(
            animation  = tween(900, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            Color.White.copy(alpha = 0.10f),
            Color.White.copy(alpha = 0.35f),
            Color.White.copy(alpha = 0.10f),
        ),
        start = androidx.compose.ui.geometry.Offset(shimmerOffset - 300f, 0f),
        end   = androidx.compose.ui.geometry.Offset(shimmerOffset + 300f, 0f)
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier            = Modifier.fillMaxWidth()
    ) {
        // ── Opening quote mark placeholder ────────────────────────────────────
        Box(
            modifier = Modifier
                .align(Alignment.Start)
                .width(48.dp)
                .height(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White.copy(alpha = pulseAlpha * 0.3f))
                .background(shimmerBrush)
        )

        // ── Text line placeholders ────────────────────────────────────────────
        listOf(1f, 0.85f, 0.92f, 0.75f, 0.88f).forEach { widthFraction ->
            Box(
                modifier = Modifier
                    .fillMaxWidth(widthFraction)
                    .height(18.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color.White.copy(alpha = pulseAlpha * 0.3f))
                    .background(shimmerBrush)
            )
        }

        // ── Closing quote mark placeholder ────────────────────────────────────
        Box(
            modifier = Modifier
                .align(Alignment.End)
                .width(48.dp)
                .height(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White.copy(alpha = pulseAlpha * 0.3f))
                .background(shimmerBrush)
        )
    }
}

// ─── Shimmer Dot Indicators ───────────────────────────────────────────────────

@Composable
fun ShimmerDotIndicators() {
    val shimmerTransition = rememberInfiniteTransition(label = "dotShimmer")

    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment     = Alignment.CenterVertically
    ) {
        // ✅ Each dot pulses with a staggered delay
        repeat(5) { i ->
            val alpha by shimmerTransition.animateFloat(
                initialValue  = 0.25f,
                targetValue   = 0.85f,
                animationSpec = infiniteRepeatable(
                    animation   = tween(
                        durationMillis = 700,
                        delayMillis    = i * 120,   // stagger each dot
                        easing         = EaseInOutSine
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "dotAlpha_$i"
            )

            val width by animateDpAsState(
                targetValue   = 7.dp,
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                label         = "dotWidth_$i"
            )

            Box(
                modifier = Modifier
                    .height(7.dp)
                    .width(width)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = alpha))
            )
        }
    }
}