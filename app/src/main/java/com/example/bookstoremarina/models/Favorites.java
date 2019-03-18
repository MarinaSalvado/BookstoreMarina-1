package com.example.bookstoremarina.models;

import io.realm.RealmObject;

public class Favorites extends RealmObject {
    private String id;
    private String ivImageLinks;
    private String tvPublishedDate;
    private String tvTitle;

    public Favorites(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIvImageLinks() {
        return ivImageLinks;
    }

    public void setIvImageLinks(String ivImageLinks) {
        this.ivImageLinks = ivImageLinks;
    }

    public String getTvPublishedDate() {
        return tvPublishedDate;
    }

    public void setTvPublishedDate(String tvPublishedDate) {
        this.tvPublishedDate = tvPublishedDate;
    }

    public String getTvTitle() {
        return tvTitle;
    }

    public void setTvTitle(String tvTitle) {
        this.tvTitle = tvTitle;
    }


}