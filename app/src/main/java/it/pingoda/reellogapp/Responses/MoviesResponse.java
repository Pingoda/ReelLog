package it.pingoda.reellogapp.Responses;

import java.util.List;

import it.pingoda.reellogapp.Models.Movie;

public class MoviesResponse {

    public int page;
    public List<Movie> results;
    public int total_pages;
    public int total_results;
}