package com.kmit.musicapp.interfaces;


import java.util.ArrayList;

public interface HomeListener {
    void onStart();
    void onEnd(String success, ArrayList<Object> arrayListFeatured);
}