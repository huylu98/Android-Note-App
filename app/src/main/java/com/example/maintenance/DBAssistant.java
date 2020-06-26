package com.example.maintenance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DBAssistant {
    private NotesDBHelper dbHelper;
    private SQLiteDatabase db;

    public DBAssistant(Context context) {
        this.dbHelper = new NotesDBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public void addNote(Note note) {
        ContentValues values = new ContentValues();
        putContentValues(values, note);
        db.insert(NotesDBHelper.TABLE_NOTES, null, values);
    }

    public void updateNote(int noteId, Note updated) {
        ContentValues values = new ContentValues();
        putContentValues(values, updated);
        db.update(NotesDBHelper.TABLE_NOTES, values,
                NotesDBHelper.KEY_ID + "= ?",
                new String[]{Integer.toString(noteId)});
    }

    public void deleteNote(int noteId) {
        db.delete(NotesDBHelper.TABLE_NOTES,
                NotesDBHelper.KEY_ID + "= " + noteId, null);
    }

    public ArrayList<Note> getNotes() {
        ArrayList<Note> notes = new ArrayList<>();
        Cursor cursor = db.query(NotesDBHelper.TABLE_NOTES, null, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(NotesDBHelper.KEY_ID);
            int dateIndex = cursor.getColumnIndex(NotesDBHelper.KEY_DATE);
            int taskIndex = cursor.getColumnIndex(NotesDBHelper.KEY_TASK);
            int amountIndex = cursor.getColumnIndex(NotesDBHelper.KEY_AMOUNT);
            int distanceIndex = cursor.getColumnIndex(NotesDBHelper.KEY_DISTANCE);
            do {
                Note note = new Note(
                        cursor.getInt(idIndex),
                        cursor.getString(dateIndex),
                        cursor.getString(taskIndex),
                        cursor.getString(amountIndex),
                        cursor.getString(distanceIndex)
                );
                notes.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return notes;
    }

    public void close() {
        dbHelper.close();
    }

    private void putContentValues(ContentValues values, Note note) {
        values.put(NotesDBHelper.KEY_DATE, note.getDate());
        values.put(NotesDBHelper.KEY_TASK, note.getTask());
        values.put(NotesDBHelper.KEY_AMOUNT, note.getAmount());
        values.put(NotesDBHelper.KEY_DISTANCE, note.getDistance());
    }
}
