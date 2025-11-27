package it.pingoda.reellogapp.Responses;

import com.google.gson.annotations.SerializedName;
import java.util.List;

import it.pingoda.reellogapp.Models.Genre;

public class GenreListResponse {

    @SerializedName("genres")
    public List<Genre> genres;
}