package com.codesample.whatid.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Folder.class,
        parentColumns = "id",
        childColumns = "folderId",
        onDelete = ForeignKey.CASCADE))
public class Account {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int folderId; // Folder 테이블의 기본키를 참조
    public String url;
    public String userId;
    public String password;
    public String memo;

    public Account() {}

    public Account(int folderId, String url, String userId, String password, String memo) {
        this.folderId = folderId;
        this.url = url;
        this.userId = userId;
        this.password = password;
        this.memo = memo;
    }
}
