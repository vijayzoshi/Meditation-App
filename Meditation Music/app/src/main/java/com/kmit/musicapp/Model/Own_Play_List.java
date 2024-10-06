package com.kmit.musicapp.Model;

import java.util.ArrayList;

public class Own_Play_List {
    String user_playlist_id;
    String user_playlist_name;
    String music_count;
    ArrayList<String> images;

    public Own_Play_List(String user_playlist_id, String user_playlist_name, String music_count, ArrayList<String> images) {
        this.user_playlist_id = user_playlist_id;
        this.user_playlist_name = user_playlist_name;
        this.music_count = music_count;
        this.images = images;
    }

    public String getUser_playlist_id() {
        return user_playlist_id;
    }

    public void setUser_playlist_id(String user_playlist_id) {
        this.user_playlist_id = user_playlist_id;
    }

    public String getUser_playlist_name() {
        return user_playlist_name;
    }

    public void setUser_playlist_name(String user_playlist_name) {
        this.user_playlist_name = user_playlist_name;
    }

    public String getMusic_count() {
        return music_count;
    }

    public void setMusic_count(String music_count) {
        this.music_count = music_count;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }
}
