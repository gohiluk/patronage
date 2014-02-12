package com.example.app;

import android.graphics.Bitmap;

/**
 * Created by gohilukk on 12.02.14.
 */
public class Item {
    String title;
    Bitmap image;

    public Item(String title, Bitmap image)
    {
        this.title = title;
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
