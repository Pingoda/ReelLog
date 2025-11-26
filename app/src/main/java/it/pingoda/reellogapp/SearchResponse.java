package it.pingoda.reellogapp;

import com.google.gson.annotations.SerializedName;
import java.util.List;

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