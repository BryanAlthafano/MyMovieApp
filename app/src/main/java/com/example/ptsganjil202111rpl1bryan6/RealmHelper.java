package com.example.ptsganjil202111rpl1bryan6;

import android.util.Log;

import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmHelper {

    Realm realm;
    List<MovieModel> movieList;

    public RealmHelper(Realm realm) {
        this.realm = realm;
    }

    public void save(final MovieModel movieModel) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (realm != null) {
                    Log.e("Created", "Database was created");
                    Number currentIdNum = realm.where(MovieModel.class).max("id");
                    int nextId;
                    if (currentIdNum == null) {
                        nextId = 1;
                    } else {
                        nextId = currentIdNum.intValue() + 1;
                    }
                    movieModel.setId(nextId);
                    MovieModel itemModel = realm.copyToRealm(movieModel);
                    final RealmResults<MovieModel> item = realm.where(MovieModel.class).findAll();
                } else {
                    Log.e("pppp", "execute: Database not Exist");
                }
            }
        });
    }

    public List<MovieModel> getAllMovies() {
        RealmResults<MovieModel> results = realm.where(MovieModel.class).findAll();
        return results;
    }

    public List delete(MovieModel movie){
        final RealmResults<MovieModel> model = realm.where(MovieModel.class).equalTo("name", movie.getName()).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                model.deleteAllFromRealm();
                final RealmResults<MovieModel> all = realm.where(MovieModel.class).findAll();
                movieList = realm.copyFromRealm(all);
                Collections.sort(movieList);
            }
        });
        Log.d("Movie Model", "" + movieList.size());
        return movieList;
    }
}
