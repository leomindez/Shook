package com.shook.util;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by leonelmendez on 28/12/14.
 */
public class NavigationController {

    public static void navigateTo(ActionBarActivity actionBarActivity,Class<?> toClass,boolean finishActivity){
        Intent intent = new Intent(actionBarActivity,toClass);
        actionBarActivity.startActivity(intent);

        if(finishActivity)
            actionBarActivity.finish();
    }



}
