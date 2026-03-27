package com.example.aimotivationapp.Screens

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


@Composable
fun MotivationSplashScreen(mainNavBackStack: SnapshotStateList<Any>) {

    val coroutineScope = rememberCoroutineScope()

    val firebaseAuth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Red)
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {

        Text(
            "Motivation",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal,
            modifier = Modifier
                .clip(RoundedCornerShape(40.dp))
        )

    }
//
    val isUserLoggedIn = firebaseAuth.currentUser != null
    LaunchedEffect(Unit) {

        coroutineScope.launch {
            delay(1000)


            if (isUserLoggedIn) {

                mainNavBackStack.add(TeamHeadScreen)
                mainNavBackStack.remove(SplashScreen)
            } else {

                mainNavBackStack.add(LoginScreen)
                mainNavBackStack.remove(SplashScreen)
            }
        }
    }


}

