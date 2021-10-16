package com.example.noticiaapp.Realm;

import android.util.Log;

import com.example.noticiaapp.Model.NewsModel;

import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmHelper {
    Realm realm;
    List<NewsModel> movieList;

    public RealmHelper(Realm realm) {
        this.realm = realm;
    }

    public void save(final NewsModel movieModel) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (realm != null) {
                    Log.e("Created", "Database was created");
                    Number currentIdNum = realm.where(NewsModel.class).max("id");
                    int nextId;
                    if (currentIdNum == null) {
                        nextId = 1;
                    } else {
                        nextId = currentIdNum.intValue() + 1;
                    }
                    movieModel.setId(nextId);
                    NewsModel itemModel = realm.copyToRealm(movieModel);
                    final RealmResults<NewsModel> item = realm.where(NewsModel.class).findAll();
                } else {
                    Log.e("pppp", "execute: Database not Exist");
                }
            }
        });
    }

    public List<NewsModel> getAllNews() {
        RealmResults<NewsModel> results = realm.where(NewsModel.class).findAll();
        return results;
    }

    public List delete(NewsModel news){
        final RealmResults<NewsModel> model = realm.where(NewsModel.class).equalTo("name", news.getName()).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                model.deleteAllFromRealm();
                final RealmResults<NewsModel> all = realm.where(NewsModel.class).findAll();
                movieList = realm.copyFromRealm(all);
                Collections.sort(movieList);
            }
        });
        Log.d("Movie Model", "" + movieList.size());
        return movieList;
    }
}
