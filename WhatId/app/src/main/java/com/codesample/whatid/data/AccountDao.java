package com.codesample.whatid.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverter;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AccountDao {
    @Insert
    public long addAccount(Account account); // 계정 추가

    @Query("select * from Account where id=:id")
    public Account getAccount(int id); // id로 계정 조회

    @Query("select * from Account ORDER BY created ASC")
    @TypeConverter
    public List<Account> getAccounts(); // 계정 전체 조회

    @Query("select * from Account where folderId=:folderId ORDER BY created ASC")
    @TypeConverter
    public List<Account> getAccountsByFolder(int folderId); // 폴더별로 계정 조회

    @Query("select * from Account where folderId=:folderId ORDER BY created DESC")
    @TypeConverter
    public List<Account> getAccountsByFolderDesc(int folderId); // 폴더별로 계정 조회

    @Query("select * from Account where folderId=:folderId ORDER BY url ASC")
    @TypeConverter
    public List<Account> getAccountsByFolderOrderUrl(int folderId);

    @Query("select * from Account where folderId=:folderId ORDER BY updated DESC")
    @TypeConverter
    public List<Account> getAccountsByFolderOrderUpdated(int folderId);

    @Update
    public int saveAccount(Account account); // 계정 업데이트

    @Delete
    public void deleteAccount(Account account); // 계정 삭제
}
