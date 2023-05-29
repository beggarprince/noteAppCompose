package com.example.composenoteapp

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.composenoteapp.note.Note

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteView(
    note: Note,
    onUpdateNote: (Note, String, String) -> Unit,
    onUpdateCancel: () -> Unit,
    onDeleteClick: (Note) -> Unit
) {
    var edit by remember { mutableStateOf(false) }
    var tempTitle by remember { mutableStateOf(note.title) }
    var tempNote by remember { mutableStateOf(note.note) }
    var  dialogShown by remember { mutableStateOf(false)}

    if(!edit) BackHandler(enabled = true , onUpdateCancel)
    else if(edit) BackHandler(enabled = true){edit = !edit}
    val updateHandler: () -> Unit = {
        onUpdateNote(note, tempNote, tempTitle)
    }

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

if(dialogShown) {
    Dialog(onDismissRequest = { dialogShown = false }) {

        Box(modifier = Modifier.background(Color.White),
        ) {
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 8.dp)
                    .defaultMinSize(minHeight = 50.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Delete Note?")
                Row(modifier = Modifier) {
                    Button(onClick =  {dialogShown = false} ) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = null)
                    }

                    Button(onClick = { onDeleteClick(note)
                    onUpdateCancel()}
                    ) {
                        Icon(Icons.Rounded.Check, contentDescription = null)
                    }
                }
            }
        }
    }
}
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(gradient)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (!edit) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)

                )
                Text(
                    text = note.note,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
                        .padding(16.dp)
                        .fillMaxWidth()
                )
                Text(
                    text = note.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
                        .padding(8.dp)
                        .fillMaxWidth()
                )
                Text(
                    text = "TAG: " + note.tag,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
                        .padding(8.dp)
                        .fillMaxWidth()
                )
            }

            else {
                TextField(
                    value = tempTitle,
                    onValueChange = { tempTitle = it },
                    label = { Text("Type new note title") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .heightIn(max = 500.dp)
                    ,
                    colors = TextFieldDefaults.textFieldColors(
                        //background = MaterialTheme.colorScheme.surface,
                        textColor = MaterialTheme.colorScheme.onSurface,
                        disabledTextColor = Color.Gray,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = Color.Gray
                    )
                )
                TextField(
                    value = tempNote,
                    onValueChange = { tempNote = it },
                    label = { Text("Type new note text") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        //backgroundColor = MaterialTheme.colorScheme.surface,
                        textColor = MaterialTheme.colorScheme.onSurface,
                        disabledTextColor = Color.Gray,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = Color.Gray
                    )
                )
            }
            Row(
                modifier = Modifier
                    //.padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .background(gradientFinal)
                ,
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.End
            ) {
                val buttonModifier = Modifier.padding(end = 8.dp)

                if (!edit) {
                    OutlinedButton(
                        onClick = {
//                             onDeleteClick(note)
//                        onUpdateCancel()
                                  dialogShown = true
                                  }
                        ,
                        modifier = buttonModifier
                    ) {
                        Icon(
                            imageVector = Icons.Rounded
                                .Delete,
                            contentDescription = "Delete"
                        )
                        Text("Delete")
                    }
                    OutlinedButton(
                        onClick = { edit = !edit },
                        modifier = buttonModifier
                    ) {
                        Icon(
                            imageVector = Icons.Rounded
                                .Edit,
                            contentDescription = "Edit"
                        )
                        Text("Edit")
                    }
                }
                else {
                    OutlinedButton(
                        onClick = updateHandler,
                        modifier = buttonModifier
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Check,
                            contentDescription = "Save"
                        )
                        Text("Save")
                    }
                }
            }


        }


    }
}


@Preview
@Composable
fun NoteViewPreview()
{
    NoteView(note = Note("This is a note\n" +
            "\n" +
            "This is another paragraph\n" +
            "\n" +
            "lakjdlsfjlsjfsldjfs isjdalkdjfijf kj",
        "NoteView() Preview","5/13/2023" ,"Unspecified"),
        { note: Note, string: String, String -> {}},{}, {})
}


