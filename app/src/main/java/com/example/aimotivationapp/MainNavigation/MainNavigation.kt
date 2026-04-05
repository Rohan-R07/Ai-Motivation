package com.example.aimotivationapp.MainNavigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf

import androidx.compose.runtime.remember


import androidx.navigation3.runtime.entryProvider

import androidx.navigation3.ui.NavDisplay
import com.example.aimotivationapp.Screens.AuthScreens.LoginScreen
import com.example.aimotivationapp.Screens.AuthScreens.TeamHeadLoginScreen
//import com.example.aimotivationapp.Screens.AuthScreens.UserCheckingScreen
import com.example.aimotivationapp.Screens.HomeScreens.FacultyScreen
import com.example.aimotivationapp.Screens.HomeScreens.TeamHeadScreen
import com.example.aimotivationapp.Screens.MotivationSplashScreen

@Composable
fun MainNavigation() {

//
    val backStack = remember { mutableStateListOf<Any>(SplashScreen) }


    NavDisplay(
        backStack = backStack,
        onBack = {
            backStack.removeLastOrNull()
        },

        entryProvider = entryProvider {
            entry<SplashScreen>{
                MotivationSplashScreen(mainNavBackStack = backStack )
            }

            entry<LoginScreen>{
                LoginScreen(backStack)
            }
            entry<FacultyScreen>{
                FacultyScreen(backStack)
            }
            entry<TeamHeadScreen>{
                TeamHeadScreen(mainNavigation = backStack)
            }

            entry<UserCheckingScreen>{
//                UserCheckingScreen(mainNavBackStack = backStack)
            }

            entry<TeamHeadLoginScreen>{
                TeamHeadLoginScreen(backStack)
            }

        }
    )

}