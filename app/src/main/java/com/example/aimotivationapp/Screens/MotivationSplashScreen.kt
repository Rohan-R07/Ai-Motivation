package com.example.aimotivationapp.Screens

import android.util.Log
import androidx.collection.emptyLongSet
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aimotivationapp.MainNavigation.LoginScreen
import com.example.aimotivationapp.MainNavigation.SplashScreen
import com.example.aimotivationapp.MainNavigation.TeamHeadScreen
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


import androidx.compose.animation.core.*
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aimotivationapp.Datastore.PreferenceSavingViewModel
import com.example.aimotivationapp.Datastore.PreferenceViewModelFactory
import com.example.aimotivationapp.Datastore.UserPreferences
import com.example.aimotivationapp.Datastore.UserRepository
import com.example.aimotivationapp.MainNavigation.UserCheckingScreen
import kotlinx.coroutines.delay

@Composable
fun MotivationSplashScreen(
    mainNavBackStack: SnapshotStateList<Any>
) {
    val context = LocalContext.current

    val repo = remember {
        UserRepository(UserPreferences(context))
    }

    val viewModel: PreferenceSavingViewModel = viewModel(
        factory = PreferenceViewModelFactory(repo)
    )

    val theEnterValue = viewModel.userName.collectAsState().value.toString()


    val colorScheme = MaterialTheme.colorScheme
    val coroutineScope = rememberCoroutineScope()

    var startAnimation by remember { mutableStateOf(false) }

    // Simple fade in
    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 700,
            easing = EaseInOutSine
        ),
        label = "alpha"
    )

    // Text fade in slightly after logo
    val textAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 600,
            delayMillis = 400,
            easing = EaseInOutSine
        ),
        label = "textAlpha"
    )

    // ─── Infinite logo animation ─────────────────────────────────
    val infiniteTransition = rememberInfiniteTransition(label = "logoAnim")

    // Soft scale breathe — like iOS app icons loading
    val logoScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logoScale"
    )

    // Corner radius morphing — squircle to rounded
    val cornerRadius by infiniteTransition.animateFloat(
        initialValue = 20f,
        targetValue = 28f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cornerMorph"
    )

    // Soft glow alpha behind logo
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.15f,
        targetValue = 0.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    val firebaseAuth = FirebaseAuth.getInstance()
    val isUserLoggedIn = firebaseAuth.currentUser != null


    // ─── Launch & Navigate ───────────────────────────────────────
    LaunchedEffect(Unit) {
        startAnimation = true

        delay(1000)
        val isthEnterValueTrue = theEnterValue == "null" // is Empty
        Log.d("SplashScreen", "USer Logged In $isUserLoggedIn The Value $isthEnterValueTrue")
        coroutineScope.launch {
            delay(1200)


            if (isUserLoggedIn) {
                if (theEnterValue.isEmpty()) {
                    mainNavBackStack.add(UserCheckingScreen)
                    mainNavBackStack.remove(SplashScreen)
                } else {
                    mainNavBackStack.add(TeamHeadScreen)
                    mainNavBackStack.remove(SplashScreen)
                }

            } else {
                mainNavBackStack.add(LoginScreen)
                mainNavBackStack.remove(SplashScreen)
            }


//            if (isUserLoggedIn){
//                mainNavBackStack.add(TeamHeadScreen)
//                mainNavBackStack.remove(SplashScreen)
//
//            }
//            else{
//                mainNavBackStack.add(LoginScreen)
//                mainNavBackStack.remove(SplashScreen)
//            }

        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.alpha(alpha)
        ) {

            Box(
                contentAlignment = Alignment.Center
            ) {
                // Soft glow behind logo
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .scale(logoScale)
                        .background(
                            color = colorScheme.primary.copy(alpha = glowAlpha),
                            shape = RoundedCornerShape(cornerRadius.dp)
                        )
                        .blur(16.dp)
                )

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(72.dp)
                        .scale(logoScale)
                        .background(
                            color = colorScheme.primaryContainer,
                            shape = RoundedCornerShape(cornerRadius.dp)
                        )
                ) {
                    // ─────────────────────────────────────────────
                    // TODO: Replace Text with your logo Image below
                    // ─────────────────────────────────────────────
                    // Image(
                    //     painter = painterResource(R.drawable.ic_your_logo),
                    //     contentDescription = null,
                    //     modifier = Modifier.size(44.dp)
                    // )
                    // ─────────────────────────────────────────────
                    Text(
                        text = "M",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            color = colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.alpha(textAlpha)
            ) {
                Text(
                    text = "Motivato",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = colorScheme.onBackground,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 22.sp,
                        letterSpacing = 0.2.sp
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Motivate Your Team... :)",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = colorScheme.onSurface.copy(alpha = 0.45f),
                        fontWeight = FontWeight.Normal,
                        letterSpacing = 0.2.sp
                    )
                )
            }
        }


    }
}