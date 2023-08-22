package com.example.composenoteapp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
lateinit  var date: String

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentDate = LocalDate.now()
        date = currentDate.toString()
        var currentNote = Note("a", date, "")

        val db = Room.databaseBuilder(
            applicationContext,
            NoteDatabase::class.java, "my-db"
        ).allowMainThreadQueries()
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


                    val listInit = vm.noteQuery(NoteViewModel.QueryType.NEWEST)
                    vm.notes.clear()
                    for (l in listInit) vm.initializeNoteList(l)

                    val roomDbTags = vm.getTags()

                    var control by rememberSaveable {
                        mutableStateOf("Home")
                    }
                    when (control) {
                        "Home" -> Home(
                            addNote = { control = "AddNote" },
                            expandedView = {
                                    note: Note ->
                               currentNote = note
                                control = "ViewNote"
                            },
                            onDeleteClick = { note: Note ->
                                vm.deleteNote(note)
                            },
                            returnByTag = { tag: String ->
                                val queryEnum: NoteViewModel.QueryType = try {
                                        NoteViewModel.QueryType.valueOf(tag)
                                }catch (e: IllegalArgumentException){
                                    NoteViewModel.QueryType.TAG
                                }

                                val list = vm.noteQuery(queryEnum, tag)

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

                        "AddNote" -> AddNote(
                            saveNote = {
                            control = "Home"

                            if (currentNote.note != "") { //There is in fact a note
                                if (currentNote.title == "") {
                                    currentNote.title = vm.titleCreate(currentNote.note)
                                }
                                val newNote = Note(
                                    currentNote.note,
                                    currentNote.title,
                                    date,
                                    currentNote.tag)
                                 vm.addNote(newNote)

                                Toast.makeText(applicationContext, R.string.notesuccesful , Toast.LENGTH_SHORT).show();
                            }
                            else{
                                //note not added Toast
                                Toast.makeText(applicationContext, R.string.noteunsuccesful , Toast.LENGTH_SHORT).show();

                            }
                            currentNote = Note()
                        },
                            onCanceledClick = {
                                control = "Home"
                            },
                            note = currentNote
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

                                    Toast.makeText(applicationContext, R.string.notedeleted , Toast.LENGTH_SHORT).show();
                                }
                            )
                        }
                    }

                }
            }
        }//setContent
    }

}



