package com.example.aimotivationapp.Screens.AuthScreens

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aimotivationapp.Datastore.PreferenceSavingViewModel
import com.example.aimotivationapp.Datastore.PreferenceViewModelFactory
import com.example.aimotivationapp.Datastore.UserPreferences
import com.example.aimotivationapp.Datastore.UserRepository
import com.example.aimotivationapp.MainNavigation.LoginScreen
import com.example.aimotivationapp.Utils.isLandscape
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import com.example.aimotivationapp.FirebaseFirestore.FirestoreViewModel
import com.example.aimotivationapp.MainNavigation.FacultyScreen
import com.example.aimotivationapp.MainNavigation.TeamHeadLoginScreen
import org.w3c.dom.Text
import kotlin.math.sign
import kotlin.math.sin
//

//@Composable
//fun LoginPotrait(firebaseAuth: FirebaseAuth, mainBackStack: SnapshotStateList<Any>) {
//    Column(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        val context = LocalContext.current
//        val repo = remember {
//            UserRepository(UserPreferences(context))
//        }
//        val name = remember { mutableStateOf("") }
//
//        val viewModel: PreferenceSavingViewModel = viewModel(
//            factory = PreferenceViewModelFactory(repo)
//        )
//        val firestore: FirestoreViewModel = viewModel()
//
//        var userEmail = remember { mutableStateOf("") }
//        var password = remember { mutableStateOf("") }
//
//        var teamHead = remember { mutableStateOf("") }
//        val firebase = FirebaseAuth.getInstance()
//        TextField(
//            value = userEmail.value,
//            onValueChange = { userEmail.value = it },
//            label = { Text(text = "Email") },
//        )
//
//        TextField(
//            value = password.value,
//            onValueChange = { password.value = it },
//            label = { Text(text = "password") },
//        )
//
//        TextField(
//            name.value,
//            onValueChange = { name.value = it },
//            label = { Text(text = "Name") },
//        )
//        val signINSucesfullStae = remember { mutableStateOf(false) }
//        Button(
//            onClick = {
//                firebase.createUserWithEmailAndPassword(
//                    userEmail.value,
//                    password.value
//                )
//                    .addOnSuccessListener {
//                        Log.d("Login", "Login Successful")
//                        signINSucesfullStae.value = true
//                        firestore.checkUserRoleforHeadLoginScreen(
//                            email = teamHead.value,
//                            onSuccess = {
//                                Log.d("Login", "Firestore Successful")
//                                viewModel.saveName(name.value)
//                                viewModel.saveEmail(teamHead.value)
//                                mainBackStack.add(FacultyScreen)
//                                mainBackStack.remove(LoginScreen)
//                            },
//                            onFailure = {
//                                Log.d("Login", "Firestore Failed")
//                            }
//                        )
//                    }
//                    .addOnFailureListener { task ->
//                        Log.d("Login", "Login Failed")
//                        Log.d("Login portrait",userEmail.value)
//                        Log.d("Login",task.message.toString())
//                        signINSucesfullStae.value = false
//                    }
//
//
//            }
//        ) {
//            Text("Login")
//        }
//
//        TextField(
//            teamHead.value,
//            onValueChange = { teamHead.value = it },
//            label = { Text(text = "Team Head") },
//        )
//
//        Button(
//            onClick = {
//
//            }
//        ) {
//            Text("Firestore")
//        }
//
//    }
//}


//firebaseAuth.signInWithEmailAndPassword(
//signInEmail, signInPassword
//).addOnSuccessListener { authResult ->
//    Log.d("Login","sucessfully Loggedn")
//
//    val cleanedSignInHeadID =
//        signInHeadId.trim().lowercase()
//
//
//    coroutineScope.launch {
//        snackbarHostState.showSnackbar(
//            "Login Successful",
//            withDismissAction = true,
//            duration = SnackbarDuration.Short
//        )
//
//
//        Log.d("Monitor", cleanedSignInHeadID)
//        fireStoreViewModel.checkUserRoleforHeadLoginScreen(cleanedSignInHeadID,
//            onSuccess = {
//                Log.d("Login", "Firestore Successful")
//
//                signInLoading = false
//                prefViewModel.saveName(signInName)
//
//                prefViewModel.saveEmail(
//                    cleanedSignInHeadID
//                )
//                mainBackStack.add(FacultyScreen)
//                mainBackStack.remove(LoginScreen)
//            },
//            onFailure = {
//                Log.d("Login", "Firestore Failed")
//
//
//                signInLoading = false
//                Toast.makeText(
//                    context,
//                    "Invalid Head ID",
//                    Toast.LENGTH_SHORT
//                ).show()
//            })
//
//
//    }
//
//}.addOnFailureListener { task ->
//    signInLoading = false
//
//    Log.d(
//        "LoginScreen", task.message.toString()
//    )
//
//    Log.d("Login", "Login Failed")
//    Log.d("Login", task.message.toString())
//    Toast.makeText(
//        context,
//        "Check Email and Pasword",
//        Toast.LENGTH_SHORT
//    ).show()
//}
//
//import androidx.compose.animation.AnimatedContent
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.SnackbarHost
//import androidx.compose.material3.Text
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.draw.rotate
//import androidx.compose.ui.platform.LocalContext
//
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.aimotivationapp.Utils.isLandscape
//
//import kotlinx.coroutines.launch
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.LinearEasing

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import kotlin.math.sin


