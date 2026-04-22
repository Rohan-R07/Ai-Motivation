package com.example.aimotivationapp.MainNavigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


import androidx.navigation3.runtime.entryProvider

import androidx.navigation3.ui.NavDisplay
import com.example.aimotivationapp.Screens.AuthScreens.LoginScreen
import com.example.aimotivationapp.Screens.AuthScreens.TeamHeadLoginScreen
import com.example.aimotivationapp.Screens.GenerateMotivation.CreateMotivationScreen

//import com.example.aimotivationapp.Screens.AuthScreens.UserCheckingScreen
import com.example.aimotivationapp.Screens.HomeScreens.FacultyScreen
import com.example.aimotivationapp.Screens.HomeScreens.TeamHeadScreen
import com.example.aimotivationapp.Screens.MotivationSplashScreen
import com.example.aimotivationapp.Screens.SettingsScreen

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun MainNavigation() {

//
    val backStack = remember { mutableStateListOf<Any>(SplashScreen) }


    var previousSize by remember { mutableStateOf(backStack.size) }

    NavDisplay(
        backStack = backStack,
        onBack = {
            backStack.removeLastOrNull()
        },

        transitionSpec = {

            val isPush = backStack.size > previousSize
            previousSize = backStack.size

            if (isPush) {
                // 👉 POP (BACK BUTTON)
                slideInHorizontally { -it } + fadeIn() togetherWith
                        slideOutHorizontally { it } + fadeOut()
            } else {


                // 👉 PUSH
                slideInHorizontally { it } + fadeIn() togetherWith
                        slideOutHorizontally { -it / 3 } + fadeOut()
            }
        },

        entryProvider = entryProvider {
            entry<SplashScreen> {
                MotivationSplashScreen(mainNavBackStack = backStack)
            }
            entry<LoginScreen> {
                LoginScreen(backStack)
            }
            entry<FacultyScreen> {
                FacultyScreen(mainBackStack = backStack)
            }
            entry<TeamHeadScreen> {
                TeamHeadScreen(mainNavigation = backStack)
            }
            entry<CreateMotivation> {
                CreateMotivationScreen(backStack)
            }
            entry<TeamHeadLoginScreen> {
                TeamHeadLoginScreen(backStack)
            }

            entry<SettingsScreen> {
                SettingsScreen(backStack)
            }
        }
    )
}