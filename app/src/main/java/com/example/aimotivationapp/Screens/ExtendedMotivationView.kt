package com.example.aimotivationapp.Screens.HomeScreens.Utils

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.zIndex
import android.content.Intent
import androidx.compose.foundation.clickable
import com.example.aimotivationapp.Screens.GenerateMotivation.QuoteDecorativeCircles
import com.example.aimotivationapp.Screens.GenerateMotivation.QuoteDotIndicators
import com.example.aimotivationapp.Screens.GenerateMotivation.SwipeHintOverlay
import com.example.aimotivationapp.Screens.GenerateMotivation.gradientForTone
import com.example.aimotivationapp.UploadBackend.Motivation

// ─────────────────────────────────────────────────────────────────────────────
// Maps your Motivation.tone string → toneColor string used in gradientForTone()
// Matches exactly what your existing gradientForTone() expects
// ─────────────────────────────────────────────────────────────────────────────
private fun toneToColorKey(tone: String): String = when (tone) {
    ToneColor.Motivational.toString() -> "purple"
    ToneColor.Encouraging.toString()  -> "blue"
    ToneColor.Positive.toString()     -> "green"
    ToneColor.Uplifting.toString()    -> "orange"
    ToneColor.Friendly.toString()     -> "pink"
    else                              -> "purple"
}


@Composable
fun MotivationDetailFullScreen(
    motivation: Motivation,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    // ── Map tone → gradient colors using your existing gradientForTone() ──────
    val colorKey = toneToColorKey(motivation.tone)
    val gradientColors = gradientForTone(colorKey)

    // ── Animate gradient colors smoothly ──────────────────────────────────────
    val animColor1 by animateColorAsState(
        targetValue = gradientColors[0],
        animationSpec = tween(700, easing = EaseInOutCubic),
        label = "c1"
    )
    val animColor2 by animateColorAsState(
        targetValue = gradientColors[1],
        animationSpec = tween(700, easing = EaseInOutCubic),
        label = "c2"
    )
    val animColor3 by animateColorAsState(
        targetValue = gradientColors[2],
        animationSpec = tween(700, easing = EaseInOutCubic),
        label = "c3"
    )

    // ── Share helper ──────────────────────────────────────────────────────────
    fun shareMotivation() {
        val shareText = buildString {
            append("${mapToneToEmoji(motivation.tone)} ${motivation.tone}\n\n")
            append("\"${motivation.message}\"\n\n")
            append("— Sent via AI Motivation App")
        }
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        context.startActivity(Intent.createChooser(intent, "Share motivation via…"))
    }

    // ── Full screen Box with gradient + decorations ───────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(animColor1, animColor2, animColor3)
                )
            )
    ) {

        // ── Decorative circles — your exact existing component ────────────────
        QuoteDecorativeCircles(
            darkColor = animColor1,
            lightColor = animColor3
        )

        // ── Back / close button ───────────────────────────────────────────────
        IconButton(
            onClick = onDismiss,
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(start = 16.dp, top = 12.dp)
                .size(44.dp)
                .zIndex(10f)
                .shadow(12.dp, CircleShape)
                .background(Color.White.copy(alpha = 0.18f), CircleShape)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Close",
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }

        // ── Center: scrollable quote card ─────────────────────────────────────
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .statusBarsPadding()
                .padding(
                    top = 80.dp,      // clears back button
                    start = 24.dp,
                    end = 24.dp,
                    bottom = 140.dp   // clears bottom share bar
                )
                .fillMaxWidth()
        ) {
            DetailScrollableQuoteCard(motivation = motivation)
        }

        // ── Bottom fixed bar: share button ────────────────────────────────────
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    bottom = 32.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Subtle hint text
            Text(
                text = "tap ← to close",
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.40f),
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.2.sp
            )

            // Share button — full width pill style
            DetailShareButton(onClick = { shareMotivation() })
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Scrollable Quote Card
// Short text → wraps height naturally
// Long text  → scrolls internally, card height capped
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun DetailScrollableQuoteCard(motivation: Motivation) {
    val cardScrollState = rememberScrollState()

    val showTopFade by remember {
        derivedStateOf { cardScrollState.value > 0 }
    }
    val showBottomFade by remember {
        derivedStateOf {
            cardScrollState.value < cardScrollState.maxValue
                    && cardScrollState.maxValue > 0
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()   // ✅ short text = small card
    ) {
        // ── Glass card ────────────────────────────────────────────────────────
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            color = Color.White.copy(alpha = 0.15f),
            tonalElevation = 0.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(cardScrollState)  // ✅ only card scrolls
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // ── Open quote mark ───────────────────────────────────────────
                Text(
                    text = "\u201C",
                    fontSize = 56.sp,
                    color = Color.White.copy(alpha = 0.55f),
                    fontWeight = FontWeight.Black,
                    lineHeight = 40.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    textAlign = TextAlign.Start
                )

                // ── Quote text ────────────────────────────────────────────────
                Text(
                    text = motivation.message,
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 28.sp,
                    textAlign = TextAlign.Center
                )

                // ── Close quote mark ──────────────────────────────────────────
                Text(
                    text = "\u201D",
                    fontSize = 56.sp,
                    color = Color.White.copy(alpha = 0.55f),
                    fontWeight = FontWeight.Black,
                    lineHeight = 40.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    textAlign = TextAlign.End
                )

                HorizontalDivider(
                    color = Color.White.copy(alpha = 0.25f),
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                // ── Tone badge row ────────────────────────────────────────────
                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = Color.White.copy(alpha = 0.18f)
                ) {
                    Row(
                        modifier = Modifier.padding(
                            horizontal = 16.dp,
                            vertical = 7.dp
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = mapToneToEmoji(motivation.tone),
                            fontSize = 14.sp
                        )
                        Text(
                            text = motivation.tone,
                            fontSize = 13.sp,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // ── Timestamp ─────────────────────────────────────────────────
                if (motivation.time != null) {
                    Text(
                        text = formatTime(motivation.time),
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.50f),
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // ── Top fade — shows when user has scrolled down ──────────────────────
        AnimatedVisibility(
            visible = showTopFade,
            enter = fadeIn(tween(200)),
            exit = fadeOut(tween(200)),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Black.copy(alpha = 0.22f),
                                Color.Transparent
                            )
                        )
                    )
            )
        }

        // ── Bottom fade — hints more content below ────────────────────────────
        AnimatedVisibility(
            visible = showBottomFade,
            enter = fadeIn(tween(200)),
            exit = fadeOut(tween(200)),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.22f)
                            )
                        )
                    )
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Share Button — pill style, matches your SendToTeamButton aesthetic
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun DetailShareButton(onClick: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "shareScale"
    )

    Button(
        onClick = {
            isPressed = true
            onClick()
            isPressed = false
        },
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White.copy(alpha = 0.22f),
            contentColor = Color.White
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        contentPadding = PaddingValues(horizontal = 32.dp, vertical = 14.dp),
        modifier = Modifier
            .shadow(16.dp, RoundedCornerShape(50))
            .graphicsLayer { scaleX = scale; scaleY = scale }
    ) {
        Icon(
            imageVector = Icons.Default.Share,
            contentDescription = "Share",
            tint = Color.White,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Share",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            letterSpacing = 0.3.sp
        )
    }
}