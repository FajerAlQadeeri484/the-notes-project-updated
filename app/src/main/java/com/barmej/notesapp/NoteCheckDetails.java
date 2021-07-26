package com.barmej.notesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.barmej.notesapp.data.CheckNote;

public class NoteCheckDetails extends AppCompatActivity {

    int checked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_check_details);

        final EditText checkNoteEditText = findViewById(R.id.checkNoteEditText);
        final CheckBox checkNoteCheckBox = findViewById(R.id.checkNoteCheckBox);
        ConstraintLayout background2 = findViewById(R.id.background2);
        Button update2 = findViewById(R.id.update2);

        CheckNote chkNoteData = (CheckNote) getIntent().getSerializableExtra("NOTE");
        checkNoteEditText.setText(chkNoteData.getNote());
        checkNoteCheckBox.setChecked(chkNoteData.isChecked());
        background2.setBackgroundColor(chkNoteData.getColor());

        int colorChosen = chkNoteData.getColor();
        if (checkNoteCheckBox.isChecked()){
            background2.setBackgroundColor(Color.parseColor("#ADFF2F"));
            checked = 1;
        }else {
            background2.setBackgroundColor(colorChosen);
            checked = 0;
        }
        checkNoteCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkNoteCheckBox.isChecked()){
                    background2.setBackgroundColor(Color.parseColor("#ADFF2F"));
                    checked = 1;
                }else {
                    background2.setBackgroundColor(colorChosen);
                    checked = 0;
                }
            }
        });

        Bundle bundle = getIntent().getExtras();
        final int itemPosition = bundle.getInt("POSITION");

        update2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textUpdated = checkNoteEditText.getText().toString();
                Intent data = new Intent();
                data.putExtra("TEXT_UPDATED",textUpdated);
                data.putExtra("P",itemPosition);
                data.putExtra("CHECK_UPDATED",checked);
                setResult(RESULT_OK,data);
                finish();
            }
        });


    }
}