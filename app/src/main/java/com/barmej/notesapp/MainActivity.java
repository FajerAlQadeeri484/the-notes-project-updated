package com.barmej.notesapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.barmej.notesapp.adapter.PhotoNoteAdapter;
import com.barmej.notesapp.data.CheckNote;
import com.barmej.notesapp.data.Note;
import com.barmej.notesapp.data.PhotoNote;
import com.barmej.notesapp.listener.ItemClickListener;
import com.barmej.notesapp.listener.ItemLongClickListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<Note> mItems;
    private PhotoNoteAdapter mAdapter;

    private RecyclerView.LayoutManager mStaggeredGridLayoutManager;

    private static final int ADD_NOTE = 145;
    private static final int EDIT_NOTE = 147;
    private static final int EDIT_PHOTO = 148;
    private Uri pic;
    private int kinds = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recycler_view_photos);
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2,1);
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        mItems = new ArrayList<Note>();
        mAdapter = new PhotoNoteAdapter(mItems,
                new ItemClickListener() {
                    @Override
                    public void onClickItem(int position) {
                        edit(position);
                    }
            },
                new ItemLongClickListener() {
                    @Override
                    public void onLongClickItem(int position) {
                        deleteItem(position);
                    }
        });
        mRecyclerView.setAdapter(mAdapter);

        findViewById(R.id.floating_button_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAddNewPhotoActivity();
            }
        });
    }

    private void startAddNewPhotoActivity(){
        Intent intent = new Intent(this, AddNewNoteActivity.class);
        startActivityForResult(intent, ADD_NOTE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NOTE){
            if (data != null){
                Uri photoUri = data.getParcelableExtra(Constants.EXTRA_PHOTO_URI);
                int theCheck = data.getIntExtra(Constants.EXTRA_CHECK_MARK,1);
                //boolean boo = data.getBooleanExtra(Constants.EXTRA_CHECK,false);
                String checkN = data.getStringExtra(Constants.EXTRA_CHECK);
                String nnote = data.getStringExtra(Constants.EXTRA_NOTE);
                int color = data.getIntExtra(Constants.EXTRA_COLOR,1);
                System.out.println("ppp"+nnote);
                if (photoUri!=null){
                    PhotoNote photoNote = new PhotoNote(nnote,color,photoUri);
                    addItem(photoNote);
                }else if (checkN!=null){
                    boolean tOrf;
                    if (theCheck == 1){
                        tOrf = true;
                    }else {
                        tOrf = false;
                    }
                    CheckNote checkNote = new CheckNote(nnote,color,tOrf);
                    addItem(checkNote);
                }else{
                    Note note = new Note(nnote,color);
                    addItem(note);
                }

            }else{
                Toast.makeText(this,R.string.didnt_add_note, Toast.LENGTH_LONG).show();
            }
        }
        else if (requestCode == EDIT_NOTE){
            if (resultCode == RESULT_OK){
                Uri photoUpdated;
                int check;
                String textUpdated = data.getExtras().getString("TEXT_UPDATED");
                int itemPosition = data.getExtras().getInt("P");
                Note note = mItems.get(itemPosition);
                if (kinds == 1){
                    photoUpdated = data.getExtras().getParcelable("PHOTO_UPDATED");
                    if (photoUpdated==null){
                        photoUpdated = pic;
                        ((PhotoNote) note).setImage(photoUpdated);
                    }
                }else if (kinds == 2){
                    check = data.getExtras().getInt("CHECK_UPDATED");
                    if (check==1){
                        ((CheckNote) note).setChecked(true);
                    }else if (check==0){
                        ((CheckNote) note).setChecked(false);
                    }else {
                        System.out.println("didn't work");
                    }

                }
                note.setNote(textUpdated);
                mAdapter.notifyItemChanged(itemPosition);
            }
        }
        /*else if (requestCode == EDIT_PHOTO){
            if (resultCode == RESULT_OK){
                Uri photoUpdated;
                String textUpdated = data.getExtras().getString("TEXT_UPDATED");
                int itemPosition = data.getExtras().getInt("P");
                photoUpdated = data.getExtras().getParcelable("PHOTO_UPDATED");
                if (photoUpdated==null){
                    photoUpdated = pic;
                }
                Note note = mItems.get(itemPosition);
                note.setNote(textUpdated);
                ((PhotoNote) note).setImage(photoUpdated);
                mAdapter.notifyItemChanged(itemPosition);
            }
        }*/
    }

    private void addItem(Note note){
        mItems.add(note);
        mAdapter.notifyItemInserted(mItems.size()-1);
    }

    private void edit(int position){
        Note note = mItems.get(position);
        if (note instanceof PhotoNote){
            kinds = 1;
            pic = ((PhotoNote) note).getImage();
            Intent intentToNotePhotoDetails = new Intent(MainActivity.this,NotePhotoDetails.class);
            intentToNotePhotoDetails.putExtra("NOTE", note);
            intentToNotePhotoDetails.putExtra("POSITION", position);
            startActivityForResult(intentToNotePhotoDetails, EDIT_NOTE);

        }else if (note instanceof CheckNote){
            kinds = 2;
            Intent intentToNoteCheckDetails = new Intent(MainActivity.this,NoteCheckDetails.class);
            intentToNoteCheckDetails.putExtra("NOTE", note);
            intentToNoteCheckDetails.putExtra("POSITION", position);
            startActivityForResult(intentToNoteCheckDetails, EDIT_NOTE);

        }else {
            kinds = 3;
            Intent intentToNoteDetails = new Intent(MainActivity.this,NoteDetails.class);
            intentToNoteDetails.putExtra("NOTE", note);
            intentToNoteDetails.putExtra("POSITION", position);
            startActivityForResult(intentToNoteDetails, EDIT_NOTE);
        }

    }

    private void deleteItem(final int position){
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage(R.string.delete_confirmation)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mItems.remove(position);
                        mAdapter.notifyItemRemoved(position);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();
        alertDialog.show();
    }
}
