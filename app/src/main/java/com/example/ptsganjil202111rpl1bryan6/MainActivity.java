package com.example.ptsganjil202111rpl1bryan6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    Realm realm;
    RealmHelper realmHelper;
    RecyclerView recyclerView;
    private List<MovieModel> list;
    MainAdapter adapter;
    GridLayoutManager gridLayoutManager;
    String title;
    ImageView nav_movie;
    ImageView nav_favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rvMovie);
        recyclerView.setHasFixedSize(true);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().allowWritesOnUiThread(true).build();
        realm = Realm.getInstance(realmConfiguration);
        realmHelper = new RealmHelper(realm);

        AndroidNetworking.initialize(getApplicationContext());
        getData();

        nav_favorite = findViewById(R.id.nav_favorite);
        nav_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FavoriteMovie.class);
                startActivity(intent);
            }
        });
    }

    private void getData(){
        AndroidNetworking.get("https://api.themoviedb.org/3/movie/popular?api_key=436265fbd6f13ffa15b3bdf49b3d524c")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: "+ response.toString());
                        {
                            try {
                                list = new ArrayList<>();
                                JSONArray jsonArray = response.getJSONArray("results");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    list.add(new MovieModel(
                                            i, false,
                                    title = jsonObject.getString("title"),
                                            jsonObject.getString("overview"),
                                            jsonObject.getString("release_date"),
                                            jsonObject.getString("poster_path"),
                                            jsonObject.getString("backdrop_path"),
                                            jsonObject.getString("popularity")));
                                    final RealmResults<MovieModel> model = realm.where(MovieModel.class).equalTo("name", title).findAll();
                                    if (!model.isEmpty()) {
                                    list.get(i).setFavorite(true);
                                    }
                                }
                                gridLayoutManager = new GridLayoutManager(MainActivity.this, 1);
                                recyclerView.setLayoutManager(gridLayoutManager);
                                adapter = new MainAdapter(list, MainActivity.this, new MainAdapter.Callback() {
                                    @Override
                                    public void onClick(int position) {
                                        Toast.makeText(MainActivity.this, "Item Clicked", Toast.LENGTH_SHORT).show();
                                        Intent move = new Intent(getApplicationContext(), DetailMovie.class);
                                        MovieModel show = list.get(position);
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

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d(TAG, "onError: "+ error);
                        // handle error
                    }
                });
    }
}