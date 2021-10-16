package com.example.noticiaapp.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class NewsModel extends RealmObject implements Comparable{

    @PrimaryKey
    private Integer id;
    private Boolean favorite;
    private String name;
    private String releaseDate;
    private String image;
    private String url_web;

    public NewsModel(Integer id, Boolean favorite, String name, String releaseDate, String image, String url_web) {
        this.id = id;
        this.favorite = favorite;
        this.name = name;
        this.releaseDate = releaseDate;
        this.image = image;
        this.url_web = url_web;
    }

    public NewsModel() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl_web() {
        return url_web;
    }

    public void setUrl_web(String url_web) {
        this.url_web = url_web;
    }

    @Override
    public int compareTo(Object o) {
        int compare = ((NewsModel)o).getId();
        return this.id-compare;
    }
}
