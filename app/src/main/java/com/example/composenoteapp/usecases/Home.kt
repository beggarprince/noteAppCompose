package com.example.composenoteapp

import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composenoteapp.note.Note
import com.example.composenoteapp.note.NoteItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    onContinueClicked: () -> Unit,
    onExpandClick: (Note) -> Unit,
    onDeleteClick: (Note) -> Unit,
    returnByTag: (String) -> Unit,
    textSearch: (String) -> Unit,
    notes: SnapshotStateList<Note>,
    tags: List<String>
) {
    var isExpanded by remember { mutableStateOf(false) }
    var textSearchValue by remember { mutableStateOf("") }

    Surface(

        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            // Top row, Search and tag button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 8.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    //Menu Dropdown button
                    Button(
                        onClick = { isExpanded = !isExpanded },
                        //modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Menu,
                            contentDescription = null
                        )
                    }
                    TextField(
                        value = textSearchValue,
                        label = { Text("Search") },
                        onValueChange = { textSearchValue = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(4.dp),
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    Log.d(ContentValues.TAG, "SEARCH BUTTON CLICKED")
                                    textSearch(textSearchValue)
                                },
                            )
                            {
                                Icon(
                                    imageVector = Icons.Rounded.Search,
                                    contentDescription = null
                                )
                            }
                        },

                        )
                }

                // Dropdown Menu
                DropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false },
                    modifier = Modifier,
                ) {
                    DropdownMenuItem(
                        text = { Text("Alphabetical") },
                        onClick = { returnByTag("alphaOverride") })
                    DropdownMenuItem(
                        text = { Text("Newest") },
                        onClick = { returnByTag("newestOverride") })
                    tags.forEach { tag ->
                        DropdownMenuItem(
                            onClick = { returnByTag(tag) },
                            text = { Text(tag) }
                        )
                    }
                }
            }

            // Notes
            LazyColumn(
                modifier = Modifier.weight(1f) // Occupy remaining space
            ) {
                items(items = notes) { item ->
                    NoteItem(note = item, onExpandClick, onDeleteClick)
                }
            }

            // Bottom Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.End,
            ) {
                Button(
                    onClick = onContinueClicked,
                    modifier = Modifier,
                ) {
                    Icon(imageVector = Icons.Rounded.Create, contentDescription = null)
                }
            }

        }

    }
}

@Preview
@Composable
fun HomePreview() {
    val items = listOf("A", "B")

    Home(
        //modifier = Modifier,
        {

        },
        {}, {}, {}, {},
        SnapshotStateList<Note>(),
        items
    )
}

