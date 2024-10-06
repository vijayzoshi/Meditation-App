package com.kmit.musicapp.asyncTask;

import android.os.AsyncTask;
import android.util.Log;

import com.kmit.musicapp.Util.Constant;
import com.kmit.musicapp.Util.JSONParser;

import okhttp3.RequestBody;

public class Asy_payment extends AsyncTask<String, String, String> {

    private RequestBody requestBody;
    private Listener_Recommended_Album listener_recommended_album;

    public Asy_payment(RequestBody requestBody, Listener_Recommended_Album listener_recommended_album) {
        this.listener_recommended_album = listener_recommended_album;
        this.requestBody = requestBody;
    }

    @Override
    protected void onPreExecute() {
        listener_recommended_album.onStart();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            String json = JSONParser.okhttpPost(Constant.Payment, requestBody);
            return json;
        } catch (Exception e) {
            Log.e("Asy_Artist_detail", "" + e.getMessage().toString());
            e.printStackTrace();
            return "0";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        listener_recommended_album.onEnd(s);
        super.onPostExecute(s);
    }


    public interface Listener_Recommended_Album {
        void onStart();

        void onEnd(String success);
    }
}