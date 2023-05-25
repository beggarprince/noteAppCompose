# noteAppCompose
Simple note taking app written with jetpack compose.

Notes are stored using Room Database. Notes can be searched by their title and text content, as well as filtered by their tag, date, and how old/new they are.
Notes can be updated, with their tag, text, and title changed. Date will update automatically.

Consists of composables -
  MainControl - Holds viewmodel and calls composables
  Home - Holds lazycolumn of all notes stored on device, as well as the search, filter, and expand functionality
  ExpandNote - Displays note and all it's text on the home screen
  NoteView - Takes user out of the home screen to focus on a single note. From here, note can be edited or deleted.
