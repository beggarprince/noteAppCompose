package com.example.composenoteapp

import androidx.compose.runtime.mutableStateOf
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NotesDB")
data class Note (
     var note: String ="",
    var picture: Int =0,
    var title: String ="",
     var date: String ="",
     var tag: String ="Unspecified",
     @PrimaryKey(autoGenerate = true)
     val id:Int? = null,
        ) {
    //No Title
    constructor(note: String, date: String, tag: String) : this(note, -1, "", date, tag)

    //Title
    constructor(note: String, title: String, date: String, tag: String) : this(
        note,
        -1,
        title,
        date,
        tag
    )
}