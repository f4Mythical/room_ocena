package com.example.app;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
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
    private ListView listView;

    private FilmDataBase filmDataBase;
    private List<Film> filmList;
    private ArrayAdapter<Film> arrayAdapter;

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
        arrayAdapter = new ArrayAdapter<>(
                MainActivity.this,
                android.R.layout.simple_list_item_1,
                filmList
        );
        listView.setAdapter(arrayAdapter);
//        if (filmDataBase.zwrocWszystkieFilmyDAO().zwrocWszystkieFilmy().isEmpty()) {
//            filmDataBase.zwrocWszystkieFilmyDAO().wstawFilmDoBazy(
//                    new Film("Atlantyk", 12, false, "Jan Kowalski", "Dramat")
//            );
//        }
        btnSend.setOnClickListener(v -> {
//        filmDataBase.zwrocWszystkieFilmyDAO().wstawFilmDoBazy(
//            new Film(
//                    eTName.getText().toString().trim(),
//                    Integer.parseInt(eTAgeCategory.getText().toString().trim()),
//                    czyWioska.isChecked(),
//                    eTOwner.getText().toString().trim(),
//                    spinner.getSelectedItem().toString()
//            )
//        );
            Film film = new Film(
                    eTName.getText().toString().trim(),
                    Integer.parseInt(eTAgeCategory.getText().toString().trim()),
                    czyWioska.isChecked(),
                    eTOwner.getText().toString().trim(),
                    spinner.getSelectedItem().toString()
            );
            filmDataBase.zwrocWszystkieFilmyDAO().wstawFilmDoBazy(film);
            filmList.clear();
            filmList.addAll(filmDataBase.zwrocWszystkieFilmyDAO().zwrocWszystkieFilmy());
            arrayAdapter.notifyDataSetChanged();
            wyczysc();
        });
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Film doUsuniecia = filmList.get(i);
            filmDataBase.zwrocWszystkieFilmyDAO().usunZBazy(doUsuniecia);
            filmList.remove(i);
            arrayAdapter.notifyDataSetChanged();
        });
    }

    private void ini() {
        eTName = findViewById(R.id.eTName);
        eTAgeCategory = findViewById(R.id.eTAgeCategory);
        eTOwner = findViewById(R.id.eTOwner);
        spinner = findViewById(R.id.spinner);
        btnSend = findViewById(R.id.btnSend);
        czyWioska = findViewById(R.id.checkBoxVillage);
        listView = findViewById(R.id.listView);
    }
    private void wyczysc(){
        eTName.setText("");
        eTAgeCategory.setText("");
        eTOwner.setText("");
        czyWioska.setChecked(false);
    }

}