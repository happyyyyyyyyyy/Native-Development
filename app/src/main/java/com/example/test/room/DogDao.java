package com.example.test.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DogDao {
    @Insert
    void insert(DogData dogData);

    @Update
    void update(DogData dogData);

    @Delete
    void delete(DogData dogData);

    @Query("SELECT * FROM DogData")
    List<DogData> getAll();

    @Query("DELETE FROM DogData")
    void deleteAll();




}
