package it.pingoda.reellogapp.Models;

public class ListDetails {
    private String description;
    private int id;
    private String name;
    private int item_count;
    private String poster_path;

    public String getName() { return name; }
    public int getId() { return id; }
    public int getItemCount() { return item_count; }
    public String getPosterPath() { return poster_path; }
}