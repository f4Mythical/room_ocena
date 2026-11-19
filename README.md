# Room Ocena

Prosta aplikacja na Androida napisana w Javie, w której zarządzasz własną listą filmów. Powstała, żeby pokazać, jak używać biblioteki **Room** do trzymania danych w lokalnej bazie: dodawać filmy, wyświetlać je, edytować i usuwać.



---

## Spis treści

- [Jak uruchomić projekt](#jak-uruchomić-projekt)
- [Jak wygląda baza danych](#jak-wygląda-baza-danych)
- [Z czego składa się projekt](#z-czego-składa-się-projekt)
- [Co potrafi aplikacja](#co-potrafi-aplikacja)
- [Budowa krok po kroku](#budowa-krok-po-kroku)
- [Podgląd bazy w App Inspection](#podgląd-bazy-w-app-inspection)
- [Dobrze wiedzieć](#dobrze-wiedzieć)

---

## Jak uruchomić projekt

1. Otwórz projekt w Android Studio.
2. Poczekaj, aż Gradle skończy synchronizację.
3. Uruchom aplikację na emulatorze albo na prawdziwym telefonie.

Projekt korzysta z Javy 11 i działa od Androida API 26.

Zależności Room są w pliku `app/build.gradle.kts`:

```kotlin
implementation("androidx.room:room-runtime:2.6.1")
annotationProcessor("androidx.room:room-compiler:2.6.1")
```

Jeśli coś w nich zmienisz, kliknij **Sync Project with Gradle Files**. Do samego Room nie musisz niczego dopisywać w `AndroidManifest.xml`.

---

## Jak wygląda baza danych

Baza nazywa się **`film_db`**, a tabela z filmami **`film`**.

| Pole | Typ | Opis |
| --- | --- | --- |
| `id` | `int` | Klucz główny, nadawany automatycznie |
| `nazwa` | `String` | Tytuł filmu |
| `kategoriaWiekowa` | `int` | Kategoria wiekowa |
| `czyWioska` | `boolean` | Czy film ma być dostępny w wioskach |
| `tworca` | `String` | Twórca filmu |
| `gatunek` | `String` | Gatunek filmu |

---

## Z czego składa się projekt

Wszystkie pliki Javy leżą w `app/src/main/java/com/example/app/`.

- **`Film.java`**: encja Room (`@Entity`), czyli opis jednego filmu. Ma publiczne pola, automatyczny klucz główny i metodę `toString()`, która decyduje o tym, jak film wygląda na liście.
- **`FilmDAO.java`**: interfejs z metodami do dodawania, aktualizowania, usuwania i pobierania filmów.
- **`FilmDataBase.java`**: klasa bazy danych, która dziedziczy po `RoomDatabase`.
- **`MainActivity.java`**: tworzy bazę, obsługuje formularz i pokazuje filmy w `ListView`.
- **`app/src/main/res/layout/activity_main.xml`**: układ głównego ekranu.
- **`app/src/main/res/values/strings.xml`**: gatunki w spinnerze (Horror i Musical).

---

## Co potrafi aplikacja

### Dodawanie filmu

Wpisz dane w formularzu, zaznacz checkbox, jeśli film ma być dostępny w wioskach, wybierz gatunek i kliknij **Wstaw**. Film trafi do bazy i od razu pojawi się na liście.

### Edycja filmu

Przytrzymaj wybraną pozycję na liście. Jej dane wpiszą się do formularza. Popraw, co trzeba, i kliknij **Aktualizuj**.

### Usuwanie filmu

Dotknij pozycji na liście. Aplikacja zapyta, czy na pewno chcesz ją usunąć. **Tak** usuwa film, **Nie** anuluje.

---

## Budowa krok po kroku

### Etap 1: przygotowanie bazy Room

Dodaj zależności Room do Gradle i zsynchronizuj projekt. Potem stwórz trzy podstawowe elementy: encję `Film`, interfejs `FilmDAO` i klasę `FilmDataBase`.

#### `Film.java`

```java
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

    public Film(String nazwa, int kategoriaWiekowa, boolean czyWioska,
                String tworca, String gatunek) {
        this.nazwa = nazwa;
        this.kategoriaWiekowa = kategoriaWiekowa;
        this.czyWioska = czyWioska;
        this.tworca = tworca;
        this.gatunek = gatunek;
    }

    @Override
    public String toString() {
        return nazwa + " | " + kategoriaWiekowa + "+ | "
                + (czyWioska ? "wioski: tak" : "wioski: nie")
                + " | " + tworca + " | " + gatunek;
    }
}
```

#### `FilmDAO.java`

```java
@Dao
public interface FilmDAO {
    @Insert
    void wstawFilmDoBazy(Film film);

    @Insert
    void wstawFilmyDoBazy(Film... filmy);

    @Delete
    void usunZBazy(Film film);

    @Update
    void aktualizujFilm(Film film);

    @Query("SELECT * FROM film")
    List<Film> zwrocWszystkieFilmy();
}
```

#### `FilmDataBase.java`

```java
@Database(entities = {Film.class}, version = 1)
public abstract class FilmDataBase extends RoomDatabase {
    public abstract FilmDAO zwrocWszystkieFilmyDAO();
}
```

W `MainActivity` bazę tworzysz, podając kontekst aplikacji i nazwę pliku bazy:

```java
filmDataBase = Room.databaseBuilder(
        getApplicationContext(),
        FilmDataBase.class,
        "film_db"
)
.allowMainThreadQueries()
.fallbackToDestructiveMigration()
.build();
```

Jeśli chcesz, żeby na start było w niej coś do zobaczenia, dodaj przykładowy film, gdy baza jest pusta:

```java
if (filmDataBase.zwrocWszystkieFilmyDAO()
        .zwrocWszystkieFilmy().isEmpty()) {
    filmDataBase.zwrocWszystkieFilmyDAO().wstawFilmDoBazy(
            new Film("Atlantyk", 12, false, "Jan Kowalski", "Dramat")
    );
}
```

### Etap 2: lista filmów i dodawanie

Pobierz filmy z bazy, zrób z nich `ArrayAdapter` i podepnij go do `ListView`. Po kliknięciu **Wstaw** tworzysz nowy obiekt `Film`, zapisujesz go przez DAO i odświeżasz listę.

Pola w `MainActivity`:

```java
private FilmDataBase filmDataBase;
private List<Film> filmList;
private ArrayAdapter<Film> arrayAdapter;
private ListView listView;
private EditText eTName, eTAgeCategory, eTOwner;
private Spinner spinner;
private CheckBox czyWioska;
private Button btnSend;
```

Gdy widoki są już połączone przez `findViewById(...)`, tworzysz bazę i adapter:

```java
filmDataBase = Room.databaseBuilder(
        getApplicationContext(),
        FilmDataBase.class,
        "film_db"
)
.allowMainThreadQueries()
.fallbackToDestructiveMigration()
.build();

filmList = filmDataBase.zwrocWszystkieFilmyDAO()
        .zwrocWszystkieFilmy();

arrayAdapter = new ArrayAdapter<>(
        MainActivity.this,
        android.R.layout.simple_list_item_1,
        filmList
);

listView.setAdapter(arrayAdapter);
```

Obsługa przycisku **Wstaw**:

```java
btnSend.setOnClickListener(view -> {
    Film film = new Film(
            eTName.getText().toString().trim(),
            Integer.parseInt(eTAgeCategory.getText().toString().trim()),
            czyWioska.isChecked(),
            eTOwner.getText().toString().trim(),
            spinner.getSelectedItem().toString()
    );

    filmDataBase.zwrocWszystkieFilmyDAO()
            .wstawFilmDoBazy(film);

    filmList.clear();
    filmList.addAll(
            filmDataBase.zwrocWszystkieFilmyDAO()
                    .zwrocWszystkieFilmy()
    );

    arrayAdapter.notifyDataSetChanged();

    eTName.setText("");
    eTAgeCategory.setText("");
    eTOwner.setText("");
    czyWioska.setChecked(false);
});
```

### Etap 3: usuwanie po kliknięciu

Na początek najprostsza wersja: kliknięcie filmu od razu usuwa go z bazy i z listy.

```java
listView.setOnItemClickListener((adapterView, view, position, id) -> {
    Film filmDoUsuniecia = filmList.get(position);

    filmDataBase.zwrocWszystkieFilmyDAO()
            .usunZBazy(filmDoUsuniecia);

    filmList.remove(position);
    arrayAdapter.notifyDataSetChanged();
});
```

### Etap 4: edycja po przytrzymaniu

Do układu dodaj przycisk aktualizacji. Na początku niech będzie niewidoczny:

```xml
<Button
    android:id="@+id/btnUpdate"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="Aktualizuj"
    android:visibility="invisible" />
```

W `MainActivity` potrzebujesz pola na edytowany film i przycisk:

```java
private Film edytowanyFilm;
private Button btnUpdate;
```

Przycisk łączysz z widokiem tam, gdzie reszta inicjalizacji:

```java
btnUpdate = findViewById(R.id.btnUpdate);
```

Przytrzymanie pozycji zapamiętuje film i wpisuje jego dane do formularza:

```java
listView.setOnItemLongClickListener((adapterView, view, position, id) -> {
    edytowanyFilm = filmList.get(position);

    eTName.setText(edytowanyFilm.nazwa);
    eTAgeCategory.setText(
            String.valueOf(edytowanyFilm.kategoriaWiekowa)
    );
    eTOwner.setText(edytowanyFilm.tworca);
    czyWioska.setChecked(edytowanyFilm.czyWioska);

    for (int i = 0; i < spinner.getCount(); i++) {
        if (spinner.getItemAtPosition(i).toString()
                .equals(edytowanyFilm.gatunek)) {
            spinner.setSelection(i);
            break;
        }
    }

    btnUpdate.setVisibility(View.VISIBLE);
    return true;
});
```

Po kliknięciu **Aktualizuj** dane obiektu się zmieniają i trafiają do bazy przez DAO:

```java
btnUpdate.setOnClickListener(view -> {
    if (edytowanyFilm == null) {
        return;
    }

    edytowanyFilm.nazwa = eTName.getText().toString().trim();
    edytowanyFilm.kategoriaWiekowa = Integer.parseInt(
            eTAgeCategory.getText().toString().trim()
    );
    edytowanyFilm.czyWioska = czyWioska.isChecked();
    edytowanyFilm.tworca = eTOwner.getText().toString().trim();
    edytowanyFilm.gatunek = spinner.getSelectedItem().toString();

    filmDataBase.zwrocWszystkieFilmyDAO()
            .aktualizujFilm(edytowanyFilm);

    arrayAdapter.notifyDataSetChanged();

    edytowanyFilm = null;
    btnUpdate.setVisibility(View.INVISIBLE);

    eTName.setText("");
    eTAgeCategory.setText("");
    eTOwner.setText("");
    czyWioska.setChecked(false);
});
```

### Etap 5: potwierdzenie usunięcia

Zamiast kasować film od razu, pokaż okno z pytaniem. Film znika dopiero po kliknięciu **Tak**:

```java
listView.setOnItemClickListener((adapterView, view, position, id) -> {
    Film filmDoUsuniecia = filmList.get(position);

    new AlertDialog.Builder(MainActivity.this)
            .setTitle("Usuwanie")
            .setMessage(
                    "Czy na pewno usunąć film "
                            + filmDoUsuniecia.nazwa + "?"
            )
            .setPositiveButton("Tak", (dialog, which) -> {
                filmDataBase.zwrocWszystkieFilmyDAO()
                        .usunZBazy(filmDoUsuniecia);

                filmList.remove(filmDoUsuniecia);
                arrayAdapter.notifyDataSetChanged();

                if (filmDoUsuniecia == edytowanyFilm) {
                    edytowanyFilm = null;
                    btnUpdate.setVisibility(View.INVISIBLE);
                }
            })
            .setNegativeButton("Nie", null)
            .show();
});
```

Do `MainActivity.java` dodaj jeszcze import:

```java
import androidx.appcompat.app.AlertDialog;
```

---
---

## Dobrze wiedzieć

- `allowMainThreadQueries()` jest tu tylko dla wygody, bo to projekt ćwiczeniowy. W prawdziwej aplikacji zapytania do bazy powinny działać poza głównym wątkiem.
- `fallbackToDestructiveMigration()` ułatwia życie podczas nauki, ale przy zmianie wersji bazy dotychczasowe dane przepadną.
