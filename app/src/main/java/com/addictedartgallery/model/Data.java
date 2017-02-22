package com.addictedartgallery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("auth_request")
    @Expose
    private String authRequest;
    @SerializedName("expire")
    @Expose
    private Integer expire;
    @SerializedName("realm")
    @Expose
    private String realm;
    @SerializedName("docs")
    @Expose
    private String docs;

    /**
     *
     * @return
     * The authRequest
     */
    public String getAuthRequest() {
        return authRequest;
    }

    /**
     *
     * @param authRequest
     * The auth_request
     */
    public void setAuthRequest(String authRequest) {
        this.authRequest = authRequest;
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

    /**
     *
     * @return
     * The docs
     */
    public String getDocs() {
        return docs;
    }

    /**
     *
     * @param docs
     * The docs
     */
    public void setDocs(String docs) {
        this.docs = docs;
    }

}
