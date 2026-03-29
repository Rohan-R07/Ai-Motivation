package com.example.aimotivationapp.Screens.AuthScreens

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aimotivationapp.Datastore.PreferenceSavingViewModel
import com.example.aimotivationapp.Datastore.PreferenceViewModelFactory
import com.example.aimotivationapp.Datastore.UserPreferences
import com.example.aimotivationapp.Datastore.UserRepository
import com.example.aimotivationapp.MainNavigation.LoginScreen
import com.example.aimotivationapp.MainNavigation.TeamHeadScreen
import com.example.aimotivationapp.MainNavigation.UserCheckingScreen
import kotlinx.coroutines.delay
import kotlin.math.log


@Composable
fun UserCheckingScreen(
    mainNavBackStack: SnapshotStateList<Any>
) {
    val colorScheme = MaterialTheme.colorScheme
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val repo = remember {
        UserRepository(UserPreferences(context))
    }

    val viewModel: PreferenceSavingViewModel = viewModel(
        factory = PreferenceViewModelFactory(repo)
    )

    val theEnterValue = viewModel.userName.collectAsState().value.toString()

    val onClickButtonState = remember { mutableStateOf(false) }

    // ─── State ────────────────────────────────
    var headId by rememberSaveable { mutableStateOf("") }
    var name by rememberSaveable { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var headIdError by remember { mutableStateOf<String?>(null) }
    var nameError by remember { mutableStateOf<String?>(null) }

    // ─── Responsive values ────────────────────────────────
    val horizontalPadding = when {
        screenWidth < 360.dp -> 16.dp
        screenWidth < 600.dp -> 24.dp
        screenWidth < 840.dp -> 32.dp
        else -> 48.dp
    }

    val topSpacing = when {
        screenHeight < 600.dp -> 32.dp
        screenHeight < 800.dp -> 56.dp
        else -> 72.dp
    }

    val iconSize = when {
        screenWidth < 360.dp -> 56.dp
        else -> 64.dp
    }

    val titleFontSize = when {
        screenWidth < 360.dp -> 22.sp
        else -> 24.sp
    }

    val buttonHeight = when {
        screenWidth < 360.dp -> 48.dp
        else -> 52.dp
    }

    // ─── Validation ──────────────────────────────────────────────
    fun validate(): Boolean {
        var isValid = true
        if (headId.isBlank()) {
            headIdError = "Head ID is required"
            isValid = false
        } else {
            headIdError = null
        }
        if (name.isBlank()) {
            nameError = "Name is required"
            isValid = false
        } else {
            nameError = null
        }
        return isValid
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = horizontalPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(topSpacing))

            // ── Top Icon ──────────────────────────────────────────
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(iconSize)
                    .background(
                        color = colorScheme.primaryContainer,
                        shape = RoundedCornerShape(18.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.Rounded.VerifiedUser,
                    contentDescription = null,
                    tint = colorScheme.primary,
                    modifier = Modifier.size(iconSize * 0.47f)
                )
            }

            Spacer(modifier = Modifier.height(screenHeight * 0.025f))

            // ── Title ─────────────────────────────────────────────
            Text(
                text = "Verify Your Identity",
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = colorScheme.onBackground,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = titleFontSize
                ),
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Enter your Head ID and name\nto continue",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = colorScheme.onSurface.copy(alpha = 0.5f),
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                ),
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(screenHeight * 0.05f))

            // ── Form Card ─────────────────────────────────────────
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 600.dp),
                shape = RoundedCornerShape(20.dp),
                color = colorScheme.surfaceVariant.copy(alpha = 0.4f),
                border = BorderStroke(
                    width = 0.5.dp,
                    color = colorScheme.outline.copy(alpha = 0.2f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    // ── Head ID Field ─────────────────────────────
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Head ID",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = colorScheme.onSurface.copy(alpha = 0.6f),
                                fontWeight = FontWeight.Medium,
                                letterSpacing = 0.3.sp
                            )
                        )
                        OutlinedTextField(
                            value = headId,
                            onValueChange = {
                                headId = it
                                if (headIdError != null) headIdError = null
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text(
                                    text = "e.g. HD-00123",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = colorScheme.onSurface.copy(alpha = 0.3f)
                                    )
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.Badge,
                                    contentDescription = null,
                                    tint = if (headIdError != null)
                                        colorScheme.error
                                    else
                                        colorScheme.primary.copy(alpha = 0.7f),
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            isError = headIdError != null,
                            supportingText = if (headIdError != null) {
                                {
                                    Text(
                                        text = headIdError!!,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = colorScheme.error
                                        )
                                    )
                                }
                            } else null,
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Down) }
                            ),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = colorScheme.primary,
                                unfocusedBorderColor = colorScheme.outline.copy(alpha = 0.3f),
                                errorBorderColor = colorScheme.error,
                                focusedContainerColor = colorScheme.surface,
                                unfocusedContainerColor = colorScheme.surface,
                                errorContainerColor = colorScheme.errorContainer.copy(alpha = 0.1f)
                            )
                        )
                    }

                    // ── Name Field ────────────────────────────────
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Full Name",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = colorScheme.onSurface.copy(alpha = 0.6f),
                                fontWeight = FontWeight.Medium,
                                letterSpacing = 0.3.sp
                            )
                        )
                        OutlinedTextField(
                            value = name,
                            onValueChange = {
                                name = it
                                if (nameError != null) nameError = null
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text(
                                    text = "e.g. John Doe",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = colorScheme.onSurface.copy(alpha = 0.3f)
                                    )
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.Person,
                                    contentDescription = null,
                                    tint = if (nameError != null)
                                        colorScheme.error
                                    else
                                        colorScheme.primary.copy(alpha = 0.7f),
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            isError = nameError != null,
                            supportingText = if (nameError != null) {
                                {
                                    Text(
                                        text = nameError!!,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = colorScheme.error
                                        )
                                    )
                                }
                            } else null,
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                }
                            ),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = colorScheme.primary,
                                unfocusedBorderColor = colorScheme.outline.copy(alpha = 0.3f),
                                errorBorderColor = colorScheme.error,
                                focusedContainerColor = colorScheme.surface,
                                unfocusedContainerColor = colorScheme.surface,
                                errorContainerColor = colorScheme.errorContainer.copy(alpha = 0.1f)
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(screenHeight * 0.035f))

            // ── Submit Button ─────────────────────────────────────
            Button(
                onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    if (validate()) {
                        isLoading = true
                        onClickButtonState.value = true
                        viewModel.saveName(name)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 600.dp)
                    .height(buttonHeight),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.primary,
                    contentColor = colorScheme.onPrimary,
                    disabledContainerColor = colorScheme.surfaceVariant,
                    disabledContentColor = colorScheme.onSurface.copy(alpha = 0.3f)
                ),
                enabled = !isLoading
            ) {

                AnimatedContent(
                    targetState = isLoading,
                    transitionSpec = {
                        fadeIn(
                            tween(200)
                        ) togetherWith fadeOut(tween(200))
                    },
                    label = "buttonContent"
                ) { loading ->
                    if (loading) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            CircularProgressIndicator(
                                Modifier.size(18.dp), colorScheme.onPrimary, 2.dp
                            )
                            Text(
                                text = "Verifying...",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Submit & Continue",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                            Icon(
                                imageVector = Icons.Rounded.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }




            Log.d("added name", theEnterValue)

            Spacer(modifier = Modifier.height(16.dp))


            // ---- TEAM HEAD BUTTON ----------

            LoginAsTeamButton(onClick = {
//                mainNavBackStack.add(TeamHeadScreen)
            })

            Spacer(modifier = Modifier.height(24.dp))
        }

    }

    val isNameNull = theEnterValue.isEmpty()

    if (onClickButtonState.value) {
        LaunchedEffect(Unit) {
            delay(1000)
            mainNavBackStack.add(LoginScreen)

        }
    }

    // Firestore part
}


