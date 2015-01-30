package com.shook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Toast;

import com.facebook.widget.LoginButton;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.shook.util.NavigationController;

import java.util.Arrays;
import java.util.List;


public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //UIUtils.hideActionBar(MainActivity.this);

        isUserSessionStarted();
        LoginButton loginButton = (LoginButton)findViewById(R.id.facebook_login_button);
        loginButton.setOnClickListener(MainActivity.this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.facebook_login_button:
                facebookLoginWithParse();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode,resultCode,data);
    }

    private void facebookLoginWithParse(){

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Creating session....");
        progressDialog.show();

        List<String> permissions = Arrays.asList("public_profile","user_about_me",
               "user_location");
        ParseFacebookUtils.logIn(permissions,MainActivity.this,new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if(parseUser == null){
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this,getString(R.string.error_message),Toast.LENGTH_SHORT).show();
                }else if(parseUser.isNew()){
                    progressDialog.dismiss();
                    NavigationController.navigateTo(MainActivity.this, TimeLineActivity.class,true);
                }else{
                    progressDialog.dismiss();
                    NavigationController.navigateTo(MainActivity.this, TimeLineActivity.class,true);
                }
            }
        });
    }

    private void isUserSessionStarted(){
            ParseUser parseUser = ParseUser.getCurrentUser();
            if(parseUser != null && ParseFacebookUtils.isLinked(parseUser)){
                NavigationController.navigateTo(MainActivity.this,TimeLineActivity.class,true);
            }
    }



}

