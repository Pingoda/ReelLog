package it.pingoda.reellogapp.Services;

import it.pingoda.reellogapp.Models.CreateList;
import it.pingoda.reellogapp.Responses.CreateListResponse;
import it.pingoda.reellogapp.Responses.ListItemsResponse;
import it.pingoda.reellogapp.Responses.UserListsResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDbListsApi {

    @Headers({
            "accept: application/json",
            "content-type: application/json"
    })
    @POST("list")
    Call<CreateListResponse> createList(
            @Body CreateList requestBody,
            @Query("api_key") String apiKey,
            @Query("session_id") String sessionId
    );

    @GET("account/{account_id}/lists")
    Call<UserListsResponse> getUserLists(
            @Path("account_id") int accountId,
            @Query("api_key") String apiKey,
            @Query("session_id") String sessionId
    );

    @GET("list/{list_id}")
    Call<ListItemsResponse> getListItems(
            @Path("list_id") int listId,
            @Query("api_key") String apiKey
    );
}