package com.example.noticiaapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.noticiaapp.Model.NewsModel;
import com.example.noticiaapp.R;
import com.example.noticiaapp.Realm.RealmHelper;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.callback.Callback;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> implements Filterable {

    private List<NewsModel> newsModel;
    private List<NewsModel> newsModels;
    Context context;
    Callback callback;
    Realm realm;
    RealmHelper realmHelper;

    @Override
    public Filter getFilter() {
        return newsFilter;
    }

    private final Filter newsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<NewsModel> filterNewsList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filterNewsList.addAll(newsModel);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (NewsModel newsModel : newsModel) {
                    if (newsModel.getName().toLowerCase().contains(filterPattern))
                        filterNewsList.add(newsModel);
                }
            }
            FilterResults results = new FilterResults();
            results.values = filterNewsList;
            results.count = filterNewsList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            newsModel.clear();
            newsModel.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };

    public interface Callback{
        void onClick (int position);
    }

    public HomeAdapter(List<NewsModel> list, Context context, Callback callback){
        this.newsModel = list;
        this.context = context;
        this.callback = callback;
        newsModels = new ArrayList<>(newsModel);
    }

    @NonNull
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Realm.init(context);
        RealmConfiguration configuration = new  RealmConfiguration.Builder().allowWritesOnUiThread(true).build();
        realm = Realm.getInstance(configuration);
        realmHelper = new RealmHelper(realm);

        final NewsModel getData = newsModel.get(position);
        String name = getData.getName();
        String releaseDate = getData.getReleaseDate();

        holder.name.setText(name);
        holder.releaseDate.setText(releaseDate);

        Glide.with(holder.image)
                .load(newsModel.get(position).getImage())
                .into(holder.image);

        if (newsModel.get(position).getFavorite()) {
            holder.favoriteButton.setImageResource(R.drawable.ic_favorite_tapped);
        } else {
            holder.favoriteButton.setImageResource(R.drawable.ic_favorite_tap);
        }
        holder.favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newsModel.get(position).getFavorite()) {
                    holder.favoriteButton.setImageResource(R.drawable.ic_favorite_tap);
                    Toast.makeText(context, "Berita dihapus dari list favorite kamu", Toast.LENGTH_SHORT).show();
                    newsModel.get(position).setFavorite(false);
                    realmHelper.delete(newsModel.get(position));
                } else {
                    newsModel.get(position).setFavorite(true);
                    holder.favoriteButton.setImageResource(R.drawable.ic_favorite_tapped);
                    Toast.makeText(context, "Berita ditambahkan ke list favorite kamu", Toast.LENGTH_SHORT).show();
                    realmHelper.save(newsModel.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (newsModel != null) ? newsModel.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
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
}
