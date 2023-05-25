package com.example.composenoteapp.Room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.composenoteapp.note.Note

@Database(entities = arrayOf(Note::class), version = 1)
abstract class NoteDatabase: RoomDatabase(){
    abstract fun noteDao(): NoteDao
}