// ─── Screen Size Classification ──────────────────────────────────
private enum class ScreenClass { COMPACT, MEDIUM, EXPANDED }

private fun widthClass(widthDp: Dp): ScreenClass = when {
    widthDp < 360.dp -> ScreenClass.COMPACT
    widthDp < 600.dp -> ScreenClass.MEDIUM
    else -> ScreenClass.EXPANDED
}

private fun heightClass(heightDp: Dp): ScreenClass = when {
    heightDp < 600.dp -> ScreenClass.COMPACT
    heightDp < 800.dp -> ScreenClass.MEDIUM
    else -> ScreenClass.EXPANDED
}


@Composable
fun LoginPotrait(
    firebaseAuth: FirebaseAuth, mainBackStack: SnapshotStateList<Any>
) {
    // firebase Firestore


    val colorScheme = MaterialTheme.colorScheme
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val wClass = widthClass(screenWidth)
    val hClass = heightClass(screenHeight)

    val fireStoreViewModel: FirestoreViewModel = viewModel()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val repo = remember { UserRepository(UserPreferences(context)) }
    val prefViewModel: PreferenceSavingViewModel = viewModel(
        factory = PreferenceViewModelFactory(repo)
    )

    // ─── Auth Mode Toggle ────────────────────────────────────────
    var isRegisterMode by rememberSaveable { mutableStateOf(false) }

    // ─── Sign In State ───────────────────────────────────────────
    var signInEmail by rememberSaveable { mutableStateOf("") }
    var signInPassword by rememberSaveable { mutableStateOf("") }
    var signInName by rememberSaveable { mutableStateOf("") }
    var signInHeadId by rememberSaveable { mutableStateOf("") }
    var signInPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var signInLoading by remember { mutableStateOf(false) }
    var signInEmailError by remember { mutableStateOf<String?>(null) }
    var signInPasswordError by remember { mutableStateOf<String?>(null) }
    var signInNameError by remember { mutableStateOf<String?>(null) }
    var signInHeadIdError by remember { mutableStateOf<String?>(null) }

    // ─── Register State ──────────────────────────────────────────
    var regEmail by rememberSaveable { mutableStateOf("") }
    var regName by rememberSaveable { mutableStateOf("") }
    var regPassword by rememberSaveable { mutableStateOf("") }
    var regRetypePassword by rememberSaveable { mutableStateOf("") }
    var regHeadId by rememberSaveable { mutableStateOf("") }
    var regPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var regRetypeVisible by rememberSaveable { mutableStateOf(false) }
    var regLoading by remember { mutableStateOf(false) }
    var regEmailError by remember { mutableStateOf<String?>(null) }
    var regNameError by remember { mutableStateOf<String?>(null) }
    var regPasswordError by remember { mutableStateOf<String?>(null) }
    var regRetypeError by remember { mutableStateOf<String?>(null) }
    var regHeadIdError by remember { mutableStateOf<String?>(null) }

    // ─── Responsive Dimensions ───────────────────────────────────
    val horizontalPadding = when (wClass) {
        ScreenClass.COMPACT -> 16.dp
        ScreenClass.MEDIUM -> 28.dp
        ScreenClass.EXPANDED -> 48.dp
    }

    val headerHeight = when {
        isLandscape() -> 120.dp
        hClass == ScreenClass.COMPACT -> 160.dp
        hClass == ScreenClass.MEDIUM -> 220.dp
        else -> 260.dp
    }

    val cardCornerRadius = when (wClass) {
        ScreenClass.COMPACT -> 16.dp
        ScreenClass.MEDIUM -> 22.dp
        ScreenClass.EXPANDED -> 28.dp
    }

    val fieldCornerRadius = when (wClass) {
        ScreenClass.COMPACT -> 12.dp
        ScreenClass.MEDIUM -> 14.dp
        ScreenClass.EXPANDED -> 16.dp
    }

    val cardPadding = when (wClass) {
        ScreenClass.COMPACT -> 16.dp
        ScreenClass.MEDIUM -> 22.dp
        ScreenClass.EXPANDED -> 28.dp
    }

    val fieldSpacing = when (hClass) {
        ScreenClass.COMPACT -> 10.dp
        ScreenClass.MEDIUM -> 14.dp
        ScreenClass.EXPANDED -> 18.dp
    }

    val buttonHeight = when (wClass) {
        ScreenClass.COMPACT -> 48.dp
        ScreenClass.MEDIUM -> 52.dp
        ScreenClass.EXPANDED -> 56.dp
    }

    val buttonFontSize = when (wClass) {
        ScreenClass.COMPACT -> 14.sp
        ScreenClass.MEDIUM -> 15.sp
        ScreenClass.EXPANDED -> 16.sp
    }

    val titleFontSize = when (wClass) {
        ScreenClass.COMPACT -> 24.sp
        ScreenClass.MEDIUM -> 28.sp
        ScreenClass.EXPANDED -> 32.sp
    }

    val subtitleFontSize = when (wClass) {
        ScreenClass.COMPACT -> 12.sp
        ScreenClass.MEDIUM -> 13.sp
        ScreenClass.EXPANDED -> 15.sp
    }

    val labelFontSize = when (wClass) {
        ScreenClass.COMPACT -> 11.sp
        ScreenClass.MEDIUM -> 12.sp
        ScreenClass.EXPANDED -> 13.sp
    }

    val iconSize = when (wClass) {
        ScreenClass.COMPACT -> 20.dp
        ScreenClass.MEDIUM -> 22.dp
        ScreenClass.EXPANDED -> 24.dp
    }

    val logoSize = when (wClass) {
        ScreenClass.COMPACT -> 56.dp
        ScreenClass.MEDIUM -> 64.dp
        ScreenClass.EXPANDED -> 72.dp
    }

    val maxFormWidth = when (wClass) {
        ScreenClass.COMPACT -> 480.dp
        ScreenClass.MEDIUM -> 520.dp
        ScreenClass.EXPANDED -> 600.dp
    }

    val sectionGap = when (hClass) {
        ScreenClass.COMPACT -> 12.dp
        ScreenClass.MEDIUM -> 20.dp
        ScreenClass.EXPANDED -> 28.dp
    }

    // ─── Validations ─────────────────────────────────────────────
    fun validateSignIn(): Boolean {
        var valid = true

        if (signInEmail.isBlank()) {
            signInEmailError = "Email is required"; valid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(signInEmail).matches()) {
            signInEmailError = "Invalid email format"; valid = false
        } else signInEmailError = null

        if (signInPassword.isBlank()) {
            signInPasswordError = "Password is required"; valid = false
        } else if (signInPassword.length < 6) {
            signInPasswordError = "Min 6 characters"; valid = false
        } else signInPasswordError = null

        if (signInName.isBlank()) {
            signInNameError = "Name is required"; valid = false
        } else signInNameError = null

        if (signInHeadId.isBlank()) {
            signInHeadIdError = "Head ID is required"; valid = false
        } else signInHeadIdError = null

        return valid
    }

    fun validateRegister(): Boolean {
        var valid = true

        if (regEmail.isBlank()) {
            regEmailError = "Email is required"; valid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(regEmail).matches()) {
            regEmailError = "Invalid email format"; valid = false
        } else regEmailError = null

        if (regName.isBlank()) {
            regNameError = "Name is required"; valid = false
        } else regNameError = null

        if (regPassword.isBlank()) {
            regPasswordError = "Password is required"; valid = false
        } else if (regPassword.length < 6) {
            regPasswordError = "Min 6 characters"; valid = false
        } else regPasswordError = null

        if (regRetypePassword.isBlank()) {
            regRetypeError = "Please retype password"; valid = false
        } else if (regRetypePassword != regPassword) {
            regRetypeError = "Passwords don't match"; valid = false
        } else regRetypeError = null

        if (regHeadId.isBlank()) {
            regHeadIdError = "Head ID is required"; valid = false
        } else regHeadIdError = null

        return valid
    }


    // ─── UI ──────────────────────────────────────────────────────
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets(0)
    ) { contentPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .background(colorScheme.background)
                .imePadding()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ══════════════════════════════════════════════════════
            // ═══  ANIMATED HEADER (replaces hero image)  ══════════
            // ══════════════════════════════════════════════════════
            AnimatedGradientHeader(
                headerHeight = headerHeight,
                logoSize = logoSize,
                titleFontSize = titleFontSize,
                subtitleFontSize = subtitleFontSize,
                isRegisterMode = isRegisterMode,
                cardCornerRadius = cardCornerRadius
            )

            Spacer(modifier = Modifier.height(sectionGap))

            // ── Auth Form Card ───────────────────────────────────
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontalPadding)
                    .widthIn(max = maxFormWidth),
                shape = RoundedCornerShape(cardCornerRadius),
                color = colorScheme.surfaceVariant.copy(alpha = 0.35f),
                border = BorderStroke(0.5.dp, colorScheme.outline.copy(alpha = 0.15f))
            ) {
                AnimatedContent(
                    targetState = isRegisterMode, transitionSpec = {
                        (fadeIn(tween(300)) + slideInVertically { it / 8 }) togetherWith (fadeOut(
                            tween(200)
                        ) + slideOutVertically { -it / 8 })
                    }, label = "authSwitch"
                ) { register ->

                    if (!register) {
                        // ════════════════════════════════════════════
                        // ═══  SIGN IN FORM  ════════════════════════
                        // ════════════════════════════════════════════
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(cardPadding),
                            verticalArrangement = Arrangement.spacedBy(fieldSpacing)
                        ) {
                            Text(
                                text = "Sign In", style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold, color = colorScheme.onBackground
                                )
                            )

                            // Email
                            StyledTextField(
                                label = "Email",
                                value = signInEmail,
                                onValueChange = {
                                    signInEmail = it; signInEmailError = null
                                },
                                placeholder = "Enter email",
                                icon = Icons.Rounded.Email,
                                error = signInEmailError,
                                cornerRadius = fieldCornerRadius,
                                iconSize = iconSize,
                                labelFontSize = labelFontSize,
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Email,
                                onImeAction = {
                                    focusManager.moveFocus(FocusDirection.Down)
                                })

                            // Password
                            StyledTextField(
                                label = "Password",
                                value = signInPassword,
                                onValueChange = {
                                    signInPassword = it; signInPasswordError = null
                                },
                                placeholder = "Enter password",
                                icon = Icons.Rounded.Lock,
                                error = signInPasswordError,
                                cornerRadius = fieldCornerRadius,
                                iconSize = iconSize,
                                labelFontSize = labelFontSize,
                                isPassword = true,
                                passwordVisible = signInPasswordVisible,
                                onTogglePassword = {
                                    signInPasswordVisible = !signInPasswordVisible
                                },
                                imeAction = ImeAction.Next,
                                onImeAction = {
                                    focusManager.moveFocus(FocusDirection.Down)
                                })

                            // Name
                            StyledTextField(
                                label = "Full Name",
                                value = signInName,
                                onValueChange = {
                                    signInName = it; signInNameError = null
                                },
                                placeholder = "Enter your name",
                                icon = Icons.Rounded.Person,
                                error = signInNameError,
                                cornerRadius = fieldCornerRadius,
                                iconSize = iconSize,
                                labelFontSize = labelFontSize,
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Text,
                                capitalization = KeyboardCapitalization.Words,
                                onImeAction = {
                                    focusManager.moveFocus(FocusDirection.Down)
                                })

                            // Head ID
                            StyledTextField(
                                label = "Head ID",
                                value = signInHeadId,
                                onValueChange = {
                                    signInHeadId = it; signInHeadIdError = null
                                },
                                placeholder = "e.g. HD-00123",
                                icon = Icons.Rounded.Badge,
                                error = signInHeadIdError,
                                cornerRadius = fieldCornerRadius,
                                iconSize = iconSize,
                                labelFontSize = labelFontSize,
                                imeAction = ImeAction.Done,
                                keyboardType = KeyboardType.Text,
                                onImeAction = {
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                })

                            Spacer(modifier = Modifier.height(4.dp))


                            // Sign In Button
                            PrimaryActionButton(
                                text = "Sign In",
                                isLoading = signInLoading,
                                height = buttonHeight,
                                fontSize = buttonFontSize,
                                cornerRadius = fieldCornerRadius,
                                onClick = {
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                    if (validateSignIn()) {
                                        signInLoading = true
//
                                        Log.d("Monitor", signInHeadId)

                                        firebaseAuth.signInWithEmailAndPassword(
                                            signInEmail, signInPassword
                                        ).addOnSuccessListener { authResult ->
                                            Log.d("Login", "sucessfully Loggedn")

                                            val cleanedSignInHeadID =
                                                signInHeadId.trim().lowercase()


                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar(
                                                    "Login Successful",
                                                    withDismissAction = true,
                                                    duration = SnackbarDuration.Short
                                                )


                                                Log.d("Monitor", cleanedSignInHeadID)
                                                fireStoreViewModel.checkUserRoleforHeadLoginScreen(
                                                    cleanedSignInHeadID,
                                                    onSuccess = {
                                                        Log.d("Login", "Firestore Successful")

                                                        signInLoading = false
                                                        prefViewModel.saveName(signInName)

                                                        prefViewModel.saveEmail(
                                                            cleanedSignInHeadID
                                                        )
                                                        mainBackStack.add(FacultyScreen)
                                                        mainBackStack.remove(LoginScreen)
                                                    },
                                                    onFailure = {
                                                        Log.d("Login", "Firestore Failed")


                                                        signInLoading = false
                                                        Toast.makeText(
                                                            context,
                                                            "Invalid Head ID",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    })


                                            }

                                        }.addOnFailureListener { task ->
                                            signInLoading = false

                                            Log.d(
                                                "LoginScreen", task.message.toString()
                                            )

                                            Log.d("Login", "Login Failed")
                                            Log.d("Login", task.message.toString())
                                            Toast.makeText(
                                                context,
                                                "Check Email and Pasword",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                })

                            // Toggle to Register
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "New User?",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = colorScheme.onSurface.copy(alpha = 0.5f),
                                        fontSize = subtitleFontSize
                                    )
                                )
                                TextButton(onClick = { isRegisterMode = true }) {
                                    Text(
                                        text = "Register",
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = colorScheme.primary,
                                            fontSize = subtitleFontSize
                                        )
                                    )
                                }
                            }
                        }
