package com.kmit.musicapp.asyncTask;

import android.os.AsyncTask;
import android.util.Log;

import com.kmit.musicapp.Model.Banner_Slide;
import com.kmit.musicapp.Util.Constant;
import com.kmit.musicapp.Util.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;

public class Asy_Banner_Slide extends AsyncTask<String, String, Boolean> {

    private RequestBody requestBody;
    private ArrayList<Banner_Slide> banner_slides;
    Listener_Banner_Slide homeListener;

    public Asy_Banner_Slide(RequestBody requestBody,Listener_Banner_Slide homeListener) {
        this.requestBody = requestBody;
        banner_slides = new ArrayList<>();
        this.homeListener =homeListener;
    }

    @Override
    protected void onPreExecute() {
        homeListener.onStart();
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String jsons = JSONParser.okhttpPost(Constant.Home, requestBody);
        Log.e("aaaaaaabanner", "" + jsons);
        try {
            JSONArray  jsonarray = new JSONArray (jsons);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);


                String category_id = jsonobject.getString("category_id");
                String category_name = jsonobject.getString("category_name");
                String category_banner_text = jsonobject.getString("category_banner_text");
                String category_banner = jsonobject.getString("category_banner");
                String music_count = jsonobject.getString("music_count");

                banner_slides.add(new Banner_Slide(category_id,category_name,category_banner_text,category_banner,music_count));

            }
        } catch (JSONException e) {
            Log.e("aaaaaaabanner",""+e);
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean s) {
        homeListener.onEnd(String.valueOf(s), banner_slides);
        super.onPostExecute(s);
    }


    public interface Listener_Banner_Slide {
        void onStart();
        void onEnd(String success, ArrayList<Banner_Slide> arrayListFeatured);
    }
}