package it.pingoda.reellogapp.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import it.pingoda.reellogapp.BuildConfig;
import it.pingoda.reellogapp.R;
import it.pingoda.reellogapp.Models.ListDetails;
import it.pingoda.reellogapp.Models.ListItem;
import it.pingoda.reellogapp.Responses.ListItemsResponse;
import it.pingoda.reellogapp.Services.TMDbListsApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    private List<ListDetails> listDetails;
    private final TMDbListsApi apiService;
    private final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w185";

    private static final int POSTER_WIDTH_DP = 129;
    private static final int POSTER_HEIGHT_DP = 74;

    public ListAdapter(List<ListDetails> listDetails, TMDbListsApi apiService) {
        this.listDetails = listDetails;
        this.apiService = apiService;
    }

    public void setLists(List<ListDetails> newList) {
        this.listDetails = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        ListDetails currentList = listDetails.get(position);

        holder.listName.setText(currentList.getName());
        holder.listCount.setText("Elementi: " + currentList.getItemCount());

        String posterPath = currentList.getPosterPath();

        if ((posterPath == null || posterPath.isEmpty()) && currentList.getItemCount() > 0) {
            generateCollageOrPlaceholder(holder.listPoster, currentList.getId());
        } else if (posterPath != null && !posterPath.isEmpty()) {
            String fullImageUrl = IMAGE_BASE_URL + posterPath;
            Glide.with(holder.itemView.getContext())
                    .load(fullImageUrl)
                    .placeholder(R.drawable.placeholder_list_default_icon)
                    .error(R.drawable.placeholder_list_default_icon)
                    .into(holder.listPoster);
        } else {
            holder.listPoster.setImageResource(R.drawable.placeholder_list_default_icon);
        }
    }

    @Override
    public int getItemCount() {
        return listDetails != null ? listDetails.size() : 0;
    }

    private int dpToPx(int dp, Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    private void generateCollageOrPlaceholder(ImageView targetView, int listId) {
        targetView.setImageResource(R.drawable.placeholder_list_default_icon);

        Call<ListItemsResponse> call = apiService.getListItems(listId, BuildConfig.TMDB_API_KEY);

        call.enqueue(new Callback<ListItemsResponse>() {
            @Override
            public void onResponse(@NonNull Call<ListItemsResponse> call, @NonNull Response<ListItemsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<String> posterPaths = new ArrayList<>();

                    for (ListItem item : response.body().getItems()) {
                        if (posterPaths.size() < 4 && item.getPosterPath() != null && !item.getPosterPath().isEmpty()) {
                            posterPaths.add(item.getPosterPath());
                        }
                    }

                    if (posterPaths.size() >= 2) {
                        generateAndDisplayCollage(targetView, posterPaths);
                    } else {
                        targetView.setImageResource(R.drawable.placeholder_list_default_icon);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ListItemsResponse> call, @NonNull Throwable t) {
                targetView.setImageResource(R.drawable.placeholder_list_default_icon);
            }
        });
    }

    private void generateAndDisplayCollage(ImageView targetView, List<String> posterPaths) {
        final Context context = targetView.getContext();
        final int fixedWidthPx = dpToPx(POSTER_WIDTH_DP, context);
        final int fixedHeightPx = dpToPx(POSTER_HEIGHT_DP, context);

        new Thread(() -> {
            try {
                List<Bitmap> bitmaps = new ArrayList<>();
                String imageCollageBaseUrl = "https://image.tmdb.org/t/p/w92";

                for (String path : posterPaths) {
                    String fullUrl = imageCollageBaseUrl + path;
                    Bitmap bitmap = Glide.with(context)
                            .asBitmap()
                            .load(fullUrl)
                            .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get();
                    bitmaps.add(bitmap);
                }

                Bitmap collageBitmap = combineBitmaps(bitmaps, fixedWidthPx, fixedHeightPx);

                if (collageBitmap != null) {
                    targetView.post(() -> {
                        ViewGroup.LayoutParams params = targetView.getLayoutParams();
                        params.width = fixedWidthPx;
                        params.height = fixedHeightPx;
                        targetView.setLayoutParams(params);

                        targetView.setImageBitmap(collageBitmap);
                    });
                } else {
                    targetView.post(() -> targetView.setImageResource(R.drawable.placeholder_list_default_icon));
                }

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                targetView.post(() -> targetView.setImageResource(R.drawable.placeholder_list_default_icon));
            }
        }).start();
    }

    private Bitmap combineBitmaps(List<Bitmap> bitmaps, int targetW, int targetH) {
        if (targetW <= 0 || targetH <= 0 || bitmaps.isEmpty()) return null;

        Bitmap finalBitmap = Bitmap.createBitmap(targetW, targetH, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(finalBitmap);
        canvas.drawColor(android.graphics.Color.WHITE);

        int num = bitmaps.size();
        float halfW = targetW / 2f;
        float halfH = targetH / 2f;

        RectF[] destRects = new RectF[4];
        destRects[0] = new RectF(0, 0, halfW, halfH);
        destRects[1] = new RectF(halfW, 0, targetW, halfH);
        destRects[2] = new RectF(0, halfH, halfW, targetH);
        destRects[3] = new RectF(halfW, halfH, targetW, targetH);

        for (int i = 0; i < num && i < 4; i++) {
            Bitmap b = bitmaps.get(i);
            if (b != null) {
                RectF destRect = destRects[i];

                float scaleX = destRect.width() / (float) b.getWidth();
                float scaleY = destRect.height() / (float) b.getHeight();

                float scale = Math.max(scaleX, scaleY);

                float newWidth = b.getWidth() * scale;
                float newHeight = b.getHeight() * scale;

                float dx = destRect.left + (destRect.width() - newWidth) / 2f;
                float dy = destRect.top + (destRect.height() - newHeight) / 2f;

                RectF finalDest = new RectF(dx, dy, dx + newWidth, dy + newHeight);

                canvas.drawBitmap(b, null, finalDest, null);
            }
        }
        return finalBitmap;
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        TextView listName;
        TextView listCount;
        ImageView listPoster;

        ListViewHolder(View itemView) {
            super(itemView);
            listName = itemView.findViewById(R.id.listTitle);
            listCount = itemView.findViewById(R.id.listElemNumber);
            listPoster = itemView.findViewById(R.id.listPoster);
        }
    }
}