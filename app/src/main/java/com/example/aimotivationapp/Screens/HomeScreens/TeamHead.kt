package com.example.aimotivationapp.Screens.HomeScreens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aimotivationapp.Datastore.PreferenceSavingViewModel
import com.example.aimotivationapp.Datastore.PreferenceViewModelFactory
import com.example.aimotivationapp.Datastore.UserPreferences
import com.example.aimotivationapp.Datastore.UserRepository
import com.example.aimotivationapp.MainNavigation.LoginScreen
import com.example.aimotivationapp.MainNavigation.TeamHeadScreen
import com.example.aimotivationapp.MainNavigation.UserCheckingScreen
import com.example.aimotivationapp.Screens.HomeScreens.Utils.HeroHeader
import com.example.aimotivationapp.Screens.HomeScreens.Utils.MotivationCard
import com.example.aimotivationapp.Screens.HomeScreens.Utils.RecentMotivation
import com.example.aimotivationapp.Screens.HomeScreens.Utils.ToneChip
import com.example.aimotivationapp.Screens.HomeScreens.Utils.ToneColor
import com.example.aimotivationapp.Screens.HomeScreens.Utils.sampleMotivations
import com.google.firebase.auth.FirebaseAuth


@Composable
fun TeamHeadScreen(
//    userName: String = "Alex",
    mainNavigation : SnapshotStateList<Any>
//    motivations: List<RecentMotivation> = sampleMotivations,
//    onCreateMotivation: () -> Unit = {},
//    onSettings: () -> Unit = {},
) {

    val context = LocalContext.current
    val repo = remember {
        UserRepository(UserPreferences(context))
    }

    val viewModel: PreferenceSavingViewModel = viewModel(
        factory = PreferenceViewModelFactory(repo)
    )

    val userName = viewModel.userName.collectAsState().value.toString()





    val motivation = sampleMotivations

    val colorScheme = MaterialTheme.colorScheme

    val firebaseAuth = FirebaseAuth.getInstance()
    // Local mutable list so deletions update the UI instantly
    val motivationList = remember { mutableStateListOf(*motivation.toTypedArray()) }

    Scaffold(containerColor = colorScheme.background) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 48.dp),
        ) {

            // ── Hero header with gradient blob background
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
                        // TODO Generate motivation Screen
                    },
                )
            }

            // ── Section label
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp, top = 32.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        "Recent Motivations",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.5).sp,
                        ),
                        color = colorScheme.onBackground,
                    )
                    Surface(shape = CircleShape, color = colorScheme.primaryContainer) {
                        Text(
                            "${motivationList.size}",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        )
                    }
                }
            }

            // ── Hint text
            if (motivationList.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 60.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🎉", fontSize = 40.sp)
                            Spacer(Modifier.height(12.dp))
                            Text(
                                "No motivations yet.\nCreate one for your team!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }

            // ── Motivation cards  (swipe-to-delete enabled)
            items(motivationList, key = { it.id }) { motivation ->
                Spacer(Modifier.height(12.dp))
                SwipeToDeleteWrapper(
                    onDelete = { motivationList.remove(motivation) },
                ) {
                    MotivationCard(motivation)
                }
            }
        }
    }
}

// ─── Hero Header ─────────────────────────────────────────────────────────────


@Preview(showBackground = true,)
@Composable
private fun HeroHeaderPrivew() {

    Surface(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        HeroHeader(userName = "Alex", onSettings = {}, onCreateMotivation = {})
    }
}




