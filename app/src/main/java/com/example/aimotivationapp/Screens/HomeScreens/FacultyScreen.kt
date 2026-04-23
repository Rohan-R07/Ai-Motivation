package com.example.aimotivationapp.Screens.HomeScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aimotivationapp.Datastore.PreferenceSavingViewModel
import com.example.aimotivationapp.Datastore.PreferenceViewModelFactory
import com.example.aimotivationapp.Datastore.UserPreferences
import com.example.aimotivationapp.Datastore.UserRepository
import com.example.aimotivationapp.MainNavigation.FacultyScreen
import com.example.aimotivationapp.MainNavigation.LoginScreen
import com.google.firebase.auth.FirebaseAuth

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.Notes
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.SentimentDissatisfied
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aimotivationapp.Screens.HomeScreens.Utils.MotivationCard
import com.example.aimotivationapp.Screens.HomeScreens.Utils.RecentMotivation
import com.example.aimotivationapp.Screens.HomeScreens.Utils.ToneColor
import com.example.aimotivationapp.UploadBackend.Motivation
import com.example.aimotivationapp.UploadBackend.UploadFetchMotivationQuotes
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin


// ─── Screen Size ─────────────────────────────────────────────────
enum class SClass { COMPACT, MEDIUM, EXPANDED }

private fun wClass(w: Dp) = when {
    w < 360.dp -> SClass.COMPACT
    w < 600.dp -> SClass.MEDIUM
    else -> SClass.EXPANDED
}

private fun hClass(h: Dp) = when {
    h < 600.dp -> SClass.COMPACT
    h < 800.dp -> SClass.MEDIUM
    else -> SClass.EXPANDED
}

// ─── Tone / Category Helpers ─────────────────────────────────────
//private fun toneIcon(tone: String): ImageVector = when (tone) {
//    "All" -> Icons.Rounded.Category
//    "Success" -> Icons.Rounded.EmojiEvents
//    "Growth" -> Icons.Rounded.TrendingUp
//    "Leadership" -> Icons.Rounded.Star
//    "Perseverance" -> Icons.Rounded.LocalFireDepartment
//    "Creativity" -> Icons.Rounded.AutoAwesome
//    "Wisdom" -> Icons.Rounded.Psychology
//    "Focus" -> Icons.Rounded.School
//    else -> Icons.Rounded.Notes
//}

private fun toneColor(tone: String): Color = when (tone) {
    "Success" -> Color(0xFFFFB300)
    "Growth" -> Color(0xFF43A047)
    "Leadership" -> Color(0xFF1E88E5)
    "Perseverance" -> Color(0xFFE53935)
    "Creativity" -> Color(0xFF8E24AA)
    "Wisdom" -> Color(0xFF6D4C41)
    "Focus" -> Color(0xFF00ACC1)
    else -> Color(0xFF546E7A)
}

// ─── Custom Quote Mark drawn with Canvas ─────────────────────────
@Composable
private fun QuoteMark(
    size: Dp,
    color: Color,
    flipped: Boolean = false
) {
    Canvas(
        modifier = Modifier
            .size(size)
            .rotate(if (flipped) 180f else 0f)
    ) {
        val dotR = size.toPx() * 0.17f
        val gap = size.toPx() * 0.24f
        val baseY = size.toPx() * 0.55f
        val baseX = size.toPx() * 0.10f

        // ── Left comma ───────────────────────────────────────
        drawCircle(color, dotR, Offset(baseX + dotR, baseY))
        val lTail = Path().apply {
            moveTo(baseX + dotR * 0.3f, baseY + dotR * 0.4f)
            cubicTo(
                baseX, baseY + dotR * 1.4f,
                baseX + dotR * 0.1f, baseY + dotR * 2.2f,
                baseX + dotR * 1.0f, baseY + dotR * 2.5f
            )
            cubicTo(
                baseX + dotR * 1.6f, baseY + dotR * 2.5f,
                baseX + dotR * 1.8f, baseY + dotR * 1.8f,
                baseX + dotR * 1.2f, baseY + dotR * 1.2f
            )
            close()
        }
        drawPath(lTail, color)

        // ── Right comma ──────────────────────────────────────
        val rx = baseX + gap + dotR * 2f
        drawCircle(color, dotR, Offset(rx + dotR, baseY))
        val rTail = Path().apply {
            moveTo(rx + dotR * 0.3f, baseY + dotR * 0.4f)
            cubicTo(
                rx, baseY + dotR * 1.4f,
                rx + dotR * 0.1f, baseY + dotR * 2.2f,
                rx + dotR * 1.0f, baseY + dotR * 2.5f
            )
            cubicTo(
                rx + dotR * 1.6f, baseY + dotR * 2.5f,
                rx + dotR * 1.8f, baseY + dotR * 1.8f,
                rx + dotR * 1.2f, baseY + dotR * 1.2f
            )
            close()
        }
        drawPath(rTail, color)
    }
}


