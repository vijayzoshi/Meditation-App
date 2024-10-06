package com.kmit.musicapp.Model;

public class Played {

    String music_id;
    String category_id;
    String music_title;
    String music_file;
    String music_image;

    String music_duration;
    String playCount;
    String is_in_playlist;

    String category_name;
    String is_liked;
    String like_count;


    public Played(String music_id, String category_id, String music_title, String music_file, String music_image, String music_duration,  String category_name, String is_liked, String is_in_playlist, String like_count) {
        this.music_id = music_id;
        this.category_id = category_id;
        this.music_title = music_title;
        this.music_file = music_file;
        this.music_image = music_image;
        this.music_duration = music_duration;
        this.category_name = category_name;
        this.is_liked = is_liked;
        this.is_in_playlist = is_in_playlist;
        this.like_count = like_count;
    }


    public String getMusic_id() {
        return music_id;
    }

    public void setMusic_id(String music_id) {
        this.music_id = music_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getMusic_title() {
        return music_title;
    }

    public void setMusic_title(String music_title) {
        this.music_title = music_title;
    }

    public String getMusic_file() {
        return music_file;
    }

    public void setMusic_file(String music_file) {
        this.music_file = music_file;
    }

    public String getMusic_image() {
        return music_image;
    }

    public void setMusic_image(String music_image) {
        this.music_image = music_image;
    }

    public String getMusic_duration() {
        return music_duration;
    }

    public void setMusic_duration(String music_duration) {
        this.music_duration = music_duration;
    }

    public String getPlayCount() {
        return playCount;
    }

    public void setPlayCount(String playCount) {
        this.playCount = playCount;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getIs_liked() {
        return is_liked;
    }

    public void setIs_liked(String is_liked) {
        this.is_liked = is_liked;
    }

    public String getIs_in_playlist() {
        return is_in_playlist;
    }

    public void setIs_in_playlist(String is_in_playlist) {
        this.is_in_playlist = is_in_playlist;
    }

    public String getLike_count() {
        return like_count;
    }

    public void setLike_count(String like_count) {
        this.like_count = like_count;
    }

}
