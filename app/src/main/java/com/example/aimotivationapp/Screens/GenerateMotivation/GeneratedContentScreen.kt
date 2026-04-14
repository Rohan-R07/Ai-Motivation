package com.example.aimotivationapp.Screens.GenerateMotivation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─── Data ───────────────────────────────────────────────────────────────────
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.graphicsLayer
//import androidx.compose.ui.input.pointer.pointerInput
//import androidx.compose.ui.platform.LocalView
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.core.view.WindowCompat
//import androidx.core.view.WindowInsetsCompat
//import androidx.core.view.WindowInsetsControllerCompat
//import com.example.aimotivationapp.Screens.HomeScreens.Utils.RecentMotivation
//
//// ─── Data Model ──────────────────────────────────────────────────────────────
//
////data class RecentMotivation(
////    val id: Int,
////    val tone: String,
////    val toneColor: String,
////    val message: String,
////    val timeAgo: String,
////    val sentBy: String,
////    val emoji: String,
////)
//
//// ─── Gradient Palettes per tone ───────────────────────────────────────────────
//
//fun gradientForTone(toneColor: String): List<Color> {
//    return when (toneColor.lowercase()) {
//        "purple" -> listOf(Color(0xFF6A5ACD), Color(0xFF9B8FE0), Color(0xFFB8A9FF))
//        "blue"   -> listOf(Color(0xFF1A6B8A), Color(0xFF2899C4), Color(0xFF56CCF2))
//        "violet" -> listOf(Color(0xFF8B2FC9), Color(0xFFBB56E8), Color(0xFFE040FB))
//        "green"  -> listOf(Color(0xFF0D5C4A), Color(0xFF1B8A6B), Color(0xFF43C6AC))
//        "orange" -> listOf(Color(0xFF7B3F00), Color(0xFFBF6B0A), Color(0xFFFFB347))
//        "indigo" -> listOf(Color(0xFF1A237E), Color(0xFF3949AB), Color(0xFF7986CB))
//        "pink"   -> listOf(Color(0xFF880E4F), Color(0xFFC2185B), Color(0xFFF48FB1))
//        "teal"   -> listOf(Color(0xFF004D5B), Color(0xFF00838F), Color(0xFF4DD0E1))
//        "red"    -> listOf(Color(0xFF7F0000), Color(0xFFC62828), Color(0xFFEF9A9A))
//        "gold"   -> listOf(Color(0xFF5D4037), Color(0xFF8D6E63), Color(0xFFFFD54F))
//        else     -> listOf(Color(0xFF6A5ACD), Color(0xFF9B8FE0), Color(0xFFB8A9FF))
//    }
//}
//
//// ─── Sample Data ─────────────────────────────────────────────────────────────
//
//val sampleMotivations = listOf(
//    RecentMotivation(
//        id = 1,
//        tone = "Inspirational",
//        toneColor = "purple",
//        message = "Your Positive attitude and dedication are the cornerstones of our success. Keep pushing boundaries and inspiring those around you. Every small step you take today is a giant leap towards our collective goals.",
//        timeAgo = "2m ago",
//        sentBy = "Alex",
//        emoji = "🚀"
//    ),
//    RecentMotivation(
//        id = 2,
//        tone = "Encouraging",
//        toneColor = "blue",
//        message = "Believe in yourself and all that you are. Know that there is something inside you that is greater than any challenge, any obstacle, any setback you may face today.",
//        timeAgo = "10m ago",
//        sentBy = "Sarah",
//        emoji = "💪"
//    ),
//    RecentMotivation(
//        id = 3,
//        tone = "Bold",
//        toneColor = "violet",
//        message = "Success is not final, failure is not fatal: It is the courage to continue that counts. Rise up, show up, and never give up on the vision you hold in your heart.",
//        timeAgo = "1h ago",
//        sentBy = "Mike",
//        emoji = "🔥"
//    ),
//    RecentMotivation(
//        id = 4,
//        tone = "Calm",
//        toneColor = "green",
//        message = "The secret of getting ahead is getting started. Your consistency today builds the extraordinary tomorrow. You are capable of far more than you imagine.",
//        timeAgo = "2h ago",
//        sentBy = "Emma",
//        emoji = "🌿"
//    ),
//    RecentMotivation(
//        id = 5,
//        tone = "Energetic",
//        toneColor = "orange",
//        message = "Champions are not made in gyms. Champions are made from something deep inside them — a desire, a dream, a vision. Push yourself because no one else will do it for you.",
//        timeAgo = "3h ago",
//        sentBy = "Jordan",
//        emoji = "⚡"
//    ),
//    RecentMotivation(
//        id = 6,
//        tone = "Focused",
//        toneColor = "indigo",
//        message = "Do not watch the clock; do what it does — keep going. Your hard work is building something magnificent, brick by brick, day by day. The team believes in you.",
//        timeAgo = "5h ago",
//        sentBy = "Chris",
//        emoji = "🎯"
//    ),
//    RecentMotivation(
//        id = 7,
//        tone = "Resilient",
//        toneColor = "pink",
//        message = "Strength does not come from winning. Your struggles develop your strengths. When you go through hardships and decide not to surrender, that is strength.",
//        timeAgo = "1d ago",
//        sentBy = "Taylor",
//        emoji = "💎"
//    ),
//)
//
//// ─── Main Screen ─────────────────────────────────────────────────────────────
//
//@Composable
//fun GeneratedMotivationQuoteScreen(
//    motivations: List<RecentMotivation> = sampleMotivations,
//    onBack: () -> Unit = {},
//    onSendToTeam: (RecentMotivation) -> Unit = {}
//) {
//    // ── Hide status bar ──────────────────────────────────────────────────────
//    val view = LocalView.current
//    SideEffect {
//        val window = (view.context as? android.app.Activity)?.window ?: return@SideEffect
//        WindowCompat.setDecorFitsSystemWindows(window, false)
//        WindowInsetsControllerCompat(window, view).apply {
//            hide(WindowInsetsCompat.Type.statusBars())
//            systemBarsBehavior =
//                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//        }
//    }
//
//    // ── State ────────────────────────────────────────────────────────────────
//    var currentIndex by remember { mutableIntStateOf(0) }
//    var isForward by remember { mutableStateOf(true) }
//    var dragOffset by remember { mutableFloatStateOf(0f) }
//    val swipeThreshold = 80f
//
//    val currentMotivation = motivations[currentIndex]
//    val gradientColors = gradientForTone(currentMotivation.toneColor)
//
//    // ── Animated gradient ────────────────────────────────────────────────────
//    val animColor1 by animateColorAsState(
//        targetValue = gradientColors[0],
//        animationSpec = tween(700, easing = EaseInOutCubic),
//        label = "c1"
//    )
//    val animColor2 by animateColorAsState(
//        targetValue = gradientColors[1],
//        animationSpec = tween(700, easing = EaseInOutCubic),
//        label = "c2"
//    )
//    val animColor3 by animateColorAsState(
//        targetValue = gradientColors[2],
//        animationSpec = tween(700, easing = EaseInOutCubic),
//        label = "c3"
//    )
//
//    // ── Swipe offset animation ───────────────────────────────────────────────
//    val animatedDragOffset by animateFloatAsState(
//        targetValue = dragOffset,
//        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
//        label = "drag"
//    )
//
//    // ── Swipe handler ────────────────────────────────────────────────────────
//    fun goNext() {
//        if (currentIndex < motivations.lastIndex) {
//            isForward = true
//            currentIndex++
//        }
//        dragOffset = 0f
//    }
//
//    fun goPrev() {
//        if (currentIndex > 0) {
//            isForward = false
//            currentIndex--
//        }
//        dragOffset = 0f
//    }
//
//    // ── Root ─────────────────────────────────────────────────────────────────
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(
//                Brush.verticalGradient(
//                    colors = listOf(animColor1, animColor2, animColor3)
//                )
//            )
//            .pointerInput(currentIndex) {
//                detectHorizontalDragGestures(
//                    onDragEnd = {
//                        when {
//                            dragOffset < -swipeThreshold -> goNext()
//                            dragOffset > swipeThreshold  -> goPrev()
//                            else                         -> dragOffset = 0f
//                        }
//                    },
//                    onDragCancel = { dragOffset = 0f },
//                    onHorizontalDrag = { _, dragAmount ->
//                        val newOffset = dragOffset + dragAmount
//                        // Resist at edges
//                        dragOffset = when {
//                            newOffset < 0 && currentIndex == motivations.lastIndex ->
//                                newOffset * 0.2f
//                            newOffset > 0 && currentIndex == 0 ->
//                                newOffset * 0.2f
//                            else -> newOffset.coerceIn(-300f, 300f)
//                        }
//                    }
//                )
//            }
//    ) {
//        // ── Background decorative circles ────────────────────────────────────
//        QuoteDecorativeCircles(animColor1, animColor3)
//
//        // ── Swipe hint overlay (subtle) ──────────────────────────────────────
//        SwipeHintOverlay(
//            dragOffset = animatedDragOffset,
//            canGoNext = currentIndex < motivations.lastIndex,
//            canGoPrev = currentIndex > 0
//        )
//
//        // ── Back button ──────────────────────────────────────────────────────
//        IconButton(
//            onClick = onBack,
//            modifier = Modifier
//                .align(Alignment.TopStart)
//                .statusBarsPadding()
//                .padding(start = 16.dp, top = 12.dp)
//                .size(44.dp)
//                .shadow(12.dp, CircleShape)
//                .background(Color.White.copy(alpha = 0.18f), CircleShape)
//        ) {
//            Icon(
//                imageVector = Icons.Default.ArrowBack,
//                contentDescription = "Back",
//                tint = Color.White,
//                modifier = Modifier.size(22.dp)
//            )
//        }
//
//        // ── Tone badge ───────────────────────────────────────────────────────
//        AnimatedContent(
//            targetState = currentIndex,
//            transitionSpec = { fadeIn(tween(400)) togetherWith fadeOut(tween(300)) },
//            modifier = Modifier
//                .align(Alignment.TopEnd)
//                .statusBarsPadding()
//                .padding(end = 16.dp, top = 12.dp),
//            label = "toneBadge"
//        ) { idx ->
//            ToneBadge(
//                emoji = motivations[idx].emoji,
//                tone = motivations[idx].tone
//            )
//        }
//
//        // ── Animated quote content ───────────────────────────────────────────
//        val enterAnim = if (isForward)
//            slideInHorizontally(
//                initialOffsetX = { it },
//                animationSpec = tween(500, easing = EaseOutQuart)
//            ) + fadeIn(tween(400))
//        else
//            slideInHorizontally(
//                initialOffsetX = { -it },
//                animationSpec = tween(500, easing = EaseOutQuart)
//            ) + fadeIn(tween(400))
//
//        val exitAnim = if (isForward)
//            slideOutHorizontally(
//                targetOffsetX = { -it },
//                animationSpec = tween(400, easing = EaseInQuart)
//            ) + fadeOut(tween(300))
//        else
//            slideOutHorizontally(
//                targetOffsetX = { it },
//                animationSpec = tween(400, easing = EaseInQuart)
//            ) + fadeOut(tween(300))
//
//        AnimatedContent(
//            targetState = currentIndex,
//            transitionSpec = { enterAnim togetherWith exitAnim },
//            modifier = Modifier
//                .align(Alignment.Center)
//                .padding(horizontal = 28.dp)
//                .offset(y = (-20).dp),
//            label = "quoteContent"
//        ) { idx ->
//            QuoteCard(motivations[idx])
//        }
//
//        // ── Bottom section ───────────────────────────────────────────────────
//        Column(
//            modifier = Modifier
//                .align(Alignment.BottomCenter)
//                .navigationBarsPadding()
//                .padding(start = 24.dp, end = 24.dp, bottom = 36.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.spacedBy(20.dp)
//        ) {
//            // Swipe hint text
//            AnimatedVisibility(
//                visible = dragOffset == 0f,
//                enter = fadeIn(tween(300)),
//                exit = fadeOut(tween(200))
//            ) {
//                Text(
//                    text = "swipe to explore",
//                    fontSize = 12.sp,
//                    color = Color.White.copy(alpha = 0.50f),
//                    fontWeight = FontWeight.Medium,
//                    letterSpacing = 1.5.sp
//                )
//            }
//
//            // Dot indicators
//            QuoteDotIndicators(
//                total = motivations.size,
//                currentIndex = currentIndex
//            )
//
//            // Action row
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                // Sender info
//                AnimatedContent(
//                    targetState = currentIndex,
//                    transitionSpec = {
//                        fadeIn(tween(400)) + slideInVertically { it / 2 } togetherWith
//                                fadeOut(tween(300))
//                    },
//                    label = "senderInfo"
//                ) { idx ->
//                    SenderChip(
//                        sentBy = motivations[idx].sentBy,
//                        timeAgo = motivations[idx].timeAgo
//                    )
//                }
//
//                // Send to Team button
//                SendToTeamButton(
//                    onClick = { onSendToTeam(currentMotivation) }
//                )
//            }
//        }
//    }
//}
//
//// ─── Quote Card ──────────────────────────────────────────────────────────────
//
//@Composable
//fun QuoteCard(motivation: RecentMotivation) {
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        Text(
//            text = "\u201C",
//            fontSize = 90.sp,
//            fontWeight = FontWeight.Black,
//            color = Color.White.copy(alpha = 0.20f),
//            lineHeight = 50.sp,
//            modifier = Modifier
//                .align(Alignment.Start)
//                .offset(x = (-4).dp, y = 20.dp)
//        )
//
//        Text(
//            text = motivation.message,
//            fontSize = 21.sp,
//            fontWeight = FontWeight.Bold,
//            color = Color.White,
//            textAlign = TextAlign.Center,
//            lineHeight = 33.sp,
//            letterSpacing = 0.2.sp
//        )
//
//        Text(
//            text = "\u201D",
//            fontSize = 90.sp,
//            fontWeight = FontWeight.Black,
//            color = Color.White.copy(alpha = 0.20f),
//            lineHeight = 50.sp,
//            modifier = Modifier
//                .align(Alignment.End)
//                .offset(x = 4.dp, y = (-20).dp)
//        )
//    }
//}
//
//// ─── Tone Badge ──────────────────────────────────────────────────────────────
//
//@Composable
//fun ToneBadge(emoji: String, tone: String) {
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.spacedBy(4.dp),
//        modifier = Modifier
//            .clip(RoundedCornerShape(50))
//            .background(Color.White.copy(alpha = 0.18f))
//            .padding(horizontal = 12.dp, vertical = 6.dp)
//    ) {
//        Text(text = emoji, fontSize = 14.sp)
//        Text(
//            text = tone,
//            fontSize = 12.sp,
//            fontWeight = FontWeight.SemiBold,
//            color = Color.White
//        )
//    }
//}
//
//// ─── Sender Chip ─────────────────────────────────────────────────────────────
//
//@Composable
//fun SenderChip(sentBy: String, timeAgo: String) {
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.spacedBy(8.dp),
//        modifier = Modifier
//            .clip(RoundedCornerShape(50))
//            .background(Color.White.copy(alpha = 0.15f))
//            .padding(horizontal = 14.dp, vertical = 8.dp)
//    ) {
//        // Avatar circle
//        Box(
//            modifier = Modifier
//                .size(28.dp)
//                .background(Color.White.copy(alpha = 0.30f), CircleShape),
//            contentAlignment = Alignment.Center
//        ) {
//            Text(
//                text = sentBy.first().toString(),
//                fontSize = 13.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color.White
//            )
//        }
//        Column {
//            Text(
//                text = sentBy,
//                fontSize = 12.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color.White
//            )
//            Text(
//                text = timeAgo,
//                fontSize = 10.sp,
//                color = Color.White.copy(alpha = 0.70f)
//            )
//        }
//    }
//}
//
//// ─── Send To Team Button ─────────────────────────────────────────────────────
//
//@Composable
//fun SendToTeamButton(onClick: () -> Unit) {
//    var isPressed by remember { mutableStateOf(false) }
//    val scale by animateFloatAsState(
//        targetValue = if (isPressed) 0.94f else 1f,
//        animationSpec = spring(stiffness = Spring.StiffnessMedium),
//        label = "btnScale"
//    )
//
//    Button(
//        onClick = {
//            isPressed = true
//            onClick()
//            isPressed = false
//        },
//        shape = RoundedCornerShape(50),
//        colors = ButtonDefaults.buttonColors(
//            containerColor = Color.White.copy(alpha = 0.22f),
//            contentColor = Color.White
//        ),
//        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
//        contentPadding = PaddingValues(horizontal = 22.dp, vertical = 14.dp),
//        modifier = Modifier
//            .shadow(16.dp, RoundedCornerShape(50))
//            .graphicsLayer { scaleX = scale; scaleY = scale }
//    ) {
//        Icon(
//            imageVector = Icons.Default.Send,
//            contentDescription = null,
//            modifier = Modifier.size(15.dp),
//            tint = Color.White
//        )
//        Spacer(modifier = Modifier.width(8.dp))
//        Text(
//            text = "Send to team",
//            fontWeight = FontWeight.Bold,
//            fontSize = 14.sp,
//            letterSpacing = 0.3.sp
//        )
//    }
//}
//
//// ─── Dot Indicators ──────────────────────────────────────────────────────────
//
//@Composable
//fun QuoteDotIndicators(total: Int, currentIndex: Int) {
//    Row(
//        horizontalArrangement = Arrangement.spacedBy(6.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        repeat(total) { i ->
//            val width by animateDpAsState(
//                targetValue = if (i == currentIndex) 28.dp else 7.dp,
//                animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
//                label = "dot_$i"
//            )
//            val alpha by animateFloatAsState(
//                targetValue = if (i == currentIndex) 1f else 0.38f,
//                animationSpec = tween(300),
//                label = "dotAlpha_$i"
//            )
//            Box(
//                modifier = Modifier
//                    .height(7.dp)
//                    .width(width)
//                    .clip(CircleShape)
//                    .background(Color.White.copy(alpha = alpha))
//            )
//        }
//    }
//}
//
//// ─── Swipe Hint Overlay ───────────────────────────────────────────────────────
//
//@Composable
//fun SwipeHintOverlay(
//    dragOffset: Float,
//    canGoNext: Boolean,
//    canGoPrev: Boolean
//) {
//    val nextAlpha = if (dragOffset < -20f && canGoNext)
//        ((-dragOffset - 20f) / 100f).coerceIn(0f, 0.25f)
//    else 0f
//
//    val prevAlpha = if (dragOffset > 20f && canGoPrev)
//        ((dragOffset - 20f) / 100f).coerceIn(0f, 0.25f)
//    else 0f
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        // Right edge glow when swiping left (next)
//        if (nextAlpha > 0f) {
//            Box(
//                modifier = Modifier
//                    .fillMaxHeight()
//                    .width(80.dp)
//                    .align(Alignment.CenterEnd)
//                    .background(
//                        Brush.horizontalGradient(
//                            colors = listOf(
//                                Color.Transparent,
//                                Color.White.copy(alpha = nextAlpha)
//                            )
//                        )
//                    )
//            )
//        }
//        // Left edge glow when swiping right (prev)
//        if (prevAlpha > 0f) {
//            Box(
//                modifier = Modifier
//                    .fillMaxHeight()
//                    .width(80.dp)
//                    .align(Alignment.CenterStart)
//                    .background(
//                        Brush.horizontalGradient(
//                            colors = listOf(
//                                Color.White.copy(alpha = prevAlpha),
//                                Color.Transparent
//                            )
//                        )
//                    )
//            )
//        }
//    }
//}
//
//// ─── Decorative Circles ──────────────────────────────────────────────────────
//
//@Composable
//fun QuoteDecorativeCircles(darkColor: Color, lightColor: Color) {
//    Box(modifier = Modifier.fillMaxSize()) {
//        Box(
//            modifier = Modifier
//                .size(260.dp)
//                .offset(x = 80.dp, y = (-80).dp)
//                .align(Alignment.TopEnd)
//                .background(
//                    Brush.radialGradient(
//                        colors = listOf(lightColor.copy(alpha = 0.28f), Color.Transparent)
//                    ),
//                    CircleShape
//                )
//        )
//        Box(
//            modifier = Modifier
//                .size(200.dp)
//                .offset(x = (-60).dp, y = 50.dp)
//                .align(Alignment.BottomStart)
//                .background(
//                    Brush.radialGradient(
//                        colors = listOf(darkColor.copy(alpha = 0.32f), Color.Transparent)
//                    ),
//                    CircleShape
//                )
//        )
//        Box(
//            modifier = Modifier
//                .size(340.dp)
//                .align(Alignment.Center)
//                .background(
//                    Brush.radialGradient(
//                        colors = listOf(Color.White.copy(alpha = 0.05f), Color.Transparent)
//                    ),
//                    CircleShape
//                )
//        )
//    }
//}