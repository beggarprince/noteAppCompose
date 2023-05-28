package com.example.composenoteapp.note

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.composenoteapp.Room.NoteDao

class NoteViewModel(private val dao: NoteDao)
    : ViewModel(){
    var notes = mutableStateListOf<Note>()
    var init = false
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
        init = true
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

    fun titleCreate(note: String): String{
        val length = 12
        val title = note.substring(0,length) +"..."
        Log.d(TAG, title)
        return title;
    }

    fun search(text: String): List<Note>{
        val list = dao.searchText(text)
        return if(list.isEmpty()) emptyList()
        else return list
    }
}



