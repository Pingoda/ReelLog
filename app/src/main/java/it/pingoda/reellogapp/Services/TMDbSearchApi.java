package it.pingoda.reellogapp.Services;

import it.pingoda.reellogapp.Responses.SearchResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TMDbSearchApi {

    @GET("search/multi")
    Call<SearchResponse> searchMulti(
            @Query("api_key") String apiKey,
            @Query("query") String query,
            @Query("language") String language
    );
}