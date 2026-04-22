package com.example.aimotivationapp.Screens.HomeScreens.Utils

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aimotivationapp.Screens.HomeScreens.MotivationTone
import com.example.aimotivationapp.UploadBackend.Motivation
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun MotivationCard(motivation: Motivation) {
    val colorScheme = MaterialTheme.colorScheme
    val context = LocalContext.current

    // TONE COLOUR Section
    val accentContainer = when (motivation.tone) {

        ToneColor.Friendly.toString() -> colorScheme.tertiaryContainer
        ToneColor.Encouraging.toString() -> colorScheme.secondaryContainer
        ToneColor.Uplifting.toString() -> colorScheme.primaryContainer
        ToneColor.Positive.toString() -> colorScheme.errorContainer
        ToneColor.Motivational.toString() -> colorScheme.surfaceVariant

        else -> colorScheme.surfaceVariant
    }

    val accentContent = when (motivation.tone) {

        ToneColor.Friendly.toString() -> colorScheme.onTertiaryContainer
        ToneColor.Encouraging.toString() -> colorScheme.onSecondaryContainer
        ToneColor.Uplifting.toString() -> colorScheme.onPrimaryContainer
        ToneColor.Positive.toString() -> colorScheme.onErrorContainer
        ToneColor.Motivational.toString() -> colorScheme.onSurfaceVariant

        else -> colorScheme.onSurfaceVariant
    }
    val accentPrimary = when (motivation.tone) {
        ToneColor.Friendly.toString() -> colorScheme.tertiaryContainer
        ToneColor.Encouraging.toString() -> colorScheme.secondaryContainer
        ToneColor.Uplifting.toString() -> colorScheme.primaryContainer
        ToneColor.Positive.toString() -> colorScheme.errorContainer
        ToneColor.Motivational.toString() -> colorScheme.onTertiaryContainer
        else -> {
            colorScheme.surfaceVariant

        }
    }


    // Share intent helper — opens the system share sheet (WhatsApp, Gmail, etc.)
    fun shareMotivation() {
        val shareText = buildString {
            append("${mapToneToEmoji(motivation.tone)} ${motivation.tone} \n\n")
            append("\"${motivation.message}\"\n\n")
            append("— Sent via Ai Motivation App")
        }
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        context.startActivity(Intent.createChooser(intent, "Share motivation via…"))
    }

//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 20.dp),
//        shape = RoundedCornerShape(24.dp),
//        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
//        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
//    ) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .border(
                width = 1.2.dp,
                brush = Brush.linearGradient(
                    listOf(
                        accentPrimary.copy(alpha = 0.7f),
                        accentPrimary.copy(alpha = 0.2f)
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ){
        Column(modifier = Modifier.fillMaxWidth()) {

            // Top accent strip
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(accentPrimary, accentPrimary.copy(alpha = 0.3f)),
                        )
                    )
                    .clickable {

                        // TODO Navigate to the Create motivation screen

                    },
            )

            Column(modifier = Modifier.padding(18.dp)) {

                // Tone badge + time
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(accentContainer),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(mapToneToEmoji(motivation.tone), fontSize = 16.sp)
                        }
                        Surface(shape = RoundedCornerShape(8.dp), color = accentContainer) {
                            Text(
                                motivation.tone,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 0.3.sp,
                                ),
                                color = accentContent,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            )
                        }
                    }

                }

                Spacer(Modifier.height(14.dp))

                // Message with left accent bar
                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .width(3.dp)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(2.dp))
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        accentPrimary,
                                        accentPrimary.copy(alpha = 0.2f)
                                    ),
                                )
                            ),
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        motivation.message,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            lineHeight = 22.sp,
                            fontStyle = FontStyle.Italic,
                        ),
                        color = colorScheme.onSurface,
                    )
                }

                Spacer(Modifier.height(14.dp))

                HorizontalDivider(
                    color = colorScheme.outlineVariant.copy(alpha = 0.5f),
                    thickness = 0.8.dp,
                )
                Spacer(Modifier.height(10.dp))

                // Footer: team + sender + SHARE button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // Team + sender info
                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {


                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                        ) {


                            Text(
                                formatTime(motivation.time),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                color = colorScheme.onSurface,
                            )
                        }
                    }

                    // Share button
                    FilledTonalButton(
                        onClick = { shareMotivation() },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = accentContainer,
                            contentColor = accentContent,
                        ),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                    ) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = "Share",
                            modifier = Modifier.size(15.dp),
                        )
                        Spacer(Modifier.width(5.dp))
                        Text(
                            "Share",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        )
                    }
                }
            }
        }
    }
}

fun formatTime(timestamp: Timestamp?): String {
    return timestamp?.toDate()?.let {
        SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(it)
    } ?: ""
}

fun mapToneToEmoji(tone: String): String {
    return when (tone) {

        ToneColor.Friendly.toString() -> "😊"
        ToneColor.Encouraging.toString() -> "💪"
        ToneColor.Uplifting.toString() -> "🔥"
        ToneColor.Positive.toString() -> "⚡"
        ToneColor.Motivational.toString() -> "💡"
        else -> {
            "✨"
        }
    }
}