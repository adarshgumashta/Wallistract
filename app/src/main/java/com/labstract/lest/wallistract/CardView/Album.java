package com.labstract.lest.wallistract.CardView;

/**
 * Created by Adi on 15-01-2017.
 */
public class Album {
    private int thumbnail;
    private String Title;
    private String NoOfImages;
    public Album() {
    }
    public Album(int thumbnail,String title,String NoOfImages) {
        this.thumbnail = thumbnail;
        this.Title=title;
        this.NoOfImages=NoOfImages;
    }
    public int getThumbnail() {
        return thumbnail;
    }
    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getNoOfImages() {
        return NoOfImages;
    }

    public void setNoOfImages(String noOfImages) {
        NoOfImages = noOfImages;
    }
}
