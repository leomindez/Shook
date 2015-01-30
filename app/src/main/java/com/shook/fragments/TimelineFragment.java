package com.shook.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.melnykov.fab.FloatingActionButton;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.shook.DetailBookActivity;
import com.shook.NewBookActivity;
import com.shook.R;
import com.shook.adapters.CommonParseAdapter;
import com.shook.util.NavigationController;
import com.shook.util.SharedPreferencesHelper;

/**
 * Created by leonelmendez on 03/01/15.
 */
public class TimelineFragment extends Fragment implements View.OnClickListener,AdapterView.OnItemClickListener,SwipeRefreshLayout.OnRefreshListener{


    private GridView timeLine;
    private SwipeRefreshLayout swipeRefreshLayout;
    public static TimelineFragment newInstance(){
        TimelineFragment timelineFragment = new TimelineFragment();
        return timelineFragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.timeline_fragment_layout,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton floatingActionButton = (FloatingActionButton)view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(TimelineFragment.this);

        timeLine = (GridView)view.findViewById(R.id.timeline_container);
        timeLine.setOnItemClickListener(TimelineFragment.this);
        getBooks(timeLine);

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.refresh_layout_container);
        swipeRefreshLayout.setOnRefreshListener(TimelineFragment.this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                NavigationController.navigateTo((ActionBarActivity)getActivity(), NewBookActivity.class,false);
                break;
        }
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        ParseObject parseObject = (ParseObject) parent.getAdapter().getItem(position);
        Intent detailActivity = new Intent(getActivity(), DetailBookActivity.class);
        Bundle detailBundle = new Bundle();
        detailBundle.putString("url_image", parseObject.getParseFile("image").getUrl());
        detailBundle.putString("username", SharedPreferencesHelper.getUsername(getActivity()));
        detailBundle.putString("facebookId",SharedPreferencesHelper.getUserId(getActivity()));
        detailBundle.putString("book_title",parseObject.getString("title"));
        detailBundle.putString("book_desc",parseObject.getString("description"));
        detailBundle.putString("book_author",parseObject.getString("author"));
        detailActivity.putExtras(detailBundle);

        startActivity(detailActivity);

    }

    @Override
    public void onRefresh() {

        getBooks(timeLine);
        swipeRefreshLayout.setRefreshing(false);

    }

    private void getBooks(GridView booksContainer){

        CommonParseAdapter commonParseAdapter = new CommonParseAdapter(getActivity(),new ParseQueryAdapter.QueryFactory<ParseObject>() {
            @Override
            public ParseQuery<ParseObject> create() {

                ParseQuery<ParseObject> query = new ParseQuery<>("Book");
                query.include("owner");
                query.orderByDescending("createdAt");
                return query;
            }
        });

        booksContainer.setAdapter(commonParseAdapter);
        commonParseAdapter.loadObjects();
    }
}
