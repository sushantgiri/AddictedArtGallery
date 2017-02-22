package com.addictedartgallery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProfileData  implements Serializable{

    @SerializedName("access")
    @Expose
    private String access;
    @SerializedName("arlist")
    @Expose
    private List<ArList> arlist = new ArrayList<>();
    @SerializedName("billing")
    @Expose
    private Billing billing;
    @SerializedName("birthday")
    @Expose
    private String birthday;
    @SerializedName("cart")
    @Expose
    private Cart cart;
    @SerializedName("favourites")
    @Expose
    private List<String> favourites = new ArrayList<>();
    @SerializedName("has_app")
    @Expose
    private Boolean hasApp;
    @SerializedName("login")
    @Expose
    private String login;
    @SerializedName("login_d")
    @Expose
    private String loginD;
    @SerializedName("newsletter")
    @Expose
    private Boolean newsletter;
    @SerializedName("registered")
    @Expose
    private String registered;
    @SerializedName("snstopic")
    @Expose
    private String snstopic;
    @SerializedName("subscribe_id")
    @Expose
    private String subscribeId;
    @SerializedName("subscribe_secret")
    @Expose
    private String subscribeSecret;
    @SerializedName("update")
    @Expose
    private String update;
    @SerializedName("viewed")
    @Expose
    private Viewed viewed;

    public String getLoginD() {
        return loginD;
    }

    public void setLoginD(String loginD) {
        this.loginD = loginD;
    }

    /**
     *
     * @return

     * The access
     */
    public String getAccess() {
        return access;
    }

    /**
     *
     * @param access
     * The access
     */
    public void setAccess(String access) {
        this.access = access;
    }

    /**
     *
     * @return
     * The arlist
     */
    public List<ArList> getArlist() {
        return arlist;
    }

    /**
     *
     * @param arlist
     * The arlist
     */
    public void setArlist(List<ArList> arlist) {
        this.arlist = arlist;
    }

    /**
     *
     * @return
     * The billing
     */
    public Billing getBilling() {
        return billing;
    }

    /**
     *
     * @param billing
     * The billing
     */
    public void setBilling(Billing billing) {
        this.billing = billing;
    }

    /**
     *
     * @return
     * The birthday
     */
    public String getBirthday() {
        return birthday;
    }

    /**
     *
     * @param birthday
     * The birthday
     */
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    /**
     *
     * @return
     * The cart
     */
    public Cart getCart() {
        return cart;
    }

    /**
     *
     * @param cart
     * The cart
     */
    public void setCart(Cart cart) {
        this.cart = cart;
    }

    /**
     *
     * @return
     * The favourites
     */
    public List<String> getFavourites() {
        return favourites;
    }

    /**
     *
     * @param favourites
     * The favourites
     */
    public void setFavourites(List<String> favourites) {
        this.favourites = favourites;
    }

    /**
     *
     * @return
     * The hasApp
     */
    public Boolean getHasApp() {
        return hasApp;
    }

    /**
     *
     * @param hasApp
     * The has_app
     */
    public void setHasApp(Boolean hasApp) {
        this.hasApp = hasApp;
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
     * The newsletter
     */
    public Boolean getNewsletter() {
        return newsletter;
    }

    /**
     *
     * @param newsletter
     * The newsletter
     */
    public void setNewsletter(Boolean newsletter) {
        this.newsletter = newsletter;
    }

    /**
     *
     * @return
     * The registered
     */
    public String getRegistered() {
        return registered;
    }

    /**
     *
     * @param registered
     * The registered
     */
    public void setRegistered(String registered) {
        this.registered = registered;
    }


    /**
     *
     * @return
     * The snstopic
     */
    public String getSnstopic() {
        return snstopic;
    }

    /**
     *
     * @param snstopic
     * The snstopic
     */
    public void setSnstopic(String snstopic) {
        this.snstopic = snstopic;
    }

    /**
     *
     * @return
     * The subscribeId
     */
    public String getSubscribeId() {
        return subscribeId;
    }

    /**
     *
     * @param subscribeId
     * The subscribe_id
     */
    public void setSubscribeId(String subscribeId) {
        this.subscribeId = subscribeId;
    }

    /**
     *
     * @return
     * The subscribeSecret
     */
    public String getSubscribeSecret() {
        return subscribeSecret;
    }

    /**
     *
     * @param subscribeSecret
     * The subscribe_secret
     */
    public void setSubscribeSecret(String subscribeSecret) {
        this.subscribeSecret = subscribeSecret;
    }

    /**
     *
     * @return
     * The update
     */
    public String getUpdate() {
        return update;
    }

    /**
     *
     * @param update
     * The update
     */
    public void setUpdate(String update) {
        this.update = update;
    }

    /**
     *
     * @return
     * The viewed
     */
    public Viewed getViewed() {
        return viewed;
    }

    /**
     *
     * @param viewed
     * The viewed
     */
    public void setViewed(Viewed viewed) {
        this.viewed = viewed;
    }

}
