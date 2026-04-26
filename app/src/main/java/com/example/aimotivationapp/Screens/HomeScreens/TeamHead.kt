//package com.example.aimotivationapp.Screens.HomeScreens
//
//import android.util.Log
//import android.widget.Toast
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.core.EaseInQuart
//import androidx.compose.animation.core.EaseOutQuart
//import androidx.compose.animation.core.tween
//import androidx.compose.animation.fadeIn
//import androidx.compose.animation.fadeOut
//import androidx.compose.animation.scaleIn
//import androidx.compose.animation.scaleOut
//import androidx.compose.animation.slideInVertically
//import androidx.compose.animation.slideOutVertically
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.WindowInsets
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//
//import androidx.compose.ui.text.font.FontStyle
//import androidx.compose.ui.unit.sp
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.snapshots.SnapshotStateList
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.foundation.layout.widthIn
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.geometry.Rect
//import androidx.compose.ui.graphics.graphicsLayer
//import androidx.compose.ui.platform.LocalConfiguration
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.Dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.aimotivationapp.Datastore.PreferenceSavingViewModel
//import com.example.aimotivationapp.Datastore.PreferenceViewModelFactory
//import com.example.aimotivationapp.Datastore.UserPreferences
//import com.example.aimotivationapp.Datastore.UserRepository
//import com.example.aimotivationapp.MainNavigation.CreateMotivation
//import com.example.aimotivationapp.MainNavigation.LoginScreen
//import com.example.aimotivationapp.MainNavigation.SettingsScreen
//import com.example.aimotivationapp.MainNavigation.TeamHeadScreen
//import com.example.aimotivationapp.Screens.GenerateMotivation.GeneraterMotivationSCreen
//import com.example.aimotivationapp.Screens.GenerateMotivation.mapColorToTone
//import com.example.aimotivationapp.Screens.HomeScreens.Utils.DeleteConfirmationDialog
//import com.example.aimotivationapp.Screens.HomeScreens.Utils.HeroHeader
//import com.example.aimotivationapp.Screens.HomeScreens.Utils.MotivationCard
//import com.example.aimotivationapp.Screens.HomeScreens.Utils.MotivationDetailFullScreen
//import com.example.aimotivationapp.Screens.HomeScreens.Utils.RecentMotivation
//import com.example.aimotivationapp.Screens.HomeScreens.Utils.mapToneToEmoji
//import com.example.aimotivationapp.UploadBackend.Motivation
//
//import com.example.aimotivationapp.UploadBackend.UploadFetchMotivationQuotes
//import com.google.firebase.Firebase
//import com.google.firebase.auth.FirebaseAuth
//
//
//@Composable
//fun TeamHeadScreen(
//    mainNavigation: SnapshotStateList<Any>
//) {
//    val context = LocalContext.current
//    val repo = remember {
//        UserRepository(UserPreferences(context))
//    }
//
//    val viewModel: PreferenceSavingViewModel = viewModel(
//        factory = PreferenceViewModelFactory(repo)
//    )
//
//    val fetchingViewModel: UploadFetchMotivationQuotes = viewModel()
//    val userName = viewModel.userName.collectAsState().value.toString()
//
//
//    val colorScheme = MaterialTheme.colorScheme
//    val alertDialogState = remember { mutableStateOf(false) }
//
//
//    // 🔥 Responsive Setup
//    val config = LocalConfiguration.current
//    val screenWidth = config.screenWidthDp
//
//
//    var selectedBounds by remember { mutableStateOf<Rect?>(null) }
//    val horizontalPadding = when {
//        screenWidth > 600 -> 48.dp   // tablet
//        screenWidth > 400 -> 32.dp   // large phones
//        else -> 20.dp                // small phones
//    }
//    val currentUserEmail = FirebaseAuth.getInstance()?.currentUser?.email.toString()
//    LaunchedEffect(Unit) {
//        fetchingViewModel.listenMotivations(currentUserEmail)
//    }
//    var selectedMotivationAnimation by remember { mutableStateOf<Motivation?>(null) }
//
//    var selectedMotivation by remember { mutableStateOf<Motivation?>(null) }
//
//    val fetchedMotivation = fetchingViewModel.motivations.value
//
//
//    val contentMaxWidth = if (screenWidth > 600) 700.dp else Dp.Unspecified
//
//    Scaffold(
//        containerColor = colorScheme.background,
//        contentWindowInsets = WindowInsets(0)
//    )
//    { padding ->
//
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding),
//            contentAlignment = Alignment.TopCenter
//        ) {
//            AnimatedVisibility(
//                visible = selectedMotivationAnimation != null,
//                enter = scaleIn(
//                    initialScale = 0.7f,
//                    animationSpec = tween(400)
//                ) + fadeIn(),
//                exit = scaleOut(
//                    targetScale = 0.7f
//                ) + fadeOut()
//
//            ) {
//                selectedMotivation?.let {
//                    MotivationDetailFullScreen(
//                        motivation = it,
//                        onDismiss = {
//                            selectedMotivationAnimation = null
//                        }
//                    )
//                }
//            }
//
//            // ✅ AnimatedVisibility so it slides in beautifully
//            AnimatedVisibility(
//                visible = selectedMotivationAnimation != null,
//                enter = slideInVertically(
//                    initialOffsetY = { it },
//                    animationSpec = tween(480, easing = EaseOutQuart)
//                ) + fadeIn(tween(300)),
//                exit = slideOutVertically(
//                    targetOffsetY = { it },
//                    animationSpec = tween(380, easing = EaseInQuart)
//                ) + fadeOut(tween(250))
//            ) {
//                selectedMotivationAnimation?.let { motivation ->
//                    MotivationDetailFullScreen(
//                        motivation = motivation,
//                        onDismiss = { selectedMotivationAnimation = null }
//                    )
//                }
//            }
//            LazyColumn(
//                modifier = Modifier
//                    .widthIn(max = contentMaxWidth)
//                    .fillMaxWidth(),
//                contentPadding = PaddingValues(bottom = 48.dp),
//            ) {
//
//
//                item {
//                    HeroHeader(
//                        userName = userName,
//                        onSettings = {
//                            mainNavigation.add(SettingsScreen(true))
//                        },
//                        onCreateMotivation = {
//                            mainNavigation.add(CreateMotivation)
//                        }
//                    )
//                }
//
//                // ── Section label
//                item {
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(
//                                start = horizontalPadding,
//                                end = horizontalPadding,
//                                top = 32.dp,
//                                bottom = 4.dp
//                            ),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                    ) {
//                        Text(
//                            "Recent Motivations",
//                            style = MaterialTheme.typography.titleLarge.copy(
//                                fontWeight = FontWeight.ExtraBold
//                            ),
//                            color = colorScheme.onBackground,
//                        )
//
//                        Surface(
//                            shape = CircleShape,
//                            color = colorScheme.primaryContainer
//                        ) {
//                            Text(
//                                "${fetchedMotivation.size}",
//                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
//                                style = MaterialTheme.typography.labelMedium,
//                                color = colorScheme.onPrimaryContainer
//                            )
//                        }
//                    }
//                }
//
//                // ── Empty state
//                if (fetchedMotivation.isEmpty()) {
//                    item {
//                        Box(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(top = (screenWidth * 0.15f).dp),
//                            contentAlignment = Alignment.Center,
//                        ) {
//                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                                Text("🎉", fontSize = 40.sp)
//                                Spacer(Modifier.height(12.dp))
//                                Text(
//                                    "No motivations yet.\nCreate one for your team!",
//                                    textAlign = TextAlign.Center,
//                                    color = colorScheme.onSurfaceVariant,
//                                )
//                            }
//                        }
//                    }
//
//
//                }
//
//                // TODO ADD Firebase stuff
////                items(
////                    items = fetchedMotivation,
////                    key = { it.id }
////                ) { motivation ->
////                    Spacer(Modifier.padding(12.dp))
////                    // TODO SWIPE TO DELETE CARD
////                    SwipeToDeleteWrapper(
////                        onDelete = {
////                            selectedMotivation = motivation
////                            alertDialogState.value = true
////                        }
////                    ) {
////
////                        MotivationCard(
////                            motivation = motivation,
////                            onClick = {
////                                selectedMotivationAnimation = motivation
////                            },
////                        )
////
////                    }
////
////
////                }
//                items(
//                    items = fetchedMotivation,
//                    key = { it.id }
//                ) { motivation ->
//                    Spacer(Modifier.padding(12.dp))
//                    SwipeToDeleteWrapper(
//                        onDelete = {
//                            selectedMotivation = motivation
//                            alertDialogState.value = true
//                        }
//                    ) {
//                        MotivationCard(
//                            motivation = motivation,
//                            onClick = {
//                                selectedMotivationAnimation = motivation   // ✅ triggers full screen
//                            }
//                        )
//                    }
//                }
//
//            }
//
//        }
//        selectedMotivation?.let { items ->
//
//            DeleteConfirmationDialog(
//                isVisible = true,
//                onDismiss = { selectedMotivation = null },
//                onConfirm = {
//
//                    fetchingViewModel.deleteMotivation(
//                        currentUserEmail,
//                        docId = items.id,
//                        onSuccess = {
//                            selectedMotivation = null
//                        },
//                        onFailure = {
//                            Log.d("DeleteMotivation", it)
//                        }
//                    )
//                }
//            )
//        }
//    }
//}
//
//// ─── Hero Header ─────────────────────────────────────────────────────────────
//
//
//enum class MotivationTone(val label: String, val emoji: String) {
//    Friendly("Friendly", "😊"),
//    Encouraging("Encouraging", "💪"),
//    Inspiring("Inspiring", "🌟"),
//    Bold("Bold", "🔥"),
//}
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//private fun SwipeToDeleteWrapper(
//    onDelete: () -> Unit,
//    content: @Composable () -> Unit,
//) {
//    val dismissState = rememberSwipeToDismissBoxState(
//        confirmValueChange = { value ->
//            if (value == SwipeToDismissBoxValue.EndToStart) {
//                onDelete()
//                false
//            } else false
//        },
//        positionalThreshold = { totalDistance -> totalDistance * 0.4f },
//    )
//
//    SwipeToDismissBox(
//        state = dismissState,
//        enableDismissFromStartToEnd = false,   // only swipe LEFT
//        enableDismissFromEndToStart = true,
//        backgroundContent = {
//            // Red delete background revealed on swipe
//            val fraction = dismissState.progress
//            val bgAlpha = (fraction * 3f).coerceIn(0f, 1f)
//            val iconScale = lerp(0.7f, 1f, (fraction * 4f).coerceIn(0f, 1f))
//
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(horizontal = 20.dp)
//                    .clip(RoundedCornerShape(24.dp))
//                    .background(
//                        MaterialTheme.colorScheme.errorContainer.copy(alpha = bgAlpha)
//                    ),
//                contentAlignment = Alignment.CenterEnd,
//            ) {
//                Column(
//                    modifier = Modifier
//                        .padding(end = 28.dp)
//                        .graphicsLayer { scaleX = iconScale; scaleY = iconScale },
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                ) {
//                    Icon(
//                        Icons.Default.Delete,
//                        contentDescription = "Delete",
//                        tint = MaterialTheme.colorScheme.onErrorContainer,
//                        modifier = Modifier.size(26.dp),
//                    )
//                    Spacer(Modifier.height(4.dp))
//                    Text(
//                        "Delete",
//                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
//                        color = MaterialTheme.colorScheme.onErrorContainer,
//                    )
//                }
//            }
//        },
//        content = { content() },
//    )
//}
//
//// Small lerp helper
//private fun lerp(start: Float, stop: Float, fraction: Float) =
//    start + (stop - start) * fraction
//
//
//


package com.example.aimotivationapp.Screens.HomeScreens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInQuart
import androidx.compose.animation.core.EaseOutQuart
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aimotivationapp.Datastore.PreferenceSavingViewModel
import com.example.aimotivationapp.Datastore.PreferenceViewModelFactory
import com.example.aimotivationapp.Datastore.UserPreferences
import com.example.aimotivationapp.Datastore.UserRepository
import com.example.aimotivationapp.MainNavigation.CreateMotivation
import com.example.aimotivationapp.MainNavigation.SettingsScreen
import com.example.aimotivationapp.Screens.HomeScreens.Utils.DeleteConfirmationDialog
import com.example.aimotivationapp.Screens.HomeScreens.Utils.HeroHeader
import com.example.aimotivationapp.Screens.HomeScreens.Utils.MotivationCard
import com.example.aimotivationapp.Screens.HomeScreens.Utils.MotivationDetailFullScreen
import com.example.aimotivationapp.UploadBackend.Motivation
import com.example.aimotivationapp.UploadBackend.UploadFetchMotivationQuotes
import com.google.firebase.auth.FirebaseAuth

@Composable
fun TeamHeadScreen(
    mainNavigation: androidx.compose.runtime.snapshots.SnapshotStateList<Any>
) {
    val context = LocalContext.current
    val repo = remember { UserRepository(UserPreferences(context)) }

    val viewModel: PreferenceSavingViewModel = viewModel(
        factory = PreferenceViewModelFactory(repo)
    )
    val fetchingViewModel: UploadFetchMotivationQuotes = viewModel()

    val userName = viewModel.userName.collectAsState().value.toString()
    val colorScheme = MaterialTheme.colorScheme

    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp

    val horizontalPadding = when {
        screenWidth > 600 -> 48.dp
        screenWidth > 400 -> 32.dp
        else -> 20.dp
    }

    val currentUserEmail = FirebaseAuth.getInstance()
        ?.currentUser?.email.toString()

    LaunchedEffect(Unit) {
        fetchingViewModel.listenMotivations(currentUserEmail)
    }

    val fetchedMotivation = fetchingViewModel.motivations.value
    val contentMaxWidth = if (screenWidth > 600) 700.dp else Dp.Unspecified


    var selectedMotivationDetail by remember { mutableStateOf<Motivation?>(null) }
    var selectedMotivation by remember { mutableStateOf<Motivation?>(null) }
    val alertDialogState = remember { mutableStateOf(false) }

    // ── ✅ ROOT Box — everything stacks here ───────────────────
    Box(modifier = Modifier.fillMaxSize()) {

        // ── Layer 1: Main screen ───────────────────────────────
        Scaffold(
            containerColor = colorScheme.background,
            contentWindowInsets = WindowInsets(0)
        ) { padding ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.TopCenter
            ) {
                LazyColumn(
                    modifier = Modifier
                        .widthIn(max = contentMaxWidth)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(bottom = 48.dp),
                ) {

                    // ── Hero header ────────────────────────────
                    item {
                        HeroHeader(
                            userName = userName,
                            onSettings = {
                                mainNavigation.add(SettingsScreen(true))
                            },
                            onCreateMotivation = {
                                mainNavigation.add(CreateMotivation)
                            }
                        )
                    }

                    // ── Section label ──────────────────────────
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = horizontalPadding,
                                    end = horizontalPadding,
                                    top = 32.dp,
                                    bottom = 4.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                "Recent Motivations",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold
                                ),
                                color = colorScheme.onBackground,
                            )
                            Surface(
                                shape = CircleShape,
                                color = colorScheme.primaryContainer
                            ) {
                                Text(
                                    "${fetchedMotivation.size}",
                                    modifier = Modifier.padding(
                                        horizontal = 10.dp,
                                        vertical = 4.dp
                                    ),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }

                    // ── Empty state ────────────────────────────
                    if (fetchedMotivation.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = (screenWidth * 0.15f).dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("🎉", fontSize = 40.sp)
                                    Spacer(Modifier.height(12.dp))
                                    Text(
                                        "No motivations yet.\nCreate one for your team!",
                                        textAlign = TextAlign.Center,
                                        color = colorScheme.onSurfaceVariant,
                                    )
                                }
                            }
                        }
                    }

                    // ── Motivation cards ───────────────────────
                    items(
                        items = fetchedMotivation,
                        key = { it.id }
                    ) { motivation ->
                        Spacer(Modifier.height(12.dp))
                        SwipeToDeleteWrapper(
                            onDelete = {
                                selectedMotivation = motivation
                                alertDialogState.value = true
                            }
                        ) {
                            MotivationCard(
                                motivation = motivation,
                                onClick = {

                                    selectedMotivationDetail = motivation
                                }
                            )
                        }
                    }
                }
            }

            // ── Delete dialog ──────────────────────────────────
            selectedMotivation?.let { item ->
                DeleteConfirmationDialog(
                    isVisible = alertDialogState.value,
                    onDismiss = {
                        selectedMotivation = null
                        alertDialogState.value = false
                    },
                    onConfirm = {
                        fetchingViewModel.deleteMotivation(
                            currentUserEmail,
                            docId = item.id,
                            onSuccess = {
                                selectedMotivation = null
                                alertDialogState.value = false
                            },
                            onFailure = { error ->
                                Log.d("DeleteMotivation", error)
                            }
                        )
                    }
                )
            }
        }

        AnimatedVisibility(
            visible = selectedMotivationDetail != null,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(480, easing = EaseOutQuart)
            ) + fadeIn(tween(300)),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(380, easing = EaseInQuart)
            ) + fadeOut(tween(250)),
            modifier = Modifier.fillMaxSize()
        ) {

            // 🔥 THIS BOX BLOCKS ALL TOUCHES BELOW
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)

                    // ✅ IMPORTANT: consume ALL clicks
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { /* do nothing */ }
            ) {

                selectedMotivationDetail?.let { motivation ->

                    MotivationDetailFullScreen(
                        motivation = motivation,
                        onDismiss = {
                            selectedMotivationDetail = null
                        }
                    )
                }
            }
        }
    }
}

// ─── Swipe To Delete Wrapper ──────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeToDeleteWrapper(
    onDelete: () -> Unit,
    content: @Composable () -> Unit,
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
                false
            } else false
        },
        positionalThreshold = { totalDistance -> totalDistance * 0.4f },
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        backgroundContent = {
            val fraction = dismissState.progress
            val bgAlpha = (fraction * 3f).coerceIn(0f, 1f)
            val iconScale = lerp(0.7f, 1f, (fraction * 4f).coerceIn(0f, 1f))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        MaterialTheme.colorScheme.errorContainer.copy(alpha = bgAlpha)
                    ),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Column(
                    modifier = Modifier
                        .padding(end = 28.dp)
                        .graphicsLayer {
                            scaleX = iconScale
                            scaleY = iconScale
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.size(26.dp),
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Delete",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onErrorContainer,
                    )
                }
            }
        },
        content = { content() },
    )
}

private fun lerp(start: Float, stop: Float, fraction: Float) =
    start + (stop - start) * fraction

enum class MotivationTone(val label: String, val emoji: String) {
    Friendly("Friendly", "😊"),
    Encouraging("Encouraging", "💪"),
    Inspiring("Inspiring", "🌟"),
    Bold("Bold", "🔥"),
}