package com.kmit.musicapp.asyncTask;

import android.os.AsyncTask;
import android.util.Log;

import com.kmit.musicapp.Model.Home_order;
import com.kmit.musicapp.Util.Constant;
import com.kmit.musicapp.Util.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;

public class Asy_Home_com extends AsyncTask<String, String, Boolean> {

    private RequestBody requestBody;
    private ArrayList<Home_order> home_com;
    private Listener_Home_com listener_home_com;

    public Asy_Home_com(RequestBody requestBody,Listener_Home_com listener_home_com) {
        Log.e("dcdcff","");
        this.requestBody = requestBody;
        this.listener_home_com = listener_home_com;
        this.home_com = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        listener_home_com.onStart();
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String json = JSONParser.okhttpPost(Constant.Home_Component_List,requestBody);

        Log.e("fgkiih",""+json);
        try {
            JSONArray  jsonarray = new JSONArray (json);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String home_components_id = jsonobject.getString("home_components_id");
                String home_components_name = jsonobject.getString("home_components_name");
                String home_components_order = jsonobject.getString("home_components_order");
                String home_components_status = jsonobject.getString("home_components_status");
                home_com.add(new Home_order(home_components_id,home_components_name,home_components_order,home_components_status));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return true;

    }

    @Override
    protected void onPostExecute(Boolean s) {
        listener_home_com.onEnd(String.valueOf(s), home_com);
        super.onPostExecute(s);
    }


    public interface Listener_Home_com {
        void onStart();
        void onEnd(String success, ArrayList<Home_order> arrayListFeatured);
    }
}