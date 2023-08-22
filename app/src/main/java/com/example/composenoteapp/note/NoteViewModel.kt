package com.example.composenoteapp.note

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.composenoteapp.Room.NoteDao

class NoteViewModel(private val dao: NoteDao) : ViewModel() {
    var notes = mutableStateListOf<Note>()
    private var init = false

    fun deleteNote(note: Note) {
        notes.remove(note)
        dao.delete(note)
    }

    fun addNote(note: Note) {
        notes.add(note)
        dao.insert(note)
    }

    fun initializeNoteList(note: Note) {
        notes.add(note)
        init = true
    }

    private fun getNotesAlphabetically(): List<Note> {
        val list = dao.getNotesAlphabetically()
        Log.d(TAG, "Alphabetical return")
        return if (list.isEmpty()) emptyList()
        else return list
    }

    private fun getNotesNewest(): List<Note> {
        val list = dao.getNotesByDateNewest()
        return if (list.isEmpty()) emptyList()
        else return list
    }

    fun updateNote(note: Note) {
        dao.update(note)
    }

    fun getTags(): List<String> {
        val list = dao.retrieveUniqueTags()
        return if (list.isEmpty()) emptyList()
        else return list
    }

    private fun getNotesByTags(tag: String): List<Note> {
        val list = dao.getNotesByTag(tag)
        return if (list.isEmpty()) emptyList()
        else return list
    }

    fun titleCreate(note: String): String {
        var length = 12
        if(length > note.length) length = note.length
        val title = note.substring(0, length) + "..."
        Log.d(TAG, title)
        return title;
    }

    fun search(text: String): List<Note> {
        val list = dao.searchText(text)
        return if (list.isEmpty()) emptyList()
        else return list
    }

    enum class QueryType {
        ALPHABETICAL,
        NEWEST,
        TAG,
    }

    fun noteQuery(queryType: QueryType, tag: String? = null): List<Note> {
        return when (queryType) {
            QueryType.ALPHABETICAL -> getNotesAlphabetically()
            QueryType.NEWEST -> getNotesNewest()
            QueryType.TAG -> {
                if (tag != null) getNotesByTags(tag)
                else emptyList()
            }
        }
    }
}



