package com.codesample.whatid.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverter;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FolderDao {
    @Insert
    public void addFolder(Folder folder); // 폴더 추가

    @Query("select * from Folder")
    @TypeConverter
    public List<Folder> getFolders(); // 폴더 전체 조회

    @Query("select * from Account where folderId=:folderId")
    public Folder getFolderItems(int folderId); // 폴더 별 계정 조회

    @Update
    public int saveFolder(Folder folder); // 폴더명 업데이트

    @Delete
    public int deleteFolder(Folder folder); // 폴더 삭제
}
