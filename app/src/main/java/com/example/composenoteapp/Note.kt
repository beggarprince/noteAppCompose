package com.example.composenoteapp

import androidx.compose.runtime.mutableStateOf
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NotesDB")
data class Note (
     val note: String ="",
    val picture: Int =0,
    val title: String ="",
     val date: String ="",
     @PrimaryKey(autoGenerate = true)
     val id:Int? = null,
        )
{
    constructor(note: String, date:String): this(note, -1, "", date)
    constructor(note: String, title:String, date:String): this(note, -1, title, date)

}