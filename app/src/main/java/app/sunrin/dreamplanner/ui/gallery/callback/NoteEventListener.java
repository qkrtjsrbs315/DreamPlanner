package app.sunrin.dreamplanner.ui.gallery.callback;

import app.sunrin.dreamplanner.ui.gallery.Note;

public interface NoteEventListener {

    void onNoteClick(Note note);

    void onNoteLongClick (Note note);
}


