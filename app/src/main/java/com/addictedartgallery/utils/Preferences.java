package com.addictedartgallery.utils;


import android.content.Context;
import android.content.SharedPreferences;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Preferences {

    private final Context context;

    private static final String
            PREFERENCE_NAME = "UserProfile",
            EXPIRE = "expire",
            PASSWORD = "password",
            USERNAME = "username",
            ACCESS_TOKEN = "access_token",
            REALM = "realm",
            LOGIN = "login",
            REGISTRATION_TOKEN = "token",
            ALREADY_SUBSCRIBED = "isSubscribed",
            ENDPOINT_ARN = "endPointArn",
            FRESH_INSTALL = "freshInstall";

    public Preferences(Context context) {
        this.context = context;
    }

    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public void setAlreadySubscribed(boolean isSubscribed)
    {
        getSharedPreferences().edit().putBoolean(ALREADY_SUBSCRIBED,isSubscribed).apply();
    }

    public boolean isAlreadySubscribed()
    {
        return getSharedPreferences().getBoolean(ALREADY_SUBSCRIBED,false);
    }

    public void setEndpointArn(String endpointArn)
    {
        getSharedPreferences().edit().putString(ENDPOINT_ARN,endpointArn).apply();
    }

    public String getEndpointArn()
    {
        return getSharedPreferences().getString(ENDPOINT_ARN,null);
    }

    public void setRegistrationToken(String token)
    {
        getSharedPreferences().edit().putString(REGISTRATION_TOKEN,token).apply();
    }

    public String getRegistrationToken()
    {
        return getSharedPreferences().getString(REGISTRATION_TOKEN,null);
    }

    public void setExpire(String expire) {
        getSharedPreferences().edit().putString(EXPIRE, expire).apply();
    }

    public String getExpire() {
        return getSharedPreferences().getString(EXPIRE, null);
    }

    public void setPassword(String password) {
        getSharedPreferences().edit().putString(PASSWORD, password).apply();
    }

    public String getPassword() {
        return getSharedPreferences().getString(PASSWORD, null);
    }

    public void setUsername(String username) {
        getSharedPreferences().edit().putString(USERNAME, username).apply();
    }

    public String getUsername() {
        return getSharedPreferences().getString(USERNAME, null);
    }

    public void setAccessToken(String accessToken) {
        getSharedPreferences().edit().putString(ACCESS_TOKEN, accessToken).apply();
    }

    public String getAccessToken() {
        return getSharedPreferences().getString(ACCESS_TOKEN, null);
    }

    public void setRealm(String realm) {
        getSharedPreferences().edit().putString(REALM, realm).apply();
    }

    public String getRealm() {
        return getSharedPreferences().getString(REALM, null);
    }


    public void setUserDetails(String accessToken, String expire, String userName, String password, String realm) {
        getSharedPreferences().edit()
                .putString(ACCESS_TOKEN, accessToken)
                .putString(EXPIRE, expire)
                .putString(USERNAME, userName)
                .putString(PASSWORD, password)
                .putString(REALM, realm)
                .apply();
    }

    public void setFacebookDetails(String accessToken, String expire, String realm, String login, String username) {
        getSharedPreferences().edit()
                .putString(ACCESS_TOKEN, accessToken)
                .putString(EXPIRE, expire)
                .putString(REALM, realm)
                .putString(USERNAME, username)
                .putString(LOGIN, login)
                .apply();
    }

    public void setFreshInstall(boolean isFreshInstall)
    {
        getSharedPreferences().edit().putBoolean(FRESH_INSTALL, isFreshInstall).apply();
    }

    public boolean isFreshInstall()
    {
        return getSharedPreferences().getBoolean(FRESH_INSTALL, true);
    }

    public void clearData()
    {
        getSharedPreferences().edit()
                .putString(EXPIRE,null)
                .putString(PASSWORD,null)
                .putString(USERNAME, null)
                .putString(REALM,null)
                .putString(LOGIN,null)
                .putString(ACCESS_TOKEN,null)
                .putBoolean(ALREADY_SUBSCRIBED,false)
                .putString(ENDPOINT_ARN,null)
                .apply();
    }

}