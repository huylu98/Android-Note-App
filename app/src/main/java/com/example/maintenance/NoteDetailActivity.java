package com.example.maintenance;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.util.Locale;

public class NoteDetailActivity extends AppCompatActivity {
    Intent intent;
    TextView tvDate;
    TextView tvTask;
    TextView tvDistance;
    TextView tvAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        tvDate = findViewById(R.id.dateField);
        tvTask = findViewById(R.id.taskField);
        tvDistance = findViewById(R.id.distanceField);
        tvAmount = findViewById(R.id.amountField);
        intent = getIntent();

        setNoteData(intent);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.note_details));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void setNoteData(Intent intent) {
        String dt = intent.getStringExtra("distance");
        double cv = Double.parseDouble(intent.getStringExtra("amount"));
        Locale locale = new Locale("vi", "VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        tvDate.setText(intent.getStringExtra("date"));
        tvTask.setText(intent.getStringExtra("task"));
        tvDistance.setText(String.format("%s KM", dt));
        tvAmount.setText(currencyFormatter.format(cv));
    }
}
