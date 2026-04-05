package com.example.aimotivationapp.Screens.HomeScreens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aimotivationapp.Datastore.PreferenceSavingViewModel
import com.example.aimotivationapp.Datastore.PreferenceViewModelFactory
import com.example.aimotivationapp.Datastore.UserPreferences
import com.example.aimotivationapp.Datastore.UserRepository
import com.example.aimotivationapp.MainNavigation.FacultyScreen
import com.example.aimotivationapp.MainNavigation.LoginScreen
import com.example.aimotivationapp.MainNavigation.UserCheckingScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth

@Composable
fun FacultyScreen(mainBackStack: SnapshotStateList<Any>) {

    val firebaseauth = FirebaseAuth.getInstance()
    Column(

        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current
        val repo = remember {
            UserRepository(UserPreferences(context))
        }

        val viewModel: PreferenceSavingViewModel = viewModel(
            factory = PreferenceViewModelFactory(repo)
        )

        val theEnterValue = viewModel.userName.collectAsState().value.toString()
        val theEnterEmail = viewModel.userEmail.collectAsState().value.toString()

        Text("Email is $theEnterEmail")
        Text("Name is $theEnterValue")
        Button(
            onClick = {
                firebaseauth.signOut()
                viewModel.clearData()
                mainBackStack.add(LoginScreen)
                mainBackStack.remove(FacultyScreen)
                Log.d("DATA ","name $theEnterValue email $theEnterEmail")
            }
        ) {

            Text("logout")
        }

        Log.d("FacultyScreen","theEnterValue $theEnterEmail")
    }

}