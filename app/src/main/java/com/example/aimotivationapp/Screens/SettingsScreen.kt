package com.example.aimotivationapp.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aimotivationapp.Datastore.PreferenceSavingViewModel
import com.example.aimotivationapp.Datastore.PreferenceViewModelFactory
import com.example.aimotivationapp.Datastore.UserPreferences
import com.example.aimotivationapp.Datastore.UserRepository
import com.example.aimotivationapp.MainNavigation.FacultyScreen
import com.example.aimotivationapp.MainNavigation.LoginScreen
import com.example.aimotivationapp.MainNavigation.SettingsScreen
import com.example.aimotivationapp.MainNavigation.TeamHeadScreen
import com.example.aimotivationapp.Screens.HomeScreens.FacultyScreen
import com.google.firebase.auth.FirebaseAuth

// ─── Main Settings Screen ─────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    mainBackStack: SnapshotStateList<Any>,
    isHead: Boolean
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    val firebaseAuth = FirebaseAuth.getInstance()
    val context = LocalContext.current

    val repo = remember { UserRepository(UserPreferences(context)) }
    val viewModel: PreferenceSavingViewModel = viewModel(
        factory = PreferenceViewModelFactory(repo)
    )

    val isHeadEmail = viewModel.userEmail.collectAsState().value

    val userName = viewModel.userName.collectAsState().value.toString()
    val userEmail = firebaseAuth.currentUser?.email ?: "No email found"

    // ── Responsive Sizing ─────────────────────────────────────
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val isCompact = screenWidth < 400.dp
    val horizontalPadding = when {
        screenWidth < 400.dp -> 12.dp
        screenWidth < 600.dp -> 16.dp
        screenWidth < 840.dp -> 32.dp
        else -> 64.dp
    }
    val cardRadius = if (isCompact) 16.dp else 22.dp
    val profileRadius = if (isCompact) 18.dp else 24.dp

    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            mainBackStack.removeLastOrNull()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = colorScheme.onSurface
                        )
                    }
                },
                title = {
                    Text(
                        text = "Settings",
                        fontWeight = FontWeight.Bold,
                        fontSize = if (isCompact) 18.sp else 22.sp,
                        color = colorScheme.onSurface
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.surface,
                    navigationIconContentColor = colorScheme.onSurface,
                    titleContentColor = colorScheme.onSurface
                )
            )
        },
        containerColor = colorScheme.surface
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = horizontalPadding)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // ── Profile Card
            ProfileCard(
                userName = userName,
                userEmail = userEmail,
                profileRadius = profileRadius,
                isCompact = isCompact
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── Account Section ───────────────────────────────
            SectionTitle(title = "Account")
            SettingsCard(cardRadius = cardRadius) {
                SettingsTile(
                    icon = Icons.Outlined.Email,
                    title = "Email",
                    subtitle = userEmail,
                    isCompact = isCompact,
                    onClick = {}
                )
            }
            SettingsDivider()
            if (!isHead) {

                SettingsCard(cardRadius = cardRadius) {
                    SettingsTile(
                        icon = Icons.Outlined.Email,
                        title = "Head Email",
                        subtitle = isHeadEmail,
                        isCompact = isCompact,
                        onClick = {}
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // ── Privacy & Support Section ─────────────────────
            SectionTitle(title = "Privacy & Support")
            SettingsCard(cardRadius = cardRadius) {
                SettingsTile(
                    icon = Icons.AutoMirrored.Filled.Help,
                    title = "Help & Support",
                    subtitle = "Get assistance and contact support",
                    isCompact = isCompact,
                    onClick = {}
                )
                SettingsDivider()
                SettingsTile(
                    icon = Icons.Outlined.Info,
                    title = "About App",
                    subtitle = "Version 1.0.0",
                    isCompact = isCompact,
                    onClick = {}
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ── Logout Button ─────────────────────────────────
            Button(
                onClick = { showLogoutDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isCompact) 50.dp else 56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.error,
                    contentColor = colorScheme.onError
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "Logout",
                    tint = colorScheme.onError,
                    modifier = Modifier.size(if (isCompact) 18.dp else 20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Logout",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = if (isCompact) 14.sp else 16.sp,
                    color = colorScheme.onError
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── App Version Footer ────────────────────────────
            Text(
                text = "App Version 1.0.0",
                fontSize = 13.sp,
                color = colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // ── Logout Confirmation Dialog ────────────────────────────
    if (showLogoutDialog) {
        LogoutConfirmationDialog(
            onDismiss = { showLogoutDialog = false },
            onConfirm = {
                showLogoutDialog = false
                viewModel.clearData()
                firebaseAuth.signOut()
                mainBackStack.add(LoginScreen)
                mainBackStack.remove(SettingsScreen)

                if (isHead){
                mainBackStack.remove(TeamHeadScreen)
                }
                else{
                    mainBackStack.remove(FacultyScreen)

                }
            }
        )
    }
}

// ─── Profile Card ─────────────────────────────────────────────
@Composable
fun ProfileCard(
    userName: String,
    userEmail: String,
    profileRadius: Dp = 24.dp,
    isCompact: Boolean = false
) {
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(profileRadius),
                ambientColor = colorScheme.primary.copy(alpha = 0.2f),
                spotColor = colorScheme.primary.copy(alpha = 0.25f)
            )
            .clip(RoundedCornerShape(profileRadius))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        colorScheme.primary,
                        colorScheme.tertiary
                    )
                )
            )
            .padding(if (isCompact) 14.dp else 20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // ── Avatar ────────────────────────────────────────
            Box(
                modifier = Modifier
                    .size(if (isCompact) 52.dp else 64.dp)
                    .clip(CircleShape)
                    .background(colorScheme.onPrimary.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Avatar",
                    tint = colorScheme.onPrimary,
                    modifier = Modifier.size(if (isCompact) 26.dp else 34.dp)
                )
            }

            Spacer(modifier = Modifier.width(if (isCompact) 10.dp else 14.dp))

            // ── Name & Email ──────────────────────────────────
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = userName,
                    fontSize = if (isCompact) 16.sp else 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = userEmail,
                    fontSize = if (isCompact) 12.sp else 14.sp,
                    color = colorScheme.onPrimary.copy(alpha = 0.85f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// ─── Section Title
@Composable
fun SectionTitle(title: String) {
    val colorScheme = MaterialTheme.colorScheme
    Text(
        text = title,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = colorScheme.primary,
        letterSpacing = 0.8.sp,
        modifier = Modifier.padding(start = 6.dp, bottom = 10.dp)
    )
}

// ─── Settings Card Container ──────────────────────────────────
@Composable
fun SettingsCard(
    cardRadius: Dp = 22.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(cardRadius),
        color = colorScheme.surface,
        shadowElevation = 2.dp,
        tonalElevation = 1.dp
    ) {
        Column(content = content)
    }
}

// ─── Settings Tile
@Composable
fun SettingsTile(
    icon: ImageVector,
    title: String,
    subtitle: String?,
    isCompact: Boolean = false,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                horizontal = if (isCompact) 12.dp else 16.dp,
                vertical = if (isCompact) 10.dp else 14.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // ── Icon Box ──────────────────────────────────────────
        Box(
            modifier = Modifier
                .size(if (isCompact) 36.dp else 42.dp)
                .clip(RoundedCornerShape(if (isCompact) 10.dp else 12.dp))
                .background(colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = colorScheme.onPrimaryContainer,
                modifier = Modifier.size(if (isCompact) 18.dp else 22.dp)
            )
        }

        Spacer(modifier = Modifier.width(if (isCompact) 10.dp else 14.dp))

        // ── Title & Subtitle
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontSize = if (isCompact) 13.sp else 15.sp,
                color = colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    fontSize = if (isCompact) 11.sp else 13.sp,
                    color = colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

    }
}


// ─── Divider ──────────────────────────────────────────────────
@Composable
fun SettingsDivider() {
    val colorScheme = MaterialTheme.colorScheme
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 16.dp),
        thickness = 1.dp,
        color = colorScheme.outlineVariant
    )
}

// ─── Logout Confirmation Dialog
@Composable
fun LogoutConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        containerColor = colorScheme.surface,
        iconContentColor = colorScheme.error,
        icon = {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(colorScheme.errorContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = null,
                    tint = colorScheme.onErrorContainer,
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        title = {
            Text(
                text = "Logout",
                fontWeight = FontWeight.Bold,
                color = colorScheme.onSurface,
                fontSize = 18.sp
            )
        },
        text = {
            Text(
                text = "Are you sure you want to logout? " +
                        "You will need to login again to access your account.",
                color = colorScheme.onSurfaceVariant,
                fontSize = 14.sp
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.error,
                    contentColor = colorScheme.onError
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Logout",
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = colorScheme.onSurfaceVariant
                )
            ) {
                Text(
                    text = "Cancel",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    )
}