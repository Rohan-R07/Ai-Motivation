package com.example.aimotivationapp.Screens.GenerateMotivation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aimotivationapp.Screens.HomeScreens.Utils.RecentMotivation
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aimotivationapp.Datastore.PreferenceSavingViewModel
import com.example.aimotivationapp.Datastore.PreferenceViewModelFactory
import com.example.aimotivationapp.Datastore.UserPreferences
import com.example.aimotivationapp.Datastore.UserRepository
import com.example.aimotivationapp.MainNavigation.CreateMotivation
import com.example.aimotivationapp.MainNavigation.TeamHeadScreen
import com.example.aimotivationapp.UploadBackend.UploadFetchMotivationQuotes
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.log


// ─── Options ─────────────────────────────────────────────────────────────────

private val toneOptions = listOf("Motivational", "Encouraging", "Positive", "Uplifting", "Friendly")
private val purposeOptions = listOf("Welcome", "Remainder", "Appreciation", "Push", "Team Reset")
private val lengthOptions = listOf("Short", "Medium", "Long")

// ─── View State ──────────────────────────────────────────────────────────────

private enum class MotivationViewState { CREATE, GENERATED }

// ─── Root Screen ─────────────────────────────────────────────────────────────

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun CreateMotivationScreen(
    mainBackStack: SnapshotStateList<Any>
) {


    val firebaseAuth = FirebaseAuth.getInstance()
    val currentEmail = firebaseAuth.currentUser?.email.toString()


    val aiViewModel: AiViewModel = viewModel()

    var selectedTone by remember { mutableStateOf<String?>(null) }
    var selectedPurpose by remember { mutableStateOf<String?>(null) }
    var selectedLength by remember { mutableStateOf<String?>(null) }
    var contextText by remember { mutableStateOf("") }
    var viewState by remember { mutableStateOf(MotivationViewState.CREATE) }

    val parsedMotivations = aiViewModel.parsedMotivations
    val isLoading = aiViewModel.isLoading
    val isRegenerating = aiViewModel.isRegenerating
    val errorMessage = aiViewModel.errorMessage

    val uploadFetchViewModel: UploadFetchMotivationQuotes = viewModel()


    // This prevents crash during exit animation when reset() clears the list
    var stableMotivations by remember { mutableStateOf<List<RecentMotivation>>(emptyList()) }

    // ── Only update stableMotivations when list is NOT empty ─────────────────
    LaunchedEffect(parsedMotivations) {
        if (parsedMotivations.isNotEmpty()) {
            stableMotivations = parsedMotivations  // ✅ Never goes empty mid-animation
            viewState = MotivationViewState.GENERATED
        }
    }

    val view = LocalView.current
    val isGenerated = viewState == MotivationViewState.GENERATED

    SideEffect {
        val window = (view.context as? android.app.Activity)?.window ?: return@SideEffect
        if (isGenerated) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowInsetsControllerCompat(window, view).apply {
                hide(WindowInsetsCompat.Type.statusBars())
                systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            WindowCompat.setDecorFitsSystemWindows(window, true)
            WindowInsetsControllerCompat(window, view)
                .show(WindowInsetsCompat.Type.statusBars())
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(errorMessage) {
        if (errorMessage != null) {
            snackbarHostState.showSnackbar(
                message = errorMessage,
                duration = SnackbarDuration.Short
            )
            aiViewModel.clearError()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        AnimatedContent(
            targetState = viewState,
            transitionSpec = {
                if (targetState == MotivationViewState.GENERATED) {
                    (slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(520, easing = EaseOutQuart)
                    ) + fadeIn(tween(400))) togetherWith
                            (slideOutVertically(
                                targetOffsetY = { -it / 3 },
                                animationSpec = tween(400, easing = EaseInQuart)
                            ) + fadeOut(tween(300)))
                } else {
                    (slideInVertically(
                        initialOffsetY = { -it / 3 },
                        animationSpec = tween(520, easing = EaseOutQuart)
                    ) + fadeIn(tween(400))) togetherWith
                            (slideOutVertically(
                                targetOffsetY = { it },
                                animationSpec = tween(400, easing = EaseInQuart)
                            ) + fadeOut(tween(300)))
                }
            },
            label = "screenTransition"
        ) { state ->

            val prompt = """
                Generate 5 team motivation messages.
                Tone: $selectedTone
                Purpose: $selectedPurpose
                Length: $selectedLength
                Context: $contextText
                Rules:
                - Return ONLY JSON
                - Format must be: {"motivations": ["...", "..."]}
                - No explanation, no extra text
                - Each item must be unique
                - Follow tone, purpose and length strictly
            """.trimIndent()

            when (state) {
                MotivationViewState.CREATE -> {
                    CreateFormContent(
                        selectedTone = selectedTone,
                        selectedPurpose = selectedPurpose,
                        selectedLength = selectedLength,
                        contextText = contextText,
                        isLoading = isLoading,
                        onToneSelect = { selectedTone = it },
                        onPurposeSelect = { selectedPurpose = it },
                        onLengthSelect = { selectedLength = it },
                        onContextChange = { contextText = it },
                        onBack = { mainBackStack.removeLast() },
                        onGenerate = { aiViewModel.generateMotivation(prompt) }
                    )
                }

                MotivationViewState.GENERATED -> {
                    if (stableMotivations.isNotEmpty()) {
                        GeneraterMotivationSCreen(
                            motivations = stableMotivations,
                            isRegenerating = isRegenerating,
                            onBack = {
                                viewState = MotivationViewState.CREATE
                                aiViewModel.reset()
                            },
                            onSendToTeam = { motivation ->
                                // TODO FIREBASE FIRESTORE UPLOADING IS DONE RIGHT HERE

                                Log.d("CreateMotivationScreen", motivation.message)
                                Log.d("CreateMotivationScreen", motivation.tone)


                                uploadFetchViewModel.addMotivation(
                                    email = currentEmail.trim(),
                                    quote = motivation.message,
                                    tone = motivation.tone,
                                    onSucesses = {
                                        mainBackStack.add(TeamHeadScreen)
                                        mainBackStack.remove(CreateMotivation)
                                    }
                                )


                            },
                            onRegenerate = { aiViewModel.regenerateMotivation() }
                        )
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

// ─── Create Form Content ──────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateFormContent(
    selectedTone: String?,
    selectedPurpose: String?,
    selectedLength: String?,
    contextText: String,
    isLoading: Boolean,
    onToneSelect: (String) -> Unit,
    onPurposeSelect: (String) -> Unit,
    onLengthSelect: (String) -> Unit,
    onContextChange: (String) -> Unit,
    onBack: () -> Unit,
    onGenerate: () -> Unit
) {
    // Disable button while loading OR if not all options selected
    val canGenerate = selectedTone != null &&
            selectedPurpose != null &&
            selectedLength != null &&
            !isLoading

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Create Motivation",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val horizontalPadding = if (maxWidth > 600.dp) maxWidth * 0.1f else 16.dp

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = horizontalPadding)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Spacer(Modifier.height(8.dp))

                FormSectionLabel("Tone")
                FormChipGroup(
                    options = toneOptions,
                    selected = selectedTone,
                    onSelect = onToneSelect
                )

                Spacer(Modifier.height(8.dp))

                FormSectionLabel("Purpose")
                FormChipGroup(
                    options = purposeOptions,
                    selected = selectedPurpose,
                    onSelect = onPurposeSelect
                )

                Spacer(Modifier.height(8.dp))

                FormSectionLabel("Length")
                FormChipGroup(
                    options = lengthOptions,
                    selected = selectedLength,
                    onSelect = onLengthSelect
                )

                Spacer(Modifier.height(8.dp))

                FormContextInputField(
                    value = contextText,
                    onValueChange = onContextChange
                )

                Spacer(Modifier.height(16.dp))

                FormGenerateButton(
                    enabled = canGenerate,
                    isLoading = isLoading,
                    onClick = onGenerate
                )

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun GeneraterMotivationSCreen(
    motivations: List<RecentMotivation>,
    isRegenerating: Boolean = false,
    onBack: () -> Unit = {},
    onSendToTeam: (RecentMotivation) -> Unit = {},
    onRegenerate: () -> Unit = {}
) {

    if (motivations.isEmpty()) return

    var currentIndex by remember { mutableIntStateOf(0) }
    var isForward by remember { mutableStateOf(true) }
    var dragOffset by remember { mutableFloatStateOf(0f) }
    val swipeThreshold = 80f

    // ── Reset index when new motivations arrive after regenerate ──────────────
    LaunchedEffect(motivations) {
        currentIndex = 0
    }

    val currentMotivation = motivations[currentIndex]
    val gradientColors = gradientForTone(currentMotivation.toneColor)

    val animColor1 by animateColorAsState(
        gradientColors[0],
        tween(700, easing = EaseInOutCubic),
        label = "c1"
    )
    val animColor2 by animateColorAsState(
        gradientColors[1],
        tween(700, easing = EaseInOutCubic),
        label = "c2"
    )
    val animColor3 by animateColorAsState(
        gradientColors[2],
        tween(700, easing = EaseInOutCubic),
        label = "c3"
    )

    val animatedDragOffset by animateFloatAsState(
        targetValue = dragOffset,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "drag"
    )

    fun goNext() {
        if (currentIndex < motivations.lastIndex) {
            isForward = true; currentIndex++
        }; dragOffset = 0f
    }

    fun goPrev() {
        if (currentIndex > 0) {
            isForward = false; currentIndex--
        }; dragOffset = 0f
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(animColor1, animColor2, animColor3)))
            .pointerInput(currentIndex) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        when {
                            dragOffset < -swipeThreshold -> goNext()
                            dragOffset > swipeThreshold -> goPrev()
                            else -> dragOffset = 0f
                        }
                    },
                    onDragCancel = { dragOffset = 0f },
                    onHorizontalDrag = { _, amount ->
                        val new = dragOffset + amount
                        dragOffset = when {
                            new < 0 && currentIndex == motivations.lastIndex -> new * 0.2f
                            new > 0 && currentIndex == 0 -> new * 0.2f
                            else -> new.coerceIn(-300f, 300f)
                        }
                    }
                )
            }
    ) {
        QuoteDecorativeCircles(animColor1, animColor3)

        SwipeHintOverlay(
            dragOffset = animatedDragOffset,
            canGoNext = currentIndex < motivations.lastIndex,
            canGoPrev = currentIndex > 0
        )

        // ── Back button ───────────────────────────────────────────────────────
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(start = 16.dp, top = 12.dp)
                .size(44.dp)
                .shadow(12.dp, CircleShape)
                .background(Color.White.copy(alpha = 0.18f), CircleShape)
        ) {
            Icon(
                Icons.Default.ArrowBack,
                "Back",
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }

        // ── Slide quote cards ─────────────────────────────────────────────────
        val enterAnim = if (isForward)
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(500, easing = EaseOutQuart)
            ) + fadeIn(tween(400))
        else
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(500, easing = EaseOutQuart)
            ) + fadeIn(tween(400))

        val exitAnim = if (isForward)
            slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(400, easing = EaseInQuart)
            ) + fadeOut(tween(300))
        else
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(400, easing = EaseInQuart)
            ) + fadeOut(tween(300))

        // ── Quote card OR shimmer ─────────────────────────────────────────────
        AnimatedContent(
            targetState = currentIndex,
            transitionSpec = { enterAnim togetherWith exitAnim },
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 28.dp)
                .offset(y = (-20).dp),
            label = "quote"
        ) { idx ->
            // ✅ Show shimmer placeholder instead of quote while regenerating
            if (isRegenerating) {
                ShimmerQuotePlaceholder()
            } else {
                QuoteCard(motivations[idx])
            }
        }

        // ── Bottom area ───────────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(start = 24.dp, end = 24.dp, bottom = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            AnimatedVisibility(
                visible = dragOffset == 0f && !isRegenerating,
                enter = fadeIn(tween(300)),
                exit = fadeOut(tween(200))
            ) {
                Text(
                    "swipe to explore",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.50f),
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.5.sp
                )
            }

            // ✅ Show shimmer dots while regenerating
            if (isRegenerating) {
                ShimmerDotIndicators()
            } else {
                QuoteDotIndicators(total = motivations.size, currentIndex = currentIndex)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RegenerateButton(
                    isLoading = isRegenerating,
                    onClick = onRegenerate
                )
                SendToTeamButton { onSendToTeam(currentMotivation) }
            }
        }
    }
}

