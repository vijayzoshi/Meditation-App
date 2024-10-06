package com.kmit.musicapp.asyncTask;

import android.os.AsyncTask;
import android.util.Log;

import com.kmit.musicapp.Model.Own_Play_List;
import com.kmit.musicapp.Util.Constant;
import com.kmit.musicapp.Util.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;

public class Asy_User_Playlist extends AsyncTask<String, String, String> {

    private RequestBody requestBody;
    private Listener_Played listener_played;
        private ArrayList<Own_Play_List> playeds;

    public Asy_User_Playlist(RequestBody requestBody,Listener_Played listener_played) {
        this.listener_played = listener_played;
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
        String json = JSONParser.okhttpPost(Constant.Get_User_Playlists, requestBody);
        try {
            Log.e("fgkiih", "" + json);
            JSONArray jsonarray = new JSONArray(json);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String user_playlist_id = jsonobject.getString("user_playlist_id");
                String user_playlist_name = jsonobject.getString("user_playlist_name");
                String music_count = jsonobject.getString("music_count");
                JSONArray artists = jsonobject.getJSONArray("images");
                ArrayList<String> play_artists = new ArrayList<>();
                for (int j = 0; j < artists.length(); j++) {
                    JSONObject artist = artists.getJSONObject(j);
                    String artist_ids = artist.getString("music_image");
                    play_artists.add(artist_ids);

                }

                Own_Play_List played = new Own_Play_List(user_playlist_id,user_playlist_name,music_count,play_artists);
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

        void onEnd(String success, ArrayList<Own_Play_List> artists);
    }
}