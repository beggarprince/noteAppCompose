package com.example.composenoteapp

import androidx.compose.runtime.mutableStateOf
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NotesDB")
data class Note (
    @PrimaryKey val note: String,
    val picture: Int,
    val title: String
        )
{
    constructor(note: String): this(note, -1, "")
    constructor(note: String, title:String): this(note, -1, title)

}