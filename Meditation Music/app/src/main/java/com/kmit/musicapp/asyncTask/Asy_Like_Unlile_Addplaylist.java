package com.kmit.musicapp.asyncTask;

import android.os.AsyncTask;
import android.util.Log;

import com.kmit.musicapp.Util.JSONParser;

import okhttp3.RequestBody;

public class Asy_Like_Unlile_Addplaylist extends AsyncTask<String, String, String> {

    private RequestBody requestBody;
    private Listener_Artist listener_artist;
    private String s;


    public Asy_Like_Unlile_Addplaylist(RequestBody requestBody, String s, Listener_Artist categoryListener) {

        this.s = s;
        this.listener_artist = categoryListener;
        this.requestBody = requestBody;
    }

    @Override
    protected void onPreExecute() {
        listener_artist.onStart();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            String json = JSONParser.okhttpPost(s, requestBody);

            return "1";
        } catch (Exception ee) {
            Log.e("item_and_click", "" + ee.getMessage().toString());
            ee.printStackTrace();
            return "0";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        Log.e("item_and_click", "" + s);

        listener_artist.onEnd(
                s
        );
        super.onPostExecute(s);
    }


    public interface Listener_Artist {
        void onStart();

        void onEnd(String success);
    }
}
