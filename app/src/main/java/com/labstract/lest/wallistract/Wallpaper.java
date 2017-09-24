package com.labstract.lest.wallistract;

import java.io.Serializable;

/**
 * Created by Adi on 21-01-2017.
 */
public class Wallpaper implements Serializable {
    private static final long serialVersionUID = 1L;
    private String url, photoJson;
    private int width, height;

    public Wallpaper() {
    }

    public Wallpaper( String url) {
        this.photoJson = photoJson;
        this.url = url;
        this.width = width;
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhotoJson() {
        return photoJson;
    }

    public void setPhotoJson(String photoJson) {
        this.photoJson = photoJson;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
