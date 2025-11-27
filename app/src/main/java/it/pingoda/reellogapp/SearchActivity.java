package it.pingoda.reellogapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {

    private static final String TMDB_API_KEY = "340865ece50ffcd840ec7a6115eadcaf";
    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/";
    private static final String API_LANGUAGE = "it-IT";
    private static final String MEDIA_TYPE_MOVIE = "movie";

    private EditText searchBar;
    private RecyclerView recyclerView;
    private SearchAdapter searchAdapter;
    private TMDbSearchApi tmdbSearchApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getWindow().setStatusBarColor(getResources().getColor(R.color.sfondoNero));

        initRetrofit();
        GenreCache.initialize(initGenreApi(), TMDB_API_KEY);
        GenreCache.loadGenres();

        searchBar = findViewById(R.id.search_bar);
        recyclerView = findViewById(R.id.recyclerSearchResults);

        List<Movie> initialMovieList = new ArrayList<>();
        searchAdapter = new SearchAdapter(this, initialMovieList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(searchAdapter);

        setupSearchListener();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setSelectedItemId(R.id.nav_search);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                Intent intent = new Intent(getApplicationContext(), Homepage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                return true;

            } else if (id == R.id.nav_search) {
                return true;
            }
            return false;
        });
    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TMDB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        tmdbSearchApi = retrofit.create(TMDbSearchApi.class);
    }

    private TMDbGenreApi initGenreApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TMDB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(TMDbGenreApi.class);
    }

    private void setupSearchListener() {
        searchBar.setOnEditorActionListener((v, actionId, event) -> {
            boolean isSearchAction = actionId == EditorInfo.IME_ACTION_SEARCH;
            boolean isEnterKey = (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN);

            if (isSearchAction || isEnterKey) {
                String query = v.getText().toString().trim();
                if (!query.isEmpty()) {
                    fetchMoviesFromApi(query);
                } else {
                    searchAdapter.updateList(new ArrayList<>());
                }
                return true;
            }
            return false;
        });
    }

    private void fetchMoviesFromApi(String query) {
        tmdbSearchApi.searchMulti(TMDB_API_KEY, query, API_LANGUAGE).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> rawResults = response.body().results;
                    List<Movie> filteredResults = new ArrayList<>();

                    for (Movie movie : rawResults) {
                        if (movie.media_type != null && movie.media_type.equals(MEDIA_TYPE_MOVIE)) {
                            filteredResults.add(movie);
                        }
                    }
                    searchAdapter.updateList(filteredResults);
                } else {
                    Toast.makeText(SearchActivity.this, "Errore API", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Toast.makeText(SearchActivity.this, "Errore di connessione", Toast.LENGTH_SHORT).show();
            }
        });
    }
}