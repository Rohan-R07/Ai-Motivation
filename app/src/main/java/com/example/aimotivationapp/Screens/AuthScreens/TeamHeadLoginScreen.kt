//package com.example.aimotivationapp.Screens.AuthScreens
//
//
//import android.util.Log
//import android.widget.Toast
//import androidx.compose.animation.AnimatedContent
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.core.LinearEasing
//import androidx.compose.animation.core.RepeatMode
//import androidx.compose.animation.core.animateFloat
//import androidx.compose.animation.core.infiniteRepeatable
//import androidx.compose.animation.core.rememberInfiniteTransition
//import androidx.compose.animation.core.tween
//import androidx.compose.animation.fadeIn
//import androidx.compose.animation.fadeOut
//import androidx.compose.animation.slideInVertically
//import androidx.compose.animation.slideOutVertically
//import androidx.compose.animation.togetherWith
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.imePadding
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.widthIn
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardActions
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.rounded.ArrowBack
//import androidx.compose.material.icons.rounded.AdminPanelSettings
//import androidx.compose.material.icons.rounded.ArrowForward
//import androidx.compose.material.icons.rounded.Email
//import androidx.compose.material.icons.rounded.Groups
//import androidx.compose.material.icons.rounded.Lock
//import androidx.compose.material.icons.rounded.SupervisorAccount
//import androidx.compose.material.icons.rounded.Visibility
//import androidx.compose.material.icons.rounded.VisibilityOff
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.IconButtonDefaults
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.OutlinedTextFieldDefaults
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.runtime.setValue
//import androidx.compose.runtime.snapshots.SnapshotStateList
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.focus.FocusDirection
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalConfiguration
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalFocusManager
//import androidx.compose.ui.platform.LocalSoftwareKeyboardController
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.text.input.VisualTransformation
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.aimotivationapp.FirebaseFirestore.FirestoreViewModel
//import com.example.aimotivationapp.MainNavigation.LoginScreen
//import com.example.aimotivationapp.MainNavigation.TeamHeadLoginScreen
//import com.example.aimotivationapp.MainNavigation.UserCheckingScreen
//import com.google.firebase.auth.FirebaseAuth
//import kotlin.math.log
//
//@Composable
//fun TeamHeadLoginScreen(
//    mainNavigation: SnapshotStateList<Any>
//) {
//
//    val fireStoreViewModel: FirestoreViewModel = viewModel()
//
//
//    val firebaseAuth = FirebaseAuth.getInstance()
//
//    val colorScheme = MaterialTheme.colorScheme
//    val keyboardController = LocalSoftwareKeyboardController.current
//    val focusManager = LocalFocusManager.current
//    val configuration = LocalConfiguration.current
//    val screenWidth = configuration.screenWidthDp.dp
//    val screenHeight = configuration.screenHeightDp.dp
//
//    // ─── State ────────────────────────────────
//    var email by rememberSaveable { mutableStateOf("") }
//    var password by rememberSaveable { mutableStateOf("") }
//    var passwordVisible by rememberSaveable { mutableStateOf(false) }
//    var isLoading by remember { mutableStateOf(false) }
//    var emailError by remember { mutableStateOf<String?>(null) }
//    var passwordError by remember { mutableStateOf<String?>(null) }
//
//    // ─── Responsive values ────────────────────────────────
//    val horizontalPadding = when {
//        screenWidth < 360.dp -> 16.dp
//        screenWidth < 600.dp -> 24.dp
//        screenWidth < 840.dp -> 32.dp
//        else -> 48.dp
//    }
//
//    val topSpacing = when {
//        screenHeight < 600.dp -> 24.dp
//        screenHeight < 800.dp -> 40.dp
//        else -> 56.dp
//    }
//
//    val iconContainerSize = when {
//        screenWidth < 360.dp -> 72.dp
//        else -> 80.dp
//    }
//
//    val titleFontSize = when {
//        screenWidth < 360.dp -> 26.sp
//        else -> 28.sp
//    }
//
//    val buttonHeight = when {
//        screenWidth < 360.dp -> 50.dp
//        else -> 54.dp
//    }
//
//    // ─── Validation ──────────────────────────────────────────────
//    fun validate(): Boolean {
//        var isValid = true
//
//        if (email.isBlank()) {
//            emailError = "Email is required"
//            isValid = false
//        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            emailError = "Invalid email format"
//            isValid = false
//        } else {
//            emailError = null
//        }
//
//        if (password.isBlank()) {
//            passwordError = "Password is required"
//            isValid = false
//        } else if (password.length < 6) {
//            passwordError = "Password must be at least 6 characters"
//            isValid = false
//        } else {
//            passwordError = null
//        }
//
//        return isValid
//    }
//
//    val context = LocalContext.current
//    // ─── Handle Login ────────────────────────────────────────────
//    fun handleLogin() {
//        keyboardController?.hide()
//        focusManager.clearFocus()
//
//        if (validate()) {
//            isLoading = true
//
//            firebaseAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        val cleanedEmail = FirebaseAuth.getInstance().currentUser?.email
//                            ?.trim()
//                            ?.lowercase()
//                        fireStoreViewModel.checkUserRole(
//                            email = cleanedEmail!!,
//                            onSuccess = {
//                                Log.d("TeamHeadLoginScreen", "Success full appi")
//                                isLoading = false
//                            },
//                            onFailure = {
//                                Log.d("TeamHeadLoginScreen", "failed appi")
//                                isLoading = false
//                                Toast.makeText(context,"Failed to Check form firestore teamhead not found",Toast.LENGTH_SHORT).show()
//                                firebaseAuth.signOut()
//                            }
//                        )
//                    }else{
//                        isLoading = false
//                        Log.d("TeamHeadLoginScreen", task.exception.toString())
//
//                        Toast.makeText(context,"Failed to authinticate",Toast.LENGTH_SHORT).show()
//
//
//                    }
//                }
//        }
//
////        if (validate()) {
////
////            isLoading = true
////
////
////        }
//    }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(colorScheme.background)
//            .imePadding()
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .verticalScroll(rememberScrollState())
//                .padding(horizontal = horizontalPadding),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//
//            Spacer(modifier = Modifier.height(topSpacing))
//
//            // ── Back Button ──────────────────────────────────────
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.Start
//            ) {
//                IconButton(
//                    onClick = {
//                        mainNavigation.removeLast()
////                        mainNavigation.remove(TeamHeadLoginScreen)
//                    },
//                    modifier = Modifier
//                        .size(40.dp)
//                        .clip(CircleShape)
//                        .background(colorScheme.surfaceVariant.copy(alpha = 0.5f))
//                ) {
//                    Icon(
//                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
//                        contentDescription = "Back",
//                        tint = colorScheme.onSurface,
//                        modifier = Modifier.size(20.dp)
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(screenHeight * 0.02f))
//
//            // ── Animated Header Icon ──────────────────────────────
//            AnimatedGradientIcon(
//                size = iconContainerSize,
//                colorScheme = colorScheme
//            )
//
//            Spacer(modifier = Modifier.height(screenHeight * 0.025f))
//
//            // ── Title ─────────────────────────────────────────────
//            Text(
//                text = "Team Head Login",
//                style = MaterialTheme.typography.headlineMedium.copy(
//                    color = colorScheme.onBackground,
//                    fontWeight = FontWeight.Bold,
//                    fontSize = titleFontSize
//                ),
//                modifier = Modifier.padding(horizontal = 8.dp)
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Text(
//                text = "Access your team management dashboard",
//                style = MaterialTheme.typography.bodyMedium.copy(
//                    color = colorScheme.onSurface.copy(alpha = 0.6f),
//                    fontWeight = FontWeight.Normal,
//                    textAlign = TextAlign.Center,
//                    lineHeight = 20.sp
//                ),
//                modifier = Modifier.padding(horizontal = 8.dp)
//            )
//
//            Spacer(modifier = Modifier.height(screenHeight * 0.04f))
//
//            // ── Login Form Card ───────────────────────────────────
//            Surface(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .widthIn(max = 600.dp),
//                shape = RoundedCornerShape(24.dp),
//                color = colorScheme.surfaceVariant.copy(alpha = 0.4f),
//                border = BorderStroke(
//                    width = 0.5.dp,
//                    color = colorScheme.outline.copy(alpha = 0.2f)
//                )
//            ) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(24.dp),
//                    verticalArrangement = Arrangement.spacedBy(20.dp)
//                ) {
//
//                    // ── Email Field ───────────────────────────────
//                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
//                        Text(
//                            text = "Email Address",
//                            style = MaterialTheme.typography.labelMedium.copy(
//                                color = colorScheme.onSurface.copy(alpha = 0.7f),
//                                fontWeight = FontWeight.SemiBold,
//                                letterSpacing = 0.4.sp
//                            )
//                        )
//                        OutlinedTextField(
//                            value = email,
//                            onValueChange = {
//                                email = it
//                                if (emailError != null) emailError = null
//                            },
//                            modifier = Modifier.fillMaxWidth(),
//                            placeholder = {
//                                Text(
//                                    text = "teamhead@example.com",
//                                    style = MaterialTheme.typography.bodyMedium.copy(
//                                        color = colorScheme.onSurface.copy(alpha = 0.3f)
//                                    )
//                                )
//                            },
//                            leadingIcon = {
//                                Icon(
//                                    imageVector = Icons.Rounded.Email,
//                                    contentDescription = null,
//                                    tint = if (emailError != null)
//                                        colorScheme.error
//                                    else
//                                        colorScheme.primary.copy(alpha = 0.7f),
//                                    modifier = Modifier.size(22.dp)
//                                )
//                            },
//                            isError = emailError != null,
//                            supportingText = if (emailError != null) {
//                                {
//                                    Row(
//                                        verticalAlignment = Alignment.CenterVertically,
//                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
//                                    ) {
//                                        Text(
//                                            text = emailError!!,
//                                            style = MaterialTheme.typography.labelSmall.copy(
//                                                color = colorScheme.error,
//                                                fontWeight = FontWeight.Medium
//                                            )
//                                        )
//                                    }
//                                }
//                            } else null,
//                            singleLine = true,
//                            keyboardOptions = KeyboardOptions(
//                                keyboardType = KeyboardType.Email,
//                                imeAction = ImeAction.Next
//                            ),
//                            keyboardActions = KeyboardActions(
//                                onNext = { focusManager.moveFocus(FocusDirection.Down) }
//                            ),
//                            shape = RoundedCornerShape(16.dp),
//                            colors = OutlinedTextFieldDefaults.colors(
//                                focusedBorderColor = colorScheme.primary,
//                                unfocusedBorderColor = colorScheme.outline.copy(alpha = 0.3f),
//                                errorBorderColor = colorScheme.error,
//                                focusedContainerColor = colorScheme.surface,
//                                unfocusedContainerColor = colorScheme.surface,
//                                errorContainerColor = colorScheme.errorContainer.copy(alpha = 0.1f)
//                            )
//                        )
//                    }
//
//                    // ── Password Field ────────────────────────────
//                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
//                        Text(
//                            text = "Password",
//                            style = MaterialTheme.typography.labelMedium.copy(
//                                color = colorScheme.onSurface.copy(alpha = 0.7f),
//                                fontWeight = FontWeight.SemiBold,
//                                letterSpacing = 0.4.sp
//                            )
//                        )
//                        OutlinedTextField(
//                            value = password,
//                            onValueChange = {
//                                password = it
//                                if (passwordError != null) passwordError = null
//                            },
//                            modifier = Modifier.fillMaxWidth(),
//                            placeholder = {
//                                Text(
//                                    text = "Enter your password",
//                                    style = MaterialTheme.typography.bodyMedium.copy(
//                                        color = colorScheme.onSurface.copy(alpha = 0.3f)
//                                    )
//                                )
//                            },
//                            leadingIcon = {
//                                Icon(
//                                    imageVector = Icons.Rounded.Lock,
//                                    contentDescription = null,
//                                    tint = if (passwordError != null)
//                                        colorScheme.error
//                                    else
//                                        colorScheme.primary.copy(alpha = 0.7f),
//                                    modifier = Modifier.size(22.dp)
//                                )
//                            },
//                            trailingIcon = {
//                                IconButton(
//                                    onClick = { passwordVisible = !passwordVisible }
//                                ) {
//                                    Icon(
//                                        imageVector = if (passwordVisible)
//                                            Icons.Rounded.Visibility
//                                        else
//                                            Icons.Rounded.VisibilityOff,
//                                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
//                                        tint = colorScheme.onSurface.copy(alpha = 0.5f),
//                                        modifier = Modifier.size(20.dp)
//                                    )
//                                }
//                            },
//                            visualTransformation = if (passwordVisible)
//                                VisualTransformation.None
//                            else
//                                PasswordVisualTransformation(),
//                            isError = passwordError != null,
//                            supportingText = if (passwordError != null) {
//                                {
//                                    Row(
//                                        verticalAlignment = Alignment.CenterVertically,
//                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
//                                    ) {
//                                        Text(
//                                            text = passwordError!!,
//                                            style = MaterialTheme.typography.labelSmall.copy(
//                                                color = colorScheme.error,
//                                                fontWeight = FontWeight.Medium
//                                            )
//                                        )
//                                    }
//                                }
//                            } else null,
//                            singleLine = true,
//                            keyboardOptions = KeyboardOptions(
//                                keyboardType = KeyboardType.Password,
//                                imeAction = ImeAction.Done
//                            ),
//                            keyboardActions = KeyboardActions(
//                                onDone = { handleLogin() }
//                            ),
//                            shape = RoundedCornerShape(16.dp),
//                            colors = OutlinedTextFieldDefaults.colors(
//                                focusedBorderColor = colorScheme.primary,
//                                unfocusedBorderColor = colorScheme.outline.copy(alpha = 0.3f),
//                                errorBorderColor = colorScheme.error,
//                                focusedContainerColor = colorScheme.surface,
//                                unfocusedContainerColor = colorScheme.surface,
//                                errorContainerColor = colorScheme.errorContainer.copy(alpha = 0.1f)
//                            )
//                        )
//                    }
//
//                    // ── Forgot Password ───────────────────────────
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.End
//                    ) {
//                        TextButton(
//                            onClick = {
//                                // TODO forgot password
//                            },
//                            enabled = !isLoading
//                        ) {
//                            Text(
//                                text = "Forgot Password?",
//                                style = MaterialTheme.typography.labelMedium.copy(
//                                    color = colorScheme.primary,
//                                    fontWeight = FontWeight.SemiBold
//                                )
//                            )
//                        }
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(screenHeight * 0.03f))
//
//            // ── Login Button ──────────────────────────────────────
//            Button(
//                onClick = { handleLogin() },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .widthIn(max = 600.dp)
//                    .height(buttonHeight),
//                shape = RoundedCornerShape(16.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = colorScheme.primary,
//                    contentColor = colorScheme.onPrimary,
//                    disabledContainerColor = colorScheme.surfaceVariant,
//                    disabledContentColor = colorScheme.onSurface.copy(alpha = 0.3f)
//                ),
//                enabled = !isLoading
//            ) {
//                AnimatedContent(
//                    targetState = isLoading,
//                    transitionSpec = {
//                        fadeIn(tween(200)) togetherWith fadeOut(tween(200))
//                    },
//                    label = "loginButtonContent"
//                ) { loading ->
//                    if (loading) {
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.spacedBy(12.dp)
//                        ) {
//                            CircularProgressIndicator(
//                                modifier = Modifier.size(20.dp),
//                                color = colorScheme.onPrimary,
//                                strokeWidth = 2.5.dp
//                            )
//                            Text(
//                                text = "Signing in...",
//                                style = MaterialTheme.typography.labelLarge.copy(
//                                    fontWeight = FontWeight.Bold,
//                                    fontSize = 16.sp
//                                )
//                            )
//                        }
//                    } else {
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.spacedBy(10.dp)
//                        ) {
//                            Text(
//                                text = "Sign In as Team Head",
//                                style = MaterialTheme.typography.labelLarge.copy(
//                                    fontWeight = FontWeight.Bold,
//                                    fontSize = 16.sp
//                                )
//                            )
//                            Icon(
//                                imageVector = Icons.Rounded.ArrowForward,
//                                contentDescription = null,
//                                modifier = Modifier.size(20.dp)
//                            )
//                        }
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Spacer(modifier = Modifier.height(32.dp))
//        }
//    }
//}
//
//
//// ─── Animated Gradient Icon Component ────────────────────────────
//@Composable
//fun AnimatedGradientIcon(
//    size: Dp,
//    colorScheme: androidx.compose.material3.ColorScheme
//) {
//    val infiniteTransition = rememberInfiniteTransition(label = "iconAnim")
//
//    val shimmerOffset by infiniteTransition.animateFloat(
//        initialValue = -1f,
//        targetValue = 2f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(3000, easing = LinearEasing),
//            repeatMode = RepeatMode.Restart
//        ),
//        label = "iconShimmer"
//    )
//
//    Box(
//        contentAlignment = Alignment.Center,
//        modifier = Modifier
//            .size(size)
//            .background(
//                brush = Brush.linearGradient(
//                    colors = listOf(
//                        colorScheme.primary,
//                        colorScheme.tertiary,
//                        colorScheme.primary
//                    ),
//                    start = Offset(shimmerOffset * 400f, 0f),
//                    end = Offset(shimmerOffset * 400f + 400f, 400f)
//                ),
//                shape = RoundedCornerShape(22.dp)
//            )
//    ) {
//        Icon(
//            imageVector = Icons.Rounded.SupervisorAccount,
//            contentDescription = null,
//            tint = colorScheme.onPrimary,
//            modifier = Modifier.size(size * 0.5f)
//        )
//    }
//}


