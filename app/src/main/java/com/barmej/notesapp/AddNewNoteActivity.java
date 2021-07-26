package com.barmej.notesapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.barmej.notesapp.adapter.PhotoNoteAdapter;

public class AddNewNoteActivity extends AppCompatActivity {

    int color;
    int realColor;
    int checked;

    private CardView cardViewPhoto;
    private CardView cardViewNote;
    private CardView cardViewCheckNote;
    private ImageView photoImageView;
    private EditText photoNoteEditText;
    private EditText noteEditText;
    private EditText checkNoteEditText;
    private CheckBox checkNoteCheckBox;

    private String noteAdded_Note;
    private String noteAdded_Photo;
    private String noteAdded_Check;

    Uri mSelectedPhotoUri;
    private static final int READ_PHOTO_FROM_GALLERY_PERMISSION = 130;
    private static final int PICK_IMAGE = 120;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_note);

        cardViewPhoto = findViewById(R.id.cardViewPhoto);
        cardViewNote = findViewById(R.id.cardViewNote);
        cardViewCheckNote = findViewById(R.id.cardViewCheckNote);
        photoImageView = findViewById(R.id.photoImageView);
        photoNoteEditText = findViewById(R.id.photoNoteEditText);
        noteEditText = findViewById(R.id.noteEditText);
        checkNoteEditText = findViewById(R.id.checkNoteEditText);
        checkNoteCheckBox = findViewById(R.id.checkNoteCheckBox);
        Button submitBtn = findViewById(R.id.button_submit);
        color=getResources().getColor(R.color.blue);

        realColor = color;

        checkNoteCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkNoteCheckBox.isChecked()){
                    cardViewCheckNote.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ADFF2F")));
                    realColor = color;
                    color = Color.parseColor("#ADFF2F");
                    checked = 1;
                }else {
                    cardViewCheckNote.setBackgroundTintList(ColorStateList.valueOf(realColor));
                    color = realColor;
                    checked = 0;
                }
            }
        });


        photoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPhoto();

            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == READ_PHOTO_FROM_GALLERY_PERMISSION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                firePickPhotoIntent();
            } else {
                Toast.makeText(this, R.string.read_permission_needed_to_access_files, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE){
            if (data != null && data.getData() != null){
                setSelectedPhoto(data.getData());
                getContentResolver().takePersistableUriPermission(data.getData(), Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                Toast.makeText(this, R.string.failed_to_get_image, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void selectPhoto(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PHOTO_FROM_GALLERY_PERMISSION);
        } else {
            firePickPhotoIntent();
        }
    }

    private void setSelectedPhoto(Uri data){
        photoImageView.setImageURI(data);
        mSelectedPhotoUri = data;
    }

    private void submit(){
        if (cardViewNote.getVisibility() == View.VISIBLE){
            noteAdded_Note = noteEditText.getText().toString();
            if (!TextUtils.isEmpty(noteAdded_Note)){
                Intent intent = new Intent();
                intent.putExtra(Constants.EXTRA_NOTE, noteAdded_Note);
                intent.putExtra(Constants.EXTRA_COLOR, color);
                setResult(RESULT_OK, intent);
                finish();
            }else{
                Toast.makeText(this, R.string.select_note, Toast.LENGTH_LONG).show();
            }
        } else if (cardViewPhoto.getVisibility() == View.VISIBLE){
            noteAdded_Photo = photoNoteEditText.getText().toString();
            if (mSelectedPhotoUri != null && !TextUtils.isEmpty(noteAdded_Photo)){
                Intent intent = new Intent();
                intent.putExtra(Constants.EXTRA_PHOTO_URI, mSelectedPhotoUri);
                intent.putExtra(Constants.EXTRA_NOTE, noteAdded_Photo);
                intent.putExtra(Constants.EXTRA_COLOR, color);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(this, R.string.select_picture_and_note, Toast.LENGTH_LONG).show();
            }
        } else if (cardViewCheckNote.getVisibility() == View.VISIBLE) {
            noteAdded_Check = checkNoteEditText.getText().toString();
            if (!TextUtils.isEmpty(noteAdded_Check)) {
                Intent intent = new Intent();
                intent.putExtra(Constants.EXTRA_NOTE, noteAdded_Check);
                intent.putExtra(Constants.EXTRA_COLOR, realColor);
                //intent.putExtra(Constants.EXTRA_CHECK, checkNoteCheckBox.isChecked());
                intent.putExtra(Constants.EXTRA_CHECK, noteAdded_Check);
                intent.putExtra(Constants.EXTRA_CHECK_MARK, checked);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(this, R.string.select_note, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, R.string.error, Toast.LENGTH_LONG).show();
        }
    }

    private void firePickPhotoIntent(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), PICK_IMAGE);
    }


    public void onColorRadioGroupClicked(View view){
        switch (view.getId()){
            case R.id.radioButtonBlue:
                    cardViewPhoto.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
                    cardViewNote.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
                    if (checkNoteCheckBox.isChecked()){
                        cardViewCheckNote.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ADFF2F")));
                        realColor = getResources().getColor(R.color.blue);
                        color = Color.parseColor("#ADFF2F");
                    }else {
                        cardViewCheckNote.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
                        color=getResources().getColor(R.color.blue);
                        realColor = color;
                    }
                    color=getResources().getColor(R.color.blue);
                    realColor = color;
                break;

            case R.id.radioButtonYellow:
                    cardViewPhoto.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.yellow)));
                    cardViewNote.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.yellow)));
                if (checkNoteCheckBox.isChecked()){
                    cardViewCheckNote.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ADFF2F")));
                    realColor = getResources().getColor(R.color.yellow);
                    color = Color.parseColor("#ADFF2F");
                }else {
                    cardViewCheckNote.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.yellow)));
                    color=getResources().getColor(R.color.yellow);
                    realColor = color;
                }
                    color=getResources().getColor(R.color.yellow);
                    realColor = color;
                break;

            case R.id.radioButtonRed:
                    cardViewPhoto.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                    cardViewNote.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                    if (checkNoteCheckBox.isChecked()){
                        cardViewCheckNote.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ADFF2F")));
                        realColor = getResources().getColor(R.color.red);
                        color = Color.parseColor("#ADFF2F");
                    }else {
                        cardViewCheckNote.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                        color=getResources().getColor(R.color.red);
                        realColor = color;
                    }
                    color=getResources().getColor(R.color.red);
                    realColor = color;
                break;
        }
    }

    public void onTypeRadioGroupClicked(View view){
        switch (view.getId()){
            case R.id.radioButtonNote:
                    cardViewPhoto.setVisibility(View.INVISIBLE);
                    cardViewNote.setVisibility(View.VISIBLE);
                    cardViewCheckNote.setVisibility(View.INVISIBLE);
                break;
            case R.id.radioButtonCheckBox:
                    cardViewPhoto.setVisibility(View.INVISIBLE);
                    cardViewNote.setVisibility(View.INVISIBLE);
                    cardViewCheckNote.setVisibility(View.VISIBLE);
                break;
            case R.id.radioButtonPhoto:
                    cardViewPhoto.setVisibility(View.VISIBLE);
                    cardViewNote.setVisibility(View.INVISIBLE);
                    cardViewCheckNote.setVisibility(View.INVISIBLE);
                break;
        }
    }

}