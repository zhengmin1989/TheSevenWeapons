package com.introspy.core;

import android.app.Application;
import android.content.Context;

public class IntrospyAndroid extends Application {

    private static Context context;

    public void onCreate(){
        super.onCreate();
        IntrospyAndroid.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return IntrospyAndroid.context;
    }
}
