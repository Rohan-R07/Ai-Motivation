package com.example.aimotivationapp.Screens.GenerateMotivation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

// ─── Data ───────────────────────────────────────────────────────────────────

data class MotivationQuote(
    val text: String,
    val gradientColors: List<Color>
)

val motivationQuotes = listOf(
    MotivationQuote(
        text = "Your Positive attitude and dedication are the cornerstones of our success. Keep pushing boundaries and inspiring those around you. Every small step you take today is a giant leap towards our collective goals.",
        gradientColors = listOf(
            Color(0xFF6A5ACD),
            Color(0xFF9B8FE0),
            Color(0xFFB8A9FF)
        )
    ),
    MotivationQuote(
        text = "Believe in yourself and all that you are. Know that there is something inside you that is greater than any challenge, any obstacle, any setback you may face today.",
        gradientColors = listOf(
            Color(0xFF1A6B8A),
            Color(0xFF2899C4),
            Color(0xFF56CCF2)
        )
    ),
    MotivationQuote(
        text = "Success is not final, failure is not fatal: It is the courage to continue that counts. Rise up, show up, and never give up on the vision you hold in your heart.",
        gradientColors = listOf(
            Color(0xFF8B2FC9),
            Color(0xFFBB56E8),
            Color(0xFFE040FB)
        )
    ),
    MotivationQuote(
        text = "The secret of getting ahead is getting started. Your consistency today builds the extraordinary tomorrow. You are capable of far more than you imagine.",
        gradientColors = listOf(
            Color(0xFF0D5C4A),
            Color(0xFF1B8A6B),
            Color(0xFF43C6AC)
        )
    ),
    MotivationQuote(
        text = "Champions are not made in gyms. Champions are made from something deep inside them — a desire, a dream, a vision. Push yourself because no one else will do it for you.",
        gradientColors = listOf(
            Color(0xFF7B3F00),
            Color(0xFFBF6B0A),
            Color(0xFFFFB347)
        )
    ),
    MotivationQuote(
        text = "Do not watch the clock; do what it does — keep going. Your hard work is building something magnificent, brick by brick, day by day. The team believes in you.",
        gradientColors = listOf(
            Color(0xFF1A237E),
            Color(0xFF3949AB),
            Color(0xFF7986CB)
        )
    ),
    MotivationQuote(
        text = "Strength does not come from winning. Your struggles develop your strengths. When you go through hardships and decide not to surrender, that is strength.",
        gradientColors = listOf(
            Color(0xFF880E4F),
            Color(0xFFC2185B),
            Color(0xFFF48FB1)
        )
    ),
)

// ─── Screen ─────────────────────────────────────────────────────────────────

@Composable
fun GenerateMotivationScreen(
    onBack: () -> Unit = {},
    onSendToTeam: (String) -> Unit = {}
) {
    // ── Hide status bar ──────────────────────────────────────────────────────
    val view = LocalView.current
    SideEffect {
        val window = (view.context as? android.app.Activity)?.window ?: return@SideEffect
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, view).apply {
            hide(WindowInsetsCompat.Type.statusBars())
            systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    // ── State ────────────────────────────────────────────────────────────────
    var currentIndex by remember { mutableIntStateOf(0) }
    var isForward by remember { mutableStateOf(true) }
    val quote = motivationQuotes[currentIndex]

    // ── Animated gradient colors ─────────────────────────────────────────────
    val animatedColor1 by animateColorAsState(
        targetValue = quote.gradientColors[0],
        animationSpec = tween(700, easing = EaseInOutCubic),
        label = "color1"
    )
    val animatedColor2 by animateColorAsState(
        targetValue = quote.gradientColors[1],
        animationSpec = tween(700, easing = EaseInOutCubic),
        label = "color2"
    )
    val animatedColor3 by animateColorAsState(
        targetValue = quote.gradientColors[2],
        animationSpec = tween(700, easing = EaseInOutCubic),
        label = "color3"
    )

    // ── Actions ──────────────────────────────────────────────────────────────
    fun goNext() {
        if (currentIndex < motivationQuotes.lastIndex) {
            isForward = true
            currentIndex++
        }
    }

    fun goPrev() {
        if (currentIndex > 0) {
            isForward = false
            currentIndex--
        }
    }

    // ── Root ─────────────────────────────────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(animatedColor1, animatedColor2, animatedColor3)
                )
            )
    ) {

        // ── Decorative circles ───────────────────────────────────────────────
        DecorativeCircles(animatedColor1, animatedColor3)

        // ── Back button ──────────────────────────────────────────────────────
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(16.dp)
                .size(42.dp)
                .shadow(8.dp, CircleShape)
                .background(Color.White.copy(alpha = 0.20f), CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }

        // ── Quote text with slide animation ──────────────────────────────────
        val enterTransition = if (isForward)
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(500, easing = EaseOutQuart)
            ) + fadeIn(tween(400))
        else
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(500, easing = EaseOutQuart)
            ) + fadeIn(tween(400))

        val exitTransition = if (isForward)
            slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(400, easing = EaseInQuart)
            ) + fadeOut(tween(300))
        else
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(400, easing = EaseInQuart)
            ) + fadeOut(tween(300))

        AnimatedContent(
            targetState = currentIndex,
            transitionSpec = { enterTransition togetherWith exitTransition },
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 32.dp)
                .offset(y = (-30).dp),
            label = "quoteContent"
        ) { index ->
            QuoteContent(motivationQuotes[index].text)
        }

        // ── Bottom bar ───────────────────────────────────────────────────────
        BottomBar(
            currentIndex = currentIndex,
            total = motivationQuotes.size,
            onSendToTeam = { onSendToTeam(quote.text) },
            onPrev = { goPrev() },
            onNext = { goNext() },
            accentColor = animatedColor1,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 32.dp)
        )
    }
}

