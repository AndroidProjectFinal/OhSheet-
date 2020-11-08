package com.example.ohsheet.entity;

import android.graphics.Bitmap;
import android.net.Uri;

public class Genre {
    private int genreId;
    private String genreName;
    private String genreImg;

    public Genre() {
    }


    public Genre(String genreName, String genreImg) {
        this.genreName = genreName;
        this.genreImg = genreImg;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public String getGenreImg() {
        return genreImg;
    }

    public void setGenreImg(String genreImg) {
        this.genreImg = genreImg;
    }

    @Override
    public String toString() {
        return genreName +" - "+genreImg;
    }
}
