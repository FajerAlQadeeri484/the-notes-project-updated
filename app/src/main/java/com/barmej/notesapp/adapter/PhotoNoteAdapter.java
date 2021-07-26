package com.barmej.notesapp.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.barmej.notesapp.R;
import com.barmej.notesapp.data.CheckNote;
import com.barmej.notesapp.data.Note;
import com.barmej.notesapp.data.PhotoNote;
import com.barmej.notesapp.listener.ItemClickListener;
import com.barmej.notesapp.listener.ItemLongClickListener;

import java.util.ArrayList;

public class PhotoNoteAdapter extends RecyclerView.Adapter<PhotoNoteAdapter.NoteViewHolder> {

    private final int PHOTO_TYPE = 0;
    private final int CHECK_TYPE = 1;
    private final int NOTE_TYPE = 2;


    private ArrayList<Note> mItems;
    private ItemClickListener mItemClickListener;
    private ItemLongClickListener mItemLongClickListener;


    public PhotoNoteAdapter(ArrayList<Note> mItems, ItemClickListener mItemClickListener, ItemLongClickListener mItemLongClickListener) {
        this.mItems = mItems;
        this.mItemClickListener = mItemClickListener;
        this.mItemLongClickListener = mItemLongClickListener;
    }


    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        NoteViewHolder viewHolder;
        if (viewType == PHOTO_TYPE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note_photo, parent, false);
            viewHolder = new PhotoNoteViewHolder(view, mItemClickListener, mItemLongClickListener);
        } else if (viewType == CHECK_TYPE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note_check, parent, false);
            viewHolder = new NoteCheckViewHolder(view, mItemClickListener, mItemLongClickListener);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
            viewHolder = new NoteViewHolder(view, mItemClickListener, mItemLongClickListener);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final NoteViewHolder holder, final int position) {
        final Note note = mItems.get(position);
        if (note instanceof PhotoNote) {
            PhotoNote photoNote = (PhotoNote) note;
            PhotoNoteViewHolder photoNoteViewHolder = (PhotoNoteViewHolder) holder;
            photoNoteViewHolder.photoIv.setImageURI(photoNote.getImage());
            holder.cardView.setBackgroundColor(note.getColor());
        } else if (note instanceof CheckNote) {
            final CheckNote checkNote = (CheckNote) note;
            final NoteCheckViewHolder noteCheckViewHolder = (NoteCheckViewHolder) holder;

            noteCheckViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final boolean isChecked = noteCheckViewHolder.checkBox.isChecked();
                    int colorChosen = note.getColor();
                    checkNote.setChecked(isChecked);
                    if (isChecked) {
                        holder.cardView.setBackgroundColor(Color.parseColor("#ADFF2F"));
                    } else {
                        holder.cardView.setBackgroundColor(colorChosen);
                    }
                }
            });
            int colorChosen = note.getColor();
            noteCheckViewHolder.checkBox.setChecked(checkNote.isChecked());
            if (checkNote.isChecked()) {
                holder.cardView.setBackgroundColor(Color.parseColor("#ADFF2F"));
            } else {
                holder.cardView.setBackgroundColor(colorChosen);
            }
        }else {
            holder.cardView.setBackgroundColor(note.getColor());
        }
        holder.theNote.setText(note.getNote());
        holder.position = position;
    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (mItems.get(position) instanceof PhotoNote) {
            return PHOTO_TYPE;
        } else if (mItems.get(position) instanceof CheckNote) {
            return CHECK_TYPE;
        } else {
            return NOTE_TYPE;
        }
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {

        private TextView theNote;
        private View cardView;
        private int position;

        public NoteViewHolder(@NonNull View itemView, final ItemClickListener mItemClickListener, final ItemLongClickListener mItemLongClickListener) {
            super(itemView);
            theNote = itemView.findViewById(R.id.note);
            cardView = itemView.findViewById(R.id.cardView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onClickItem(position);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mItemLongClickListener.onLongClickItem(position);
                    return false;
                }
            });
        }
    }

    static class PhotoNoteViewHolder extends NoteViewHolder {

        private ImageView photoIv;

        public PhotoNoteViewHolder(@NonNull View itemView, ItemClickListener mItemClickListener, ItemLongClickListener mItemLongClickListener) {
            super(itemView, mItemClickListener, mItemLongClickListener);
            photoIv = itemView.findViewById(R.id.image_view_list_item_photo);
        }
    }

    static class NoteCheckViewHolder extends NoteViewHolder {

        private CheckBox checkBox;

        public NoteCheckViewHolder(@NonNull View itemView, ItemClickListener mItemClickListener, ItemLongClickListener mItemLongClickListener) {
            super(itemView, mItemClickListener, mItemLongClickListener);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }

}
