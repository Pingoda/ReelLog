package it.pingoda.reellogapp.Responses;

import com.google.gson.annotations.SerializedName;
import java.util.List;

import it.pingoda.reellogapp.Models.Movie;

public class SearchResponse {

    @SerializedName("results")
    public List<Movie> results;

    @SerializedName("page")
    public int page;

    @SerializedName("total_pages")
    public int totalPages;

    @SerializedName("total_results")
    public int totalResults;
}