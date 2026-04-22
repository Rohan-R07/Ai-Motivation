package com.example.aimotivationapp.Screens.HomeScreens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold

import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aimotivationapp.Datastore.PreferenceSavingViewModel
import com.example.aimotivationapp.Datastore.PreferenceViewModelFactory
import com.example.aimotivationapp.Datastore.UserPreferences
import com.example.aimotivationapp.Datastore.UserRepository
import com.example.aimotivationapp.MainNavigation.CreateMotivation
import com.example.aimotivationapp.MainNavigation.LoginScreen
import com.example.aimotivationapp.MainNavigation.TeamHeadScreen
import com.example.aimotivationapp.Screens.HomeScreens.Utils.HeroHeader
import com.example.aimotivationapp.Screens.HomeScreens.Utils.MotivationCard

import com.example.aimotivationapp.UploadBackend.UploadFetchMotivationQuotes
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth


@Composable
fun TeamHeadScreen(
    mainNavigation: SnapshotStateList<Any>
) {
    val context = LocalContext.current
    val repo = remember {
        UserRepository(UserPreferences(context))
    }

    val viewModel: PreferenceSavingViewModel = viewModel(
        factory = PreferenceViewModelFactory(repo)
    )

    val fetchingViewModel: UploadFetchMotivationQuotes = viewModel()
    val userName = viewModel.userName.collectAsState().value.toString()
//    val motivation = sampleMotivations
    val firebaseAuth = FirebaseAuth.getInstance()
//    val motivationList = remember { mutableStateListOf(*motivation.toTypedArray()) }

    val colorScheme = MaterialTheme.colorScheme

    // 🔥 Responsive Setup
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp

    val horizontalPadding = when {
        screenWidth > 600 -> 48.dp   // tablet
        screenWidth > 400 -> 32.dp   // large phones
        else -> 20.dp                // small phones
    }
    val currentUserEmail = FirebaseAuth.getInstance()?.currentUser?.email.toString()
    LaunchedEffect(Unit) {
        fetchingViewModel.listenMotivations(currentUserEmail)
    }


    val fetchedMotivation = fetchingViewModel.motivations.value


    val contentMaxWidth = if (screenWidth > 600) 700.dp else Dp.Unspecified

    Scaffold(
        containerColor = colorScheme.background,
        contentWindowInsets = WindowInsets(0)
    )
    { padding ->

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

                // ── Header
                item {
                    HeroHeader(
                        userName = userName,
                        onSettings = {
                            firebaseAuth.signOut()
                            viewModel.clearData()
                            mainNavigation.add(LoginScreen)
                            mainNavigation.remove(TeamHeadScreen)
                        },
                        onCreateMotivation = {
                            mainNavigation.add(CreateMotivation)
                        }
                    )
                }

                // ── Section label
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
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelMedium,
                                color = colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }

                // ── Empty state
                if (fetchedMotivation.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = (screenWidth * 0.15f).dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
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

                // TODO ADD Firebase stuff
                items(fetchedMotivation){ motivation ->
                    Spacer(Modifier.padding(12.dp))

                    SwipeToDeleteWrapper(
                        onDelete = {

                        }
                    ) {

                        MotivationCard(motivation)
                    }
                }

            }

        }
    }
}

// ─── Hero Header ─────────────────────────────────────────────────────────────


enum class MotivationTone(val label: String, val emoji: String) {
    Friendly("Friendly", "😊"),
    Encouraging("Encouraging", "💪"),
    Inspiring("Inspiring", "🌟"),
    Bold("Bold", "🔥"),
}


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
                true
            } else false
        },
        positionalThreshold = { totalDistance -> totalDistance * 0.4f },
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,   // only swipe LEFT
        enableDismissFromEndToStart = true,
        backgroundContent = {
            // Red delete background revealed on swipe
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
                        .graphicsLayer { scaleX = iconScale; scaleY = iconScale },
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
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onErrorContainer,
                    )
                }
            }
        },
        content = { content() },
    )
}

// Small lerp helper
private fun lerp(start: Float, stop: Float, fraction: Float) =
    start + (stop - start) * fraction



