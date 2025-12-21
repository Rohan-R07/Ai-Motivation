package com.example.aimotivationapp.Screens.AuthScreens

import android.text.LoginFilter
import androidx.collection.emptyLongSet
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aimotivationapp.R
import com.example.aimotivationapp.Utils.SignInButton
import com.example.aimotivationapp.Utils.UserAndPasswardTextField
import com.example.aimotivationapp.Utils.isLandscape


@Composable
fun LoginPotrait(modifier: Modifier = Modifier) {

    var username = rememberSaveable { mutableStateOf("") }
    var password = rememberSaveable { mutableStateOf("") }
    var headEmail = rememberSaveable { mutableStateOf("") }
    var isRegister by remember { mutableStateOf(false) }


    var username2 = remember { mutableStateOf("") }
    var passward2 = remember { mutableStateOf("") }
    var passwardRetype = remember { mutableStateOf("") }

    var animatedContent = remember { mutableStateOf(false) }


        Column(
            modifier = Modifier.fillMaxSize()
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


            AnimatedContent(
                targetState = animatedContent.value,
                modifier = Modifier
            ) { target ->

                if (!target) {

                    Column(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface)
                            .verticalScroll(rememberScrollState())
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            modifier = modifier
                                .background(MaterialTheme.colorScheme.surface)
//            .padding(10.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Spacer(Modifier.padding(10.dp))
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = null,
                                modifier = Modifier.size(50.dp)

                            )
                            Spacer(Modifier.padding(6.dp))
                            Text("Motivato", fontWeight = FontWeight.Bold, fontSize = 25.sp)

                        }

                        Spacer(Modifier.padding(10.dp))

                        UserAndPasswardTextField(
                            label = "User Name",
                            placeholder = "Enter User Name",
                            value = username.value,
                            onvalueChange = {
                                username.value = it
                            },
                            isPassword = false,
                            modifier = Modifier
                                .height(80.dp)
                        )
                        Spacer(Modifier.padding(6.dp))
                        UserAndPasswardTextField(
                            label = "Password",
                            placeholder = "Enter Password",
                            value = password.value,
                            onvalueChange = { password.value = it },
                            isPassword = true,
                            modifier = Modifier
                                .height(80.dp)
                        )
                        Spacer(Modifier.padding(6.dp))
                        SignInButton(
                            isLoading = false, // TODO BACKEND IS USED HERE
                            onClick = {
                                //TODO AUTHINTICATION HERE
                            }

                        )
                        Spacer(Modifier.padding(8.dp))

                        Row(

                        ) {

                            val annotatedText = buildAnnotatedString {
                                append("The only way to do great work is to love what you do. New User ? ")

                                pushStyle(
                                    SpanStyle(
                                        color = MaterialTheme.colorScheme.tertiaryContainer,
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

                } else {


                    Column(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface)
                            .verticalScroll(rememberScrollState())
//            .horizontalScroll(rememberScrollState())
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            modifier = modifier
                                .background(MaterialTheme.colorScheme.surface)
//            .padding(10.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = null,
                                modifier = Modifier.size(50.dp)

                            )
                            Spacer(Modifier.padding(6.dp))
                            Text("Motivato", fontWeight = FontWeight.Bold, fontSize = 25.sp)

                        }

                        Spacer(Modifier.padding(10.dp))

                        UserAndPasswardTextField(
                            label = "User Name",
                            placeholder = "Enter User Name",
                            value = username.value,
                            onvalueChange = {
                                username.value = it
                            },
                            isPassword = false,
                            modifier = Modifier
                                .height(80.dp)
                        )
                        Spacer(Modifier.padding(6.dp))
                        UserAndPasswardTextField(
                            label = "Password",
                            placeholder = "Enter Password",
                            value = passward2.value,
                            onvalueChange = { passward2.value = it },
                            isPassword = true,
                            modifier = Modifier
                                .height(80.dp)
                        )
                        Spacer(Modifier.padding(6.dp))
                        UserAndPasswardTextField(
                            placeholder = "Re-type password",
                            label = "Re-type",
                            value = passwardRetype.value,
                            onvalueChange = { passwardRetype.value = it },
                            isPassword = true,
                            modifier = Modifier
                                .height(80.dp)
                        )
                        Spacer(Modifier.padding(6.dp))
                        SignInButton(
                            text = "Register",
                            isLoading = false, // TODO BACKEND IS USED HERE
                            onClick = {
                                //TODO AUTHINTICATION HERE
                            }

                        )
                        Spacer(Modifier.padding(10.dp))

                        Row(

                        ) {

                            val annotatedText = buildAnnotatedString {
                                append("The only way to do great work is to love what you do. Already a user ? ")

                                pushStyle(
                                    SpanStyle(
                                        color = MaterialTheme.colorScheme.tertiaryContainer,
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


