package com.example.noticiaapp.Activity;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.noticiaapp.Adapter.HomeAdapter;
import com.example.noticiaapp.Api.ApiInterface;
import com.example.noticiaapp.Model.NewsModel;
import com.example.noticiaapp.R;
import com.example.noticiaapp.Realm.RealmHelper;
import com.example.noticiaapp.Session.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home extends AppCompatActivity implements View.OnClickListener {

    Session session;
    TextView tv_top_username;
    String tv_username;
    RelativeLayout nav_home, nav_favorite, nav_profile;
    Realm realm;
    RealmHelper realmHelper;
    HomeAdapter adapter;
    RecyclerView recyclerView;
    private List<NewsModel> list;
    GridLayoutManager gridLayoutManager;
    String name, releaseDate, image, url;
    android.widget.SearchView searchView;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        session = new Session(Home.this);
        if (!session.isLoggedIn()) {
            moveToLogin();
        }

        //SwipeRefreshLayout
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.black);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData();
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000L);
            }
        });

        //TextView Username
        tv_top_username = findViewById(R.id.tv_top_username);

        //ShowData
        tv_username = session.getUser().get("username");
        tv_top_username.setText(tv_username);

        //Navigation
        nav_home = findViewById(R.id.nav_home);
        nav_home.setOnClickListener(this);
        nav_favorite = findViewById(R.id.nav_favorite);
        nav_favorite.setOnClickListener(this);
        nav_profile = findViewById(R.id.nav_profile);
        nav_profile.setOnClickListener(this);

        //RecyclerView
        recyclerView = findViewById(R.id.rv_news);
        recyclerView.setHasFixedSize(true);

        //Realm
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().allowWritesOnUiThread(true).build();
        realm = Realm.getInstance(realmConfiguration);
        realmHelper = new RealmHelper(realm);

        //AndroidNetworking
        AndroidNetworking.initialize(getApplicationContext());
        getData();

        //SearchView
        searchView = findViewById(R.id.search_bar);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Cari Berita...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void moveToLogin() {
        Intent intent = new Intent(Home.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_favorite:
                Intent intent = new Intent(Home.this,Favorite.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_profile:
                Intent intent2 = new Intent(Home.this, Profile.class);
                startActivity(intent2);
                finish();
                break;
        }
    }

    private void getData() {
        swipeRefreshLayout.setRefreshing(true);
        AndroidNetworking.get("https://newsapi.org/v2/top-headlines?country=id&apiKey=3a1aff6e87e845f289d241073591cc12")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: " + response.toString());
                        {
                            try {
                                list = new ArrayList<>();
                                JSONArray jsonArray = response.getJSONArray("articles");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    name = jsonObject.getString("title");
                                    releaseDate = jsonObject.getString("publishedAt");
                                    image = jsonObject.getString("urlToImage");
                                    url = jsonObject.getString("url");
                                    list.add(new NewsModel(i, false, name, releaseDate, image, url));
                                    final RealmResults<NewsModel> model = realm.where(NewsModel.class).equalTo("name", name).findAll();
                                    if (!model.isEmpty()) {
                                        list.get(i).setFavorite(true);
                                    }
                                }
                                gridLayoutManager = new GridLayoutManager(Home.this, 1);
                                recyclerView.setLayoutManager(gridLayoutManager);
                                adapter = new HomeAdapter(list, Home.this, new HomeAdapter.Callback() {
                                    @Override
                                    public void onClick(int position) {
                                        Toast.makeText(Home.this, "Item Clicked", Toast.LENGTH_SHORT).show();
                                        Intent move = new Intent(Home.this, WebView.class);
                                        NewsModel show = list.get(position);
                                        move.putExtra("url", show.getUrl_web());
                                        startActivity(move);
                                    }
                                });
                                swipeRefreshLayout.setRefreshing(false);
                                recyclerView.setAdapter(adapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(Home.this, "Error", Toast.LENGTH_SHORT).show();
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: "+ anError);
                    }
                });
    }
}