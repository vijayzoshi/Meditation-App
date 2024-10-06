package com.kmit.musicapp.asyncTask;

import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.ads.MobileAds;
import com.kmit.musicapp.Util.Constant;
import com.kmit.musicapp.Util.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

public class Asy_setting extends AsyncTask<String, String, String> {


    private String settings_flag_value;
    private JSONObject jsonobject;
    private Context context;

    public Asy_setting(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            String json = JSONParser.okhttpGET(Constant.Settings_Flag);

            JSONArray jsonarray = new JSONArray(json);

            jsonobject = jsonarray.getJSONObject(0);

            settings_flag_value = jsonobject.getString("settings_flag_value");

            if(settings_flag_value.equals("ENABLE")){
                Constant.Notification = true;
            }

            jsonobject = jsonarray.getJSONObject(1);
            settings_flag_value = jsonobject.getString("settings_flag_value");

            if(settings_flag_value.equals("ENABLE")){
                Constant.Package = true;
            }
            jsonobject = jsonarray.getJSONObject(2);
            settings_flag_value = jsonobject.getString("settings_flag_value");


            if(settings_flag_value.equals("ENABLE")){
                Constant.Ads = true;
            }
            jsonarray = jsonobject.getJSONArray("settings_flag_parameters");

            JSONObject jsonobjectsa = jsonarray.getJSONObject(0);
            Constant.appads = jsonobjectsa.getString("ads_id_value");

            jsonobjectsa = jsonarray.getJSONObject(1);
            Constant.bannerads = jsonobjectsa.getString("ads_id_value");

            jsonobjectsa = jsonarray.getJSONObject(2);
            Constant.interstitialads = jsonobjectsa.getString("ads_id_value");

            MobileAds.initialize(context, Constant.appads);
            return "1";
        } catch (Exception ee) {
            return "0";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }



}