package com.example.aimotivationapp.Screens.AuthScreens

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.SupervisorAccount
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aimotivationapp.Datastore.PreferenceSavingViewModel
import com.example.aimotivationapp.Datastore.PreferenceViewModelFactory
import com.example.aimotivationapp.Datastore.UserPreferences
import com.example.aimotivationapp.Datastore.UserRepository
import com.example.aimotivationapp.FirebaseFirestore.FirestoreViewModel
import com.example.aimotivationapp.MainNavigation.TeamHeadLoginScreen
import com.example.aimotivationapp.MainNavigation.TeamHeadScreen
import com.google.firebase.auth.FirebaseAuth

// ─── Screen Size Classification ──────────────────────────────────
private enum class ScreenSizeClass { COMPACT, MEDIUM, EXPANDED }

private fun classifyWidth(widthDp: Dp): ScreenSizeClass = when {
    widthDp < 360.dp -> ScreenSizeClass.COMPACT
    widthDp < 600.dp -> ScreenSizeClass.MEDIUM
    else -> ScreenSizeClass.EXPANDED
}

private fun classifyHeight(heightDp: Dp): ScreenSizeClass = when {
    heightDp < 600.dp -> ScreenSizeClass.COMPACT
    heightDp < 800.dp -> ScreenSizeClass.MEDIUM
    else -> ScreenSizeClass.EXPANDED
}

