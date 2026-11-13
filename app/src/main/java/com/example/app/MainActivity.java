package com.example.app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText eTName, eTAgeCategory, eTOwner;
    private Button btnSend;
    private Spinner spinner;
    private CheckBox czyWioska;

    private FilmDataBase filmDataBase;
    private List<Film> filmList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ini();
        filmDataBase = Room.databaseBuilder(
                        getApplicationContext(), FilmDataBase.class, "film_db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        filmList = filmDataBase.zwrocWszystkieFilmyDAO().zwrocWszystkieFilmy();

//        if (filmDataBase.zwrocWszystkieFilmyDAO().zwrocWszystkieFilmy().isEmpty()) {
//            filmDataBase.zwrocWszystkieFilmyDAO().wstawFilmDoBazy(
//                    new Film("Atlantyk", 12, false, "Jan Kowalski", "Dramat")
//            );
//        }
        btnSend.setOnClickListener(v -> {
        filmDataBase.zwrocWszystkieFilmyDAO().wstawFilmDoBazy(
            new Film(
                    eTName.getText().toString().trim(),
                    Integer.parseInt(eTAgeCategory.getText().toString().trim()),
                    czyWioska.isChecked(),
                    eTOwner.getText().toString().trim(),
                    spinner.getSelectedItem().toString()
            )
        );
        });
    }

    private void ini() {
        eTName = findViewById(R.id.eTName);
        eTAgeCategory = findViewById(R.id.eTAgeCategory);
        eTOwner = findViewById(R.id.eTOwner);
        spinner = findViewById(R.id.spinner);
        btnSend = findViewById(R.id.btnSend);
        czyWioska = findViewById(R.id.checkBoxVillage);
    }

}