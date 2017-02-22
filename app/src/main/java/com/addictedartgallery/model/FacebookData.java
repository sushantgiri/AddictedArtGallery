package com.addictedartgallery.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FacebookData {

    @SerializedName("result")
    @Expose
    private Boolean result;
    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("expire")
    @Expose
    private Integer expire;
    @SerializedName("login")
    @Expose
    private String login;
    @SerializedName("realm")
    @Expose
    private String realm;

    /**
     *
     * @return
     * The result
     */
    public Boolean getResult() {
        return result;
    }

    /**
     *
     * @param result
     * The result
     */
    public void setResult(Boolean result) {
        this.result = result;
    }

    /**
     *
     * @return
     * The accessToken
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     *
     * @param accessToken
     * The access_token
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     *
     * @return
     * The expire
     */
    public Integer getExpire() {
        return expire;
    }

    /**
     *
     * @param expire
     * The expire
     */
    public void setExpire(Integer expire) {
        this.expire = expire;
    }

    /**
     *
     * @return
     * The login
     */
    public String getLogin() {
        return login;
    }

    /**
     *
     * @param login
     * The login
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     *
     * @return
     * The realm
     */
    public String getRealm() {
        return realm;
    }

    /**
     *
     * @param realm
     * The realm
     */
    public void setRealm(String realm) {
        this.realm = realm;
    }

}
