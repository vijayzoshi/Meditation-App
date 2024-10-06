package com.kmit.musicapp.Util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.kmit.musicapp.R;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class Methods {
    Activity activity;

    public Methods(Activity activity) {
        this.activity = activity;
    }

    public RequestBody getAPIRequesttoken(String method) {
        RequestBody requestBody;
        requestBody = new FormBody.Builder()
                .add("user_token", method)
                .build();
        return requestBody;

    }


    public RequestBody getAPIRequest(String method, String one, String two, String three, String four, String five) {
        RequestBody requestBody;

        Log.e("aaaa", "" + one);
        Log.e("aaaa", "" + two);
        Log.e("aaaa", "" + three);
        Log.e("aaaa", "" + four);
        Log.e("aaaa", "" + five);
        switch (method) {
            case Constant.METHOD_HOME_COMPONENT_LISt:
                requestBody = new FormBody.Builder()
                        .add("user_id", two)
                        .build();
                return requestBody;
            case Constant.METHOD_HOME_Banner_Slider:
                requestBody = new FormBody.Builder()
                        .add("home_components_name", one)
                        .add("user_id", two)
                        .add("is_myProfile", three)
                        .build();


                return requestBody;
            case Constant.METHOD_HOME_Sleep_Stories:
                if (four.equals("") & five.equals("")) {
                    requestBody = new FormBody.Builder()
                            .add("home_components_name", one)
                            .add("user_id", two)
                            .add("is_myProfile", three)
                            .build();
                    return requestBody;
                } else {
                    requestBody = new FormBody.Builder()
                            .add("home_components_name", one)
                            .add("user_id", two)
                            .add("is_myProfile", three)
                            .add("start", five)
                            .add("end", four)
                            .build();
                    return requestBody;

                }

            case Constant.METHOD_HOME_Popular_Sounds:
                if (four.equals("") & five.equals("")) {
                    requestBody = new FormBody.Builder()
                            .add("home_components_name", one)
                            .add("user_id", two)
                            .add("is_myProfile", three)
                            .build();
                    return requestBody;
                } else {
                    requestBody = new FormBody.Builder()
                            .add("home_components_name", one)
                            .add("user_id", two)
                            .add("is_myProfile", three)
                            .add("start", five)
                            .add("end", four)
                            .build();
                    return requestBody;

                }
            case Constant.METHOD_HOME_Top_Sounds:
                if (four.equals("") & five.equals("")) {
                    requestBody = new FormBody.Builder()
                            .add("home_components_name", one)
                            .add("user_id", two)
                            .add("is_myProfile", three)
                            .build();
                    return requestBody;
                } else {
                    requestBody = new FormBody.Builder()
                            .add("home_components_name", one)
                            .add("user_id", two)
                            .add("is_myProfile", three)
                            .add("start", five)
                            .add("end", four)
                            .build();
                    return requestBody;

                }


            case Constant.METHOD_HOME_Recently_Played:
                if (four.equals("") & five.equals("")) {
                    requestBody = new FormBody.Builder()
                            .add("home_components_name", one)
                            .add("user_id", two)
                            .add("is_myProfile", three)
                            .build();
                    return requestBody;
                } else {
                    requestBody = new FormBody.Builder()
                            .add("home_components_name", one)
                            .add("user_id", two)
                            .add("is_myProfile", three)
                            .add("start", five)
                            .add("end", four)
                            .build();
                    return requestBody;

                }

            case Constant.METHOD_HOME_Recommended_Music:
                requestBody = new FormBody.Builder()
                        .add("home_components_name", one)
                        .add("user_id", two)
                        .add("is_myProfile", three)
                        .build();
                return requestBody;

            case Constant.METHOD_All_music:
                requestBody = new FormBody.Builder()
                        .add("user_id", one)
                        .add("start", two)
                        .add("end", three)
                        .build();
                return requestBody;
            case Constant.Music_Screen:
                requestBody = new FormBody.Builder()
                        .add("music_id", one)
                        .add("user_id", two)
                        .build();
                return requestBody;
            case Constant.METHOD_LOGIN:
                requestBody = new FormBody.Builder()
                        .add("user_email", one)
                        .add("user_password", two)
                        .build();
                return requestBody;
            case Constant.METHOD_REGISTRATION:
                requestBody = new FormBody.Builder()
                        .add("user_name", one)
                        .add("user_email", two)
                        .add("user_password", three)
                        .build();
                return requestBody;
            case Constant.METHOD_Like:
                requestBody = new FormBody.Builder()
                        .add("user_id", one)
                        .add("like_type", two)
                        .add("like_type_id", three)
                        .build();
                return requestBody;
            case Constant.METHOD_Unlike:
                requestBody = new FormBody.Builder()
                        .add("user_id", one)
                        .add("like_type", two)
                        .add("like_type_id", three)
                        .build();
                return requestBody;
            case Constant.MY_add_music_playlis:
                requestBody = new FormBody.Builder()
                        .add("user_id", one)
                        .add("user_playlist_id", two)
                        .add("music_id", three)
                        .build();
                return requestBody;
            case Constant.CreatePlayList:
                requestBody = new FormBody.Builder()
                        .add("user_id", one)
                        .add("user_playlist_name", two)
                        .build();
                return requestBody;
            case Constant.DeleteList:
                requestBody = new FormBody.Builder()
                        .add("user_id", one)
                        .add("user_playlist_id", two)
                        .build();
                return requestBody;
            case Constant.Own_play_list_detail:
                requestBody = new FormBody.Builder()
                        .add("user_id", one)
                        .add("user_playlist_id", two)
                        .build();
                return requestBody;
            case Constant.METHOD_Search:
                requestBody = new FormBody.Builder()
                        .add("search", one)
                        .build();
                return requestBody;
            case Constant.Download:
                requestBody = new FormBody.Builder()
                        .add("user_id", one)
                        .build();
                return requestBody;
            case Constant.S_Payment:
                requestBody = new FormBody.Builder()
                        .add("user_id", one)
                        .add("package_id", two)
                        .add("total_package_price", three)
                        .build();
                return requestBody;
            case Constant.Edite_profile:
                requestBody = new FormBody.Builder()
                        .add("user_id", one)
                        .add("user_name", two)
                        .add("user_profile_pic", three)
                        .build();
                return requestBody;
            case Constant.METHOD_HOME_CETEGORY:
                requestBody = new FormBody.Builder()
                        .add("home_components_name", one)
                        .add("user_id", two)
                        .build();
                return requestBody;
            case Constant.METHOD_HOME_CETEGORY_ditail:
                Log.e("categotydd", "" + one);
                Log.e("categotydd", "" + two);
                requestBody = new FormBody.Builder()
                        .add("user_id", two)
                        .add("category_id", one)

                        .build();
                return requestBody;
            case Constant.Single_Music_Screen:
                Log.e("categotydd", "" + one);
                Log.e("categotydd", "" + two);
                requestBody = new FormBody.Builder()
                        .add("music_id", one)
                        .add("user_id", two)

                        .build();
                return requestBody;
            default:
                return null;

        }

    }


    //alert message box
    public void alertBox(String message) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton(activity.getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    //network check
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String md5_Hash(String str) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            messageDigest = null;
        }
        messageDigest.update(str.getBytes(), 0, str.length());
        return new BigInteger(1, messageDigest.digest()).toString(16);
    }

    public static String getDeviceID(Context context) {
        return md5_Hash(Settings.Secure.getString(context.getContentResolver(), "android_id")).toUpperCase();
    }

}