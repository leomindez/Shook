package com.shook.model;

import com.orm.SugarRecord;

/**
 * Created by leo on 1/26/15.
 */
public class FavoriteBooks extends SugarRecord<FavoriteBooks> {

    String title;
    String author;
    String description;
    String url;

    public FavoriteBooks(){

    }

    public FavoriteBooks(String title, String author, String description, String url) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.url = url;
    }

    @Override
    public String toString() {
        return "FavoriteBooks{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
