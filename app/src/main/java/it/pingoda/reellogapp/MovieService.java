package it.pingoda.reellogapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieService {

    @GET("3/movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("3/movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("3/tv/popular")
    Call<MoviesResponse> getPopularSeries(@Query("api_key") String apiKey);

    @GET("3/discover/tv?sort_by=vote_average.desc&vote_count.gte=200")
    Call<MoviesResponse> getTopRatedSeries(@Query("api_key") String apiKey);
}