//                        Log.d("LoginScre")
                    } else {
                        // ════════════════════════════════════════════
                        // ═══  REGISTER FORM  ═══════════════════════
                        // ════════════════════════════════════════════
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(cardPadding),
                            verticalArrangement = Arrangement.spacedBy(fieldSpacing)
                        ) {
                            Text(
                                text = "Create Account",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold, color = colorScheme.onBackground
                                )
                            )

                            // Email
                            StyledTextField(
                                label = "Email",
                                value = regEmail,
                                onValueChange = {
                                    regEmail = it; regEmailError = null
                                },
                                placeholder = "Enter email",
                                icon = Icons.Rounded.Email,
                                error = regEmailError,
                                cornerRadius = fieldCornerRadius,
                                iconSize = iconSize,
                                labelFontSize = labelFontSize,
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Email,
                                onImeAction = {
                                    focusManager.moveFocus(FocusDirection.Down)
                                })

                            // Name
                            StyledTextField(
                                label = "Full Name",
                                value = regName,
                                onValueChange = {
                                    regName = it; regNameError = null
                                },
                                placeholder = "Enter your name",
                                icon = Icons.Rounded.Person,
                                error = regNameError,
                                cornerRadius = fieldCornerRadius,
                                iconSize = iconSize,
                                labelFontSize = labelFontSize,
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Text,
                                capitalization = KeyboardCapitalization.Words,
                                onImeAction = {
                                    focusManager.moveFocus(FocusDirection.Down)
                                })

                            // Password
                            StyledTextField(
                                label = "Password",
                                value = regPassword,
                                onValueChange = {
                                    regPassword = it; regPasswordError = null
                                },
                                placeholder = "Create password",
                                icon = Icons.Rounded.Lock,
                                error = regPasswordError,
                                cornerRadius = fieldCornerRadius,
                                iconSize = iconSize,
                                labelFontSize = labelFontSize,
                                isPassword = true,
                                passwordVisible = regPasswordVisible,
                                onTogglePassword = {
                                    regPasswordVisible = !regPasswordVisible
                                },
                                imeAction = ImeAction.Next,
                                onImeAction = {
                                    focusManager.moveFocus(FocusDirection.Down)
                                })

                            // Retype Password
                            StyledTextField(
                                label = "Re-type Password",
                                value = regRetypePassword,
                                onValueChange = {
                                    regRetypePassword = it; regRetypeError = null
                                },
                                placeholder = "Re-type password",
                                icon = Icons.Rounded.Repeat,
                                error = regRetypeError,
                                cornerRadius = fieldCornerRadius,
                                iconSize = iconSize,
                                labelFontSize = labelFontSize,
                                isPassword = true,
                                passwordVisible = regRetypeVisible,
                                onTogglePassword = {
                                    regRetypeVisible = !regRetypeVisible
                                },
                                imeAction = ImeAction.Next,
                                onImeAction = {
                                    focusManager.moveFocus(FocusDirection.Down)
                                })

                            // Head ID
                            StyledTextField(
                                label = "Head ID",
                                value = regHeadId,
                                onValueChange = {
                                    regHeadId = it; regHeadIdError = null
                                },
                                placeholder = "e.g. HD-00123",
                                icon = Icons.Rounded.Badge,
                                error = regHeadIdError,
                                cornerRadius = fieldCornerRadius,
                                iconSize = iconSize,
                                labelFontSize = labelFontSize,
                                imeAction = ImeAction.Done,
                                keyboardType = KeyboardType.Text,
                                onImeAction = {
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                })

                            Spacer(modifier = Modifier.height(4.dp))
                            val cleanedRegHeadID = regHeadId.trim().lowercase()


                            // Register Button
                            PrimaryActionButton(
                                text = "Register",
                                isLoading = regLoading,
                                height = buttonHeight,
                                fontSize = buttonFontSize,
                                cornerRadius = fieldCornerRadius,
                                onClick = {
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                    if (validateRegister()) {
                                        regLoading = true
                                        firebaseAuth.createUserWithEmailAndPassword(
                                            regEmail, regPassword
                                        ).addOnSuccessListener { task ->


                                            // showing snackbar
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar(
                                                    "Registration Successful",
                                                    withDismissAction = true,
                                                    duration = SnackbarDuration.Short
                                                )
                                            }



                                            prefViewModel.saveName(regName)

                                            fireStoreViewModel.checkUserRoleforHeadLoginScreen(
                                                cleanedRegHeadID,
                                                onSuccess = {
                                                    regLoading = false
                                                    prefViewModel.saveName(regName)
                                                    Log.d("Login", "Firestore Successful")

                                                    prefViewModel.saveEmail(cleanedRegHeadID)
                                                    mainBackStack.add(FacultyScreen)
                                                    mainBackStack.remove(LoginScreen)
                                                },
                                                onFailure = {
                                                    Log.d("Login", "Firestore Failed")

                                                    signInLoading = false
                                                    Toast.makeText(
                                                        context,
                                                        "Invalid Head ID",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                })


                                        }
                                            .addOnFailureListener { task ->
                                                signInLoading = false

                                                Log.d(
                                                    "LoginScreen", task.message.toString()
                                                )

                                                Log.d("Login", "Login Failed")
                                                Log.d("Login", task.message.toString())
                                                Toast.makeText(
                                                    context,
                                                    "Check Email and Pasword",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }

                                    } else {
                                        regLoading = false

//                                        Log.d(
//                                            "LoginScreen",
//                                            taskmessage.toString()
//                                        )
                                        Toast.makeText(
                                            context, "Check Email and Pasword", Toast.LENGTH_SHORT
                                        ).show()
                                    }


                                }
                            )

                            // Toggle to Sign In
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Already a user?",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = colorScheme.onSurface.copy(alpha = 0.5f),
                                        fontSize = subtitleFontSize
                                    )
                                )
                                TextButton(onClick = { isRegisterMode = false }) {
                                    Text(
                                        text = "Sign In",
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = colorScheme.primary,
                                            fontSize = subtitleFontSize
                                        )
                                    )
                                }
                            }

                        }


                    }
                }
            }
            Spacer(modifier = Modifier.height(sectionGap))

            // ── Login as Team Head Button ─────────────────────────
            GradientTeamHeadButton(
                onClick = { mainBackStack.add(TeamHeadLoginScreen) },
                buttonHeight = buttonHeight,
                buttonFontSize = buttonFontSize,
                iconSize = iconSize,
                cornerRadius = fieldCornerRadius,
                maxWidth = maxFormWidth,
                horizontalPadding = horizontalPadding
            )

            Spacer(modifier = Modifier.height(sectionGap + 16.dp))
        }


    }
}


