package com.example.test.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DogData.class}, version = 1)
public abstract class DogDataDatabase extends RoomDatabase {
    public abstract DogDao getDogDao();
}
