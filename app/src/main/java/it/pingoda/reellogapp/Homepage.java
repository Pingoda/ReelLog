package it.pingoda.reellogapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Homepage extends AppCompatActivity {

    private static final String TMDB_API_KEY = "340865ece50ffcd840ec7a6115eadcaf";
    private static final String BASE_URL = "https://api.themoviedb.org/";

    private RecyclerView recyclerPopular;
    private RecyclerView recyclerTopRated;
    private RecyclerView recyclerTvSeries;
    private RecyclerView recyclerTopTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        getWindow().setStatusBarColor(getResources().getColor(R.color.sfondoNero));

        recyclerPopular = findViewById(R.id.recyclerPopular);
        recyclerTopRated = findViewById(R.id.recyclerTopRated);
        recyclerTvSeries = findViewById(R.id.recyclerTvSeries);
        recyclerTopTv = findViewById(R.id.recyclerTopTv);

        setupRecycler(recyclerPopular);
        setupRecycler(recyclerTopRated);
        setupRecycler(recyclerTvSeries);
        setupRecycler(recyclerTopTv);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieService movieService = retrofit.create(MovieService.class);

        movieService.getPopularMovies(TMDB_API_KEY).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recyclerPopular.setAdapter(new MoviesAdapter(Homepage.this, response.body().results));
                }
            }
            @Override public void onFailure(Call<MoviesResponse> call, Throwable t) {}
        });

        movieService.getTopRatedMovies(TMDB_API_KEY).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recyclerTopRated.setAdapter(new MoviesAdapter(Homepage.this, response.body().results));
                }
            }
            @Override public void onFailure(Call<MoviesResponse> call, Throwable t) {}
        });

        movieService.getPopularSeries(TMDB_API_KEY).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recyclerTvSeries.setAdapter(new MoviesAdapter(Homepage.this, response.body().results));
                }
            }
            @Override public void onFailure(Call<MoviesResponse> call, Throwable t) {}
        });

        movieService.getTopRatedSeries(TMDB_API_KEY).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recyclerTopTv.setAdapter(new MoviesAdapter(Homepage.this, response.body().results));
                }
            }
            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.e("API", "Error", t);
            }
        });
    }

    private void setupRecycler(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }
}