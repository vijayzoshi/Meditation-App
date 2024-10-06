package com.kmit.musicapp.Util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.IdRes;

import com.kmit.musicapp.R;


public class util {
    @SuppressLint("ResourceType")
    @IdRes
    public static int getTheme(Context context) {
        String string = context.getSharedPreferences("Application", 0).getString("myTheme", "null");
        if (string.equals("light")) {
            return R.style.AppTheme;
        }
        if (string.equals("dark")) {
            return R.style.dark;
        }

        return R.style.dark
                ;
    }

    public static void setTheme(Context context, String str) {
        Editor edit = context.getSharedPreferences("Application", 0).edit();
        edit.putString("myTheme", str);
        edit.apply();
    }


    //network check
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
