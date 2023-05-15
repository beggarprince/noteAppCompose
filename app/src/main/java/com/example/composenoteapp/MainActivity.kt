package com.example.composenoteapp

import android.content.ContentValues.TAG
import android.icu.text.CaseMap.Title
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.composenoteapp.ui.theme.ComposeNoteAppTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import java.time.LocalDate


lateinit var noteSavedValue: String
lateinit var noteTitle: String
lateinit var viewNote: Note
lateinit var date: String
lateinit var currentNote: Note
private const val TAG = "DAO"

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        noteTitle =""
        noteSavedValue =""
        val currentDate = LocalDate.now()
        date = currentDate.toString()
        currentNote = Note("a", date)

        val db = Room.databaseBuilder(
            applicationContext,
            NoteDatabase::class.java, "my-db"
        ).allowMainThreadQueries().build()
        val dao = db.noteDao()

        val vm by viewModels<NoteViewModel>(
            factoryProducer = {
                object : ViewModelProvider.Factory{
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        Log.d(TAG, "Creating ViewModel of type")
                        return NoteViewModel(dao) as T
                    }
                }
            }
        )

        Thread {

                val roomDbInitialList = vm.getNotes()

                for(note in roomDbInitialList)
                {
                    vm.initializeNoteList(note)
                }

        }.start()




        setContent {
            //vm.initializeNoteList(Note("First"))
            ComposeNoteAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                   MasterControl()
                //NoteView(note = Note("String"))
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
        mutableStateOf("Home")
    }
    when (control) {
        "Home" -> Home(
            modifier,
            onContinueClicked = {control = "AddNote"},
            onExpandClick = {
                note: Note ->
                currentNote = note
                control = "ViewNote"
            },
            onDeleteClick = {note: Note ->
                vm.deleteNote(note)
            },
            vm.notes
        )
        "AddNote" -> AddNote(onContinueClicked = {
            control = "Home"
            vm.addNote(Note(noteSavedValue, noteTitle, date))
            noteSavedValue = ""
            noteTitle =""
        })
        "ViewNote" -> {
            NoteView(note = currentNote,
            onUpdateNote = { note: Note,
                noteText: String,
                title: String->
                note.title = title
                note.note = noteText
                note.date = date
                vm.updateNote(note);
                control = "Home"
                           },
            onUpdateCancel = { control = "Home" }
            )
        }
    }
}


