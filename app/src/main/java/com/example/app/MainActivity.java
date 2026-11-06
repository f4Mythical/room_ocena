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

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText eTName,eTAgeCategory,eTOwner;
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
        // builderstronger - na message.yes message.no pod anlunowanie


    }
    private void ini(){
        eTName = findViewById(R.id.eTName);
        eTAgeCategory = findViewById(R.id.eTAgeCategory);
        eTOwner = findViewById(R.id.eTOwner);
        spinner = findViewById(R.id.spinner);
        btnSend = findViewById(R.id.btnSend);
        czyWioska = findViewById(R.id.checkBoxVillage);
    }
}