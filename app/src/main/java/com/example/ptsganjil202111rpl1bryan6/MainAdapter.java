package com.example.ptsganjil202111rpl1bryan6;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.SurfaceHolder.Callback;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private List<MovieModel> movieModel;
    private List<MovieModel> movieModels;
    Context context;
    Callback callback;
    Realm realm;
    RealmHelper realmHelper;

    public interface Callback{
        void onClick (int position);
    }

    public MainAdapter(List<MovieModel> list, Context context, Callback callback){
        this.movieModel = list;
        this.context = context;
        this.callback = callback;
        movieModels = new ArrayList<>(movieModel);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position) {
        Realm.init(context);
        RealmConfiguration configuration = new  RealmConfiguration.Builder().allowWritesOnUiThread(true).build();
        realm = Realm.getInstance(configuration);
        realmHelper = new RealmHelper(realm);

        final MovieModel getData = movieModel.get(position);
        String name = getData.getName();
        String popularity = getData.getPopularity();
        String releaseDate = getData.getReleaseDate();

        holder.name.setText(name);
        holder.popularity.setText(popularity);
        holder.releaseDate.setText(releaseDate);

        Glide.with(holder.image)
                .load("https://image.tmdb.org/t/p/w500"+movieModel.get(position).getImage())
                .into(holder.image);

        if (movieModel.get(position).getFavorite()) {
            holder.favoriteButton.setImageResource(R.drawable.ic_favorite);
        } else {
            holder.favoriteButton.setImageResource(R.drawable.ic_favorite_border);
        }
        holder.favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (movieModel.get(position).getFavorite()) {
                    holder.favoriteButton.setImageResource(R.drawable.ic_favorite_border);
                    Toast.makeText(context, "Film dihapus dari list favorite kamu", Toast.LENGTH_SHORT).show();
                    movieModel.get(position).setFavorite(false);
                    realmHelper.delete(movieModel.get(position));
                } else {
                    movieModel.get(position).setFavorite(true);
                    holder.favoriteButton.setImageResource(R.drawable.ic_favorite);
                    Toast.makeText(context, "Film ditambahkan ke list favorite kamu", Toast.LENGTH_SHORT).show();
                    realmHelper.save(movieModel.get(position));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return (movieModel != null) ? movieModel.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView popularity;
        TextView releaseDate;
        ImageView image;
        ImageView favoriteButton;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            favoriteButton = itemView.findViewById(R.id.imgFavorite);
            name = itemView.findViewById(R.id.tvName);
            popularity = itemView.findViewById(R.id.tvPopularity);
            releaseDate = itemView.findViewById(R.id.tvReleaseDate);
            image = itemView.findViewById(R.id.ivImage);
            favoriteButton = itemView.findViewById(R.id.imgFavorite);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onClick(getAdapterPosition());
                }
            });
        }
    }



}
