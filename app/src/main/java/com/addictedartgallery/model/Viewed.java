package com.addictedartgallery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Viewed implements Serializable {

    @SerializedName("products")
    @Expose
    private List<String> products = new ArrayList<String>();
    @SerializedName("artists")
    @Expose
    private List<String> artists = new ArrayList<String>();
    @SerializedName("pages")
    @Expose
    private List<String> pages = new ArrayList<String>();
    @SerializedName("articles")
    @Expose
    private List<String> articles = new ArrayList<String>();

    /**
     *
     * @return
     * The products
     */
    public List<String> getProducts() {
        return products;
    }

    /**
     *
     * @param products
     * The products
     */
    public void setProducts(List<String> products) {
        this.products = products;
    }

    /**
     *
     * @return
     * The artists
     */
    public List<String> getArtists() {
        return artists;
    }

    /**
     *
     * @param artists
     * The artists
     */
    public void setArtists(List<String> artists) {
        this.artists = artists;
    }

    /**
     *
     * @return
     * The pages
     */
    public List<String> getPages() {
        return pages;
    }

    /**
     *
     * @param pages
     * The pages
     */
    public void setPages(List<String> pages) {
        this.pages = pages;
    }

    /**
     *
     * @return
     * The articles
     */
    public List<String> getArticles() {
        return articles;
    }

    /**
     *
     * @param articles
     * The articles
     */
    public void setArticles(List<String> articles) {
        this.articles = articles;
    }

}
