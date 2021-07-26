package com.barmej.notesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.barmej.notesapp.data.CheckNote;
import com.barmej.notesapp.data.Note;

public class NoteDetails extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        final EditText noteEditText = findViewById(R.id.noteEditText);
        ConstraintLayout background3 = findViewById(R.id.background3);
        Button update3 = findViewById(R.id.update3);

        Note noteData = (Note) getIntent().getSerializableExtra("NOTE");
        noteEditText.setText(noteData.getNote());
        background3.setBackgroundColor(noteData.getColor());

        Bundle bundle = getIntent().getExtras();
        final int itemPosition = bundle.getInt("POSITION");

        update3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textUpdated = noteEditText.getText().toString();
                Intent data = new Intent();
                data.putExtra("TEXT_UPDATED",textUpdated);
                data.putExtra("P",itemPosition);
                setResult(RESULT_OK,data);
                finish();
            }
        });
    }
}