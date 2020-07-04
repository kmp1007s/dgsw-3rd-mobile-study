package com.codesample.whatid.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Folder.class, Account.class, Bookmark.class, User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FolderDao getFolderDAO();
    public abstract AccountDao getAccountDAO();
    public abstract BookmarkDao getBookmarkDAO();
    public abstract UserDao getUserDAO();

    private static AppDatabase instance;
    public static AppDatabase getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(
                    context,
                    AppDatabase.class,
                    "db"
            ).build();
        }
        return instance;
    }
}
