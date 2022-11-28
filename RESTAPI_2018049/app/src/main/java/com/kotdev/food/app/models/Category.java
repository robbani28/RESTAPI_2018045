package com.kotdev.food.app.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Ignore;

public class Category {

    private String title;
    private int image_url;
    private String social_rank;

    public Category() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage_url() {
        return image_url;
    }

    public void setImage_url(int image_url) {
        this.image_url = image_url;
    }

    public String getSocial_rank() {
        return social_rank;
    }

    public void setSocial_rank(String social_rank) {
        this.social_rank = social_rank;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                ", title='" + title + '\'' +
                ", image_url='" + image_url + '\'' +
                ", social_rank=" + social_rank +
                '}';
    }
}
