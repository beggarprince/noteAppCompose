package com.example.composenoteapp.note

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composenoteapp.ExpandedView

@Composable
fun NoteItem(
    note: Note,
    onExpandClick: (Note) -> Unit,
    onDeleteClick: (Note) -> Unit
) {
    val buttonClicked = remember { mutableStateOf(false) }
    if (buttonClicked.value) {
        //----------
        //Note is Expanded
        Surface(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Column() {
                ExpandedView(note,
                    shrinkText = {
                        buttonClicked.value = false
                    },
                    openViewer = {
                        onExpandClick(note)
                    })
            }
        }
    }
    //--------------
    //Note Collapsed
    else
        Surface() {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 8.dp)
                    .border(1.dp, Color.Black),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                //Does NOT have a title
                if (note.title == "") {
                    ClickableText(
                        text = AnnotatedString(note.note),
                        modifier = Modifier.weight(1f),
                        onClick = {
                            buttonClicked.value = true

                        }
                    )
                }
                //DOES have a title
                else {
                    ClickableText(
                        text = AnnotatedString(note.title),
                        modifier = Modifier.weight(1f),
                        onClick = {
                            buttonClicked.value = true
                        },
                        style = TextStyle(fontSize = 25.sp)
                    )
                }
                Icon(imageVector = Icons.Rounded.ArrowDropDown, contentDescription = null)
            }
        }
}

@Preview
@Composable
fun NoteItemPreview() {
    NoteItem(note = Note("This is a note", "Title", ""), {}, {})
}

@Preview
@Composable
fun BigAssNoteItemPreview() {
    NoteItem(
        Note(
            "This is a note with a ton of text on it." +
                    "Ideally the app looks just as beautiful as when the note is a entire paragraph" +
                    "ladjlasfjeijalejfl adl akdjfaifakdjflakdjf aei jlkadfjaoie jkadfjlaiejfklsjfaljdfjla" +
                    "lkajdfla jdlksjfla jfsdjfk jsdlsjfdkajfsifdjskdfjaldij kdlsdjfslidjfkasld;fjs laifjdklajdflsa jsildfj"
        ), {}, {})
}
