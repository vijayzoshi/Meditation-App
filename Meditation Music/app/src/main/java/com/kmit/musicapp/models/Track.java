package com.kmit.musicapp.models;

import com.google.gson.annotations.SerializedName;
import com.kmit.musicapp.Model.Play_Artist;

import java.util.ArrayList;

/**
 * Created by Harjot on 30-Apr-16.
 */
public class Track {
    @SerializedName("title")
    private String mTitle;

    @SerializedName("id")
    private int mID;

    @SerializedName("duration")
    private int mDuration;

    @SerializedName("stream_url")
    private String mStreamURL;

    @SerializedName("artwork_url")
    private String mArtworkURL;


    String category_id;

    String playCount;

    String category_name;
    String is_liked;
    String is_in_playlist;
    String like_count;

    ArrayList<Play_Artist> artists;
    public Track(String mTitle, int mID, int mDuration, String mStreamURL, String mArtworkURL) {
        this.mTitle = mTitle;
        this.mID = mID;
        this.mDuration = mDuration;
        this.mStreamURL = mStreamURL;
        this.mArtworkURL = mArtworkURL;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getID() {
        return mID;
    }

    public int getDuration() {
        return mDuration;
    }

    public String getStreamURL() {
        return mStreamURL;
    }

    public String getArtworkURL() {
        return mArtworkURL;
    }



    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
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

    public ArrayList<Play_Artist> getArtists() {
        return artists;
    }

    public void setArtists(ArrayList<Play_Artist> artists) {
        this.artists = artists;
    }

}
