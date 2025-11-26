package it.pingoda.reellogapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TMDbGenreApi {

    @GET("genre/movie/list")
    Call<GenreListResponse> getMovieGenres(
            @Query("api_key") String apiKey
    );
}