// ═══════════════════════════════════════════════════════════════════
// ═══  ANIMATED GRADIENT HEADER  ═══════════════════════════════════
// ═══════════════════════════════════════════════════════════════════
@Composable
private fun AnimatedGradientHeader(
    headerHeight: Dp,
    logoSize: Dp,
    titleFontSize: androidx.compose.ui.unit.TextUnit,
    subtitleFontSize: androidx.compose.ui.unit.TextUnit,
    isRegisterMode: Boolean,
    cardCornerRadius: Dp
) {
    val colorScheme = MaterialTheme.colorScheme
    val infiniteTransition = rememberInfiniteTransition(label = "headerAnim")

    // Slow gradient rotation
    val gradientAngle by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f, animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing), repeatMode = RepeatMode.Restart
        ), label = "gradAngle"
    )

    // Floating orb animations
    val float1 by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f, animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = EaseInOut), repeatMode = RepeatMode.Reverse
        ), label = "float1"
    )

    val float2 by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f, animationSpec = infiniteRepeatable(
            animation = tween(5500, easing = EaseInOut), repeatMode = RepeatMode.Reverse
        ), label = "float2"
    )

    val float3 by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f, animationSpec = infiniteRepeatable(
            animation = tween(3500, easing = EaseInOut), repeatMode = RepeatMode.Reverse
        ), label = "float3"
    )

    // Pulse for logo
    val logoPulse by infiniteTransition.animateFloat(
        initialValue = 0.95f, targetValue = 1.05f, animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOut), repeatMode = RepeatMode.Reverse
        ), label = "logoPulse"
    )

    // Sparkle rotation
    val sparkleRotation by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f, animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing), repeatMode = RepeatMode.Restart
        ), label = "sparkleRot"
    )

    val primaryColor = colorScheme.primary
    val tertiaryColor = colorScheme.tertiary
    val secondaryColor = colorScheme.secondary
    val surfaceColor = colorScheme.surface
    val onPrimaryColor = colorScheme.onPrimary

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(headerHeight)
            .clip(
                RoundedCornerShape(
                    bottomStart = cardCornerRadius * 1.5f, bottomEnd = cardCornerRadius * 1.5f
                )
            )
    ) {
        // ── Animated Gradient Background ─────────────────────
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val rad = Math.toRadians(gradientAngle.toDouble())

            // Main gradient
            drawRect(
                brush = Brush.linearGradient(
                    colors = listOf(
                        primaryColor, tertiaryColor, secondaryColor, primaryColor
                    ), start = Offset(
                        (w / 2 + w / 2 * kotlin.math.cos(rad)).toFloat(),
                        (h / 2 + h / 2 * kotlin.math.sin(rad)).toFloat()
                    ), end = Offset(
                        (w / 2 - w / 2 * kotlin.math.cos(rad)).toFloat(),
                        (h / 2 - h / 2 * kotlin.math.sin(rad)).toFloat()
                    )
                )
            )

            // Wavy bottom edge
            val wavePath = Path().apply {
                moveTo(0f, h * 0.75f)
                val waveHeight = h * 0.08f
                for (x in 0..w.toInt() step 4) {
                    val xf = x.toFloat()
                    val y = h * 0.82f + waveHeight * sin(
                        (xf / w * 4 * Math.PI + gradientAngle * 0.02).toFloat()
                    )
                    lineTo(xf, y)
                }
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }
            drawPath(
                path = wavePath, brush = Brush.verticalGradient(
                    colors = listOf(
                        primaryColor.copy(alpha = 0.3f), surfaceColor.copy(alpha = 0.0f)
                    ), startY = h * 0.75f, endY = h
                ), style = Fill
            )
        }

        // ── Floating Orbs (blurred circles) ──────────────────
        // Orb 1 - top left
        Box(
            modifier = Modifier
                .size(80.dp)
                .offset(
                    x = (20 + 30 * float1).dp, y = (15 + 20 * float2).dp
                )
                .alpha(0.25f)
                .blur(24.dp)
                .background(
                    color = onPrimaryColor.copy(alpha = 0.4f), shape = CircleShape
                )
        )

        // Orb 2 - top right
        Box(
            modifier = Modifier
                .size(60.dp)
                .align(Alignment.TopEnd)
                .offset(
                    x = (-15 - 25 * float2).dp, y = (30 + 25 * float3).dp
                )
                .alpha(0.2f)
                .blur(20.dp)
                .background(
                    color = tertiaryColor.copy(alpha = 0.5f), shape = CircleShape
                )
        )

        // Orb 3 - center left
        Box(
            modifier = Modifier
                .size(50.dp)
                .offset(
                    x = (60 + 40 * float3).dp, y = (headerHeight / 2 - 25.dp + (15 * float1).dp)
                )
                .alpha(0.15f)
                .blur(16.dp)
                .background(
                    color = secondaryColor.copy(alpha = 0.6f), shape = CircleShape
                )
        )

        // ── Sparkle Icons ────────────────────────────────────
        Icon(
            imageVector = Icons.Rounded.AutoAwesome,
            contentDescription = null,
            tint = onPrimaryColor.copy(alpha = 0.15f + 0.1f * float1),
            modifier = Modifier
                .size(18.dp)
                .align(Alignment.TopStart)
                .offset(x = 50.dp, y = 25.dp)
                .rotate(sparkleRotation)
        )

        Icon(
            imageVector = Icons.Rounded.AutoAwesome,
            contentDescription = null,
            tint = onPrimaryColor.copy(alpha = 0.12f + 0.08f * float2),
            modifier = Modifier
                .size(14.dp)
                .align(Alignment.TopEnd)
                .offset(x = (-40).dp, y = 55.dp)
                .rotate(-sparkleRotation * 0.7f)
        )

        Icon(
            imageVector = Icons.Rounded.AutoAwesome,
            contentDescription = null,
            tint = onPrimaryColor.copy(alpha = 0.1f + 0.1f * float3),
            modifier = Modifier
                .size(16.dp)
                .align(Alignment.CenterStart)
                .offset(x = 30.dp, y = 10.dp)
                .rotate(sparkleRotation * 0.5f)
        )

        // ── Center Content: Logo + Title ─────────────────────
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Glowing logo container
            Box(contentAlignment = Alignment.Center) {
                // Glow ring
                Box(
                    modifier = Modifier
                        .size(logoSize * 1.4f)
                        .scale(logoPulse)
                        .alpha(0.3f)
                        .blur(12.dp)
                        .background(
                            color = onPrimaryColor, shape = RoundedCornerShape(logoSize * 0.35f)
                        )
                )

                // Logo background
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(logoSize)
                        .scale(logoPulse)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    onPrimaryColor.copy(alpha = 0.25f),
                                    onPrimaryColor.copy(alpha = 0.1f)
                                )
                            ), shape = RoundedCornerShape(logoSize * 0.3f)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.LocalFireDepartment,
                        contentDescription = null,
                        tint = onPrimaryColor,
                        modifier = Modifier.size(logoSize * 0.55f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Motivato", style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = titleFontSize,
                    color = onPrimaryColor,
                    letterSpacing = 1.sp
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            AnimatedContent(
                targetState = isRegisterMode, transitionSpec = {
                    fadeIn(tween(300)) togetherWith fadeOut(tween(200))
                }, label = "subtitleSwitch"
            ) { register ->
                Text(
                    text = if (register) "Create your account"
                    else "Welcome back", style = MaterialTheme.typography.bodyMedium.copy(
                        color = onPrimaryColor.copy(alpha = 0.75f),
                        fontSize = subtitleFontSize,
                        textAlign = TextAlign.Center,
                        letterSpacing = 0.5.sp
                    )
                )
            }
        }
    }
}


