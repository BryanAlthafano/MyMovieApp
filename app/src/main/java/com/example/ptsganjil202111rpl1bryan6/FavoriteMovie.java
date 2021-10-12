package com.example.ptsganjil202111rpl1bryan6;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class FavoriteMovie extends AppCompatActivity {

    RecyclerView recyclerView;
    FavoriteAdapter adapter;
    List<MovieModel> list;
    GridLayoutManager gridLayoutManager;
    ImageView nav_movie;
    Realm realm;
    RealmHelper realmHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_movie);

        RealmConfiguration configuration = new  RealmConfiguration.Builder().allowWritesOnUiThread(true).build();
        realm = Realm.getInstance(configuration);
        realmHelper = new RealmHelper(realm);
        list = new ArrayList<>();
        list = realmHelper.getAllMovies();
        show();

        nav_movie = findViewById(R.id.nav_movie);
        nav_movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View position) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.notifyDataSetChanged();
        show();
    }

    public void show() {
        recyclerView = findViewById(R.id.rvFavoriteMovies);
        gridLayoutManager = new GridLayoutManager(FavoriteMovie.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new FavoriteAdapter(list, FavoriteMovie.this, new FavoriteAdapter.Callback() {

            @Override
            public void onClick(int position) {
                MovieModel show = list.get(position);
                Intent move = new Intent(getApplicationContext(), DetailMovie.class);
                move.putExtra("header", show.getHeader());
                move.putExtra("name", show.getName());
                move.putExtra("image", show.getImage());
                move.putExtra("releaseDate", show.getReleaseDate());
                move.putExtra("popularity", show.getPopularity());
                move.putExtra("description", show.getDescription());
                startActivity(move);
            }
        });
        recyclerView.setAdapter(adapter);

    }


}