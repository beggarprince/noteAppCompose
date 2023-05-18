package com.example.composenoteapp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.composenoteapp.ui.theme.ComposeNoteAppTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import java.time.LocalDate
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.IntrinsicSize.*
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.IconButton
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration


lateinit var noteSavedValue: String
lateinit var noteTitle: String
lateinit var noteTag: String
lateinit var date: String
lateinit var currentNote: Note

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        noteTitle =""
        noteSavedValue =""
        val currentDate = LocalDate.now()
        date = currentDate.toString()
        currentNote = Note("a", date, "")

        val db = Room.databaseBuilder(
            applicationContext,
            NoteDatabase::class.java, "my-db"
        ).allowMainThreadQueries().build()
        val dao = db.noteDao()

        val vm by viewModels<NoteViewModel>(
            factoryProducer = {
                object : ViewModelProvider.Factory{
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        Log.d(TAG, "Creating ViewModel of type NoteViewModel")
                        return NoteViewModel(dao) as T
                    }
                }
            }
        )

        Thread {
            if(!vm.init) {
                val roomDbInitialList = vm.getNotesNewest()
                for (note in roomDbInitialList) {
                    vm.initializeNoteList(note)
                }
            }
            vm.init = true
        }.start()

        setContent {
            ComposeNoteAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                   MasterControl()
                }
            }
        }
    }
}

@Composable
fun MasterControl(modifier: Modifier = Modifier)
{

    val vm = viewModel<NoteViewModel>()
    val roomDbTags = vm.getTags()
    for (tag in roomDbTags) Log.d(TAG, tag)

    var control by rememberSaveable {
        mutableStateOf("Home")
    }
    when (control) {
        "Home" -> Home(
            modifier,
            onContinueClicked = {control = "AddNote"},
            onExpandClick = {
                note: Note ->
                currentNote = note
                control = "ViewNote"
            },
            onDeleteClick = {note: Note ->
                vm.deleteNote(note)
            },
            returnByTag = { tag: String ->
                Log.d(TAG, "TAG: " + tag)
                var list: List<Note>
                if(tag == "newestOverride") { list = vm.getNotesNewest()}
                else {  list = vm.getNotesByTags(tag) }
                vm.notes.clear()

                for(l in list)
                {
                    vm.notes.add(l)
                }

                          },
            textSearch ={
                        text: String ->
                Log.d(TAG,"LAMBDA IS RUNNING")
                val list = vm.search(text)
                vm.notes.clear()
                for(l in list)
                {
                    vm.notes.add(l)
                }
            },
            vm.notes,
            roomDbTags
        )
        "AddNote" -> AddNote(onContinueClicked = {
            control = "Home"
            if(noteSavedValue != ""){
                if(noteTitle == "")vm.addNote(Note(noteSavedValue, vm.titleCreate(noteSavedValue), date, noteTag))
                else vm.addNote(Note(noteSavedValue, noteTitle, date, noteTag))
            }
            noteSavedValue = ""
            noteTitle =""
        },
            onCanceledClick ={
                control = "Home"
            }
        )
        "ViewNote" -> {
            NoteView(note = currentNote,
            onUpdateNote = { note: Note,
                noteText: String,
                title: String->
                note.title = title
                note.note = noteText
                note.date = date
                vm.updateNote(note)
                control = "Home"
                           },
            onUpdateCancel = { control = "Home" }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    modifier: Modifier = Modifier,
    onContinueClicked: () -> Unit,
    onExpandClick: (Note) -> Unit,
    onDeleteClick: (Note) -> Unit,
    returnByTag: (String) -> Unit,
    textSearch: (String) -> Unit,
    notes: SnapshotStateList<Note>,
    tags: List<String>
) {
    var isExpanded by remember { mutableStateOf(false) }
    var textSearchValue by remember { mutableStateOf("")}

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            // Top row
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 8.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.End
                ) {
                    TextField(
                        value = textSearchValue,
                        label = {Text("Search") },
                        onValueChange = {textSearchValue = it },
                        modifier = Modifier
                            .fillMaxWidth(.8f)
                            .shadow(4.dp),
                        trailingIcon = {
                            IconButton(onClick = { Log.d(TAG,"SEARCH BUTTON CLICKED")
                                textSearch(textSearchValue)},)
                            {
                                Icon(imageVector = Icons.Rounded.Search,
                                    contentDescription =null)
                            }
                        },

                    )

                    //Menu Dropdown button
                    Button(onClick = { isExpanded = !isExpanded }) {
                        Icon(imageVector = Icons.Rounded.Menu,
                            contentDescription = null)
                    }
                }

                // Dropdown Menu
                DropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false },
                    modifier = Modifier.align(Alignment.TopEnd),
                ) {
                    DropdownMenuItem(text = { Text("Newest")}, onClick = { returnByTag("newestOverride") })
                    tags.forEach { tag ->
                        DropdownMenuItem(
                            onClick = { returnByTag(tag) },
                            text = { Text(tag) }
                        )
                    }
                }
            }

            // Notes
            LazyColumn(
                modifier = Modifier.weight(1f) // Occupy remaining space
            ) {
                items(items = notes) { item ->
                    NoteItem(note = item, onExpandClick, onDeleteClick)
                }
            }

            // Bottom Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.End,
            ) {
                Button(
                    onClick = onContinueClicked,
                    modifier = Modifier,
                ) {
                    Icon(imageVector = Icons.Rounded.Create, contentDescription = null)
                }
            }

        }

    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNote(
    modifier:Modifier = Modifier,
    onContinueClicked: () -> Unit,
    onCanceledClick: () -> Unit
)
{
    var temp by remember { mutableStateOf("")}
    var title by remember { mutableStateOf((""))}
    var tag by remember {mutableStateOf("")}

    BackHandler(enabled = true , onCanceledClick)

    Surface(modifier = Modifier.fillMaxSize()
        ) {
        Column(modifier = Modifier
            .verticalScroll(rememberScrollState())
            ,
        verticalArrangement = Arrangement.Top) {
            Text(text="Add New Note")
            TextField(value = title, onValueChange = { title = it
                noteTitle = it},
                label = {Text("Enter Title") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                )
            TextField(value = temp,
                onValueChange = {temp = it; noteSavedValue =it},
                label = {Text("Type new note")},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                value = tag,
                label = {Text("Optional Tag")},
                onValueChange = {tag = it
                    noteTag = it}
            )
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.End) {
                Button(onClick = onCanceledClick,
                    modifier = Modifier) {
                    Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null, modifier = Modifier)
                }
                  Button(onClick = onContinueClicked,
                        modifier = Modifier,
            ) {
                      Icon(
                          imageVector = Icons.Rounded.Check, contentDescription = null,
                          modifier = Modifier
                      )
                  }
            }
        }

    }

}

