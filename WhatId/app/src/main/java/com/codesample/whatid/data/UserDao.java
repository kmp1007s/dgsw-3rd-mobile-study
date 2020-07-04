package com.codesample.whatid.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverter;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    public long addUser(User user);

    @Query("select * from User")
    @TypeConverter
    public List<User> getUsers();

    @Query("select * from User where userId=:userId")
    public User getUserByUserId(String userId);
}
