package com.codesample.whatid.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverter;

import java.util.List;

@Dao
public interface BookmarkDao {
    @Insert
    public void addBookmark(Bookmark bookmark); // 북마크 추가

    @Query("select * from Bookmark")
    @TypeConverter
    public List<Bookmark> getBookmarks(); // 전체 북마크 조회

    @Delete
    public void deleteBookmark(Bookmark bookmark); // 북마크 삭제
}
