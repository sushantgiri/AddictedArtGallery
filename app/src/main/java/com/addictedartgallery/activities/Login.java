package com.addictedartgallery.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.addictedartgallery.Dashboard;
import com.addictedartgallery.R;
import com.addictedartgallery.facebook.OAuthCallback;
import com.addictedartgallery.facebook.OAuthData;
import com.addictedartgallery.facebook.http.OAuthJSONCallback;
import com.addictedartgallery.model.AuthenticateGetUser;
import com.addictedartgallery.model.AuthenticatePostUser;
import com.addictedartgallery.model.Data;
import com.addictedartgallery.model.FacebookData;
import com.addictedartgallery.model.FacebookResponse;
import com.addictedartgallery.model.PostData;
import com.addictedartgallery.utils.AndroidBug5497Workaround;
import com.addictedartgallery.utils.CustomDialog;
import com.addictedartgallery.utils.ViewUtils;
import com.addictedartgallery.widgets.dialogs.FacebookLoginError;
import com.addictedartgallery.widgets.dialogs.InvalidCredentials;
import com.addictedartgallery.widgets.dialogs.NoCredentials;
import com.addictedartgallery.widgets.dialogs.NoInternet;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login extends BaseAuth implements View.OnClickListener {


    private EditText emailText, passwordText;
    private Button loginButton, facebookButton;
    private TextView signupButton, addictedGalleryText;
    private String email, password, username;
    PostData accessData;
    FacebookData facebookData;

    boolean isFacebook;

    CustomDialog customDialog;

    Call<AuthenticateGetUser> getUserCall;
    AuthenticateGetUser authenticateGetUser;
    Data authenticateGetData;
    String encodedEmail, messageDigest1, messageDigest2, messageDigest;

    Call<AuthenticatePostUser> postUserCall;
    AuthenticatePostUser authenticatePostUser;

    Call<FacebookResponse> facebookCall;
    OAuthData oAuthData;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AndroidBug5497Workaround.assistActivity(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        customDialog = new CustomDialog(this);

        emailText = (EditText) findViewById(R.id.activity_login_email);
        passwordText = (EditText) findViewById(R.id.activity_login_password);

        loginButton = (Button) findViewById(R.id.activity_login_loginButton);
        facebookButton = (Button) findViewById(R.id.activity_login_facebook);

        signupButton = (TextView) findViewById(R.id.activity_login_sign_up);
        addictedGalleryText = (TextView) findViewById(R.id.addicted_gallery_text);


        loginButton.setOnClickListener(this);
        signupButton.setOnClickListener(this);
        facebookButton.setOnClickListener(this);
        addictedGalleryText.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == loginButton) {
            if (!ViewUtils.validateUser(emailText, passwordText))
            {
                NoCredentials.show(this);
                return;
            }

            if(ViewUtils.isOnline(this)) {
                email = emailText.getText().toString().trim();
                password = passwordText.getText().toString().trim();

                customDialog.show();

                getUserCall = apiService.getAuthToken();
                getUserCall.enqueue(authenticateGetUserCallback);
            }
            else
                NoInternet.show(this);


        } else if (view == signupButton)
            ViewUtils.openBrowser(this, getString(R.string.Registration));

        else if (view == facebookButton)
            oAuth.popup(provider, oAuthCallback);

        else if (view == addictedGalleryText)
            ViewUtils.openBrowser(this, getString(R.string.AddictedGallery));
    }

    private Callback<AuthenticateGetUser> authenticateGetUserCallback = new Callback<AuthenticateGetUser>() {
        @Override
        public void onResponse(Call<AuthenticateGetUser> call, Response<AuthenticateGetUser> response) {

            if (response.isSuccessful()) {
                authenticateGetUser = response.body();

                if (authenticateGetUser.getStatus()) {
                    authenticateGetData = authenticateGetUser.getData();

                    try {
                        encodedEmail = URLEncoder.encode(email, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        encodedEmail = null;
                    }

                    messageDigest1 = ViewUtils.getMd5Key(email + ":" + authenticateGetData.getRealm() + ":" + password);
                    messageDigest2 = ViewUtils.getMd5Key("POST:"+getString(R.string.PostUrl)+":access_expire=36000&auth_request=" + authenticateGetData.getAuthRequest() + "&expire=" + authenticateGetData.getExpire() + "&username=" + encodedEmail);
                    messageDigest = ViewUtils.getMd5Key(messageDigest1 + ":" + authenticateGetData.getAuthRequest() + ":" + messageDigest2);

                    postUserCall = apiService.getAccessToken("36000", authenticateGetData.getAuthRequest(), String.valueOf(authenticateGetData.getExpire()), email, messageDigest);
                    postUserCall.enqueue(authenticatePostUserCallback);

                } else {
                    customDialog.dismiss();
                    NoInternet.show(Login.this);
                }
            } else {
                customDialog.dismiss();
                NoInternet.show(Login.this);
            }
        }

        @Override
        public void onFailure(Call<AuthenticateGetUser> call, Throwable t) {
            customDialog.dismiss();
            NoInternet.show(Login.this);
        }
    };

    private Callback<AuthenticatePostUser> authenticatePostUserCallback = new Callback<AuthenticatePostUser>() {
        @Override
        public void onResponse(Call<AuthenticatePostUser> call, Response<AuthenticatePostUser> response) {

            if (response.isSuccessful()) {
                authenticatePostUser = response.body();

                if (authenticatePostUser.getStatus()) {
                    accessData = authenticatePostUser.getData();
                    isFacebook = false;
                    customDialog.dismiss();
                    permissions.requestPermission(Login.this,false,
                            facebookData,username,email,password,accessData, Dashboard.class);
                } else {
                    customDialog.dismiss();
                    InvalidCredentials.show(Login.this);
                }
            } else {
                customDialog.dismiss();
                InvalidCredentials.show(Login.this);
            }

        }

        @Override
        public void onFailure(Call<AuthenticatePostUser> call, Throwable t) {
            customDialog.dismiss();
            NoInternet.show(Login.this);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults) {
        if(requestCode == 111)
        {
            if(permissions.hasPermissions(this,permission))
                permissions.requestPermission(this,false,
                        facebookData,username,email,password,accessData, Dashboard.class);
            else
                permissions.permissionsNotSelected(this);

        }else if(requestCode == 112)
        {
            if(permissions.hasPermissions(this,permission))
            permissions.requestPermission(Login.this,true,
                    facebookData,username,email,password,accessData, Dashboard.class);

            else
                permissions.permissionsNotSelected(this);
        }
    }


    private OAuthCallback oAuthCallback = new OAuthCallback() {
        @Override
        public void onFinished(OAuthData data) {
            oAuthData = data;
            if (!data.status.equals("error")) {
                customDialog.show();
                data.me(oAuthJSONCallback);
            }else
                FacebookLoginError.show(Login.this);
        }
    };

    private OAuthJSONCallback oAuthJSONCallback = new OAuthJSONCallback() {
        @Override
        public void onFinished(JSONObject data) {
            try {
                String id = data.getString("id");
                username = data.getString("email");

                facebookCall = apiService.getFacebookAccessToken(oAuthData.provider, oAuthData.token, id,"36000");
                facebookCall.enqueue(facebookResponseCallback);

            } catch (JSONException e) {
                customDialog.dismiss();
                FacebookLoginError.show(Login.this);
            }

        }

        @Override
        public void onError(String message) {
            customDialog.dismiss();
            FacebookLoginError.show(Login.this);
        }
    };


    private Callback<FacebookResponse> facebookResponseCallback = new Callback<FacebookResponse>() {
        @Override
        public void onResponse(Call<FacebookResponse> call, Response<FacebookResponse> response) {

            if (response.isSuccessful()) {
                FacebookResponse facebookResponse = response.body();

                if (facebookResponse.getStatus() && facebookResponse.getData().getResult()) {
                    customDialog.dismiss();
                    facebookData = facebookResponse.getData();
                    isFacebook = true;

                    permissions.requestPermission(Login.this,true,
                            facebookData,username,email,password,accessData, Dashboard.class);
                } else {
                    customDialog.dismiss();
                    FacebookLoginError.show(Login.this);
                }
            } else {
                customDialog.dismiss();
                FacebookLoginError.show(Login.this);
            }

        }

        @Override
        public void onFailure(Call<FacebookResponse> call, Throwable t) {
            customDialog.dismiss();
            FacebookLoginError.show(Login.this);
        }
    };
}
