package com.shook.model;

import android.content.Context;

import com.orm.SugarRecord;

/**
 * Created by leo on 1/26/15.
 */
public class Book extends SugarRecord<Book> {

   String title;
   String author;
   String description;
   String url;


    public Book(){

    }


    public Book(String title, String author, String description, String url) {

        this.title = title;
        this.author = author;
        this.description = description;
        this.url = url;
    }

}
