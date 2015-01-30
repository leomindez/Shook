package com.shook.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by leonelmendez on 28/12/14.
 */
public class SharedPreferencesHelper {

    private static final String USER_INFO_PREFERENCES = "user_info";
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String USER_LOCATION = "user_location";
    public static final String USER_FIRST_TIME = "user_first";
    public static void saveUserInfo(Context context,String userId,String username,String location,String firstTime){

        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_INFO_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_ID, userId).commit();
        editor.putString(USER_NAME, username).apply();
        editor.putString(USER_LOCATION, location).apply();
        editor.putString(USER_FIRST_TIME,firstTime);

    }


    public static HashMap<String,String> getUserInfo(Context context){
            SharedPreferences sharedPreferences = context.getSharedPreferences(USER_INFO_PREFERENCES,Context.MODE_PRIVATE);
            HashMap<String,String> userInfo = new HashMap<>();
            userInfo.put(USER_ID,sharedPreferences.getString(USER_ID,""));
            userInfo.put(USER_NAME,sharedPreferences.getString(USER_NAME,""));
            userInfo.put(USER_LOCATION,sharedPreferences.getString(USER_LOCATION,""));
            userInfo.put(USER_FIRST_TIME,sharedPreferences.getString(USER_FIRST_TIME,""));
        return userInfo;
    }

    public static String getUserId (Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_INFO_PREFERENCES,Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_ID,"");
    }

    public static String getUsername (Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_INFO_PREFERENCES,Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_NAME,"");
    }
    public static String getUserLocation(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_INFO_PREFERENCES,Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_LOCATION,"");
    }
}