// ═══════════════════════════════════════════════════════════════════
// ═══  FACULTY SCREEN  ═════════════════════════════════════════════
// ═══════════════════════════════════════════════════════════════════
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacultyScreen(
    mainBackStack: SnapshotStateList<Any>,
) {
    val context = LocalContext.current
    val repo = remember {
        UserRepository(UserPreferences(context))
    }

    val viewModel: PreferenceSavingViewModel = viewModel(
        factory = PreferenceViewModelFactory(repo)
    )

    val userName = viewModel.userName.collectAsState().value.toString()


    val firebaseAuth = FirebaseAuth.getInstance()

    val fetchMotivationQuotesVM: UploadFetchMotivationQuotes = viewModel()

    val headEmail = viewModel.userEmail.collectAsState().value.toString()

    fetchMotivationQuotesVM.listenMotivations(headEmail)

    val fetchedMotivationQuotes = fetchMotivationQuotesVM.motivations.value


    val cs = MaterialTheme.colorScheme
    val cfg = LocalConfiguration.current
    val sw = cfg.screenWidthDp.dp
    val sh = cfg.screenHeightDp.dp
    val wc = wClass(sw)
    val hc = hClass(sh)

    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }
    val listState = rememberLazyListState()
    val ptrState = rememberPullToRefreshState()

    // ── Dimensions ───────────────────────────────────────────────
    val hPad = when (wc) {
        SClass.COMPACT -> 16.dp
        SClass.MEDIUM -> 22.dp
        SClass.EXPANDED -> 32.dp
    }
    val headerH = when (hc) {
        SClass.COMPACT -> 160.dp
        SClass.MEDIUM -> 200.dp
        SClass.EXPANDED -> 230.dp
    }
    val cardRadius = when (wc) {
        SClass.COMPACT -> 18.dp
        SClass.MEDIUM -> 22.dp
        SClass.EXPANDED -> 26.dp
    }
    val qFontSize = when (wc) {
        SClass.COMPACT -> 14.sp
        SClass.MEDIUM -> 15.sp
        SClass.EXPANDED -> 17.sp
    }
    val iconSz = when (wc) {
        SClass.COMPACT -> 20.dp
        SClass.MEDIUM -> 22.dp
        SClass.EXPANDED -> 24.dp
    }
    val avatarSz = when (wc) {
        SClass.COMPACT -> 46.dp
        SClass.MEDIUM -> 54.dp
        SClass.EXPANDED -> 62.dp
    }
    val gap = when (hc) {
        SClass.COMPACT -> 12.dp
        SClass.MEDIUM -> 16.dp
        SClass.EXPANDED -> 20.dp
    }

    // ── State ────────────────────────────────────────────────────
    var isLoading by remember { mutableStateOf(true) }
    var isRefreshing by remember { mutableStateOf(false) }
    var selTone by remember { mutableStateOf("All") }
    var showFilters by remember { mutableStateOf(true) }

    val quotes = remember { mutableStateListOf<Motivation>() }

    val tones = listOf(
        "All", "Friendly", "Encouraging", "Positive",
        "Uplifting", "Motivational"
    )

    val filtered by remember(quotes, selTone) {
        derivedStateOf {
            if (selTone == "All") quotes.toList()
            else quotes.filter { it.tone == selTone }
        }
    }
    var selectedMotivation by remember { mutableStateOf<Motivation?>(null) }


    // ── Load
    fun loadQuotes(initial: Boolean = false) {
        scope.launch {
            if (initial) isLoading = true else isRefreshing = true
            delay(900)
            val fetchedMotivationQuotes = fetchMotivationQuotesVM.motivations.value
            val sortedList = fetchedMotivationQuotes.toList().sortedBy { it.tone }

            quotes.clear()
            quotes.addAll(sortedList)
            isLoading = false
            isRefreshing = false
        }
    }

    LaunchedEffect(Unit) { loadQuotes(initial = true) }

    // ── UI ───────────────────────────────────────────────────────
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbar) },
        contentWindowInsets = WindowInsets(0)
    ) { pad ->

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                scope.launch {
                    loadQuotes()
                    snackbar.showSnackbar(
                        "Refreshed!",
                        withDismissAction = true,
                        duration = SnackbarDuration.Short
                    )
                }
            },
            state = ptrState,
            modifier = Modifier
                .fillMaxSize()
                .padding(pad)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .background(cs.background),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {

                // ── Hero Header ───────────────────────────────────
                item {
                    FacultyHeader(
                        userName = userName,
                        headerH = headerH,
                        avatarSz = avatarSz,
                        cardRadius = cardRadius,
                        totalQuotes = fetchedMotivationQuotes.size,
                        onSettings = {

                            firebaseAuth.signOut()
                            viewModel.clearData()
                            mainBackStack.add(LoginScreen)
                            mainBackStack.remove(FacultyScreen)
                        }
                    )
                }

                // ── Section Title + Filter Toggle ─────────────────
                item {
                    Spacer(Modifier.height(gap))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = hPad),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Motivation Feed",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = cs.onBackground
                                )
                            )
                            Text(
                                text = "${fetchedMotivationQuotes.size} quotes",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = cs.onSurface.copy(alpha = 0.5f)
                                )
                            )
                        }

                        Surface(
                            onClick = { showFilters = !showFilters },
                            shape = RoundedCornerShape(12.dp),
                            color = if (selTone != "All") cs.primaryContainer
                            else cs.surfaceVariant,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Rounded.FilterList,
                                    contentDescription = "Filter",
                                    tint = if (selTone != "All")
                                        cs.onPrimaryContainer
                                    else cs.onSurfaceVariant,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                // ── Filter Chips ──────────────────────────────────
                if (showFilters) {
                    item {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            contentPadding = PaddingValues(horizontal = hPad),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            itemsIndexed(tones) { _, tone ->
                                FilterChip(
                                    selected = selTone == tone,
                                    onClick = { selTone = tone },
                                    label = {
                                        Text(
                                            tone,
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                    },
                                    leadingIcon = if (selTone == tone) {
                                        {

                                        }
                                    } else null,
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = cs.primaryContainer,
                                        selectedLabelColor = cs.onPrimaryContainer,
                                        selectedLeadingIconColor = cs.onPrimaryContainer
                                    )
                                )
                            }
                        }
                    }
                }



                item { Spacer(Modifier.height(gap)) }

                // ── Loading ───────────────────────────────────────
                if (isLoading) {
                    item {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                CircularProgressIndicator(
                                    color = cs.primary,
                                    strokeWidth = 3.dp,
                                    modifier = Modifier.size(44.dp)
                                )
                                Text(
                                    "Fetching quotes…",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = cs.onSurface.copy(alpha = 0.55f)
                                    )
                                )
                            }
                        }
                    }
                }

                val filteredQuotes = if (selTone == "All") {
                    fetchedMotivationQuotes
                } else {
                    fetchedMotivationQuotes.filter { it.tone == selTone}
                }
                // ── Empty ─────────────────────────────────────────
                if (!isLoading && filtered.isEmpty()) {
                    item {
                        EmptyState(
                            tone = selTone,
                            onReset = { selTone = "All" },
                            hPad = hPad
                        )
                    }
                }

                // ── Quote Cards ───────────────────────────────────
                if (!isLoading && filtered.isNotEmpty()) {
                    items(
                        items = filteredQuotes,

                    ) { quote ->
                        Spacer(Modifier.padding(4.dp))
                        MotivationCard(
                            motivation = quote
                        )

                        selectedMotivation = quote


                    }
                }
            }
        }
    }
}

