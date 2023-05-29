package com.example.composenoteapp

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composenoteapp.note.Note

@Composable
fun ExpandedView(
    note: Note,
    shrinkText: () -> Unit,
    openViewer: () -> Unit
) {
    Surface(
        modifier = Modifier
            .wrapContentHeight()
            .border(1.dp, Color.Black),
        color = MaterialTheme.colorScheme.background
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { shrinkText() }
            ) {
                Text(
                    text = note.title,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = note.note,
                )
                Text(
                    text = note.date,
                    color = Color.Gray,
                )
            }
            Button(
                onClick = { openViewer() },
            ) {
                Icon(imageVector = Icons.Rounded.Edit, contentDescription = null)
            }
        }
    }
}


@Preview
@Composable
fun ExpandViewPreview() {

    ExpandedView(
        Note(
            "NOTE text goes here", "TITLE",
            "12/12/2001", "TAG HERE"
        ),
        {},
        {})
}