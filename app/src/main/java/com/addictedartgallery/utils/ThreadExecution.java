package com.addictedartgallery.utils;


import android.app.Activity;
import android.content.Intent;

import com.addictedartgallery.R;

public class ThreadExecution {

    public static void startThread(final Activity activity, final Class className)
    {
        new Thread()
        {
            @Override
            public void run() {
                try {
                    sleep(3000);
                }catch (InterruptedException e)
                {
                    startScreen(activity, className);
                }finally {
                    startScreen(activity,className);
                }
            }
        }.start();
    }


    public static void startScreen(Activity activity, Class className)
    {
        start(activity,className,null);
        activity.finish();
    }

    public static void start(Activity activity, Class className,String params)
    {
        Intent intent = new Intent(activity,className);
        intent.putExtra(activity.getString(R.string.Intent),params);
        activity.startActivity(intent);
    }
}
