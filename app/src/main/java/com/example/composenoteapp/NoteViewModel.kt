package com.example.composenoteapp

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class NoteViewModel(private val dao: NoteDao)
    : ViewModel(){
    val notes = mutableStateListOf<Note>()

    fun deleteNote(note: Note){
        notes.remove(note)
        dao.delete(note)
    }

    fun addNote(note: Note)
    {
        notes.add(note)

        dao.insert(note)
    }

    fun initializeNoteList(note : Note)
    {
        notes.add(note)
    }

    fun getNotes() :List<Note>
    {
        val list = dao.retrieveAllNotes()

        return if (list.isEmpty()) emptyList()
        else
            return list
    }

    fun updateNote(note : Note){
        dao.update(note)
    }


}

