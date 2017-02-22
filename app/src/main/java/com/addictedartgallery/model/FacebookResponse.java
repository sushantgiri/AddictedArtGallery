package com.addictedartgallery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FacebookResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private FacebookData data;

    /**
     *
     * @return
     * The status
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(Boolean status) {
        this.status = status;
    }

    /**
     *
     * @return
     * The message
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     * The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     *
     * @return
     * The data
     */
    public FacebookData getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(FacebookData data) {
        this.data = data;
    }

}
