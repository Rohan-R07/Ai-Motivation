package com.example.aimotivationapp.Screens.GenerateMotivation

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aimotivationapp.Screens.AuthScreens.LoginScreen
import kotlin.math.log

// ─── Data ────────────────────────────────────────────────────────────────────

private val toneOptions = listOf("Motivational", "Encouraging", "Positive", "Uplifting", "Friendly")
private val purposeOptions = listOf("Welcome", "Remainder", "Appreciation", "Push", "Team Reset")
private val lengthOptions = listOf("Short", "Medium", "Long")

// ─── Screen ──────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMotivationScreen(
    onBack: () -> Unit = {},
//    onGenerate: (tone: String, purpose: String, length: String, context: String) -> Unit = { _, _, _, _ -> }
) {

    // initilizing Ai View Model
    val aiViewModel: AiViewModel = viewModel()


    var selectedTone by remember { mutableStateOf<String?>(null) }
    var selectedPurpose by remember { mutableStateOf<String?>(null) }
    var selectedLength by remember { mutableStateOf<String?>(null) }
    var contextText by remember { mutableStateOf("") }

    val canGenerate = selectedTone != null && selectedPurpose != null && selectedLength != null

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Create Motivation",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val horizontalPadding = when {
                maxWidth > 600.dp -> maxWidth * 0.1f   // tablet / large screen
                else -> 16.dp
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = horizontalPadding)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Spacer(modifier = Modifier.height(8.dp))

                // ── Tone ──────────────────────────────────────────────────
                SectionLabel(title = "Tone")
                ChipGroup(
                    options = toneOptions,
                    selected = selectedTone,
                    onSelect = { selectedTone = it }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // ── Purpose ───────────────────────────────────────────────
                SectionLabel(title = "Purpose")
                ChipGroup(
                    options = purposeOptions,
                    selected = selectedPurpose,
                    onSelect = { selectedPurpose = it }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // ── Length ────────────────────────────────────────────────
                SectionLabel(title = "Length")
                ChipGroup(
                    options = lengthOptions,
                    selected = selectedLength,
                    onSelect = { selectedLength = it }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // ── Context text field ────────────────────────────────────
                ContextInputField(
                    value = contextText,
                    onValueChange = { contextText = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Prompting

//                val prompting = """
//                    generate a motivation quote to cheir my team members in a firendly way
//                """.trimIndent()

                val prompt = """
                    Generate 5 team motivation messages.

                        Tone: $selectedTone  
                        Purpose: $selectedPurpose
                        Length: $selectedLength
                        Context: $contextText
                        
                        Rules:
                        - Return ONLY JSON
                        - Format must be: {"motivations": ["...", "..."]}
                        - No explanation, no extra text
                        - Each item must be unique
                        - Follow tone, purpose and length strictly
                    """.trimIndent()

                // ── Generate button ───────────────────────────────────────
                GenerateButton(
                    enabled = canGenerate,
                    onClick = {
//                        aiViewModel.generateMotivation(prompt)
//                        if (aiViewModel.uiState.isNotEmpty()) {
//                            val lists = parseMotivations(aiViewModel.uiState)
//                            lists.forEach { item ->
//                                // TODO to add motivation quotes
//                                Log.d("AIViewModel", item.toString())
//                            }
//                        }

                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

// ─── Section Label ────────────────────────────────────────────────────────────

@Composable
private fun SectionLabel(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        ),
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

// ─── Chip Group ───────────────────────────────────────────────────────────────

@Composable
private fun ChipGroup(
    options: List<String>,
    selected: String?,
    onSelect: (String) -> Unit
) {
    // Wrap chips with a simple flow-like layout using FlowRow
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        options.forEach { option ->
            SelectableChip(
                label = option,
                isSelected = option == selected,
                onClick = { onSelect(option) }
            )
        }
    }
}

// ─── Selectable Chip ──────────────────────────────────────────────────────────

@Composable
private fun SelectableChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.surfaceVariant,
        animationSpec = tween(durationMillis = 250),
        label = "chipBackground"
    )

    val contentColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary
        else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = tween(durationMillis = 250),
        label = "chipContent"
    )

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = backgroundColor,
        tonalElevation = if (isSelected) 0.dp else 1.dp,
        shadowElevation = if (isSelected) 4.dp else 0.dp,
        modifier = Modifier.wrapContentSize()
    ) {
        Text(
            text = label,
            color = contentColor,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
        )
    }
}

// ─── Context Input Field ──────────────────────────────────────────────────────

@Composable
private fun ContextInputField(
    value: String,
    onValueChange: (String) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 130.dp)
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = "Context For Ai (optional)",
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 130.dp),
            maxLines = 8
        )
    }
}

// ─── Generate Button ──────────────────────────────────────────────────────────

@Composable
private fun GenerateButton(
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,

            ),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Text(
            text = "Generate Motivation",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            letterSpacing = 0.5.sp
        )
    }
}