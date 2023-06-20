package com.example.composenoteapp

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNote(
    modifier: Modifier = Modifier,
    onContinueClicked: () -> Unit,
    onCanceledClick: () -> Unit
) {
    var temp by remember { mutableStateOf("") }
    var title by remember { mutableStateOf(("")) }
    var tag by remember { mutableStateOf("") }

    BackHandler(enabled = true, onCanceledClick)

    val gradient = Brush.verticalGradient(
        0.0f to MaterialTheme.colorScheme.primary.copy(alpha = 0.95f),
        0.5f to MaterialTheme.colorScheme.secondary,
        1.0f to MaterialTheme.colorScheme.background
    )
    val gradientFinal = MaterialTheme.colorScheme.background.copy(alpha = .8f)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground

    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .background(gradient),
            verticalArrangement = Arrangement.Top
        ) {
            Text(text = "Add New Note")
            TextField(
                value = title, onValueChange = {
                    title = it
                    noteTitle = it
                },
                label = { Text("Enter Title") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )
            TextField(
                value = temp,
                onValueChange = { temp = it; noteSavedValue = it },
                label = { Text("Type new note") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                value = tag,
                label = { Text("Optional Tag") },
                onValueChange = {
                    tag = it
                    noteTag = it
                }
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .background(gradientFinal),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = onCanceledClick,
                    modifier = Modifier
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier
                    )
                }


                OutlinedButton(
                    onClick = onContinueClicked,
                    modifier = Modifier,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Check, contentDescription = null,
                        modifier = Modifier
                    )
                }
            }
        }

    }

}

@Preview
@Composable
fun AddNotePreview() {

    AddNote(modifier = Modifier,
        {}, {})
}