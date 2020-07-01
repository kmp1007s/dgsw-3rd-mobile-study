package com.codesample.whatid.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Folder {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;

    public Folder() {}

    public Folder(String name) {
        this.name = name;
    }
}
