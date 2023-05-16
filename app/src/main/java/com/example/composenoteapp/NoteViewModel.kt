package com.example.composenoteapp

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class NoteViewModel(private val dao: NoteDao)
    : ViewModel(){
    var notes = mutableStateListOf<Note>()

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

    fun getNotesAlphabetically(): List<Note>
    {
        val list = dao.getNotesAlphabetically()
        return if(list.isEmpty()) emptyList()
        else return list
    }

    fun getNotesNewest(): List<Note>{
        val list = dao.getNotesByDateNewest()
        return if(list.isEmpty()) emptyList()
        else return list
    }

    fun updateNote(note : Note){
        dao.update(note)
    }

    fun getTags() :List<String>
    {
        val list = dao.retrieveUniqueTags()
        return if(list.isEmpty()) emptyList()
        else return list
    }

    fun getNotesByTags(tag :String) :List<Note>
    {
        val list = dao.getNotesByTag(tag)
        return if(list.isEmpty()) emptyList()
        else return list
    }

}

