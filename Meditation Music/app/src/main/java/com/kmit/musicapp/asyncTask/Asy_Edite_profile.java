package com.kmit.musicapp.asyncTask;
import android.os.AsyncTask;
import android.util.Log;

import com.kmit.musicapp.Model.User;
import com.kmit.musicapp.Util.Constant;
import com.kmit.musicapp.Util.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.RequestBody;

public class Asy_Edite_profile extends AsyncTask<String, String, String> {

    private RequestBody requestBody;
    private Listener_Artist listener_artist;
    private String user_id;
    private String user_name;
    private String user_email;
    private String user_profile_pic;
    private String user_package_id;
    private String user_package_paid_date;
    private User user;


    public Asy_Edite_profile(RequestBody requestBody, Listener_Artist categoryListener) {
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
            String json = JSONParser.okhttpPost(Constant.EdiProfile, requestBody);
            Log.e("Asy_Logindsfc", "" + json);

            JSONArray jsonarray = new JSONArray(json);

            JSONObject jsonobject = jsonarray.getJSONObject(0);


            user_id = jsonobject.getString("user_id");
            user_name = jsonobject.getString("user_name");
            user_email = jsonobject.getString("user_email");
            user_profile_pic = jsonobject.getString("user_profile_pic");
            user = new User(user_id,user_name,user_email,user_profile_pic);
            return "1";
        } catch (Exception ee) {
            Log.e("Asy_Logindsfc", "" + ee.getMessage().toString());
            ee.printStackTrace();
            user = new User("0", "",  "", "");
            return "0";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        Log.e("Asy_Logindsfc", "" + s);

        listener_artist.onEnd(
                s,
                user
        );
        super.onPostExecute(s);
    }


    public interface Listener_Artist {
        void onStart();

        void onEnd(String success,
                   User user_id);
    }
}
