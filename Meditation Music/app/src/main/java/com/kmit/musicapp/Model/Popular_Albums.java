package com.kmit.musicapp.Model;

public class Popular_Albums {

    String album_id;
    String album_name;
    String album_image;
    String viewCount;
    String is_liked;
    String like_count;
    String music_count;

    public Popular_Albums(String album_id, String album_name, String album_image, String viewCount, String is_liked, String like_count, String music_count) {
        this.album_id = album_id;
        this.album_name = album_name;
        this.album_image = album_image;
        this.viewCount = viewCount;
        this.is_liked = is_liked;
        this.like_count = like_count;
        this.music_count = music_count;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    public String getAlbum_image() {
        return album_image;
    }

    public void setAlbum_image(String album_image) {
        this.album_image = album_image;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public String getIs_liked() {
        return is_liked;
    }

    public void setIs_liked(String is_liked) {
        this.is_liked = is_liked;
    }

    public String getLike_count() {
        return like_count;
    }

    public void setLike_count(String like_count) {
        this.like_count = like_count;
    }

    public String getMusic_count() {
        return music_count;
    }

    public void setMusic_count(String music_count) {
        this.music_count = music_count;
    }
}