// ─── Form Sub-Composables ─────────────────────────────────────────────────────

@Composable
private fun FormSectionLabel(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        ),
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
private fun FormChipGroup(
    options: List<String>,
    selected: String?,
    onSelect: (String) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        options.forEach { option ->
            FormSelectableChip(
                label = option,
                isSelected = option == selected,
                onClick = { onSelect(option) }
            )
        }
    }
}

@Composable
private fun FormSelectableChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.surfaceVariant,
        animationSpec = tween(250),
        label = "chipBg"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary
        else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = tween(250),
        label = "chipFg"
    )

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = backgroundColor,
        tonalElevation = if (isSelected) 0.dp else 1.dp,
        shadowElevation = if (isSelected) 4.dp else 0.dp,
        modifier = Modifier.wrapContentSize()
    ) {
        Text(
            text = label,
            color = contentColor,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
        )
    }
}

@Composable
private fun FormContextInputField(
    value: String,
    onValueChange: (String) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 130.dp)
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    "Context For Ai (optional)",
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 130.dp),
            maxLines = 8
        )
    }
}

@Composable
private fun FormGenerateButton(
    enabled: Boolean,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (enabled && !isLoading) 1.02f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .graphicsLayer { scaleX = pulseScale; scaleY = pulseScale }
    ) {
        if (isLoading) {
            // ── Loading state ─────────────────────────────────────────────
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = "Generating...",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                letterSpacing = 0.5.sp
            )
        } else {
            // ── Idle state ────────────────────────────────────────────────
            Text(
                text = "Generate Motivation ✨",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                letterSpacing = 0.5.sp
            )
        }
    }
}

