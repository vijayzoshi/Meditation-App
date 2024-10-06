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

public class Asy_Playlist_detail  extends AsyncTask<String, String, String> {

    private RequestBody requestBody;
    private Listener_Recommended_Album listener_recommended_album;
    private ArrayList<Played> recommended_musics;

    public Asy_Playlist_detail(RequestBody requestBody, Listener_Recommended_Album listener_recommended_album) {
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
            String json = JSONParser.okhttpPost(Constant.Get_user_playlist_music, requestBody);
            JSONArray jsonObject = new JSONArray(json);

            for (int i = 0; i < jsonObject.length(); i++) {
                JSONObject jsonobject = jsonObject.getJSONObject(i);

                String music_id = jsonobject.getString("music_id");
                String category_id = jsonobject.getString("category_id");
                String music_title = jsonobject.getString("music_title");
                String music_file = jsonobject.getString("music_file");
                String music_image = jsonobject.getString("music_image");
                String music_duration = jsonobject.getString("music_duration");
                String category_name = jsonobject.getString("category_name");
                String is_liked = jsonobject.getString("is_liked");
                String is_in_playlist = jsonobject.getString("is_in_playlist");
                String like_count = jsonobject.getString("like_count");

                Played played = new Played(music_id,  category_id, music_title, music_file,
                        music_image, music_duration,  category_name, is_liked,
                        is_in_playlist, like_count);
                recommended_musics.add(played);

            }
            return "1";
        } catch (Exception e) {
            Log.e("Asy_Playlist_detail", "" + e.getMessage().toString());
            e.printStackTrace();
            return "0";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        Log.e("Asy_Playlist_detail", "" + recommended_musics.size());
        listener_recommended_album.onEnd(s, recommended_musics);
        super.onPostExecute(s);
    }


    public interface Listener_Recommended_Album {
        void onStart();

        void onEnd(String success,
                   ArrayList<Played> recommended_musics
        );
    }
}