// ═══════════════════════════════════════════════════════════════════
// ═══  REUSABLE STYLED TEXT FIELD  ══════════════════════════════════
// ═══════════════════════════════════════════════════════════════════
@Composable
private fun StyledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector,
    error: String?,
    cornerRadius: Dp,
    iconSize: Dp,
    labelFontSize: androidx.compose.ui.unit.TextUnit,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onTogglePassword: (() -> Unit)? = null,
    imeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Text,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    onImeAction: () -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = label, style = MaterialTheme.typography.labelMedium.copy(
                color = colorScheme.onSurface.copy(alpha = 0.65f),
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.3.sp,
                fontSize = labelFontSize
            )
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = placeholder, style = MaterialTheme.typography.bodyMedium.copy(
                        color = colorScheme.onSurface.copy(alpha = 0.3f)
                    )
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (error != null) colorScheme.error
                    else colorScheme.primary.copy(alpha = 0.7f),
                    modifier = Modifier.size(iconSize)
                )
            },
            trailingIcon = if (isPassword && onTogglePassword != null) {
                {
                    IconButton(onClick = onTogglePassword) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Rounded.Visibility
                            else Icons.Rounded.VisibilityOff,
                            contentDescription = null,
                            tint = colorScheme.onSurface.copy(alpha = 0.5f),
                            modifier = Modifier.size(iconSize * 0.9f)
                        )
                    }
                }
            } else null,
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation()
            else VisualTransformation.None,
            isError = error != null,
            supportingText = if (error != null) {
                {
                    Text(
                        text = error, style = MaterialTheme.typography.labelSmall.copy(
                            color = colorScheme.error, fontWeight = FontWeight.Medium
                        )
                    )
                }
            } else null,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType, imeAction = imeAction, capitalization = capitalization
            ),
            keyboardActions = KeyboardActions(
                onNext = { onImeAction() },
                onDone = { onImeAction() }),
            shape = RoundedCornerShape(cornerRadius),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorScheme.primary,
                unfocusedBorderColor = colorScheme.outline.copy(alpha = 0.25f),
                errorBorderColor = colorScheme.error,
                focusedContainerColor = colorScheme.surface,
                unfocusedContainerColor = colorScheme.surface,
                errorContainerColor = colorScheme.errorContainer.copy(alpha = 0.1f)
            )
        )
    }
}


