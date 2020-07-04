package com.codesample.whatid.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    public String userId;
    public String password;

    public User() {}

    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }
}
