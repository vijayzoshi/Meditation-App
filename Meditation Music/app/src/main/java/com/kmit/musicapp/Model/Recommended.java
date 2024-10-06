package com.kmit.musicapp.Model;

public class Recommended {
    String name;
    String Image;
    int id;

    public Recommended(String name, String image, int id) {
        this.name = name;
        Image = image;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
