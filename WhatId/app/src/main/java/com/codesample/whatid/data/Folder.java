package com.codesample.whatid.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "userId",
        childColumns = "userId",
        onDelete = ForeignKey.CASCADE))
public class Folder {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String userId;

    public Folder() {}

    public Folder(String name, String userId) {
        this.name = name;
        this.userId = userId;
    }
}
