package com.example.aimotivationapp.Screens.HomeScreens.Utils

import android.content.Intent
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

//import androidx.compose.ui.unit.sp
//
//


@Composable
fun MotivationCard(motivation: RecentMotivation) {
    val colorScheme = MaterialTheme.colorScheme
    val context = LocalContext.current

    // TONE COLOUR Section
    val accentContainer = when (motivation.toneColor) {
        ToneColor.Friendly -> colorScheme.tertiaryContainer
        ToneColor.Encouraging -> colorScheme.secondaryContainer
        ToneColor.Inspiring -> colorScheme.primaryContainer
        ToneColor.Bold -> colorScheme.errorContainer
    }
    val accentContent = when (motivation.toneColor) {
        ToneColor.Friendly -> colorScheme.onTertiaryContainer
        ToneColor.Encouraging -> colorScheme.onSecondaryContainer
        ToneColor.Inspiring -> colorScheme.onPrimaryContainer
        ToneColor.Bold -> colorScheme.onErrorContainer
    }
    val accentPrimary = when (motivation.toneColor) {
        ToneColor.Friendly -> colorScheme.tertiary
        ToneColor.Encouraging -> colorScheme.secondary
        ToneColor.Inspiring -> colorScheme.primary
        ToneColor.Bold -> colorScheme.error
    }

    // Share intent helper — opens the system share sheet (WhatsApp, Gmail, etc.)
    fun shareMotivation() {
        val shareText = buildString {
            append("${motivation.emoji} ${motivation.tone} message for ${motivation.team}\n\n")
            append("\"${motivation.message}\"\n\n")
            append("— Sent via TeamLead App")
        }
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
            putExtra(Intent.EXTRA_SUBJECT, "Motivation for ${motivation.team}")
        }
        context.startActivity(Intent.createChooser(intent, "Share motivation via…"))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
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
                    ),
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
                            Text(motivation.emoji, fontSize = 16.sp)
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
                    Text(
                        motivation.timeAgo,
                        style = MaterialTheme.typography.labelSmall,
                        color = colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    )
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
                                    colors = listOf(accentPrimary, accentPrimary.copy(alpha = 0.2f)),
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
                            Icon(Icons.Outlined.Group, null, modifier = Modifier.size(13.dp), tint = colorScheme.onSurfaceVariant)
                            Text(
                                motivation.team,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                                color = colorScheme.onSurfaceVariant,
                            )
                        }


//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.spacedBy(4.dp),
//                        ) {
//                            Box(
//                                modifier = Modifier
//                                    .size(16.dp)
//                                    .clip(CircleShape)
//                                    .background(accentPrimary.copy(alpha = 0.15f)),
//                                contentAlignment = Alignment.Center,
//                            ) {
//                                Icon(Icons.Outlined.Person, null, modifier = Modifier.size(10.dp), tint = accentPrimary)
//                            }
//                            Text(
//                                "Rohan Rajesh",
//                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
//                                color = accentPrimary,
//                            )
//                        }
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


@Composable
fun ToneChip(
    tone: MotivationTone,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor =
        if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
    val contentColor =
        if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
    val borderMod = if (selected) Modifier.border(
        1.5.dp,
        MaterialTheme.colorScheme.primary,
        RoundedCornerShape(14.dp)
    ) else Modifier

    Surface(
        modifier = modifier
            .then(borderMod)
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        color = containerColor,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(tone.emoji, fontSize = 22.sp)
            Spacer(Modifier.height(4.dp))
            Text(
                tone.label,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                color = contentColor,
                textAlign = TextAlign.Center
            )
        }
    }
}
