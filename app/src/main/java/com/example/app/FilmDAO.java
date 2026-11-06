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
    public void wstawFilmDoBazy(Film film);
    @Insert
    public void wstawFilmyDoBazy(Film ... film);
    @Delete
    public void usunFilm(Film film);
    @Update
    public void aktualizujFilm(Film film);
    @Query("SELECT * from film");
    public List<Film> zwrocWszystkieFilmy();

}

