package it.pingoda.reellogapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.Date;

import it.pingoda.reellogapp.Responses.GenreCache;
import it.pingoda.reellogapp.Models.Movie;
import it.pingoda.reellogapp.R;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MovieViewHolder> {

    private static final String TAG = "SearchAdapter";
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    private List<Movie> movieList;
    private final Context context;

    public SearchAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        public ImageView posterImageView;
        public TextView titleTextView;
        public TextView altTitleTextView;
        public TextView ratingTextView;
        public TextView releaseDateTextView;
        public TextView mediaTypeTextView;
        public TextView genreTextView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            posterImageView = itemView.findViewById(R.id.moviePoster);

            titleTextView = itemView.findViewById(R.id.resultTitle);
            altTitleTextView = itemView.findViewById(R.id.resultAltTitle);
            ratingTextView = itemView.findViewById(R.id.rating);
            releaseDateTextView = itemView.findViewById(R.id.releaseDate);
            mediaTypeTextView = itemView.findViewById(R.id.mediaType);
            genreTextView = itemView.findViewById(R.id.genre);
        }
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_result, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        if (holder.posterImageView != null) {

            if (movie.poster_path != null && !movie.poster_path.isEmpty()) {
                String imageUrl = IMAGE_BASE_URL + movie.poster_path;

                Glide.with(context)
                        .load(imageUrl)
                        .placeholder(R.drawable.poster_placeholder)
                        .error(R.drawable.image_error)
                        .into(holder.posterImageView);
            } else {
                holder.posterImageView.setImageResource(R.drawable.no_poster_available);
            }
        } else {
            Log.e(TAG, "L'ImageView per il poster è NULLA! Verifica che l'ID usato in findViewById sia corretto.");
        }

        String mainTitle = (movie.title != null) ? movie.title : "Titolo Sconosciuto";
        holder.titleTextView.setText(mainTitle);

        holder.altTitleTextView.setText(movie.original_title != null ? movie.original_title : "N/A");

        String rating = String.format(Locale.getDefault(), "%.1f/10", movie.vote_average);
        holder.ratingTextView.setText(rating);

        if (movie.release_date != null && !movie.release_date.isEmpty()) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                Date date = inputFormat.parse(movie.release_date);

                SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ITALIAN);

                assert date != null;
                String formattedDate = outputFormat.format(date);
                holder.releaseDateTextView.setText(formattedDate);
            } catch (Exception e) {
                Log.e(TAG, "Errore nella formattazione della data: " + movie.release_date, e);
                holder.releaseDateTextView.setText(movie.release_date);
            }
        } else {
            holder.releaseDateTextView.setText("Data Sconosciuta");
        }

        holder.mediaTypeTextView.setText("Film");

        String allGenreNames = GenreCache.getGenreNames(movie.genre_ids);
        String firstGenre = allGenreNames;

        if (!allGenreNames.isEmpty()) {
            int commaIndex = allGenreNames.indexOf(',');
            if (commaIndex > 0) {
                firstGenre = allGenreNames.substring(0, commaIndex).trim();
            }
        }

        holder.genreTextView.setText(firstGenre);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public void updateList(List<Movie> newList) {
        this.movieList = newList;
        notifyDataSetChanged();
    }
}