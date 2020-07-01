package com.codesample.whatid.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Account.class,
        parentColumns = "id",
        childColumns = "accountId",
        onDelete = ForeignKey.CASCADE))
public class Bookmark {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int accountId;

    public Bookmark() {}

    public Bookmark(int accountId) {
        this.accountId = accountId;
    }
}
