package com.addictedartgallery.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.addictedartgallery.facebook.OAuth;
import com.addictedartgallery.facebook.OAuthUsers;
import com.addictedartgallery.rest.ApiClient;
import com.addictedartgallery.rest.ApiInterface;
import com.addictedartgallery.utils.Permissions;
import com.addictedartgallery.utils.Preferences;


public abstract class BaseAuth extends AppCompatActivity {

    protected OAuth oAuth;
    protected OAuthUsers users;
    protected String provider;
    protected ApiInterface apiService;
    protected Permissions permissions;
    protected Preferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        apiService = ApiClient.getClient().create(ApiInterface.class);

        permissions = new Permissions(this);
        preferences = new Preferences(this);
        preferences.setFreshInstall(false);

        oAuth = new OAuth(this);
        oAuth.initialize("ABCOnqX3O2ll69pIslg98gaw3eg");
        users = new OAuthUsers(oAuth);
        provider = "facebook";

    }
}
