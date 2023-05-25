package com.example.composenoteapp.Room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.composenoteapp.note.Note

@Dao
interface NoteDao {
    @Insert
    fun insert(note: Note)

    @Update
    fun update(note: Note)

    @Delete
    fun delete(note: Note)

    @Query("SELECT * FROM NotesDB")
    fun retrieveAllNotes(): List<Note>

    @Query("SELECT DISTINCT tag FROM NotesDB")
    fun retrieveUniqueTags(): List<String>

    @Query("SELECT * FROM NotesDB WHERE tag =:desiredTag")
    fun getNotesByTag(desiredTag: String): List<Note>

    @Query("Select * FROM NotesDB ORDER BY CASE WHEN title = '' THEN 1 ELSE 0 END, title ASC")
    fun getNotesAlphabetically(): List<Note>

    @Query("SELECT * FROM NotesDB ORDER BY date DESC")
    fun getNotesByDateNewest(): List<Note>

    @Query("SELECT * FROM NotesDB WHERE note LIKE '%' || :searchText || '%' OR  title LIKE '%' || :searchText || '%' ")
    fun searchText(searchText : String): List<Note>
}