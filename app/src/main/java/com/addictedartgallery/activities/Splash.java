package com.addictedartgallery.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.addictedartgallery.Dashboard;
import com.addictedartgallery.R;
import com.addictedartgallery.model.AuthenticateGetUser;
import com.addictedartgallery.model.AuthenticatePostUser;
import com.addictedartgallery.model.Data;
import com.addictedartgallery.model.PostData;
import com.addictedartgallery.rest.ApiClient;
import com.addictedartgallery.rest.ApiInterface;
import com.addictedartgallery.utils.Preferences;
import com.addictedartgallery.utils.ViewUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Splash extends AppCompatActivity {

    private Preferences preferences;
    String expire, password, email;
    boolean isFreshInstall;

    AVLoadingIndicatorView progressBar;
    Thread timer;

    ApiInterface apiService;
    AuthenticateGetUser authenticateGetUser;
    AuthenticatePostUser authenticatePostUser;
    PostData postData;
    Data data;
    String encodedEmail, messageDigest1, messageDigest2, messageDigest;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        ViewUtils.cleanDirectory(this);

        preferences = new Preferences(this);
        expire = preferences.getExpire();
        password = preferences.getPassword();
        email = preferences.getUsername();
        isFreshInstall = preferences.isFreshInstall();

        progressBar = (AVLoadingIndicatorView) findViewById(R.id.splash_screen_progress_bar);
        int currentDate = (int) (new Date().getTime() / 1000);
        if (expire != null) {
            int expireDate = Integer.parseInt(expire);
            if (currentDate >= expireDate) {

                if (password != null) {
                    apiService = ApiClient.getClient().create(ApiInterface.class);
                    Call<AuthenticateGetUser> call = apiService.getAuthToken();
                    call.enqueue(authenticateGetUserCallback);
                }

                else
                    startTimerThread(Login.class);


            }

            else
                startTimerThread(Dashboard.class);


        }else{

            if(isFreshInstall)
                startTimerThread(Guide.class);
            else
                startTimerThread(Login.class);

        }

    }

    private Callback<AuthenticateGetUser> authenticateGetUserCallback = new Callback<AuthenticateGetUser>() {
        @Override
        public void onResponse(Call<AuthenticateGetUser> call, Response<AuthenticateGetUser> response) {

            if (response.isSuccessful()) {
                authenticateGetUser = response.body();
                if (authenticateGetUser.getStatus()) {
                    data = authenticateGetUser.getData();
                    try {
                        encodedEmail = URLEncoder.encode(email, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        encodedEmail = null;
                    }
                    messageDigest1 = ViewUtils.getMd5Key(email + ":" + data.getRealm() + ":" + password);
                    messageDigest2 = ViewUtils.getMd5Key("POST:"+getString(R.string.PostUrl)+":access_expire=36000&auth_request=" + data.getAuthRequest() + "&expire=" + data.getExpire() + "&username=" + encodedEmail);
                    messageDigest = ViewUtils.getMd5Key(messageDigest1 + ":" + data.getAuthRequest() + ":" + messageDigest2);

                    Call<AuthenticatePostUser> postCall = apiService.getAccessToken("36000", data.getAuthRequest(), String.valueOf(data.getExpire()), email, messageDigest);
                    postCall.enqueue(authenticatePostUserCallback);

                } else
                    startScreen(Login.class);


            } else
                startScreen(Login.class);
        }

        @Override
        public void onFailure(Call<AuthenticateGetUser> call, Throwable t) {
            startScreen(Login.class);
        }
    };

    private Callback<AuthenticatePostUser> authenticatePostUserCallback = new Callback<AuthenticatePostUser>() {
        @Override
        public void onResponse(Call<AuthenticatePostUser> call, Response<AuthenticatePostUser> response) {
            if (response.isSuccessful()) {
                authenticatePostUser = response.body();
                postData = authenticatePostUser.getData();

                if (authenticatePostUser.getStatus()) {
                    preferences.setUserDetails(postData.getAuthRequest(), String.valueOf(postData.getExpire()), email, password, postData.getRealm());
                    startScreen(Dashboard.class);
                } else
                    startScreen(Login.class);

            } else
                startScreen(Login.class);

        }

        @Override
        public void onFailure(Call<AuthenticatePostUser> call, Throwable t) {
            startScreen(Login.class);
        }
    };


    private void startTimerThread(final Class className)
    {
        timer = new Thread() {
            @Override
            public void run() {
                try {

                    sleep(3000);
                } catch (InterruptedException e) {
                    startScreen(className);
                } finally {
                    startScreen(className);

                }
            }
        };

        timer.start();
    }

    private void startScreen(Class className)
    {
        Intent screenIntent = new Intent(this,className);
        startActivity(screenIntent);
        finish();
    }
}
