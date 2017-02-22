package com.addictedartgallery.utils;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import com.addictedartgallery.model.FacebookData;
import com.addictedartgallery.model.PostData;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@SuppressWarnings("WeakerAccess")
public class Permissions {


    Activity activity;
    private Preferences preferences;

    private String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    public Permissions(Activity activity) {
        this.activity = activity;
        preferences = new Preferences(activity);
    }

    public void requestPermission(Activity activity, boolean isFacebook, FacebookData facebookData, String username, String email, String password, PostData accessData, Class destination) {
        if (!hasPermissions(activity, PERMISSIONS))
            ActivityCompat.requestPermissions(activity, PERMISSIONS, 111);

        else {
            if (isFacebook)
                preferences.setFacebookDetails(facebookData.getAccessToken(), String.valueOf(facebookData.getExpire()), facebookData.getRealm(), facebookData.getLogin(), username);
            else
                preferences.setUserDetails(accessData.getAuthRequest(), String.valueOf(accessData.getExpire()), email, password, accessData.getRealm());

            activity.startActivity(new Intent(activity, destination));
            activity.finish();

        }
    }

    public boolean hasPermissions(Activity activity, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void permissionsNotSelected(Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Permissions Required");
        builder.setMessage("Please enable the requested permissions in the app settings in order to use this app");
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                System.exit(1);
            }
        });
        AlertDialog noInternet = builder.create();
        noInternet.show();
    }


}