// FACULTY HEADER
@Composable
private fun FacultyHeader(
    userName: String,
    headerH: Dp,
    avatarSz: Dp,
    cardRadius: Dp,
    totalQuotes: Int,
    onSettings: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val inf = rememberInfiniteTransition(label = "hdrAnim")

    val angle by inf.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            tween(14000, easing = LinearEasing), RepeatMode.Restart
        ),
        label = "angle"
    )
    val b1 by inf.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(4200, easing = EaseInOut), RepeatMode.Reverse
        ),
        label = "b1"
    )
    val b2 by inf.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(5600, easing = EaseInOut), RepeatMode.Reverse
        ),
        label = "b2"
    )

    val pc = cs.primary
    val tc = cs.tertiary
    val sc = cs.secondary
    val opc = cs.onPrimary

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(headerH)
            .clip(
                RoundedCornerShape(
                    bottomStart = cardRadius * 1.6f,
                    bottomEnd = cardRadius * 1.6f
                )
            )
    ) {

        // ── Animated gradient bg ──────────────────────────────────
        Canvas(Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val rd = Math.toRadians(angle.toDouble())
            drawRect(
                brush = Brush.linearGradient(
                    colors = listOf(pc, tc, sc, pc),
                    start = Offset(
                        (w / 2 + w / 2 * cos(rd)).toFloat(),
                        (h / 2 + h / 2 * sin(rd)).toFloat()
                    ),
                    end = Offset(
                        (w / 2 - w / 2 * cos(rd)).toFloat(),
                        (h / 2 - h / 2 * sin(rd)).toFloat()
                    )
                )
            )
            // Wave bottom edge
            val wpath = Path().apply {
                moveTo(0f, h * 0.78f)
                for (x in 0..w.toInt() step 3) {
                    val xf = x.toFloat()
                    lineTo(
                        xf,
                        h * 0.84f + h * 0.07f * sin(
                            (xf / w * 3.5 * Math.PI + angle * 0.018).toFloat()
                        )
                    )
                }
                lineTo(w, h); lineTo(0f, h); close()
            }
            drawPath(
                wpath,
                Brush.verticalGradient(
                    listOf(opc.copy(alpha = 0.12f), Color.Transparent),
                    h * 0.78f,
                    h
                ),
                style = Fill
            )
        }

        // ── Floating orbs ─────────────────────────────────────────
        Box(
            Modifier
                .size(90.dp)
                .offset((18 + 28 * b1).dp, (10 + 18 * b2).dp)
                .alpha(0.18f)
                .blur(22.dp)
                .background(opc, CircleShape)
        )
        Box(
            Modifier
                .size(60.dp)
                .align(Alignment.TopEnd)
                .offset((-28 - 20 * b2).dp, (35 + 22 * b1).dp)
                .alpha(0.14f)
                .blur(18.dp)
                .background(tc, CircleShape)
        )

        // ── Top bar ───────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "M",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = opc,
                    modifier = Modifier.size(22.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    "Motivato",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = opc,
                        letterSpacing = 0.5.sp
                    )
                )
            }
            // TODO Have to do settings screen
            IconButton(
                onClick = onSettings,
                modifier = Modifier
                    .size(38.dp)
                    .background(opc.copy(alpha = 0.15f), CircleShape)
            ) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = opc,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // ── User info + stats ─────────────────────────────────────
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = 20.dp)
                .padding(bottom = 22.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Avatar + name row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(avatarSz)
                        .background(
                            Brush.linearGradient(
                                listOf(opc.copy(0.28f), opc.copy(0.1f))
                            ),
                            CircleShape
                        )
                ) {
                    Text(
                        userName.take(2).uppercase(),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = opc
                        )
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            userName,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = opc
                            )
                        )

                    }
                    Text(
                        "Faculty Member",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = opc.copy(alpha = 0.65f),
                            letterSpacing = 0.4.sp
                        )
                    )
                }
            }

            // Stats pills
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatPill(
                    icon = Icons.Rounded.Notes,
                    value = "$totalQuotes",
                    label = "Quotes",
                    tint = opc
                )

            }
        }
    }
}


