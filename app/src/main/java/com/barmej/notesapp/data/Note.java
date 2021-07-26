package com.barmej.notesapp.data;

import java.io.Serializable;

public class Note implements Serializable {

    protected String note;
    protected int color;

    public Note(String note, int color) {
        this.note = note;
        this.color = color;
    }

    public String getNote() {
        return note;
    }

    public int getColor() {
        return color;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Note() {
    }
}
