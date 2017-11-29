package com.example.jeff.jeff23andme;

import java.io.Serializable;

/**
 * Created by jeff on 11/22/17.
 */

public class ImageLike implements Serializable {
    private static final long serialVersionUID = 1L;

    private String mediaID;
    private String url;
    private boolean isLiked;

    public ImageLike(String mediaID, String url, boolean isLiked) {
        this.mediaID = mediaID;
        this.url = url;
        this.isLiked = isLiked;
    }

    public String getMediaID() {
        return mediaID;
    }

    public void setMediaID(String mediaID) {
        this.mediaID = mediaID;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}
