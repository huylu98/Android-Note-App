package com.example.maintenance;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class EditActivity extends AppCompatActivity {
    Intent intent;

    EditText dateField;
    EditText taskField;
    EditText distanceField;
    EditText amountField;

    TextView tvAdd;
    TextView tvDate;
    TextView tvTask;
    TextView tvAmount;
    TextView tvDistance;

    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        findViews();
        intent = getIntent();
        setIntentData();
    }

    private void findViews() {
        dateField = findViewById(R.id.dateField);
        taskField = findViewById(R.id.taskField);
        distanceField = findViewById(R.id.distanceField);
        amountField = findViewById(R.id.amountField);

        tvDate = findViewById(R.id.tvDate);
        tvTask = findViewById(R.id.tvTask);
        tvAmount = findViewById(R.id.tvAmount);
        tvDistance = findViewById(R.id.tvDistance);
        tvAdd = findViewById(R.id.tvAdd);
        saveBtn = findViewById(R.id.btnSave);
    }

    private void setIntentData() {
        ActionBar actionBar = getSupportActionBar();
        if (intent.getExtras() != null) {
            setNoteData(intent);
            if (actionBar != null) {
                actionBar.setTitle(R.string.edit_note);
            }
        } else {
            if (actionBar != null) {
                actionBar.setTitle(R.string.add_note);
            }
        }
    }

    private void setNoteData(Intent intent) {
        dateField.setText(intent.getStringExtra("date"));
        taskField.setText(intent.getStringExtra("task"));
        distanceField.setText(intent.getStringExtra("distance"));
        amountField.setText(intent.getStringExtra("amount"));
        tvAdd.setText(R.string.edit_note);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void addNote(View view) {
        if (!checkInput())
            return;
        Intent intent = new Intent();
        intent.putExtra("date", dateField.getText().toString());
        intent.putExtra("task", taskField.getText().toString());
        intent.putExtra("distance", distanceField.getText().toString());
        intent.putExtra("amount", amountField.getText().toString());

        setResult(RESULT_OK, intent);
        finish();
    }

    private boolean isDateValid(String dateStr) {
        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    private boolean checkInput() {
        if (dateField.getText().toString().isEmpty() ||
                taskField.getText().toString().isEmpty() ||
                amountField.getText().toString().isEmpty() ||
                distanceField.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.empty_title_error), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isDateValid(dateField.getText().toString())) {
            Toast.makeText(this, getString(R.string.invalid_date), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
