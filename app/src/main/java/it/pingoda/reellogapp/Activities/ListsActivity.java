package it.pingoda.reellogapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import it.pingoda.reellogapp.BuildConfig;
import it.pingoda.reellogapp.Models.CreateList;
import it.pingoda.reellogapp.R;
import it.pingoda.reellogapp.Responses.CreateListResponse;
import it.pingoda.reellogapp.Responses.UserListsResponse;
import it.pingoda.reellogapp.Services.TMDbListsApi;

import it.pingoda.reellogapp.Models.ListDetails;
import it.pingoda.reellogapp.Fragments.CreateListDialogFragment;
import it.pingoda.reellogapp.Adapters.ListAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.ArrayList;
import java.util.List;


public class ListsActivity extends AppCompatActivity implements CreateListDialogFragment.CreateListListener {

    private static final String TMDB_API_KEY = BuildConfig.TMDB_API_KEY;
    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/";

    private static final String USER_SESSION_ID = BuildConfig.TMDB_SESSION_ID;
    private static final int USER_ACCOUNT_ID = 22375234;

    private TMDbListsApi tmdbListsApi;
    private Call<CreateListResponse> currentCreateListCall;
    private RecyclerView recyclerLists;
    private FloatingActionButton createListButton;
    private ListAdapter listAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        getWindow().setStatusBarColor(getResources().getColor(R.color.sfondoNero));
        recyclerLists = findViewById(R.id.recyclerListResults);
        createListButton = findViewById(R.id.createListButton);



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TMDB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        tmdbListsApi = retrofit.create(TMDbListsApi.class);

        listAdapter = new ListAdapter(new ArrayList<>(), tmdbListsApi);
        setupRecycler(recyclerLists);
        recyclerLists.setAdapter(listAdapter);

        createListButton.setOnClickListener(v -> showCreateListDialog());

        loadUserLists();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_lists);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_lists) {
                return true;
            } else if (id == R.id.nav_search) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_profile) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_home) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }

    private void setupRecycler(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void showCreateListDialog() {
        CreateListDialogFragment dialog = new CreateListDialogFragment();
        dialog.show(getSupportFragmentManager(), "CreateListDialogTag");
    }

    @Override
    public void onListCreated(String name, String description) {
        createNewList(name, description);
    }

    private void createNewList(String listName, String listDescription) {

        final String languageCode = "it";

        CreateList requestBody = new CreateList(
                listName,
                listDescription,
                languageCode
        );

        currentCreateListCall = tmdbListsApi.createList(requestBody, TMDB_API_KEY, USER_SESSION_ID);

        currentCreateListCall.enqueue(new Callback<CreateListResponse>() {
            @Override
            public void onResponse(@NonNull Call<CreateListResponse> call, @NonNull Response<CreateListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CreateListResponse result = response.body();
                    Toast.makeText(ListsActivity.this,
                            "Lista '" + listName + "' creata con ID: " + result.getListId(),
                            Toast.LENGTH_LONG).show();

                    loadUserLists();

                } else {
                    Toast.makeText(ListsActivity.this,
                            "Errore creazione: Codice " + response.code(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CreateListResponse> call, @NonNull Throwable t) {
                if (call.isCanceled()) return;
                Toast.makeText(ListsActivity.this,
                        "Errore di rete: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadUserLists() {
        Call<UserListsResponse> call = tmdbListsApi.getUserLists(USER_ACCOUNT_ID, TMDB_API_KEY, USER_SESSION_ID);

        call.enqueue(new Callback<UserListsResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserListsResponse> call, @NonNull Response<UserListsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ListDetails> lists = response.body().getResults();
                    listAdapter.setLists(lists);
                } else {
                    Toast.makeText(ListsActivity.this, "Errore caricamento liste: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserListsResponse> call, @NonNull Throwable t) {
                Toast.makeText(ListsActivity.this, "Errore di rete durante il caricamento", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (currentCreateListCall != null && !currentCreateListCall.isCanceled()) {
            currentCreateListCall.cancel();
        }
    }
}