package com.kmit.musicapp.asyncTask;

import android.os.AsyncTask;
import android.util.Log;

import com.kmit.musicapp.Util.Constant;
import com.kmit.musicapp.Util.JSONParser;

import java.util.ArrayList;

import okhttp3.RequestBody;

public class Asy_token extends AsyncTask<String,String,String> {

    private Tokensss tokensss;
    private RequestBody requestBody;

    public Asy_token(RequestBody requestBody,Tokensss tokensss) {
        this.tokensss = tokensss;
        this.requestBody = requestBody;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            String json = JSONParser.okhttpPost(Constant.token,requestBody);


            return "1";
        } catch (Exception ee) {
            Log.e("Asy_Artistdsfc", "" + ee.getMessage().toString());
            ee.printStackTrace();
            return "0";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        Log.e("Asy_Artistdsfc", "" + s);
        tokensss.onEnd(s);
        super.onPostExecute(s);
    }

    public interface Tokensss {
        void onEnd(String success);
    }

}
