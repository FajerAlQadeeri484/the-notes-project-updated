package com.barmej.notesapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.barmej.notesapp.data.Note;
import com.barmej.notesapp.data.PhotoNote;

import java.io.Serializable;

public class NotePhotoDetails extends AppCompatActivity {

    ImageView photoImageViewp;

    Uri mSelectedPhotoUri;
    private static final int READ_PHOTO_FROM_GALLERY_PERMISSION = 130;
    private static final int PICK_IMAGE = 120;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_photo_details);

        photoImageViewp = findViewById(R.id.photoImageView);
        final EditText photoNoteEditText = findViewById(R.id.photoNoteEditText);
        RelativeLayout background = findViewById(R.id.background);
        Button update = findViewById(R.id.update);

        PhotoNote photoNoteData = (PhotoNote) getIntent().getParcelableExtra("NOTE");
        photoImageViewp.setImageURI(photoNoteData.getImage());
        photoNoteEditText.setText(photoNoteData.getNote());
        background.setBackgroundColor(photoNoteData.getColor());

        Bundle bundle = getIntent().getExtras();
        final int itemPosition = bundle.getInt("POSITION");

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textUpdated = photoNoteEditText.getText().toString();
                Intent data = new Intent();
                data.putExtra("TEXT_UPDATED",textUpdated);
                data.putExtra("PHOTO_UPDATED",mSelectedPhotoUri);
                data.putExtra("P",itemPosition);
                setResult(RESULT_OK,data);
                finish();
            }
        });

        photoImageViewp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPhoto();
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
        photoImageViewp.setImageURI(data);
        mSelectedPhotoUri = data;
    }

    private void firePickPhotoIntent(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), PICK_IMAGE);
    }
}