// ─── Quote Sub-Composables ────────────────────────────────────────────────────

@Composable
fun QuoteCard(motivation: RecentMotivation) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "\u201C",
            fontSize = 90.sp,
            fontWeight = FontWeight.Black,
            color = Color.White.copy(alpha = 0.20f),
            lineHeight = 50.sp,
            modifier = Modifier
                .align(Alignment.Start)
                .offset(x = (-4).dp, y = 20.dp)
        )
        Text(
            text = motivation.message,
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            lineHeight = 33.sp,
            letterSpacing = 0.2.sp
        )
        Text(
            text = "\u201D",
            fontSize = 90.sp,
            fontWeight = FontWeight.Black,
            color = Color.White.copy(alpha = 0.20f),
            lineHeight = 50.sp,
            modifier = Modifier
                .align(Alignment.End)
                .offset(x = 4.dp, y = (-20).dp)
        )
    }
}

// send to team button
@Composable
fun SendToTeamButton(onClick: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "btnScale"
    )
    Button(
        onClick = { isPressed = true; onClick(); isPressed = false },
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White.copy(alpha = 0.22f),
            contentColor = Color.White
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        contentPadding = PaddingValues(horizontal = 22.dp, vertical = 14.dp),
        modifier = Modifier
            .shadow(16.dp, RoundedCornerShape(50))
            .graphicsLayer { scaleX = scale; scaleY = scale }
    ) {
        Icon(Icons.Default.Send, null, Modifier.size(15.dp), tint = Color.White)
        Spacer(Modifier.width(8.dp))
        Text("Send to team", fontWeight = FontWeight.Bold, fontSize = 14.sp, letterSpacing = 0.3.sp)
    }
}

