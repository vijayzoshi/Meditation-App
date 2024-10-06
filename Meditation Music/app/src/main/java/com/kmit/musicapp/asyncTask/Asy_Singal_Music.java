package com.kmit.musicapp.asyncTask;

import android.os.AsyncTask;
import android.util.Log;

import com.kmit.musicapp.Model.Played;
import com.kmit.musicapp.Util.Constant;
import com.kmit.musicapp.Util.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;

public class Asy_Singal_Music extends AsyncTask<String, String, String> {

    private RequestBody requestBody;
    private Listener_Played listener_played;

    public Asy_Singal_Music(RequestBody requestBody, Listener_Played listener_played) {
        this.listener_played = listener_played;
        this.requestBody = requestBody;
    }

    @Override
    protected void onPreExecute() {
        listener_played.onStart();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            String json = JSONParser.okhttpPost(Constant.SingalMusic, requestBody);
            return "1";
        } catch (Exception ee) {
            Log.e("fgkiihx", "" + ee.getMessage().toString());
            ee.printStackTrace();
            return "0";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        Log.e("fgkiihx", "" + s);

        listener_played.onEnd(s);
        super.onPostExecute(s);
    }


    public interface Listener_Played {
        void onStart();

        void onEnd(String success);
    }
}