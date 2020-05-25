package com.codesample.memo;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverter;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MemoDao {
    @Insert
    public long addMemo(Memo memo);

    @Query("select * from Memo")
    @TypeConverter
    public List<Memo> getMemos();

    @Query("select * from Memo where number=:number")
    public Memo getMemo(int number);

    @Update
    public int saveMemo(Memo memo);

    @Delete
    public int delete(Memo memo);
}
