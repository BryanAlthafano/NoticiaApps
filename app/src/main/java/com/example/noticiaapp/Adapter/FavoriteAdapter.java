package com.example.noticiaapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.noticiaapp.Activity.Favorite;
import com.example.noticiaapp.Model.NewsModel;
import com.example.noticiaapp.R;
import com.example.noticiaapp.Realm.RealmHelper;

import java.util.List;

import javax.security.auth.callback.Callback;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    Realm realm;
    RealmHelper realmHelper;
    Callback callback;
    Context context;
    public List<NewsModel> list;
    boolean delete = false;

    public interface Callback{
        void onClick(int position);
    }

    public FavoriteAdapter(List<NewsModel> list, Context context, Callback callback){
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        FavoriteAdapter.ViewHolder holder = new FavoriteAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //Realm
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().allowWritesOnUiThread(true).build();
        realm = Realm.getInstance(realmConfiguration);
        realmHelper = new RealmHelper(realm);

        final NewsModel getData = list.get(position);
        String name = getData.getName();
        String releaseDate = getData.getReleaseDate();

        holder.name.setText(name);
        holder.releaseDate.setText(releaseDate);

        Glide.with(holder.image)
                .load(list.get(position).getImage())
                .into(holder.image);

        holder.favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!delete) {
                    holder.favoriteButton.setImageResource(R.drawable.ic_favorite_tap);
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
        TextView name, releaseDate;
        ImageView image, favoriteButton;
        RelativeLayout rl_news;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name_news);
            releaseDate = itemView.findViewById(R.id.tv_date_news);
            image = itemView.findViewById(R.id.iv_img_news);
            favoriteButton = itemView.findViewById(R.id.iv_favoriteButton);
            favoriteButton = itemView.findViewById(R.id.iv_favoriteButton);
            rl_news = itemView.findViewById(R.id.rl_item_news);
            rl_news.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onClick(getAdapterPosition());
                }
            });
        }
    }

    private void delete(int position) {
        List<NewsModel> newsModelList = realmHelper.delete(list.get(position));
        list = newsModelList;
        Favorite favoriteNews = new Favorite();
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }
}
