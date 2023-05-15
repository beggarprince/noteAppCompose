package com.example.composenoteapp

import androidx.compose.runtime.mutableStateOf
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NotesDB")
data class Note (
     val note: String ="",
    val picture: Int =0,
    val title: String ="",
     @PrimaryKey(autoGenerate = true)
     val id:Int? = null,
        )
{
    constructor(note: String): this(note, -1, "")
    constructor(note: String, title:String): this(note, -1, title)

}