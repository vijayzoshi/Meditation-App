package com.kmit.musicapp.Model;

public class Banner_Slide {

    String category_id;
    String category_name;
    String category_banner_text;
    String category_banner;
    String music_count;

    public Banner_Slide(String category_id, String category_name, String category_banner_text, String category_banner, String music_count) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.category_banner_text = category_banner_text;
        this.category_banner = category_banner;
        this.music_count = music_count;
    }


    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_banner_text() {
        return category_banner_text;
    }

    public void setCategory_banner_text(String category_banner_text) {
        this.category_banner_text = category_banner_text;
    }

    public String getCategory_banner() {
        return category_banner;
    }

    public void setCategory_banner(String category_banner) {
        this.category_banner = category_banner;
    }

    public String getMusic_count() {
        return music_count;
    }

    public void setMusic_count(String music_count) {
        this.music_count = music_count;
    }
}
