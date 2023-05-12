package com.example.composenoteapp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.composenoteapp.ui.theme.ComposeNoteAppTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel


lateinit var noteSavedValue: String
lateinit var noteTitle: String

class MainActivity : ComponentActivity() {

    private val vm by viewModels<NoteViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tempNote =listOf<Note>(Note("A"), Note("B"),
            Note("In this example, we define a Person class with a name and age property. Then, we create a list of three Person objects using the listOf function, with each object having a name and age. Finally, we print the list of people to the console using println()."))
        for(note in tempNote)
        {
            vm.addNote(note)
        }

        setContent {
            vm.addNote(Note("First"))
            ComposeNoteAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                   MasterControl()
                }
            }
        }
    }
}

@Composable
fun MasterControl(modifier: Modifier = Modifier)
{
    val vm = viewModel<NoteViewModel>()
    var control by rememberSaveable {
        mutableStateOf(true)
    }
    if(control) Home(onContinueClicked = {control = false})
    else AddNote(onContinueClicked = {
        control = true
        vm.addNote(Note(noteSavedValue))
        noteSavedValue = ""
    })
}


@Composable
fun Home(
    modifier: Modifier = Modifier,
    onContinueClicked: () -> Unit
)
{
    val vm = viewModel<NoteViewModel>()

    Surface(modifier = Modifier,
    color = MaterialTheme.colorScheme.background) {

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center) {
            LazyColumn(modifier= Modifier.padding(vertical = 2.dp),)
        {
            items(items = vm.notes){
                    item ->
                NoteItem(note = item)
            }
        }
        }

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(4.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.End,
    ) {
        Button(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Rounded.Info, contentDescription =null )
        }
        Button(
            onClick = onContinueClicked,
            modifier = Modifier,
        ) {
            Icon(imageVector = Icons.Rounded.Create, contentDescription = null)
        }
    }}
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNote(
    modifier:Modifier = Modifier,
    onContinueClicked: () -> Unit
)
{
    var temp by remember { mutableStateOf("")}
    var title by remember { mutableStateOf((""))}

    Surface(modifier = Modifier.fillMaxSize()
        ) {
        Column(modifier = Modifier,
        verticalArrangement = Arrangement.Top) {
            Text(text="Add New Note")
            TextField(value = title, onValueChange = {title = it},
                label = {Text("Enter Title") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                )
            TextField(value = temp,
                onValueChange = {temp = it; noteSavedValue =it},
                label = {Text("Type new note")},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    //.fillMaxHeight()
                )
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.End) {
                Button(onClick = { /*TODO*/ },
                    modifier = Modifier) {
                    Icon(imageVector = Icons.Rounded.Phone, contentDescription = null, modifier = Modifier)
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

@Composable
fun NoteItem( note: Note)
{
    val vm = viewModel<NoteViewModel>()
    Surface(modifier = Modifier
     //   .background(color = Color.Gray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 8.dp)
            ,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = note.note,
            modifier = Modifier.weight(1f)
            )
            Button(
                modifier = Modifier
                    .weight((0.4f))
                ,
                onClick = {
                vm.deleteNote(note)
            }) {
                Icon(imageVector = Icons.Rounded.Delete, contentDescription = null,
                    //modifier = Modifier.weight(1f)
             )
            }
        }
    }
}

@Preview
@Composable
fun NoteItemPreview()
{
    NoteItem(Note("This is a note"))
}


@Preview
@Composable
fun HomePreview()
{
    Home(modifier = Modifier,
        {

        }
    )
}

@Preview
@Composable
fun AddNotePreview()
{
    AddNote(modifier = Modifier,
        {})
}
