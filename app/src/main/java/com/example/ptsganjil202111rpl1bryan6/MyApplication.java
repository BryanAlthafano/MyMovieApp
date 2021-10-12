package com.example.ptsganjil202111rpl1bryan6;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .allowWritesOnUiThread(true)
                .name("movie.db")
                .schemaVersion(0)
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
