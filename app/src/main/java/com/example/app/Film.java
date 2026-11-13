package com.example.app;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "film")
public class Film {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String nazwa;
    public int kategoriaWiekowa;
    public boolean czyWioska;
    public String tworca;
    public String gatunek;

    public Film() {
    }

    @Ignore
    public Film(String nazwa, int kategoriaWiekowa, boolean czyWioska, String tworca, String gatunek) {
        this.nazwa = nazwa;
        this.kategoriaWiekowa = kategoriaWiekowa;
        this.czyWioska = czyWioska;
        this.tworca = tworca;
        this.gatunek = gatunek;
    }

    @Override
    public String toString() {
        return nazwa + " | " + kategoriaWiekowa + "+ | " + (czyWioska ? "wioski: tak" : "wioski: nie") + " | " + tworca + " | " + gatunek;
    }
}