// Progress dot
@Composable
fun QuoteDotIndicators(total: Int, currentIndex: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(total) { i ->
            val width by animateDpAsState(
                targetValue = if (i == currentIndex) 28.dp else 7.dp,
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                label = "dot_$i"
            )
            val alpha by animateFloatAsState(
                targetValue = if (i == currentIndex) 1f else 0.38f,
                animationSpec = tween(300),
                label = "dotAlpha_$i"
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

// swipe hint overlay
@Composable
fun SwipeHintOverlay(dragOffset: Float, canGoNext: Boolean, canGoPrev: Boolean) {
    val nextAlpha =
        if (dragOffset < -20f && canGoNext) ((-dragOffset - 20f) / 100f).coerceIn(0f, 0.25f) else 0f
    val prevAlpha =
        if (dragOffset > 20f && canGoPrev) ((dragOffset - 20f) / 100f).coerceIn(0f, 0.25f) else 0f

    Box(Modifier.fillMaxSize()) {
        if (nextAlpha > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(80.dp)
                    .align(Alignment.CenterEnd)
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color.Transparent, Color.White.copy(alpha = nextAlpha))
                        )
                    )
            )
        }
        if (prevAlpha > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(80.dp)
                    .align(Alignment.CenterStart)
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color.White.copy(alpha = prevAlpha), Color.Transparent)
                        )
                    )
            )
        }
    }
}

// progress
@Composable
fun QuoteDecorativeCircles(darkColor: Color, lightColor: Color) {
    Box(Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .size(260.dp)
                .offset(x = 80.dp, y = (-80).dp)
                .align(Alignment.TopEnd)
                .background(
                    Brush.radialGradient(listOf(lightColor.copy(alpha = 0.28f), Color.Transparent)),
                    CircleShape
                )
        )
        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(x = (-60).dp, y = 50.dp)
                .align(Alignment.BottomStart)
                .background(
                    Brush.radialGradient(listOf(darkColor.copy(alpha = 0.32f), Color.Transparent)),
                    CircleShape
                )
        )
        Box(
            modifier = Modifier
                .size(340.dp)
                .align(Alignment.Center)
                .background(
                    Brush.radialGradient(
                        listOf(
                            Color.White.copy(alpha = 0.05f),
                            Color.Transparent
                        )
                    ),
                    CircleShape
                )
        )
    }
}

