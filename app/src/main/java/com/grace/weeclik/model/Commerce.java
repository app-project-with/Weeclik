package com.grace.weeclik.model;

/**
 * com.grace.weeclik.our_objects
 * Created by grace on 06/05/2018.
 */
public class Commerce {

    private String name;
    private int nbShare;
    private String thumbnail;

    public Commerce() {}

    public Commerce(String name, int nbShare, String thumbnail) {
        this.name = name;
        this.nbShare = nbShare;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getNbShare() {
        return nbShare;
    }

    public void setNbShare(int nbShare) {
        this.nbShare = nbShare;
    }

}
