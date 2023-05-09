package com.example.composenoteapp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.composenoteapp.ui.theme.ComposeNoteAppTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview


var notes = mutableListOf<String>()
lateinit var noteSavedValue: String
var notesTemp = listOf("apple", "banana","orange")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            notes.add("First")
            ComposeNoteAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                   masterControl()
                }
            }
        }
    }
}

@Composable
fun masterControl(modifier: Modifier = Modifier)
{
    var control by rememberSaveable {
        mutableStateOf(true)
    }
    if(control) Home(onContinueClicked = {control = false})
    else AddNote(onContinueClicked = {control = true
        addToList(noteSavedValue)
        noteSavedValue = ""
    })
}


@Composable
fun Home(
    modifier: Modifier = Modifier,
    onContinueClicked: () -> Unit
)
{

    Surface(modifier = Modifier,
    color = MaterialTheme.colorScheme.background) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center) {
            LazyColumn(modifier= Modifier.padding(vertical = 2.dp),)
        {
            items(items = notes){
                    item ->
                Text(item)
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
    Surface(modifier = Modifier.fillMaxSize()
        ) {
        Column(modifier = Modifier,
        verticalArrangement = Arrangement.Top) {
            Text(text="Add New Note")
            TextField(value = temp,
                onValueChange = {temp = it; noteSavedValue =it},
                label = {Text("Type new note")}
                )
                  Button(onClick = onContinueClicked,
            modifier = Modifier
            ) {
            Icon(imageVector = Icons.Rounded.Check,  contentDescription =null )
            }
        }

    }
}

fun addToList(string: String)
{
   notes.add(string)
    Log.d(TAG, "$string was added")
}

@Preview
@Composable
fun HomePreview()
{
    Home(modifier = Modifier,
        {Log.d(TAG, "Preview")}
    )
}
