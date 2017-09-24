package com.labstract.lest.wallistract;

/**
 * Created by Adi on 21-01-2017.
 */
public class Category  {
    public String id, title, noOfPhotos;

    public Category() {
    }

    public Category(String id, String title,String noOfPhotos) {
        this.id = id;
        this.title = title;
        this.noOfPhotos=noOfPhotos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNoOfPhotos() {
        return noOfPhotos;
    }

    public void setNoOfPhotos(String noOfPhotos) {
        this.noOfPhotos = noOfPhotos;
    }

}