@Composable
fun Home(
    modifier: Modifier = Modifier,
    onContinueClicked: () -> Unit,
    onExpandClick: (Note) -> Unit,
    onDeleteClick: (Note) -> Unit,
    notes : SnapshotStateList<Note>
)
{
   // val vm = viewModel<NoteViewModel>()
    Surface(modifier = Modifier.fillMaxSize(),
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
                NoteItem(note = item, onExpandClick, onDeleteClick)
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
        Column(modifier = Modifier
            //.background(Color.Gray)
            ,
        verticalArrangement = Arrangement.Top) {
            Text(text="Add New Note")
            TextField(value = title, onValueChange = { title = it; noteTitle = it},
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
fun NoteItem(note: Note,
             onExpandClick: (Note) -> Unit,
             onDeleteClick: (Note) -> Unit
             )
{
    //val vm = viewModel<NoteViewModel>()
    val labmdaHandler: () -> Unit = {onExpandClick(note) }
    val deleteHandler: () -> Unit = {onDeleteClick(note)}
    val buttonClicked = remember { mutableStateOf(false)}
    if(buttonClicked.value) {
        //----------
        //Note is Expanded
        Surface() {
            Column() {
            expandView(note)
                //Temp code to view note/title until mastercontrol can view it
                Button(onClick = labmdaHandler

                ){//{ buttonClicked.value=false }) {
                    Icon(imageVector = Icons.Rounded.Check, contentDescription = null)
                }
            }
        }
    }
    //--------------
    //Note Collapsed
    else
        Surface(modifier = Modifier
     //   .background(color = Color.Gray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 8.dp)
                //.background(Color.LightGray)
                .border(1.dp, Color.Black)
            ,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            //Does NOT have a title
            if(note.title == ""){
            ClickableText(
                text = AnnotatedString(note.note),
                modifier = Modifier.weight(1f),
                onClick = {
                    buttonClicked.value = true

                }
            )
            }
            //DOES have a title
            else
            {
                ClickableText(
                    text = AnnotatedString(note.title),
                    modifier = Modifier.weight(1f),
                    onClick = {
                        buttonClicked.value = true
                    },
                    style = TextStyle(fontSize = 25.sp)
                )
            }

            //Delete Note
            Button(
                modifier = Modifier
                    .weight((0.25f))
                ,
                onClick = deleteHandler
            ) {
                Icon(imageVector = Icons.Rounded.Delete, contentDescription = null,
                    modifier = Modifier.weight(1f)
             )
            }
        }
    }
}

@Composable
fun expandView(note :Note) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                note.title,
                fontSize = 30.sp
            )
            Text(note.note)
            Text(note.date)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteView(
    note: Note,
    onUpdateNote: (Note, String, String) -> Unit,
    onUpdateCancel: () -> Unit
)
{
 var edit by remember{ mutableStateOf(false)}
    var tempTitle by remember{ mutableStateOf(note.title)}
    var tempNote by remember{ mutableStateOf(note.note)}

    val updateHandler: () -> Unit = {
        onUpdateNote(note, tempNote, tempTitle)
    }
    Surface(modifier = Modifier
        .fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            if(!edit){
            Text(note.title,
            fontSize = 30.sp)
            Text(note.note)
            Text(note.date)
            }
            else
            {
                TextField(value = tempTitle,
                    onValueChange = {tempTitle = it},
                    label = {Text("Type new note")},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                )
                TextField(value = tempNote,
                    onValueChange = {tempNote = it},
                    label = {Text("Type new note")},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                )
                //Text(note.date)
            }

        }
        Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.End) {
            //View Mode
            if (!edit) {
                Button(onClick =  onUpdateCancel ) {
                    Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                }
                Button(
                    onClick = { edit = !edit },
                ) {
                    Icon(imageVector = Icons.Rounded.Edit, contentDescription = null)
                }
            }
            //Edit Mode
            else {
                Button(onClick = { edit = !edit } ) {
                    Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                }
                Button(
                    onClick = updateHandler,
                ) {
                    Icon(imageVector = Icons.Rounded.Check, contentDescription = null)
                }
            }
        }
    }
}

@Preview
@Composable
fun NoteViewPreview()
{
    NoteView(note = Note("As a character from a medieval-fantasy setting, Flayn from Fire Emblem Three Houses may not be familiar with the concept of a fast-food restaurant like McDonald's. However, if we were to imagine her ordering at McDonald's, she may prefer something on the lighter side, as she is depicted as a gentle and delicate character.\n" +
            "\n" +
            "She might enjoy a simple cheeseburger or a Filet-O-Fish sandwich, as they are not overly heavy or greasy. She may also prefer a side of apple slices or a salad rather than French fries. For a beverage, she might choose a small milkshake or a bottled water.\n" +
            "\n" +
            "Of course, this is just speculation based on Flayn's character traits, and she may have different preferences or dietary restrictions that we are not aware of.",
         "Flayn's Mcdonald's Order","5/13/2023"),
        {note: Note, string: String, String -> {}},{})
}


@Preview
@Composable
fun NoteItemPreview()
{
    NoteItem(note = Note("This is a note","Title",""), {}, {})
}

@Preview
@Composable
fun BigAssNoteItemPreview()
{
    NoteItem(Note("This is a note with a shit ton of text on it." +
            "Ideally the app looks just as beautiful as when the note is a entire paragraph" +
            "Either way, my eyes are burning due to the light mode, but I can't read for shit" +
            "and have to set the brightness high af on dark mode. Lord Help me. DROP DATABASE")
    ,{}, {})
}

@Preview
@Composable
fun HomePreview()
{

    Home(modifier = Modifier,
        {

        },
        {},{},
        SnapshotStateList<Note>()
    )
}

@Preview
@Composable
fun AddNotePreview()
{

    AddNote(modifier = Modifier,
        {})
}

