package it.pingoda.reellogapp.Responses;

import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import it.pingoda.reellogapp.Services.TMDbGenreApi;
import it.pingoda.reellogapp.Models.Genre;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenreCache {

    private static final String TAG = "GenreCache";
    private static Map<Integer, String> genreMap = new HashMap<>();

    private static AtomicBoolean isLoaded = new AtomicBoolean(false);

    private static TMDbGenreApi tmdbGenreApi;
    private static String apiKey;

    public static void initialize(TMDbGenreApi api, String key) {
        tmdbGenreApi = api;
        apiKey = key;
    }

    public static void loadGenres() {
        if (isLoaded.get() || tmdbGenreApi == null || apiKey == null) {
            if (isLoaded.get()) {
                Log.d(TAG, "Generi già caricati.");
            }
            return;
        }

        tmdbGenreApi.getMovieGenres(apiKey).enqueue(new Callback<GenreListResponse>() {
            @Override
            public void onResponse(Call<GenreListResponse> call, Response<GenreListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Genre> genres = response.body().genres;

                    if (genres != null) {
                        for (Genre genre : genres) {
                            genreMap.put(genre.id, genre.name);
                        }
                        isLoaded.set(true);
                        Log.i(TAG, "Caricamento generi riuscito. Totale: " + genreMap.size());
                    }
                } else {
                    Log.e(TAG, "Caricamento generi fallito: Codice " + response.code());
                }
            }

            @Override
            public void onFailure(Call<GenreListResponse> call, Throwable t) {
                Log.e(TAG, "Errore di connessione per i generi: " + t.getMessage(), t);
            }
        });
    }

    public static String getGenreNames(List<Integer> genreIds) {
        if (genreIds == null || genreIds.isEmpty() || genreMap.isEmpty()) {
            return "N/D";
        }

        StringBuilder sb = new StringBuilder();
        boolean first = true;

        for (int id : genreIds) {
            String name = genreMap.get(id);
            if (name != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(name);
                first = false;
            }
        }

        return sb.toString();
    }
}