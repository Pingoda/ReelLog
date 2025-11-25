package it.pingoda.reellogapp;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GenreListResponse {

    @SerializedName("genres")
    public List<Genre> genres;
}