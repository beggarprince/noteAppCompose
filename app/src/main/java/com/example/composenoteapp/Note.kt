package com.example.composenoteapp

import androidx.compose.runtime.mutableStateOf

data class Note (
    val note: String,
    val picture: Int,
    val title: String
        )
{
    constructor(note: String): this(note, -1, "")
    constructor(note: String, title:String): this(note, -1, title)

}