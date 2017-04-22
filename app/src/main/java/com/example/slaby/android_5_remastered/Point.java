package com.example.slaby.android_5_remastered;

import android.widget.ImageView;

/**
 * Created by Slaby on 22.04.2017.
 */

public class Point {
    static MainActivity mainActivity;
    int x;
    int y;
    int id;
    ImageView image;
    Type type;

    Point(int id, int x, int y, Type type, ImageView image) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.type = type;
        this.image = image;
    }

    public void setPointState(Type type) {
        this.type = type;
        mainActivity.setCellDrawable(id, type);
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "]" + "[" + id + "]" + "[" + type + "]";
    }
}
