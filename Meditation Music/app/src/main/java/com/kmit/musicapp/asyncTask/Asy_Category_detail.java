package com.kmit.musicapp.asyncTask;

import android.os.AsyncTask;
import android.util.Log;

import com.kmit.musicapp.Model.Play_Artist;
import com.kmit.musicapp.Model.Played;
import com.kmit.musicapp.Util.Constant;
import com.kmit.musicapp.Util.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;

public class Asy_Category_detail extends AsyncTask<String, String, String> {

    private RequestBody requestBody;
    private Listener_Recommended_Album listener_recommended_album;
    private ArrayList<Played> recommended_musics;
    private String artist_id = "";
    private String album_name = "";
    private String album_image = "";
    private String album_status = "";
    private String created_date = "";
    private String like_count = "";
    private String is_liked = "";

    public Asy_Category_detail(RequestBody requestBody, Listener_Recommended_Album listener_recommended_album) {
        this.listener_recommended_album = listener_recommended_album;
        this.requestBody = requestBody;
        recommended_musics = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        listener_recommended_album.onStart();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            String json = JSONParser.okhttpPost(Constant.Getcategory_music, requestBody);
//            JSONObject jsonObject = new JSONObject(json);
//
//            artist_id = jsonObject.getString("artist_id");
//            album_name = jsonObject.getString("album_name");
//            album_image = jsonObject.getString("album_image");
//            album_status = jsonObject.getString("album_status");
//            created_date = jsonObject.getString("created_date");
//            like_count = jsonObject.getString("like_count");
//            is_liked = jsonObject.getString("is_liked");

            JSONArray jsonarray =  new JSONArray(json);;
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);

                String music_id = jsonobject.getString("music_id");
                String category_id = jsonobject.getString("category_id");
                String music_title = jsonobject.getString("music_title");
                String music_file = jsonobject.getString("music_file");
                String music_image = jsonobject.getString("music_image");
                String music_duration = jsonobject.getString("music_duration");
                Played played = new Played(music_id, category_id, music_title, music_file,
                        music_image, music_duration,  "", is_liked,
                        "", like_count);
                recommended_musics.add(played);

            }
            return "1";
        } catch (Exception e) {
            Log.e("Asy_Album_detail", "" + e.getMessage().toString());
            e.printStackTrace();
            return "0";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        Log.e("Asy_Album_detail", "" + recommended_musics.size());
        listener_recommended_album.onEnd(s, recommended_musics,artist_id,album_name,album_image,album_status,created_date,like_count,is_liked);
        super.onPostExecute(s);
    }


    public interface Listener_Recommended_Album {
        void onStart();

        void onEnd(String success,
                   ArrayList<Played> recommended_musics,
                   String artist_id,
                   String album_name,
                   String album_image,
                   String album_status,
                   String created_date,
                   String like_count,
                   String is_liked);
    }


}