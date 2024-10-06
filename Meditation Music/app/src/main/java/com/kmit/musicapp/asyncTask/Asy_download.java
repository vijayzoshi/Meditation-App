package com.kmit.musicapp.asyncTask;

import android.os.AsyncTask;
import android.util.Log;

import com.kmit.musicapp.Util.Constant;
import com.kmit.musicapp.Util.JSONParser;

import org.json.JSONObject;

import okhttp3.RequestBody;

public class Asy_download extends AsyncTask<String, String, String> {

    private RequestBody requestBody;
    private Listener_Artist listener_artist;


    public Asy_download(RequestBody requestBody, Listener_Artist categoryListener) {

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
            String json = JSONParser.okhttpPost(Constant.Check_Download_allowed, requestBody);
            JSONObject jsonObject = new JSONObject(json);

            return jsonObject.getString("is_allow_downloads");
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
