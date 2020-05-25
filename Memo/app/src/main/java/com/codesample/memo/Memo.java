package com.codesample.memo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Memo {
    @PrimaryKey(autoGenerate = true)
    public int number;
    public String time;
    public String title;
    public String content;

    public Memo() {

    }

    public Memo(String time, String title, String content) {
        this.time = time;
        this.title = title;
        this.content = content;
    }
}
