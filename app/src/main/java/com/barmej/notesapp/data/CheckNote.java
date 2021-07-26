package com.barmej.notesapp.data;

import java.io.Serializable;

public class CheckNote extends Note implements Serializable {

    private boolean checked;


    public CheckNote(String note, int color, boolean checked) {
        super(note, color);
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

}
