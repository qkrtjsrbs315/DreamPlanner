package app.sunrin.dreamplanner.ui.gallery;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import app.sunrin.dreamplanner.R;
import app.sunrin.dreamplanner.ui.gallery.callback.NoteEventListener;
import app.sunrin.dreamplanner.ui.gallery.db.NotesDB;
import app.sunrin.dreamplanner.ui.gallery.db.NotesDao;

import static android.content.ContentValues.TAG;
import static app.sunrin.dreamplanner.ui.gallery.EditeNoteActivity.NOTE_EXTRA_Key;
public class GalleryFragment extends Fragment implements NoteEventListener {
    private RecyclerView recyclerView;
    private ArrayList<Note> notes;
    private NotesAdapter adapter;
    private NotesDao dao;
    private GalleryViewModel galleryViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        Toolbar toolbar = root.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        recyclerView = root.findViewById(R.id.note_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);

        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "출력할 문자열", Toast.LENGTH_LONG).show();


                onAddNewNotes();
            }
        });

        dao = NotesDB.getInstance(getActivity()).notesDao();

        return root;
    }


    private void loadNotes() {
        this.notes = new ArrayList<>();   //get All Note from DataBase
        List<Note> list =  dao.getNotes();
        this.notes.addAll(list);
        this.adapter=new NotesAdapter(getContext(),notes);
        this.adapter.setListener(this);

        this.recyclerView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();

    }
    private void onAddNewNotes(){
        startActivity(new Intent (getActivity(),EditeNoteActivity.class));
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadNotes();
    }

    @Override
    public void onNoteClick(Note note) {
        Intent edit = new Intent(getActivity(),EditeNoteActivity.class);
        edit.putExtra(NOTE_EXTRA_Key, note.getId());
        startActivity(edit);
    }


    @Override
    public void onNoteLongClick(final Note note) {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.app_name)
                .setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();

                    }
                }).setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dao.deleteNote(note);
                loadNotes();
            }
        }).setNegativeButton("Share", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent share = new Intent(Intent.ACTION_SEND); //share
                String text = note.getNoteText() + "\n Create on : " +
                        NoteUtils.dateFromLong(note.getNoteDate());
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT,text);
                startActivity(share);

            }
        })
                .create()
                .show();
    }

}