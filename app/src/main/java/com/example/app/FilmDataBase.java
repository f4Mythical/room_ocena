package com.example.app;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Film.class},version = 1)
public abstract class FilmDataBase extends RoomDatabase {
    public abstract FilmDAO zwrocWszystkieFilmy();
}
