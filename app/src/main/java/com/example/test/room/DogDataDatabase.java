package com.example.test.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(
        version = 1,
        entities = {DogData.class}
)
public abstract class DogDataDatabase extends RoomDatabase {
    public abstract DogDao getDogDao();
}
