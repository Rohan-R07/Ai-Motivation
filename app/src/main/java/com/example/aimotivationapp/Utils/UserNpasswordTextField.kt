package com.example.aimotivationapp.Utils

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization


@Composable
fun UserAndPasswardTextField(
    placeholder: String = "",
    label:String = "",
    value: String,
    onvalueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val usernameRegex = remember {
        Regex("^[a-zA-Z0-9._]{0,20}$")
    }

    val isValid = remember(value) {
        if (isPassword) {
            value.length >= 6 // basic password rule
        } else {
            value.length >= 3 &&
                    Regex("^[a-zA-Z0-9._]{3,20}$").matches(value)
        }
    }

    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        value = value,

        onValueChange = { newValue ->
            if (!isPassword) {
//                if (newValue.contains(" ")) return@TextField
                if (!usernameRegex.matches(newValue)) return@OutlinedTextField
            }
            onvalueChange(newValue)
        },

        label = {
            Text(
                 label,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        },

        placeholder = {
            Text(
                 placeholder,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        },

        leadingIcon = {
            Icon(
                imageVector =
                    if (isPassword) Icons.Filled.Lock else Icons.Filled.Person,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        },

        // 👇 Password masking logic
        visualTransformation =
            if (isPassword && !passwordVisible)
                PasswordVisualTransformation()
            else
                VisualTransformation.None,

        // 👇 Combined trailing icons (clear + eye)
        trailingIcon = {
            Row {
                if (value.isNotEmpty()) {
                    IconButton(onClick = { onvalueChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }

                if (isPassword) {
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible }
                    ) {
                        Icon(
                            imageVector =
                                if (passwordVisible)
                                    Icons.Default.VisibilityOff
                                else
                                    Icons.Default.Visibility,
                            contentDescription = "Toggle password",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        },

        singleLine = true,

        isError = value.isNotEmpty() && !isValid,

        supportingText = {
            if (value.isNotEmpty() && !isValid) {
                Text(
                    text =
                        if (isPassword)
                            "Minimum 6 characters"
                        else
                            "3–20 characters • letters, numbers, . or _",
                    color = MaterialTheme.colorScheme.error
                )
            }
        },

        keyboardOptions = KeyboardOptions(
            keyboardType =
                if (isPassword)
                    KeyboardType.Password
                else
                    KeyboardType.Ascii,
            imeAction = ImeAction.Next,
            capitalization = KeyboardCapitalization.None
        ),

        colors = OutlinedTextFieldDefaults.colors(
//            focusedIndicatorColor = MaterialTheme.colorScheme.background,
//            unfocusedIndicatorColor = MaterialTheme.colorScheme.background,
//            unfocusedContainerColor =
//                MaterialTheme.colorScheme.surfaceVariant

            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,

        )
    )
}
