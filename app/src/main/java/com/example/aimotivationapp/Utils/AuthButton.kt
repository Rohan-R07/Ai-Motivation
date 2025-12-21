package com.example.aimotivationapp.Utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SignInButton(
    text: String = "Sign In",
    isLoading: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

//    buttonCollor = MaterialTheme.colorScheme.errorContainer
//    buttonContentColor = MaterialTheme.colorScheme.onError
    var buttonCollor = MaterialTheme.colorScheme.primary
    var buttonContentColor = MaterialTheme.colorScheme.onPrimary
    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonCollor,
            contentColor = buttonContentColor,
            disabledContainerColor =
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            disabledContentColor =
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                strokeWidth = 4.dp,
                color = MaterialTheme.colorScheme.primary
            )

        } else {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