@Composable
fun TeamHeadLoginScreen(
    mainNavigation: SnapshotStateList<Any>
) {
    // Firebase
    val fireStoreViewModel: FirestoreViewModel = viewModel()
    val firebaseAuth = FirebaseAuth.getInstance()
    val context = LocalContext.current

    // Preference dataStore
    val repo = remember {
        UserRepository(UserPreferences(context))
    }

    val viewModel: PreferenceSavingViewModel = viewModel(
        factory = PreferenceViewModelFactory(repo)
    )


    val colorScheme = MaterialTheme.colorScheme
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val widthClass = classifyWidth(screenWidth)
    val heightClass = classifyHeight(screenHeight)

    // ─── State ────────────────────────────────────────────────────
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    // ─── Responsive Dimensions ────────────────────────────────────
    val horizontalPadding = when (widthClass) {
        ScreenSizeClass.COMPACT -> 16.dp
        ScreenSizeClass.MEDIUM -> 24.dp
        ScreenSizeClass.EXPANDED -> 48.dp
    }

    val topSpacing = when (heightClass) {
        ScreenSizeClass.COMPACT -> 12.dp
        ScreenSizeClass.MEDIUM -> 28.dp
        ScreenSizeClass.EXPANDED -> 48.dp
    }

    val iconContainerSize = when (widthClass) {
        ScreenSizeClass.COMPACT -> 60.dp
        ScreenSizeClass.MEDIUM -> 76.dp
        ScreenSizeClass.EXPANDED -> 88.dp
    }

    val titleFontSize = when (widthClass) {
        ScreenSizeClass.COMPACT -> 22.sp
        ScreenSizeClass.MEDIUM -> 28.sp
        ScreenSizeClass.EXPANDED -> 32.sp
    }

    val subtitleFontSize = when (widthClass) {
        ScreenSizeClass.COMPACT -> 13.sp
        ScreenSizeClass.MEDIUM -> 14.sp
        ScreenSizeClass.EXPANDED -> 16.sp
    }

    val buttonHeight = when (widthClass) {
        ScreenSizeClass.COMPACT -> 48.dp
        ScreenSizeClass.MEDIUM -> 54.dp
        ScreenSizeClass.EXPANDED -> 58.dp
    }

    val buttonFontSize = when (widthClass) {
        ScreenSizeClass.COMPACT -> 14.sp
        ScreenSizeClass.MEDIUM -> 16.sp
        ScreenSizeClass.EXPANDED -> 17.sp
    }

    val cardPadding = when (widthClass) {
        ScreenSizeClass.COMPACT -> 16.dp
        ScreenSizeClass.MEDIUM -> 24.dp
        ScreenSizeClass.EXPANDED -> 32.dp
    }

    val cardCornerRadius = when (widthClass) {
        ScreenSizeClass.COMPACT -> 20.dp
        ScreenSizeClass.MEDIUM -> 24.dp
        ScreenSizeClass.EXPANDED -> 28.dp
    }

    val fieldCornerRadius = when (widthClass) {
        ScreenSizeClass.COMPACT -> 12.dp
        ScreenSizeClass.MEDIUM -> 16.dp
        ScreenSizeClass.EXPANDED -> 18.dp
    }

    val fieldSpacing = when (heightClass) {
        ScreenSizeClass.COMPACT -> 12.dp
        ScreenSizeClass.MEDIUM -> 18.dp
        ScreenSizeClass.EXPANDED -> 22.dp
    }

    val labelFieldGap = when (heightClass) {
        ScreenSizeClass.COMPACT -> 4.dp
        ScreenSizeClass.MEDIUM -> 8.dp
        ScreenSizeClass.EXPANDED -> 10.dp
    }

    val sectionGap = when (heightClass) {
        ScreenSizeClass.COMPACT -> 0.015f
        ScreenSizeClass.MEDIUM -> 0.025f
        ScreenSizeClass.EXPANDED -> 0.035f
    }

    val iconSize = when (widthClass) {
        ScreenSizeClass.COMPACT -> 20.dp
        ScreenSizeClass.MEDIUM -> 22.dp
        ScreenSizeClass.EXPANDED -> 24.dp
    }

    val backButtonSize = when (widthClass) {
        ScreenSizeClass.COMPACT -> 36.dp
        ScreenSizeClass.MEDIUM -> 40.dp
        ScreenSizeClass.EXPANDED -> 44.dp
    }

    val maxFormWidth = when (widthClass) {
        ScreenSizeClass.COMPACT -> 480.dp
        ScreenSizeClass.MEDIUM -> 520.dp
        ScreenSizeClass.EXPANDED -> 600.dp
    }

    val labelFontSize = when (widthClass) {
        ScreenSizeClass.COMPACT -> 11.sp
        ScreenSizeClass.MEDIUM -> 12.sp
        ScreenSizeClass.EXPANDED -> 13.sp
    }

    val placeholderFontSize = when (widthClass) {
        ScreenSizeClass.COMPACT -> 13.sp
        ScreenSizeClass.MEDIUM -> 14.sp
        ScreenSizeClass.EXPANDED -> 15.sp
    }

    // ─── Validation ──────────────────────────────────────────────
    fun validate(): Boolean {
        var isValid = true

        if (name.isBlank()) {
            nameError = "Name is required"
            isValid = false
        } else if (name.trim().length < 2) {
            nameError = "Name must be at least 2 characters"
            isValid = false
        } else {
            nameError = null
        }

        if (email.isBlank()) {
            emailError = "Email is required"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Invalid email format"
            isValid = false
        } else {
            emailError = null
        }

        if (password.isBlank()) {
            passwordError = "Password is required"
            isValid = false
        } else if (password.length < 6) {
            passwordError = "Password must be at least 6 characters"
            isValid = false
        } else {
            passwordError = null
        }

        return isValid
    }

    // ─── Handle Login ───────────────────────────────────────────

    fun handleLogin() {
        keyboardController?.hide()
        focusManager.clearFocus()

        if (validate()) {
            isLoading = true

            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { task ->


                    val user = FirebaseAuth.getInstance().currentUser

                    user?.getIdToken(true)?.addOnSuccessListener {
                        val cleanedEmail = FirebaseAuth.getInstance().currentUser?.email
                            ?.trim()
                            ?.lowercase()

                        fireStoreViewModel.checkUserRoleforHeadLoginScreen(
                            email = cleanedEmail!!,
                            onSuccess = {
                                Log.d("TeamHeadLoginScreen", "Success full appi")
                                isLoading = false
                                viewModel.saveName(name)
                                viewModel.saveEmail("HELLO")
                                mainNavigation.add(TeamHeadScreen)
                                mainNavigation.remove(TeamHeadLoginScreen)

                            },
                            onFailure = {
                                Log.d("TeamHeadLoginScreen", "failed appi")
                                isLoading = false
                                Toast.makeText(
                                    context,
                                    "User is not a Team head",
                                    Toast.LENGTH_SHORT
                                ).show()
                                firebaseAuth.signOut()
                            }
                        )


                    }


                }
                .addOnFailureListener { task ->

                    isLoading = false
                    Log.d("TeamHeadLoginScreen", task.message.toString())
                    Toast.makeText(
                        context,
                        "Failed to authenticate please try again later",
                        Toast.LENGTH_LONG
                    ).show()
                }

        }
    }

    // ─── UI ──────────────────────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
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

            // ── Back Button ──────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = maxFormWidth),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = { mainNavigation.removeLast() },
                    modifier = Modifier
                        .size(backButtonSize)
                        .clip(CircleShape)
                        .background(colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = colorScheme.onSurface,
                        modifier = Modifier.size(backButtonSize * 0.5f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(screenHeight * sectionGap))

            // ── Animated Header Icon ─────────────────────────────
            AnimatedGradientIcon(
                size = iconContainerSize,
                colorScheme = colorScheme
            )

            Spacer(modifier = Modifier.height(screenHeight * sectionGap))

            // ── Title ────────────────────────────────────────────
            Text(
                text = "Team Head Login",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = titleFontSize
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .widthIn(max = maxFormWidth)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Access your team management dashboard",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = colorScheme.onSurface.copy(alpha = 0.6f),
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    fontSize = subtitleFontSize,
                    lineHeight = subtitleFontSize * 1.4f
                ),
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .widthIn(max = maxFormWidth)
            )

            Spacer(modifier = Modifier.height(screenHeight * sectionGap * 1.2f))

            // ── Login Form Card ──────────────────────────────────
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = maxFormWidth),
                shape = RoundedCornerShape(cardCornerRadius),
                color = colorScheme.surfaceVariant.copy(alpha = 0.4f),
                border = BorderStroke(
                    width = 0.5.dp,
                    color = colorScheme.outline.copy(alpha = 0.2f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(cardPadding),
                    verticalArrangement = Arrangement.spacedBy(fieldSpacing)
                ) {

                    // ── Name Field ───────────────────────────────
                    Column(verticalArrangement = Arrangement.spacedBy(labelFieldGap)) {
                        Text(
                            text = "Full Name",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = colorScheme.onSurface.copy(alpha = 0.7f),
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 0.4.sp,
                                fontSize = labelFontSize
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
                                    text = "Enter your full name",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = colorScheme.onSurface.copy(alpha = 0.3f),
                                        fontSize = placeholderFontSize
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
                                    modifier = Modifier.size(iconSize)
                                )
                            },
                            isError = nameError != null,
                            supportingText = if (nameError != null) {
                                {
                                    Text(
                                        text = nameError!!,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = colorScheme.error,
                                            fontWeight = FontWeight.Medium
                                        )
                                    )
                                }
                            } else null,
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next,
                                capitalization = KeyboardCapitalization.Words
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Down) }
                            ),
                            shape = RoundedCornerShape(fieldCornerRadius),
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

                    // ── Email Field ──────────────────────────────
                    Column(verticalArrangement = Arrangement.spacedBy(labelFieldGap)) {
                        Text(
                            text = "Email Address",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = colorScheme.onSurface.copy(alpha = 0.7f),
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 0.4.sp,
                                fontSize = labelFontSize
                            )
                        )
                        OutlinedTextField(
                            value = email,
                            onValueChange = {
                                email = it
                                if (emailError != null) emailError = null
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text(
                                    text = "teamhead@example.com",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = colorScheme.onSurface.copy(alpha = 0.3f),
                                        fontSize = placeholderFontSize
                                    )
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.Email,
                                    contentDescription = null,
                                    tint = if (emailError != null)
                                        colorScheme.error
                                    else
                                        colorScheme.primary.copy(alpha = 0.7f),
                                    modifier = Modifier.size(iconSize)
                                )
                            },
                            isError = emailError != null,
                            supportingText = if (emailError != null) {
                                {
                                    Text(
                                        text = emailError!!,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = colorScheme.error,
                                            fontWeight = FontWeight.Medium
                                        )
                                    )
                                }
                            } else null,
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Down) }
                            ),
                            shape = RoundedCornerShape(fieldCornerRadius),
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

                    // ── Password Field ───────────────────────────
                    Column(verticalArrangement = Arrangement.spacedBy(labelFieldGap)) {
                        Text(
                            text = "Password",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = colorScheme.onSurface.copy(alpha = 0.7f),
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 0.4.sp,
                                fontSize = labelFontSize
                            )
                        )
                        OutlinedTextField(
                            value = password,
                            onValueChange = {
                                password = it
                                if (passwordError != null) passwordError = null
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text(
                                    text = "Enter your password",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = colorScheme.onSurface.copy(alpha = 0.3f),
                                        fontSize = placeholderFontSize
                                    )
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.Lock,
                                    contentDescription = null,
                                    tint = if (passwordError != null)
                                        colorScheme.error
                                    else
                                        colorScheme.primary.copy(alpha = 0.7f),
                                    modifier = Modifier.size(iconSize)
                                )
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = { passwordVisible = !passwordVisible }
                                ) {
                                    Icon(
                                        imageVector = if (passwordVisible)
                                            Icons.Rounded.Visibility
                                        else
                                            Icons.Rounded.VisibilityOff,
                                        contentDescription = if (passwordVisible)
                                            "Hide password"
                                        else
                                            "Show password",
                                        tint = colorScheme.onSurface.copy(alpha = 0.5f),
                                        modifier = Modifier.size(iconSize * 0.9f)
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisible)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),
                            isError = passwordError != null,
                            supportingText = if (passwordError != null) {
                                {
                                    Text(
                                        text = passwordError!!,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = colorScheme.error,
                                            fontWeight = FontWeight.Medium
                                        )
                                    )
                                }
                            } else null,
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { handleLogin() }
                            ),
                            shape = RoundedCornerShape(fieldCornerRadius),
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

                    // ── Forgot Password ──────────────────────────
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = { /* TODO forgot password */ },
                            enabled = !isLoading
                        ) {
                            Text(
                                text = "Forgot Password?",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = colorScheme.primary,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = labelFontSize
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(screenHeight * sectionGap * 1.2f))

            // ── Login Button ─────────────────────────────────────
            Button(
                onClick = { handleLogin() },
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = maxFormWidth)
                    .height(buttonHeight),
                shape = RoundedCornerShape(fieldCornerRadius),
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
                        fadeIn(tween(200)) togetherWith fadeOut(tween(200))
                    },
                    label = "loginButtonContent"
                ) { loading ->
                    if (loading) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(iconSize * 0.9f),
                                color = colorScheme.onPrimary,
                                strokeWidth = 2.5.dp
                            )
                            Text(
                                text = "Signing in...",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = buttonFontSize
                                )
                            )
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "Sign In as Team Head",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = buttonFontSize
                                )
                            )
                            Icon(
                                imageVector = Icons.Rounded.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(iconSize * 0.9f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}


// ─── Animated Gradient Icon Component ────────────────────────────
@Composable
fun AnimatedGradientIcon(
    size: Dp,
    colorScheme: androidx.compose.material3.ColorScheme
) {
    val infiniteTransition = rememberInfiniteTransition(label = "iconAnim")

    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "iconShimmer"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(size)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        colorScheme.primary,
                        colorScheme.tertiary,
                        colorScheme.primary
                    ),
                    start = Offset(shimmerOffset * 400f, 0f),
                    end = Offset(shimmerOffset * 400f + 400f, 400f)
                ),
                shape = RoundedCornerShape(size * 0.275f)
            )
    ) {
        Icon(
            imageVector = Icons.Rounded.SupervisorAccount,
            contentDescription = null,
            tint = colorScheme.onPrimary,
            modifier = Modifier.size(size * 0.5f)
        )
    }
}