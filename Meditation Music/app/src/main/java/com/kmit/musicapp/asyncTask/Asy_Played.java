package com.kmit.musicapp.asyncTask;

import android.os.AsyncTask;
import android.util.Log;

import com.kmit.musicapp.Model.Play_Artist;
import com.kmit.musicapp.Model.Played;
import com.kmit.musicapp.Util.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;

public class Asy_Played extends AsyncTask<String, String, String> {

    private String s;
    private RequestBody requestBody;
    private Listener_Played listener_played;
    private ArrayList<Played> playeds;

    public Asy_Played(RequestBody requestBody,String s,Listener_Played listener_played) {
        this.listener_played = listener_played;
        this.s = s;
        this.requestBody = requestBody;
        playeds = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        listener_played.onStart();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        String json = JSONParser.okhttpPost(s, requestBody);
        try {
            Log.e("fgkiih", "" + json);
            JSONArray jsonarray = new JSONArray(json);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String music_id = jsonobject.getString("music_id");
                String category_id = jsonobject.getString("category_id");
                String music_title = jsonobject.getString("music_title");
                String music_file = jsonobject.getString("music_file");
                String music_image = jsonobject.getString("music_image");
                String music_duration = jsonobject.getString("music_duration");
                String playCount = jsonobject.getString("playCount");
                String category_name = jsonobject.getString("category_name");
                String is_liked = jsonobject.getString("is_liked");
                String is_in_playlist = jsonobject.getString("is_in_playlist");
                String like_count = jsonobject.getString("like_count");

                Played played = new Played(music_id, category_id, music_title, music_file,
                        music_image, music_duration,  category_name,  is_liked,
                        is_in_playlist, like_count);
                played.setPlayCount(playCount);
                playeds.add(played);

            }

            return "1";
        } catch (Exception ee) {
            Log.e("fgkiihx", "" + ee.getMessage().toString());
            ee.printStackTrace();
            return "0";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        Log.e("fgkiihx", "" + playeds.size());
        Log.e("fgkiihx", "" + s);

        listener_played.onEnd(s, playeds);
        super.onPostExecute(s);
    }


    public interface Listener_Played {
        void onStart();

        void onEnd(String success, ArrayList<Played> artists);
    }
}