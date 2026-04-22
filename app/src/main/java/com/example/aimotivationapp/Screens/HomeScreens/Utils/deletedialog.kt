package com.example.aimotivationapp.Screens.HomeScreens.Utils

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DeleteConfirmationDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (!isVisible) return

    val colorScheme = MaterialTheme.colorScheme

    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,

        shape = RoundedCornerShape(20.dp),

        containerColor = colorScheme.surface,
        tonalElevation = 6.dp,

        title = {
            Text(
                text = "Delete Motivation",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = colorScheme.onSurface
            )
        },

        text = {
            Text(
                text = "Are you sure you want to delete this motivation? This action cannot be undone.",
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )
        },

        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = colorScheme.error
                )
            ) {
                Text(
                    "Delete",
                    fontWeight = FontWeight.Bold
                )
            }
        },

        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = colorScheme.primary
                )
            ) {
                Text("Cancel")
            }
        }
    )
}