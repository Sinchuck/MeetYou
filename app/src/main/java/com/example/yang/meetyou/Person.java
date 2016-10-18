package com.example.yang.meetyou;

import android.graphics.drawable.Drawable;

/**
 * Created by Yang on 2016/9/25.
 */
public class Person {
    public Drawable getHeads() {
        return heads;
    }

    public void setHeads(Drawable heads) {
        this.heads = heads;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private Drawable heads;
    private String name;

    public Person(Drawable drawable, String name) {
        heads = drawable;
        this.name = name;
    }

}
