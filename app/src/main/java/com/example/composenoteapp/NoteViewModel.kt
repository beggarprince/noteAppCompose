package com.example.composenoteapp

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class NoteViewModel : ViewModel(){

    val notes = mutableStateListOf<Note>()

    fun deleteNote(note: Note){
        notes.remove(note)
    }

    fun addNote(note: Note)
    {
        notes.add(note)
    }

}