// ─── Quote Content ───────────────────────────────────────────────────────────

@Composable
fun QuoteContent(text: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Opening quotation mark
        Text(
            text = "\u201C",
            fontSize = 80.sp,
            fontWeight = FontWeight.Black,
            color = Color.White.copy(alpha = 0.25f),
            lineHeight = 60.sp,
            modifier = Modifier
                .align(Alignment.Start)
                .offset(x = (-8).dp, y = 16.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = text,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            lineHeight = 34.sp,
            letterSpacing = 0.3.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Closing quotation mark
        Text(
            text = "\u201D",
            fontSize = 80.sp,
            fontWeight = FontWeight.Black,
            color = Color.White.copy(alpha = 0.25f),
            lineHeight = 60.sp,
            modifier = Modifier
                .align(Alignment.End)
                .offset(x = 8.dp, y = (-24).dp)
        )
    }
}

// ─── Bottom Bar ──────────────────────────────────────────────────────────────

@Composable
fun BottomBar(
    currentIndex: Int,
    total: Int,
    onSendToTeam: () -> Unit,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {

        // Dot indicators
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 20.dp)
        ) {
            repeat(total) { i ->
                val width by animateDpAsState(
                    targetValue = if (i == currentIndex) 24.dp else 8.dp,
                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                    label = "dot_$i"
                )
                Box(
                    modifier = Modifier
                        .height(8.dp)
                        .width(width)
                        .clip(CircleShape)
                        .background(
                            if (i == currentIndex) Color.White
                            else Color.White.copy(alpha = 0.40f)
                        )
                )
            }
        }

        // Buttons row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Send to Team
            Button(
                onClick = onSendToTeam,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = 0.22f),
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp
                ),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                modifier = Modifier
                    .shadow(12.dp, RoundedCornerShape(50))
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward, // replace with team icon if available
                    contentDescription = null,
                    modifier = Modifier
                        .size(16.dp)
                        .padding(end = 0.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Send to team",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Prev
            NavButton(
                onClick = onPrev,
                enabled = currentIndex > 0,
                icon = Icons.Default.ArrowBack
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Next
            NavButton(
                onClick = onNext,
                enabled = currentIndex < motivationQuotes.lastIndex,
                icon = Icons.Default.ArrowForward
            )
        }
    }
}

// ─── Nav Button ──────────────────────────────────────────────────────────────

@Composable
fun NavButton(
    onClick: () -> Unit,
    enabled: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    val alpha by animateFloatAsState(
        targetValue = if (enabled) 1f else 0.4f,
        animationSpec = tween(300),
        label = "navAlpha"
    )
    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .size(48.dp)
            .shadow(10.dp, CircleShape)
            .background(Color.White.copy(alpha = 0.22f * alpha), CircleShape)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White.copy(alpha = alpha),
            modifier = Modifier.size(22.dp)
        )
    }
}

// ─── Decorative Circles ──────────────────────────────────────────────────────

@Composable
fun DecorativeCircles(darkColor: Color, lightColor: Color) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Top-right large circle
        Box(
            modifier = Modifier
                .size(220.dp)
                .offset(x = 100.dp, y = (-60).dp)
                .align(Alignment.TopEnd)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            lightColor.copy(alpha = 0.30f),
                            Color.Transparent
                        )
                    ),
                    CircleShape
                )
        )
        // Bottom-left circle
        Box(
            modifier = Modifier
                .size(180.dp)
                .offset(x = (-50).dp, y = 60.dp)
                .align(Alignment.BottomStart)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            darkColor.copy(alpha = 0.35f),
                            Color.Transparent
                        )
                    ),
                    CircleShape
                )
        )
        // Center subtle glow
        Box(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.Center)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.07f),
                            Color.Transparent
                        )
                    ),
                    CircleShape
                )
        )
    }
}