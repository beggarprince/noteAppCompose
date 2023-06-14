package com.example.composenoteapp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.example.composenoteapp.ui.theme.ComposeNoteAppTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import java.time.LocalDate
import com.example.composenoteapp.Room.NoteDatabase
import com.example.composenoteapp.note.Note
import com.example.composenoteapp.note.NoteViewModel

//Can be replaced with lambda
var noteSavedValue: String = ""
var noteTitle: String = ""
lateinit var noteTag: String
lateinit var date: String


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentDate = LocalDate.now()
        date = currentDate.toString()
        var currentNote = Note("a", date, "")

        val db = Room.databaseBuilder(
            applicationContext,
            NoteDatabase::class.java, "my-db"
        )
            .allowMainThreadQueries()
            .build()

        val dao = db.noteDao()
        val vm by viewModels<NoteViewModel>(
            factoryProducer = {
                object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        Log.d(TAG, "Creating ViewModel of type NoteViewModel")
                        return NoteViewModel(dao) as T
                    }
                }
            }
        )

        setContent {
            ComposeNoteAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {


                    val listInit = vm.getNotesNewest()
                    vm.notes.clear()
                    for (l in listInit) vm.initializeNoteList(l)

                    val roomDbTags = vm.getTags()

                    var control by rememberSaveable {
                        mutableStateOf("Home")
                    }
                    when (control) {
                        "Home" -> Home(
                            onContinueClicked = { control = "AddNote" },
                            onExpandClick = { note: Note ->
                                currentNote = note
                                control = "ViewNote"
                            },
                            onDeleteClick = { note: Note ->
                                vm.deleteNote(note)
                            },
                            returnByTag = { tag: String ->
                                var list = emptyList<Note>()
                                if (tag == "newestOverride") list = vm.getNotesNewest()
                                else if (tag == "alphaOverride") list = vm.getNotesAlphabetically()
                                else list = vm.getNotesByTags(tag)

                                vm.notes.clear()
                                for (l in list) vm.notes.add(l)

                            },
                            textSearch = { text: String ->
                                val list = vm.search(text)
                                vm.notes.clear()
                                for (l in list) {
                                    vm.notes.add(l)
                                }
                            },
                            vm.notes,
                            roomDbTags
                        )

                        "AddNote" -> AddNote(onContinueClicked = {
                            control = "Home"
                            if (noteSavedValue != "") {
                                if (noteTitle == "") vm.addNote(
                                    Note(
                                        noteSavedValue,
                                        vm.titleCreate(noteSavedValue),
                                        date,
                                        noteTag
                                    )
                                )
                                else vm.addNote(Note(noteSavedValue, noteTitle, date, noteTag))
                            }
                            noteSavedValue = ""
                            noteTitle = ""
                        },
                            onCanceledClick = {
                                control = "Home"
                            }
                        )

                        "ViewNote" -> {
                            NoteView(note = currentNote,
                                onUpdateNote = { note: Note,
                                                 noteText: String,
                                                 title: String ->
                                    note.title = title
                                    note.note = noteText
                                    note.date = date
                                    vm.updateNote(note)
                                    control = "Home"
                                },
                                onUpdateCancel = { control = "Home" },
                                onDeleteClick = { note: Note ->
                                    vm.deleteNote(note)
                                }
                            )
                        }
                    }

                }
            }
        }//setContent
    }

}



