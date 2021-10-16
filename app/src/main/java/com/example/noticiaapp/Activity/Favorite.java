package com.example.noticiaapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.noticiaapp.Adapter.FavoriteAdapter;
import com.example.noticiaapp.Model.NewsModel;
import com.example.noticiaapp.R;
import com.example.noticiaapp.Realm.RealmHelper;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class Favorite extends AppCompatActivity implements View.OnClickListener{

    RecyclerView recyclerView;
    FavoriteAdapter adapter;
    List<NewsModel> list;
    GridLayoutManager gridLayoutManager;
    ImageView nav_movie;
    Realm realm;
    RealmHelper realmHelper;
    RelativeLayout nav_home, nav_favorite, nav_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        //Realm
        RealmConfiguration configuration = new  RealmConfiguration.Builder().allowWritesOnUiThread(true).build();
        realm = Realm.getInstance(configuration);
        realmHelper = new RealmHelper(realm);
        list = new ArrayList<>();
        list = realmHelper.getAllNews();
        show();

        //Navigation
        nav_home = findViewById(R.id.nav_home);
        nav_home.setOnClickListener(this);
        nav_favorite = findViewById(R.id.nav_favorite);
        nav_favorite.setOnClickListener(this);
        nav_profile = findViewById(R.id.nav_profile);
        nav_profile.setOnClickListener(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.notifyDataSetChanged();
        show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_home:
                Intent intent = new Intent(Favorite.this,Home.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_profile:
                Intent intent2 = new Intent(Favorite.this, Profile.class);
                startActivity(intent2);
                finish();
                break;
        }
    }

    public void show() {
        recyclerView = findViewById(R.id.rv_favorite_news);
        gridLayoutManager = new GridLayoutManager(Favorite.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new FavoriteAdapter(list, Favorite.this, new FavoriteAdapter.Callback() {
            @Override
            public void onClick(int position) {
                NewsModel show = list.get(position);
                Intent move = new Intent(Favorite.this, WebView.class);
                move.putExtra("url", show.getUrl_web());
                startActivity(move);
            }
        });
        recyclerView.setAdapter(adapter);
    }
}