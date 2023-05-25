package com.example.composenoteapp

import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNote(
    modifier: Modifier = Modifier,
    onContinueClicked: () -> Unit,
    onCanceledClick: () -> Unit
)
{
    var temp by remember { mutableStateOf("") }
    var title by remember { mutableStateOf(("")) }
    var tag by remember { mutableStateOf("") }

    BackHandler(enabled = true , onCanceledClick)

    Surface(modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier
            .verticalScroll(rememberScrollState())
            ,
            verticalArrangement = Arrangement.Top) {
            Text(text="Add New Note")
            TextField(value = title, onValueChange = { title = it
                noteTitle = it},
                label = { Text("Enter Title") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )
            TextField(value = temp,
                onValueChange = {temp = it; noteSavedValue =it},
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
                onValueChange = {tag = it
                    noteTag = it}
            )
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.End) {
                Button(onClick = onCanceledClick,
                    modifier = Modifier
                ) {
                    Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null, modifier = Modifier)
                }
                Button(onClick = onContinueClicked,
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
fun AddNotePreview()
{

    AddNote(modifier = Modifier,
        {},{})
}