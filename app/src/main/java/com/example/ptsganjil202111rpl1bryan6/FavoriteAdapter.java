package com.example.ptsganjil202111rpl1bryan6;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.SurfaceHolder.Callback;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    Realm realm;
    RealmHelper realmHelper;
    Callback callback;
    Context context;
    public List<MovieModel> list;
    boolean delete = false;

    public interface Callback{
        void onClick(int position);
    }

    public FavoriteAdapter(List<MovieModel> list, Context context, Callback callback){
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().allowWritesOnUiThread(true).build();
        realm = Realm.getInstance(realmConfiguration);
        realmHelper = new RealmHelper(realm);
        this.list = list;
        this.callback = callback;
        this.context = context;
    }

    @NonNull
    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        FavoriteAdapter.ViewHolder holder = new FavoriteAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.ViewHolder holder, int position) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().allowWritesOnUiThread(true).build();
        realm = Realm.getInstance(realmConfiguration);
        realmHelper = new RealmHelper(realm);

        final MovieModel getData = list.get(position);
        String name = getData.getName();
        String popularity = getData.getPopularity();
        String releaseDate = getData.getReleaseDate();

        holder.name.setText(name);
        holder.popularity.setText(popularity);
        holder.releaseDate.setText(releaseDate);

        Glide.with(holder.image)
                .load("https://image.tmdb.org/t/p/w500"+list.get(position).getImage())
                .into(holder.image);

        holder.favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!delete) {
                    holder.favoriteButton.setImageResource(R.drawable.ic_favorite_border);
                    Toast.makeText(context, "Film dihapus dari list favorite kamu", Toast.LENGTH_SHORT).show();
                    delete(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (list != null) ? list.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView popularity;
        TextView releaseDate;
        ImageView image;
        ImageView favoriteButton;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View v) {
            super(v);
            favoriteButton = itemView.findViewById(R.id.imgFavorite);
            name = itemView.findViewById(R.id.tvName);
            popularity = itemView.findViewById(R.id.tvPopularity);
            releaseDate = itemView.findViewById(R.id.tvReleaseDate);
            image = itemView.findViewById(R.id.ivImage);
            favoriteButton = itemView.findViewById(R.id.imgFavorite);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View position) {
                    callback.onClick(getAdapterPosition());
                }
            });
        }

    }

    private void delete(int position) {
        List<MovieModel> movieModelList = realmHelper.delete(list.get(position));
        list = movieModelList;
        FavoriteMovie favoriteMovie = new FavoriteMovie();
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

}
