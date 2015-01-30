package com.shook;

import android.app.Application;

import com.orm.SugarApp;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

/**
 * Created by leonelmendez on 28/12/14.
 */
public class ShookApplication extends SugarApp{

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(getApplicationContext(),getString(R.string.parse_application_id),getString(R.string.parse_client_key));
        ParseFacebookUtils.initialize(getString(R.string.facebook_app_id));
    }
}