@Composable
fun LoginAsTeamButton(
    onClick: () -> Unit,
    isLoading: Boolean = false
) {
    val colorScheme = MaterialTheme.colorScheme
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val infiniteTransition = rememberInfiniteTransition(label = "btnAnim")

    // Shimmer sweep
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )

    // Responsive values
    val buttonHeight = when {
        screenWidth < 360.dp -> 52.dp
        else -> 56.dp
    }

    val iconSize = when {
        screenWidth < 360.dp -> 28.dp
        else -> 30.dp
    }

    val fontSize = when {
        screenWidth < 360.dp -> 14.sp
        else -> 15.sp
    }

    Button(
        onClick = onClick,
        enabled = !isLoading,
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 600.dp)
            .height(buttonHeight),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = colorScheme.onPrimary,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = colorScheme.onSurface.copy(alpha = 0.4f)
        ),
        contentPadding = PaddingValues(0.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            colorScheme.primary,
                            colorScheme.tertiary,
                            colorScheme.primary
                        ),
                        start = Offset(shimmerOffset * 400f, 0f),
                        end = Offset(shimmerOffset * 400f + 400f, 100f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            AnimatedContent(
                targetState = isLoading,
                transitionSpec = {
                    fadeIn(tween(250)) togetherWith fadeOut(tween(250))
                },
                label = "btnContent"
            ) { loading ->
                if (loading) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                        Text(
                            text = "Signing in...",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = fontSize,
                                letterSpacing = 0.3.sp
                            )
                        )
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        // Left icon
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(iconSize)
                                .background(
                                    color = colorScheme.onPrimary.copy(alpha = 0.15f),
                                    shape = CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Groups,
                                contentDescription = null,
                                tint = colorScheme.onPrimary,
                                modifier = Modifier.size(iconSize * 0.6f)
                            )
                        }

                        Text(
                            text = "Login as Team Head",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = fontSize,
                                letterSpacing = 0.3.sp,
                                color = colorScheme.onPrimary
                            )
                        )

                        // Right arrow icon
                        Icon(
                            imageVector = Icons.Rounded.ArrowForwardIos,
                            contentDescription = null,
                            tint = colorScheme.onPrimary.copy(alpha = 0.7f),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}