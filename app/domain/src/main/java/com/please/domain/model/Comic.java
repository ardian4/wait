package com.please.domain.model;


import androidx.annotation.NonNull;

import java.util.Objects;

public class Comic {
    //null alternative
    public final static Comic DUMMY = new Comic();

    private int mId;
    private String mTitle;
    private String mImage;
    private String mDescription;
    private boolean mIsFavorite;


    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        this.mImage = image;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public boolean isFavorite() {
        return mIsFavorite;
    }

    public void setFavorite(boolean isFavorite) {
        this.mIsFavorite = isFavorite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comic comic = (Comic) o;
        return mId == comic.mId && mIsFavorite == comic.mIsFavorite && Objects.equals(mTitle, comic.mTitle) && Objects.equals(mImage, comic.mImage) && Objects.equals(mDescription, comic.mDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mId, mTitle, mImage, mDescription, mIsFavorite);
    }

    @NonNull
    @Override
    public String toString() {
        return "Comic{" +
                "mId=" + mId +
                ", mTitle='" + mTitle + '\'' +
                ", mImage='" + mImage + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mIsFavorite=" + mIsFavorite +
                '}';
    }
}
