package com.example.listapp;

import android.graphics.drawable.Drawable;

public class Item {

    private String text;
    private String subtitle;

    public Item(String text) {
        this.text = text;
        this.subtitle = subtitle;
    }


    public String getTitle() {
        return text;
    }

    public String getSubtitle() {
        return subtitle;
    }
}