// ═══════════════════════════════════════════════════════════════════
// ═══  STAT PILL  ══════════════════════════════════════════════════
// ═══════════════════════════════════════════════════════════════════
@Composable
private fun StatPill(
    icon: ImageVector,
    value: String,
    label: String,
    tint: Color
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = tint.copy(alpha = 0.18f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(icon, null, tint = tint, modifier = Modifier.size(14.dp))
            Text(
                "$value $label",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = tint,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}


// ═══════════════════════════════════════════════════════════════════
// ═══  EMPTY STATE  ════════════════════════════════════════════════
// ═══════════════════════════════════════════════════════════════════
@Composable
private fun EmptyState(
    tone: String,
    onReset: () -> Unit,
    hPad: Dp
) {
    val cs = MaterialTheme.colorScheme
    val inf = rememberInfiniteTransition(label = "emptyAnim")
    val fy by inf.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            tween(2200, easing = EaseInOut), RepeatMode.Reverse
        ),
        label = "fy"
    )

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = hPad)
            .padding(vertical = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Box(Modifier.offset(y = fy.dp)) {
            Box(
                Modifier
                    .size(90.dp)
                    .background(cs.surfaceVariant, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Rounded.SentimentDissatisfied,
                    null,
                    tint = cs.onSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.size(44.dp)
                )
            }
        }

        Text(
            if (tone != "All") "No \"$tone\" quotes yet"
            else "No quotes added yet",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = cs.onBackground
            )
        )

        Text(
            if (tone != "All")
                "Your team head hasn't added any quotes with this tone yet."
            else
                "Your team head hasn't added any quotes yet. Check back soon!",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = cs.onSurface.copy(alpha = 0.55f),
                textAlign = TextAlign.Center
            )
        )

        if (tone != "All") {
            Surface(
                onClick = onReset,
                shape = RoundedCornerShape(14.dp),
                color = cs.primaryContainer
            ) {
                Row(
                    Modifier.padding(horizontal = 22.dp, vertical = 13.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Rounded.Refresh,
                        null,
                        tint = cs.onPrimaryContainer,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        "Show all quotes",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = cs.onPrimaryContainer
                        )
                    )
                }
            }
        }
    }
}
