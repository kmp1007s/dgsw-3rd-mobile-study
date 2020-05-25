package com.codesample.memo;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Memo.class}, version = 1)
public abstract class MemoDatabase extends RoomDatabase {
    public abstract MemoDao getMemoDao();

    private static MemoDatabase instance;
    public static MemoDatabase getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(
                    context,
                    MemoDatabase.class,
                    "db"
            ).build();
        }
        return instance;
    }
}
