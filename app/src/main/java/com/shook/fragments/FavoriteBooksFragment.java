package com.shook.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.shook.model.FavoriteBooks;

import java.util.List;

/**
 * Created by leo on 1/26/15.
 */
public class FavoriteBooksFragment extends Fragment {


    public FavoriteBooksFragment(){}

    public static FavoriteBooksFragment newInstance(){
        FavoriteBooksFragment favoriteBooksFragment = new FavoriteBooksFragment();
        return favoriteBooksFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<FavoriteBooks> favoriteBooks = FavoriteBooks.listAll(FavoriteBooks.class);
        Log.d("TAG_FAVORITE",favoriteBooks.toString());
    }
}
