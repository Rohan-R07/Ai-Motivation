package com.example.aimotivationapp.MainNavigation

import kotlinx.serialization.Serializable

//
//@Serializable
//sealed interface MainNavigationRoutes : NavKey{

    @Serializable
    data object SplashScreen

    @Serializable
    data object TeamHeadScreen

    @Serializable
    data object FacultyScreen

    @Serializable
    data object LoginScreen
//}