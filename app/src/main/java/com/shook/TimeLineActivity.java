package com.shook;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.internal.ImageRequest;
import com.facebook.model.GraphUser;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.shook.fragments.MyBooksFragment;
import com.shook.fragments.TimelineFragment;
import com.shook.util.SharedPreferencesHelper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.net.URISyntaxException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class TimeLineActivity extends ActionBarActivity implements AdapterView.OnItemClickListener{

    private ActionBarDrawerToggle drawerToggle;
    private static final String DRAWER_LIST_POSITION_TAG = "drawer_list_position";
    private int DRAWER_LIST_POSITION = 0;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(getString(R.string.app_name));
            setSupportActionBar(toolbar);
        }

        String[] drawerItems = getResources().getStringArray(R.array.drawer_list_items);
        ListView drawerList = (ListView) findViewById(R.id.left_drawer_list);

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(TimeLineActivity.this, android.R.layout.simple_list_item_1, drawerItems);
        drawerList.setAdapter(stringArrayAdapter);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
        };
        drawerLayout.setDrawerListener(drawerToggle);


        HashMap<String,String> userInfo = SharedPreferencesHelper.getUserInfo(TimeLineActivity.this);

        CircleImageView profileUserImage = (CircleImageView)findViewById(R.id.profile_image);

        if(userInfo.get(SharedPreferencesHelper.USER_FIRST_TIME).equals("yes")){
            setProfileImage(profileUserImage,userInfo.get(SharedPreferencesHelper.USER_ID));
        }else{
            getUserInfoFromFacebook(profileUserImage);
        }

        //setProfileImage(profileUserImage,userInfo.get(SharedPreferencesHelper.USER_ID));

        ((TextView) findViewById(R.id.username_text)).setText(userInfo.get(SharedPreferencesHelper.USER_NAME));
        ((TextView) findViewById(R.id.user_location)).setText(userInfo.get(SharedPreferencesHelper.USER_LOCATION));

        drawerList.setOnItemClickListener(TimeLineActivity.this);


        if(savedInstanceState != null){
            DRAWER_LIST_POSITION = savedInstanceState.getInt(DRAWER_LIST_POSITION_TAG);
        }

        drawerList.setSelection(DRAWER_LIST_POSITION);
        fragmentSwitcher(TimelineFragment.newInstance());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(DRAWER_LIST_POSITION_TAG,DRAWER_LIST_POSITION);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }




    private void setProfileImage(final CircleImageView profileImage,String imageUrl){
        try {
            Picasso.with(TimeLineActivity.this)
                    .load(ImageRequest.getProfilePictureUrl(imageUrl,180,180).toString())
                    .noFade()
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            profileImage.setImageBitmap(bitmap);
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }




    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                DRAWER_LIST_POSITION = position;
                fragmentSwitcher(TimelineFragment.newInstance());
                toolbar.setTitle("Home");

                break;
            case 1:
                DRAWER_LIST_POSITION = position;
                fragmentSwitcher(MyBooksFragment.newInstace());
                toolbar.setTitle("My Books");
                break;
            case 2:
                DRAWER_LIST_POSITION = position;
                toolbar.setTitle("Favorites");
                break;
        }
    }

    private void fragmentSwitcher(Fragment  fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
        drawerLayout.closeDrawers();

    }



    private void getUserInfoFromFacebook(final CircleImageView profileUserImage) {

        Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        if (user != null) {

                            Log.d("TAG_USER_INFO", user.toString());
                            //SharedPreferencesHelper.saveUserInfo(MainActivity.this,user.getId(),user.getUsername(),"");
                            SharedPreferencesHelper.saveUserInfo(TimeLineActivity.this, user.getId(), user.getName(), "","yes");
                            setProfileImage(profileUserImage,user.getId());
                            ParseUser currentUser = ParseUser.getCurrentUser();
                            currentUser.saveEventually();

                        } else if (response.getError() != null) {
                            Log.d("TAG_USER_INFO",response.toString());
                            Toast.makeText(TimeLineActivity.this, getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        request.executeAsync();

    }


}
