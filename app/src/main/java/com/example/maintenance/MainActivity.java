package com.example.maintenance;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int CM_DELETE_ID = 1;
    private static final int CM_EDIT_ID = 2;
    private static final int ADD_NOTE = 1;
    private static final int EDIT_NOTE = 2;
    private int editedNoteIndex = 0;

    public static ArrayList<Note> notes = new ArrayList<>();
    private static ArrayList<Note> notesFiltered;
    private DBAssistant db;

    EditText searchField;
    ListView notesList;

    NoteAdapter noteAdapter;
    AlertDialog deleteDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DBAssistant(this);
        setNotesList();
        setSearchField();
        registerForContextMenu(notesList);
    }

    private void setNotesList() {
        notes = db.getNotes();
        notesFiltered = new ArrayList<>(notes);
        noteAdapter = new NoteAdapter(this, notesFiltered);
        notesList = findViewById(R.id.notesList);
        notesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Note note = notesFiltered.get(position);
                Intent intent = new Intent(MainActivity.this, NoteDetailActivity.class);
                putIntentExtras(intent, note);
                startActivity(intent);
            }
        });
        notesList.setAdapter(noteAdapter);
    }

    private void setSearchField() {
        searchField = findViewById(R.id.searchField);
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterNotes();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        for (int i = 0; i < menu.size(); i++) {
            Drawable drawable = menu.getItem(i).getIcon();
            if (drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
            }
        }
        filterNotes();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addItem) {
            addNote(null);
        }
        filterNotes();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_EDIT_ID, 0, R.string.edit);
        menu.add(0, CM_DELETE_ID, 0, R.string.delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();

        switch (item.getItemId()) {
            case CM_DELETE_ID:
                showDeleteDialog(acmi.position);
                return true;
            case CM_EDIT_ID:
                editedNoteIndex = acmi.position;
                editNote(notesFiltered.get(editedNoteIndex));
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void showDeleteDialog(final int notePosition) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        dialogBuilder.setTitle(R.string.delete_title);
        dialogBuilder.setMessage(R.string.delete_message);
        dialogBuilder.setCancelable(true);

        dialogBuilder.setPositiveButton(
                R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Note note = notesFiltered.get(notePosition);
                        notesFiltered.remove(note);
                        notes.remove(note);
                        db.deleteNote(note.getId());
                        noteAdapter.notifyDataSetChanged();
                    }
                });

        dialogBuilder.setNegativeButton(
                R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        deleteDialog = dialogBuilder.create();
        deleteDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        notes = db.getNotes();
        notesFiltered = new ArrayList<>(notes);
        db.close();
    }

    public void addNote(View view) {
        Intent intent = new Intent(this, EditActivity.class);
        startActivityForResult(intent, ADD_NOTE);
    }

    private void editNote(Note note) {
        Intent intent = new Intent(this, EditActivity.class);
        putIntentExtras(intent, note);
        startActivityForResult(intent, EDIT_NOTE);
    }

    private void putIntentExtras(Intent intent, Note note) {
        intent.putExtra("date", note.getDate());
        intent.putExtra("task", note.getTask());
        intent.putExtra("distance", note.getDistance());
        intent.putExtra("amount", note.getAmount());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NOTE && data != null) {
            db.addNote(convertResultDataToNote(data));
            notes = db.getNotes();
        } else if (requestCode == EDIT_NOTE && data != null) {
            replaceNoteDataFromIntent(data);
        }
        refreshActivity();
    }

    private Note convertResultDataToNote(Intent data) {
        String date = data.getStringExtra("date");
        String task = data.getStringExtra("task");
        String distance = data.getStringExtra("distance");
        String amount = data.getStringExtra("amount");
        return new Note(date, task, amount, distance);
    }

    private void replaceNoteDataFromIntent(Intent data) {
        String date = data.getStringExtra("date");
        String task = data.getStringExtra("task");
        String distance = data.getStringExtra("distance");
        String amount = data.getStringExtra("amount");

        Note note = notes.get(notes.indexOf(notesFiltered.get(editedNoteIndex)));
        note.setDate(date);
        note.setTask(task);
        note.setAmount(amount);
        note.setDistance(distance);

        db.updateNote(note.getId(), note);
    }

    private void refreshActivity() {
        searchField.setText("");
        notesFiltered = new ArrayList<>(notes);
        noteAdapter.refresh(notesFiltered);
    }

    public static ArrayList<Note> filterNotesByTitle(ArrayList<Note> notes, String titlePart) {
        ArrayList<Note> result = new ArrayList<>();
        for (Note note : notes) {
            if (note.getTask().toLowerCase().contains(titlePart.toLowerCase())) {
                result.add(note);
            }
        }
        return result;
    }

    private void filterNotes() {
        notesFiltered = filterNotesByTitle(notes, searchField.getText().toString());
        noteAdapter.refresh(notesFiltered);
    }

    public void clearSearchField(View view) {
        searchField.setText("");
        noteAdapter.refresh(notesFiltered);
    }
}