// ─── Gradient Helper ──────────────────────────────────────────────────────────

fun gradientForTone(toneColor: String): List<Color> = when (toneColor.lowercase()) {
    "purple" -> listOf(Color(0xFF6A5ACD), Color(0xFF9B8FE0), Color(0xFFB8A9FF))
    "blue" -> listOf(Color(0xFF1A6B8A), Color(0xFF2899C4), Color(0xFF56CCF2))
    "violet" -> listOf(Color(0xFF8B2FC9), Color(0xFFBB56E8), Color(0xFFE040FB))
    "green" -> listOf(Color(0xFF0D5C4A), Color(0xFF1B8A6B), Color(0xFF43C6AC))
    "orange" -> listOf(Color(0xFF7B3F00), Color(0xFFBF6B0A), Color(0xFFFFB347))
    "indigo" -> listOf(Color(0xFF1A237E), Color(0xFF3949AB), Color(0xFF7986CB))
    "pink" -> listOf(Color(0xFF880E4F), Color(0xFFC2185B), Color(0xFFF48FB1))
    "teal" -> listOf(Color(0xFF004D5B), Color(0xFF00838F), Color(0xFF4DD0E1))
    "red" -> listOf(Color(0xFF7F0000), Color(0xFFC62828), Color(0xFFEF9A9A))
    "gold" -> listOf(Color(0xFF5D4037), Color(0xFF8D6E63), Color(0xFFFFD54F))
    else -> listOf(Color(0xFF6A5ACD), Color(0xFF9B8FE0), Color(0xFFB8A9FF))
}

fun mapColorToTone(color: String): String {
    return when (color.lowercase()) {
        "purple" -> "Motivational"
        "blue" -> "Encouraging"
        "green" -> "Positive"
        "orange" -> "Uplifting"
        "pink" -> "Friendly"
        else -> "Motivational"
    }
}


//------------------------ Regenerate Button------------------------------------

//@Composable
//fun RegenerateButton(
//    onClick: () -> Unit,
//    isLoading: Boolean = false
//) {
//    var isPressed by remember { mutableStateOf(false) }
//
//    val scale by animateFloatAsState(
//        targetValue = if (isPressed) 0.94f else 1f,
//        animationSpec = spring(stiffness = Spring.StiffnessMedium),
//        label = "regenScale"
//    )
//
//    // Spin animation while regenerating
//    val infiniteTransition = rememberInfiniteTransition(label = "spin")
//    val rotation by infiniteTransition.animateFloat(
//        initialValue = 0f,
//        targetValue = if (isLoading) 360f else 0f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(800, easing = LinearEasing),
//            repeatMode = RepeatMode.Restart
//        ),
//        label = "rotation"
//    )
//
//    Button(
//        onClick = {
//            if (!isLoading) {
//                isPressed = true
//                onClick()
//                isPressed = false
//            }
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
//            imageVector = Icons.Default.Refresh,
//            contentDescription = "Regenerate",
//            modifier = Modifier
//                .size(15.dp)
//                .graphicsLayer { rotationZ = if (isLoading) rotation else 0f },
//            tint = Color.White
//        )
//        Spacer(Modifier.width(8.dp))
//        Text(
//            text = if (isLoading) "Regenerating..." else "Regenerate",
//            fontWeight = FontWeight.Bold,
//            fontSize = 14.sp,
//            letterSpacing = 0.3.sp
//        )
//    }
//}
@Composable
fun RegenerateButton(
    onClick: () -> Unit,
    isLoading: Boolean = false
) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "regenScale"
    )

    // ── Spin animation while regenerating ─────────────────────────────────────
    val infiniteTransition = rememberInfiniteTransition(label = "spin")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Button(
        onClick = {
            if (!isLoading) {
                isPressed = true
                onClick()
                isPressed = false
            }
        },
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White.copy(alpha = 0.22f),
            contentColor = Color.White
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        contentPadding = PaddingValues(horizontal = 22.dp, vertical = 14.dp),
        modifier = Modifier
            .shadow(16.dp, RoundedCornerShape(50))
            .graphicsLayer { scaleX = scale; scaleY = scale }
    ) {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "Regenerate",
            tint = Color.White,
            modifier = Modifier
                .size(15.dp)
                // ✅ Only spin when loading
                .graphicsLayer { rotationZ = if (isLoading) rotation else 0f }
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = if (isLoading) "Regenerating..." else "Regenerate",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            letterSpacing = 0.3.sp
        )
    }
}

