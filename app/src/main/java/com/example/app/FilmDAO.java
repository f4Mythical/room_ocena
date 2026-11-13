package com.example.app;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FilmDAO {
    @Insert
    void wstawFilmDoBazy(Film film);

    @Insert
    void wstawFilmyDoBazy(Film... film);

    @Delete
    void usunZBazy(Film film);

    @Update
    void aktualizujFilm(Film film);

    @Query("SELECT * FROM film")
    List<Film> zwrocWszystkieFilmy();
}