enum class MotivationTone(val label: String, val emoji: String) {
    Friendly("Friendly", "😊"),
    Encouraging("Encouraging", "💪"),
    Inspiring("Inspiring", "🌟"),
    Bold("Bold", "🔥"),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMotivation(
    teams: List<String> = listOf(
        "Marketing Team",
        "Product Team",
        "Design Team",
        "Engineering Team",
        "QA Team"
    ),
    onBack: () -> Unit = {},
    onSend: (team: String, tone: MotivationTone, message: String) -> Unit = { _, _, _ -> },
) {
    var selectedTeam by remember { mutableStateOf("") }
    var selectedTone by remember { mutableStateOf(MotivationTone.Friendly) }
    var contextNote by remember { mutableStateOf("") }
    var generatedText by remember { mutableStateOf("") }
    var isGenerating by remember { mutableStateOf(false) }
    var teamDropdownExpanded by remember { mutableStateOf(false) }

    fun generateMotivation() {
        isGenerating = true
        generatedText = ""
        val templates = mapOf(
            MotivationTone.Friendly to listOf(
                "Hey ${selectedTeam.ifBlank { "team" }}! Your hard work and dedication never go unnoticed. Every small win counts, and you've been stacking them up beautifully. Keep being awesome! 🙌",
                "Just a quick note to say – you're doing wonderfully, ${selectedTeam.ifBlank { "team" }}. The energy and care you bring every day truly makes a difference. Thank you for being you! 😊",
            ),
            MotivationTone.Encouraging to listOf(
                "To the ${selectedTeam.ifBlank { "team" }} – challenges are just opportunities in disguise. You've tackled every obstacle thrown your way with resilience and creativity. Don't stop now! 💪",
                "${selectedTeam.ifBlank { "Team" }}, every step forward – no matter how small – is progress. Believe in the work you're doing. The results will speak for themselves. Keep pushing! 🚀",
            ),
            MotivationTone.Inspiring to listOf(
                "The ${selectedTeam.ifBlank { "team" }} doesn't just meet expectations – you exceed them. Your passion for excellence inspires everyone around you. You are the reason great things happen here. 🌟",
                "Greatness isn't a destination, it's a daily decision – and ${selectedTeam.ifBlank { "you all" }} choose it every single day. Your impact is bigger than you realise. Keep shining! ✨",
            ),
            MotivationTone.Bold to listOf(
                "${selectedTeam.ifBlank { "Team" }}, this is your moment. Own it. You've trained for this, worked for this, and earned this. Now go out there and show what you're made of. No limits! 🔥",
                "No excuses. No looking back. The ${selectedTeam.ifBlank { "team" }} is unstoppable when it's locked in. You have everything you need to win. Go take it! ⚡",
            ),
        )
        val options = templates[selectedTone] ?: listOf("You are doing great!")
        generatedText = options.random()
        isGenerating = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Create Motivation",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.3).sp,
                        ),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {

            // Step 1: Team
            StepLabel("1", "Select Team")
            ExposedDropdownMenuBox(
                expanded = teamDropdownExpanded,
                onExpandedChange = { teamDropdownExpanded = it },
            ) {
                OutlinedTextField(
                    value = selectedTeam.ifBlank { "Choose a team…" },
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(teamDropdownExpanded) },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    ),
                )
                ExposedDropdownMenu(
                    expanded = teamDropdownExpanded,
                    onDismissRequest = { teamDropdownExpanded = false }) {
                    teams.forEach { team ->
                        DropdownMenuItem(
                            text = { Text(team) },
                            onClick = { selectedTeam = team; teamDropdownExpanded = false },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Group,
                                    null,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                        )
                    }
                }
            }

            // Step 2: Tone
            StepLabel("2", "Choose Tone")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MotivationTone.entries.forEach { tone ->
                    ToneChip(
                        tone = tone,
                        selected = selectedTone == tone,
                        onClick = { selectedTone = tone },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Step 3: Context
            StepLabel("3", "Context (optional)")
            OutlinedTextField(
                value = contextNote,
                onValueChange = { contextNote = it },
                placeholder = {
                    Text(
                        "e.g. Team just hit a tough sprint deadline…",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                },
                minLines = 3,
                maxLines = 5,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                ),
            )

            // Generate button
            Button(
                onClick = { generateMotivation() },
                enabled = !isGenerating,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            ) {
                if (isGenerating) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                    Spacer(Modifier.width(10.dp))
                    Text("Generating…")
                } else {
                    Icon(Icons.Default.AutoAwesome, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Generate Motivation",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            // Result card
            AnimatedVisibility(
                visible = generatedText.isNotBlank(),
                enter = fadeIn() + expandVertically()
            ) {
                GeneratedMotivationCard(
                    text = generatedText,
                    team = selectedTeam,
                    onSend = { onSend(selectedTeam, selectedTone, generatedText) },
                    onRegenerate = { generateMotivation() },
                )
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}




@Composable
private fun StepLabel(number: String, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                number,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        Text(
            label,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}



@Composable
private fun GeneratedMotivationCard(text: String, team: String, onSend: () -> Unit, onRegenerate: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(Icons.Default.AutoAwesome, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                Text(
                    "For ${team.ifBlank { "your team" }}",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium, lineHeight = 22.sp, fontStyle = FontStyle.Italic),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            Spacer(Modifier.height(18.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(
                    onClick = onRegenerate,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onPrimaryContainer),
                ) {
                    Icon(Icons.Default.Refresh, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Retry")
                }
                Button(
                    onClick = onSend,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary),
                ) {
                    Icon(Icons.Default.Send, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Send", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                }
            }
        }
    }
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
