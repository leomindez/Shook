package com.shook.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.shook.R;
import com.shook.adapters.CommonParseAdapter;

/**
 * Created by leonelmendez on 04/01/15.
 */
public class MyBooksFragment extends Fragment {



    public static MyBooksFragment newInstace(){
        MyBooksFragment myBooksFragment = new MyBooksFragment();
        return myBooksFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_books_fragment_layout,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GridView myBooksContainer = (GridView)view.findViewById(R.id.my_books_container);
        getMyBooks(myBooksContainer);
    }


    private void getMyBooks(GridView myBooksContainer){

        CommonParseAdapter commonParseAdapter = new CommonParseAdapter(getActivity(),new ParseQueryAdapter.QueryFactory<ParseObject>() {
            @Override
            public ParseQuery<ParseObject> create() {
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Book");
                query.whereEqualTo("owner", ParseUser.getCurrentUser());
                query.orderByDescending("createdAt");
                return query;
            }
        });

        myBooksContainer.setAdapter(commonParseAdapter);
        commonParseAdapter.loadObjects();

    }

}
