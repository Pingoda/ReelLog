package it.pingoda.reellogapp.Responses;

import it.pingoda.reellogapp.Models.ListDetails;
import java.util.List;

public class UserListsResponse {
    private int page;
    private List<ListDetails> results;
    private int total_pages;

    public List<ListDetails> getResults() {
        return results;
    }
}