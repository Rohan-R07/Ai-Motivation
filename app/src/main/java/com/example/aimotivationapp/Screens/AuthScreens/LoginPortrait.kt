package com.example.aimotivationapp.Screens.AuthScreens

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aimotivationapp.MainNavigation.LoginScreen
import com.example.aimotivationapp.MainNavigation.TeamHeadScreen
import com.example.aimotivationapp.R
import com.example.aimotivationapp.Screens.AuthScreens.Utils.signInInputs
import com.example.aimotivationapp.Utils.SignInButton
import com.example.aimotivationapp.Utils.UserAndPasswardTextField
import com.example.aimotivationapp.Utils.isLandscape
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


@Composable
fun LoginPotrait(
    firebaseAuth: FirebaseAuth,
    mainBackStack: SnapshotStateList<Any>
) {

    var signEmail by remember { mutableStateOf("") }

    var signPassword = remember { mutableStateOf("") }


    val registerPassword = remember { mutableStateOf("") }
    val registerEmail = remember { mutableStateOf("") }
    val passwardRetype = remember { mutableStateOf("") }

    val animatedContent = remember { mutableStateOf(false) }

    val context = LocalContext.current


    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()


    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier,
            )
        },
        contentWindowInsets = WindowInsets(0) // 🔥 removes default padding

    ) { contentpadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentpadding)
        ) {
            Card(
                modifier = Modifier
//                    .rotate(if (landscape) -90f else 0f)
                    .background(MaterialTheme.colorScheme.surface)

                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            ) {

                Image(
                    painter = painterResource(R.drawable.loginscreen),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(if (isLandscape()) 15f / 2f else 11f / 9f)
                        .rotate(if (isLandscape()) 90f else 0f),
                    contentScale = ContentScale.Crop
                )
            }

            val registerLoading = remember { mutableStateOf(false) }
            AnimatedContent(
                targetState = animatedContent.value,
                modifier = Modifier
            ) { target ->

                if (!target) {

                    var signUpLoading = remember { mutableStateOf(false) }


                    Column(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface)
                            .verticalScroll(rememberScrollState())
                            .fillMaxWidth()
                            .padding(40.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surface)
//            .padding(10.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Spacer(Modifier.padding(6.dp))
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = null,
                                modifier = Modifier.size(30.dp)

                            )
                            Spacer(Modifier.padding(6.dp))
                            Text("Motivato", fontWeight = FontWeight.Bold, fontSize = 16.sp)

                        }

                        Spacer(Modifier.padding(4.dp))

                        UserAndPasswardTextField(
                            label = "UserName",
                            placeholder = "Enter UserName",
                            value = signEmail,
                            onvalueChange = {
                                signEmail = it
                            },
                            isPassword = false,
                            modifier = Modifier
                                .height(80.dp)
                        )
//                        Spacer(Modifier.padding(6.dp))
                        UserAndPasswardTextField(
                            label = "Password",
                            placeholder = "Enter Password",
                            value = signPassword.value,
                            onvalueChange = { signPassword.value = it },
                            isPassword = true,
                            modifier = Modifier
                                .height(80.dp)
                        )
                        Spacer(Modifier.padding(3.dp))

                        SignInButton(
                            isLoading = signUpLoading.value, // TODO BACKEND IS USED HERE
                            onClick = {
                                signUpLoading.value = true
                                if (signInInputs(signEmail, signPassword.value) == null) {


                                    firebaseAuth.signInWithEmailAndPassword(
                                        signEmail,
                                        signPassword.value
                                    )

                                        .addOnCompleteListener {
                                            if (it.isSuccessful) {
                                                coroutineScope.launch {
                                                    snackbarHostState.showSnackbar(
                                                        message = "Login Successful",
                                                        withDismissAction = true,
                                                        duration = SnackbarDuration.Short,
                                                    )
                                                }
                                                signUpLoading.value = false
                                                mainBackStack.add(TeamHeadScreen)
                                                mainBackStack.remove(LoginScreen)

                                            } else {
                                                val errorMessage = it.exception?.message
                                                Log.d("FirebaseAuth", errorMessage.toString())

                                                coroutineScope.launch {
                                                    snackbarHostState.showSnackbar(
                                                        message = "Login Failed",
                                                        withDismissAction = true,
                                                        duration = SnackbarDuration.Short
                                                    )
                                                }

                                                signUpLoading.value = false
                                            }


                                        }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Please check your Email and Password",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                        )
                        Spacer(Modifier.padding(8.dp))

                        Row {

                            val annotatedText = buildAnnotatedString {
                                append("New User ? -> ")

                                pushStyle(
                                    SpanStyle(
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        fontWeight = FontWeight.ExtraBold,
                                    )
                                )
                                append("Register")
                                pop()
                            }

                            Text(
                                text = annotatedText,
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .clickable {
                                        animatedContent.value = !animatedContent.value
                                    }
                            )

                            Spacer(Modifier.padding(20.dp))
                        }


                    }


                }
                else {
                    Column(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface)
                            .verticalScroll(rememberScrollState())
//            .horizontalScroll(rememberScrollState())
                            .fillMaxWidth()
                            .padding(40.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(10.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = null,
                                modifier = Modifier.size(40.dp)

                            )
                            Spacer(Modifier.padding(6.dp))
                            Text("Motivato", fontWeight = FontWeight.Bold, fontSize = 16.sp)

                        }

                        Spacer(Modifier.padding(4.dp))

                        UserAndPasswardTextField(
                            label = "Email",
                            placeholder = "Enter Email",
                            value = registerEmail.value,
                            onvalueChange = {
                                registerEmail.value = it
                            },
                            isPassword = false,
                            modifier = Modifier
                                .height(80.dp)
                        )
//                        Spacer(Modifier.padding(3.dp))
                        UserAndPasswardTextField(
                            label = "Password",
                            placeholder = "Enter Password",
                            value = registerPassword.value,
                            onvalueChange = { registerPassword.value = it },
                            isPassword = true,
                            modifier = Modifier
                                .height(80.dp)
                        )

                        UserAndPasswardTextField(
                            placeholder = "Re-type password",
                            label = "Re-type",
                            value = passwardRetype.value,
                            onvalueChange = { passwardRetype.value = it },
                            isPassword = true,
                            modifier = Modifier
                                .height(80.dp)
                        )
                        Spacer(Modifier.padding(3.dp))
                        SignInButton(
                            text = "Register",
                            isLoading = registerLoading.value, // TODO BACKEND IS USED HERE
                            onClick = {
                                registerLoading.value = true
                                if (registerPassword != passwardRetype) {
                                    if (signInInputs(
                                            registerEmail.value,
                                            registerPassword.value
                                        ) == null
                                    ) {
                                        firebaseAuth.createUserWithEmailAndPassword(
                                            registerEmail.value,
                                            registerPassword.value
                                        )
                                            .addOnCompleteListener { task ->
                                                if (task.isSuccessful) {
                                                    coroutineScope.launch {
                                                        snackbarHostState.showSnackbar(
                                                            message = "Registration Successful",
                                                            withDismissAction = true,
                                                            duration = SnackbarDuration.Short
                                                        )
                                                    }

                                                    mainBackStack.add(TeamHeadScreen)
                                                    mainBackStack.remove(LoginScreen)
                                                    registerLoading.value = false

                                                } else {
                                                    coroutineScope.launch {
                                                        snackbarHostState.showSnackbar(
                                                            message = "Registration Failled Retry again",
                                                            withDismissAction = true,
                                                            duration = SnackbarDuration.Short
                                                        )
                                                    }

                                                    registerLoading.value = false
                                                }
                                            }
                                    }
                                } else {
                                    coroutineScope.launch {

                                        snackbarHostState.showSnackbar(
                                            message = "Password Mismatch",
                                            withDismissAction = true,
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                    registerLoading.value = false

                                }
                            }

                        )
                        Spacer(Modifier.padding(10.dp))

                        Row(

                        ) {

                            val annotatedText = buildAnnotatedString {
                                append("Already a user ? ->  ")

                                pushStyle(
                                    SpanStyle(
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        fontWeight = FontWeight.ExtraBold,
                                    )
                                )
                                append("Sign in!")
                                pop()
                            }

                            Text(
                                text = annotatedText,
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .clickable {
                                        animatedContent.value = !animatedContent.value
                                    }
                            )

                            Spacer(Modifier.padding(50.dp))

                        }


                    }
                }
            }
        }
    }
}

