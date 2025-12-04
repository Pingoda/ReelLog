package it.pingoda.reellogapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

import it.pingoda.reellogapp.Models.Movie;
import it.pingoda.reellogapp.R;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private Context context;
    private List<Movie> movieList;

    public MoviesAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        if (movie.title != null && !movie.title.isEmpty()) {
            holder.title.setText(movie.title);
        } else {
            holder.title.setText(movie.name);
        }

        String urlImmagine = "https://image.tmdb.org/t/p/w500" + movie.poster_path;
        Glide.with(context).load(urlImmagine).into(holder.poster);
    }

    @Override
    public int getItemCount() {
        if (movieList == null) return 0;
        return Math.min(movieList.size(), 15);
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView title;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.itemPoster);
            title = itemView.findViewById(R.id.itemTitle);
        }
    }
}