// ═══════════════════════════════════════════════════════════════════
// ═══  PRIMARY ACTION BUTTON  ══════════════════════════════════════
// ═══════════════════════════════════════════════════════════════════
@Composable
private fun PrimaryActionButton(
    text: String,
    isLoading: Boolean,
    height: Dp,
    fontSize: androidx.compose.ui.unit.TextUnit,
    cornerRadius: Dp,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        shape = RoundedCornerShape(cornerRadius),
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
            transitionSpec = { fadeIn(tween(200)) togetherWith fadeOut(tween(200)) },
            label = "btnAnim"
        ) { loading ->
            if (loading) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                    Text(
                        text = "Please wait...", style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold, fontSize = fontSize
                        )
                    )
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = text, style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold, fontSize = fontSize
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
}


// ═══════════════════════════════════════════════════════════════════
// ═══  GRADIENT TEAM HEAD BUTTON  ══════════════════════════════════
// ═══════════════════════════════════════════════════════════════════
@Composable
private fun GradientTeamHeadButton(
    onClick: () -> Unit,
    buttonHeight: Dp,
    buttonFontSize: androidx.compose.ui.unit.TextUnit,
    iconSize: Dp,
    cornerRadius: Dp,
    maxWidth: Dp,
    horizontalPadding: Dp
) {
    val colorScheme = MaterialTheme.colorScheme
    val infiniteTransition = rememberInfiniteTransition(label = "teamBtnAnim")

    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = -1f, targetValue = 2f, animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing), repeatMode = RepeatMode.Restart
        ), label = "shimmer"
    )

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding)
            .widthIn(max = maxWidth)
            .height(buttonHeight),
        shape = RoundedCornerShape(cornerRadius),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent, contentColor = colorScheme.onPrimary
        ),
        contentPadding = PaddingValues(0.dp),
        elevation = ButtonDefaults.buttonElevation(0.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center, modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            colorScheme.primary, colorScheme.tertiary, colorScheme.primary
                        ),
                        start = Offset(shimmerOffset * 400f, 0f),
                        end = Offset(shimmerOffset * 400f + 400f, 100f)
                    ), shape = RoundedCornerShape(cornerRadius)
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(iconSize * 1.35f)
                        .background(
                            colorScheme.onPrimary.copy(alpha = 0.15f), CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Groups,
                        contentDescription = null,
                        tint = colorScheme.onPrimary,
                        modifier = Modifier.size(iconSize * 0.75f)
                    )
                }

                Text(
                    text = "Login as Team Head", style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = buttonFontSize,
                        letterSpacing = 0.3.sp,
                        color = colorScheme.onPrimary
                    )
                )

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