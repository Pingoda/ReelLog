package it.pingoda.reellogapp.Responses;

import java.util.List;

import it.pingoda.reellogapp.Models.ListDetails;

public class ListsResponse {

    public int page;
    public List<ListDetails> results;
    public int total_pages;
    public int total_results;

}
