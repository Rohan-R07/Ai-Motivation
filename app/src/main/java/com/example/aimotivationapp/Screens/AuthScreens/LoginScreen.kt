package com.example.aimotivationapp.Screens.AuthScreens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aimotivationapp.R
import com.example.aimotivationapp.Utils.isLandscape
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(mainBackstack: SnapshotStateList<Any>) {

    val firebaseAuth = FirebaseAuth.getInstance()


//    Column(
//        modifier = Modifier
//            .background(MaterialTheme.colorScheme.surface)
//            .fillMaxSize(),
//        verticalArrangement = Arrangement.Top,
//    ) {
    LoginPotrait(firebaseAuth = firebaseAuth,mainBackstack)
//    }

}
