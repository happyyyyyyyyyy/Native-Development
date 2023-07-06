package com.example.test.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.test.dto.BookmarkDto;

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

    @Query("SELECT * FROM DogData WHERE bookmarkCheck = 1")
    List<BookmarkDto> getBookmarkAll();

    @Query("DELETE FROM DogData")
    void deleteAll();

    @Query("SELECT * FROM DogData " +
            "WHERE name Like :searchQuery " +
            "ORDER BY CASE WHEN name = :exactMatch THEN 1 ELSE 2 END, name")
    List<DogData> search(String searchQuery, String exactMatch);

    @Query("SELECT bookmarkCheck FROM DogData WHERE id = :id")
    boolean checkData(int id);

    @Query("SELECT id FROM DogData WHERE id = :id")
    int checkData2(int id);

    @Query("UPDATE DogData SET bookmarkCheck = :bookmarkCheck WHERE id = :id")
    void updateBookmarkCheck(boolean bookmarkCheck, int id);

}

