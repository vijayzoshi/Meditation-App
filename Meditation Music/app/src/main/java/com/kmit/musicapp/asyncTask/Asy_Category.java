package com.kmit.musicapp.asyncTask;

import android.os.AsyncTask;
import android.util.Log;

import com.kmit.musicapp.Model.Category;
import com.kmit.musicapp.Model.Popular_Albums;
import com.kmit.musicapp.Util.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;

public class Asy_Category extends AsyncTask<String, String, String> {

    private final String s;
    private RequestBody requestBody;
    private Listener_Recommended_Album listener_recommended_album;
    private ArrayList<Category> recommended_albums;

    public Asy_Category(RequestBody requestBody, String s, Listener_Recommended_Album listener_recommended_album) {
        this.listener_recommended_album = listener_recommended_album;
        this.s = s;
        this.requestBody = requestBody;
        recommended_albums =new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        listener_recommended_album.onStart();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            String json = JSONParser.okhttpPost(s, requestBody);
            JSONArray jsonarray = new JSONArray (json);
            Log.e("Asy_Recommended_Album",""+jsonarray.length());
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);

                String category_id = jsonobject.getString("category_id");
                String category_name = jsonobject.getString("category_name");
                String category_image = jsonobject.getString("category_icon");
                String music_count = jsonobject.getString("music_count");

                recommended_albums.add(new Category(category_id,category_name,category_image,music_count));

            }
            return "1";
        } catch (Exception e) {
            Log.e("Asy_Recommended_Album",""+e.getMessage().toString());
            e.printStackTrace();
            return "0";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        Log.e("Asy_Recommended_Album",""+recommended_albums.size());
        listener_recommended_album.onEnd(s, recommended_albums);
        super.onPostExecute(s);
    }


    public interface Listener_Recommended_Album {
        void onStart();

        void onEnd(String success, ArrayList<Category> recommended_albums);
    }
}
