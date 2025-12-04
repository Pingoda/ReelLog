package it.pingoda.reellogapp.Responses;

public class CreateListResponse {
    private boolean success;
    private int status_code;
    private int list_id;

    public int getListId() { return list_id; }
    public boolean isSuccess() { return success; }
}