@Composable
fun NoteItem(note: Note,
             onExpandClick: (Note) -> Unit,
             onDeleteClick: (Note) -> Unit
             )
{
    //val vm = viewModel<NoteViewModel>()
    val expandViewHelper: () -> Unit = {onExpandClick(note) }
    val deleteHandler: () -> Unit = {onDeleteClick(note)}
    val buttonClicked = remember { mutableStateOf(false)}
    if(buttonClicked.value) {
        //----------
        //Note is Expanded
        Surface(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Column() {
            ExpandedView(note,
                shrinkText ={ buttonClicked.value = false
                },
                openViewer = { onExpandClick(note)
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
                .border(1.dp, Color.Black)
            ,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            //Does NOT have a title
            if(note.title == ""){
            ClickableText(
                text = AnnotatedString(note.note),
                modifier = Modifier.weight(1f),
                onClick = {
                    buttonClicked.value = true

                }
            )
            }
            //DOES have a title
            else
            {
                ClickableText(
                    text = AnnotatedString(note.title),
                    modifier = Modifier.weight(1f),
                    onClick = {
                        buttonClicked.value = true
                    },
                    style = TextStyle(fontSize = 25.sp)
                )
            }

            //Delete Note
            Button(
                modifier = Modifier
                    .weight((0.25f))
                ,
                onClick = deleteHandler
            ) {
                Icon(imageVector = Icons.Rounded.Delete, contentDescription = null,
                    modifier = Modifier.weight(1f)
             )
            }
        }
    }
}

@Composable
fun ExpandedView(note: Note,
shrinkText: () -> Unit,
openViewer: () -> Unit) {
    Surface(
        modifier = Modifier
            .wrapContentHeight()
            .border(1.dp, Color.Black),
        color = MaterialTheme.colorScheme.background
    ) {
        Row(modifier = Modifier
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
            Button(onClick = { openViewer() },
            ) {
                Icon(imageVector = Icons.Rounded.Edit, contentDescription = null)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteView(
    note: Note,
    onUpdateNote: (Note, String, String) -> Unit,
    onUpdateCancel: () -> Unit
) {
    var edit by remember { mutableStateOf(false) }
    var tempTitle by remember { mutableStateOf(note.title) }
    var tempNote by remember { mutableStateOf(note.note) }

    if(!edit)BackHandler(enabled = true , onUpdateCancel)
    else if(edit) BackHandler(enabled = true){edit = !edit}
    val updateHandler: () -> Unit = {
        onUpdateNote(note, tempNote, tempTitle)
    }

    val gradient = Brush.verticalGradient(
        0.0f to MaterialTheme.colorScheme.primary.copy(alpha = 0.95f),
        0.5f to MaterialTheme.colorScheme.secondary,
        1.0f to MaterialTheme.colorScheme.background
    )
    val gradientFinal = MaterialTheme.colorScheme.background.copy(alpha = .8f)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(gradient)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (!edit) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)

                )
                Text(
                    text = note.note,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
                        .padding(16.dp)
                        .fillMaxWidth()
                )
                Text(
                    text = note.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
                        .padding(8.dp)
                        .fillMaxWidth()
                )
                Text(
                    text = "TAG: " + note.tag,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
                        .padding(8.dp)
                        .fillMaxWidth()
                )
            }

            else {
                TextField(
                    value = tempTitle,
                    onValueChange = { tempTitle = it },
                    label = { Text("Type new note title") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .heightIn(max = 500.dp)
                    ,
                    colors = TextFieldDefaults.textFieldColors(
                        //background = MaterialTheme.colorScheme.surface,
                        textColor = MaterialTheme.colorScheme.onSurface,
                        disabledTextColor = Color.Gray,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = Color.Gray
                    )
                )
                TextField(
                    value = tempNote,
                    onValueChange = { tempNote = it },
                    label = { Text("Type new note text") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        //backgroundColor = MaterialTheme.colorScheme.surface,
                        textColor = MaterialTheme.colorScheme.onSurface,
                        disabledTextColor = Color.Gray,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = Color.Gray
                    )
                )
            }
            Row(
                modifier = Modifier
                    //.padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .background(gradientFinal)
                ,
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.End
            ) {
                val buttonModifier = Modifier.padding(end = 8.dp)

                if (!edit) {
                    OutlinedButton(
                        onClick = { edit = !edit },
                        modifier = buttonModifier
                    ) {
                        Icon(
                            imageVector = Icons.Rounded
                                .Edit,
                            contentDescription = "Edit"
                        )
                        Text("Edit")
                    }
                } else {
                    OutlinedButton(
                        onClick = updateHandler,
                        modifier = buttonModifier
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Check,
                            contentDescription = "Save"
                        )
                        Text("Save")
                    }
                }
            }


        }


    }
}

@Preview
@Composable
fun NoteViewPreview()
{
    NoteView(note = Note("As a character from a medieval-fantasy setting, Flayn from Fire Emblem Three Houses may not be familiar with the concept of a fast-food restaurant like McDonald's. However, if we were to imagine her ordering at McDonald's, she may prefer something on the lighter side, as she is depicted as a gentle and delicate character.\n" +
            "\n" +
            "She might enjoy a simple cheeseburger or a Filet-O-Fish sandwich, as they are not overly heavy or greasy. She may also prefer a side of apple slices or a salad rather than French fries. For a beverage, she might choose a small milkshake or a bottled water.\n" +
            "\n" +
            "Of course, this is just speculation based on Flayn's character traits, and she may have different preferences or dietary restrictions that we are not aware of.",
         "Flayn's Mcdonald's Order","5/13/2023" ,"Unspecified"),
        {note: Note, string: String, String -> {}},{})
}

@Preview
@Composable
fun BigNoteViewPreview()
{
    NoteView(note = Note("Flayn, the enigmatic character from Fire Emblem: Three Houses, finds herself in a new and bustling environment as she steps into a McDonald's restaurant. Known for her unique personality and gentle demeanor, her choice of a Filet-O-Fish combo meal with specific side and drink selections reveals not only her preferences but also her thoughts and feelings in this unfamiliar setting.\n" +
            "\n" +
            "As Flayn peruses the menu, her eyes settle on the Filet-O-Fish combo with a side of golden fries and a refreshing Sprite drink. The complete meal, carefully curated with her personal tastes in mind, captures her attention. Flayn is enticed by the delicate balance of flavors and textures that this meal promises. The succulent fish patty nestled in a soft bun, accompanied by the crispiness of the fries and the effervescence of the Sprite, presents a harmonious and satisfying combination.\n" +
            "\n" +
            "Flayn's choice of Sprite as her drink is a testament to her fondness for refreshing and invigorating flavors. The effervescent bubbles dance on her tongue, leaving behind a delightful sensation. As she takes each sip, she appreciates the subtle sweetness and the thirst-quenching qualities of the carbonated beverage. The Sprite complements the flavors of the meal, cleansing her palate between each bite and enhancing her overall dining experience.\n" +
            "\n" +
            "Alongside her Filet-O-Fish, Flayn indulges in a side of golden fries. The crispy exterior gives way to the fluffy potato inside, providing a satisfying crunch with each bite. As she enjoys the fries, Flayn admires the contrast of textures and the comforting familiarity of this classic side dish. The fries evoke a sense of nostalgia, reminding her of simpler times and joyful memories shared with friends.\n" +
            "\n" +
            "As Flayn begins her meal, she takes a moment to appreciate the complete experience. The first bite of the Filet-O-Fish delights her senses, as the crispy exterior gives way to the tender fish, complemented by the tangy tartar sauce. She relishes the satisfying crunch of the fries, each one a perfect accompaniment to the main dish. The refreshing Sprite quenches her thirst, providing a pleasant contrast to the rich flavors of the meal. Flayn savors each morsel, fully immersing herself in the present moment.\n" +
            "\n" +
            "While enjoying her Filet-O-Fish combo, Flayn reflects on the overall experience. The vibrant atmosphere of the McDonald's restaurant, bustling with the energy of fellow patrons, contrasts with her serene monastery life. Yet, she embraces the lively ambiance, appreciating the diversity and liveliness of the people around her. The laughter and conversations create a sense of unity and connection, reminding her of the joy that can be found in shared experiences.\n" +
            "\n" +
            "Flayn's dining experience at McDonald's not only satisfies her hunger but also provides her with a glimpse into a world beyond her monastery walls. The familiar comfort of the Filet-O-Fish, accompanied by the joyous crunch of the fries and the refreshing effervescence of the Sprite, offers her a sense of familiarity amidst the new surroundings. It becomes a symbol of the diverse experiences and pleasures that exist beyond her tranquil sanctuary.\n" +
            "\n" +
            "In conclusion, Flayn's choice to order the Filet-O-Fish combo at McDonald's exemplifies her affinity for the sea and her desire for balance and harmony. The specific selection of Sprite as her drink showcases her preference for refreshing flavors, while the golden fries provide a satisfying and nostalgic side. As she indulges in each element of the combo, Flayn embraces the vibrant atmosphere and finds joy in the"
        , "Flayn's Mcdonald's Order","5/13/2023" ,"Unspecified"),
        {note: Note, string: String, String -> {}},{})
}

@Preview
@Composable
fun NoteItemPreview()
{
    NoteItem(note = Note("This is a note","Title",""), {}, {})
}

@Preview
@Composable
fun BigAssNoteItemPreview()
{
    NoteItem(Note("This is a note with a shit ton of text on it." +
            "Ideally the app looks just as beautiful as when the note is a entire paragraph" +
            "Either way, my eyes are burning due to the light mode, but I can't read for shit" +
            "and have to set the brightness high af on dark mode. Lord Help me. DROP DATABASE")
    ,{}, {})
}

@Preview
@Composable
fun HomePreview()
{
val items = listOf("A", "B")

    Home(modifier = Modifier,
        {

        },
        {},{},{},{},
        SnapshotStateList<Note>(),
        items
    )
}

@Preview
@Composable
fun AddNotePreview()
{

    AddNote(modifier = Modifier,
        {},{})
}

@Preview
@Composable
fun ExpandViewPreview(){

    ExpandedView(Note("NOTE text goes here", "TITLE",
        "FLAYN", "12/01/2022"),
        {},
        {})
}