package com.example.maintenance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NoteAdapter extends BaseAdapter {
    private Context ctx;
    private LayoutInflater inflater;
    private ArrayList<Note> notes;
    public NoteAdapter(Context context, ArrayList<Note> notes) {
        this.ctx = context;
        this.notes = notes;
        this.inflater = (LayoutInflater) this.ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.notes.size();
    }

    @Override
    public Object getItem(int i) {
        return this.notes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = this.inflater.inflate(R.layout.notes_list_item, parent, false);
        }
        Note note = getNote(position);
        ((TextView) view.findViewById(R.id.taskField)).setText(note.getTask());
        ((TextView) view.findViewById(R.id.dateField)).setText(note.getDate());
        return view;
    }

    private Note getNote(int position) {
        return (Note) getItem(position);
    }

    public void refresh(ArrayList<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }
}
