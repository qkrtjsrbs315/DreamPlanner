package app.sunrin.dreamplanner.ui.gallery;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

import app.sunrin.dreamplanner.R;
import app.sunrin.dreamplanner.ui.gallery.db.NotesDB;
import app.sunrin.dreamplanner.ui.gallery.db.NotesDao;


public class EditeNoteActivity extends AppCompatActivity {

    private EditText inputNote;
    private NotesDao dao;
    private Note temp;
    public static final  String NOTE_EXTRA_Key = "note_id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edite_note);


        inputNote = findViewById(R.id.input_note );
        dao = NotesDB.getInstance(this).notesDao();
        if(getIntent().getExtras() != null){
            int id = getIntent().getExtras().getInt(NOTE_EXTRA_Key,0);
            temp = dao.getNoteById(id);
            inputNote.setText(temp.getNoteText());
        } else temp = new Note("1234",0);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edite_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id =  item.getItemId();
        if (id == R.id.save_note)
            onSaveNote();
        return super.onOptionsItemSelected(item);
    }

    private void onSaveNote() {
        String text = inputNote.getText().toString();
        if(!text.isEmpty()){
            long date = new Date().getTime(); //system Time

            temp.setNoteDate(date);
            temp.setNoteText(text);

            if(true)
                dao.insertNote(temp);//Insert and Save note to DB
            else dao.updateNote(temp);

            finish();  //Return to MainActivity
        }

    }


}

