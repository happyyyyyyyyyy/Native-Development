package com.example.test.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DogData {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;

    public String bredFor;
}
