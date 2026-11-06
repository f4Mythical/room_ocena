package com.example.app;

import androidx.room.Entity;

@Entity (tableName = "film")
public class Film {
    private int id;
    private String nazwa;
    private int kategoriaWiekowa;
    private boolean czyWioska;
    private String tworca;
    private  String gatunek;
    public Film(){

    }

    public Film(int id, String nazwa, int kategoriaWiekowa, boolean czyWioska, String tworca, String gatunek) {
        this.id = id;
        this.nazwa = nazwa;
        this.kategoriaWiekowa = kategoriaWiekowa;
        this.czyWioska = czyWioska;
        this.tworca = tworca;
        this.gatunek = gatunek;
    }

}
