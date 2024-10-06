package com.kmit.musicapp.Util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build.VERSION;
import android.os.LocaleList;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Locale;


public class LanguageHelper {
    private static final String SELECTED_LANGUAGE = "language";

    public static Context onAttach(Context context) {
        Log.e("dofgujhh",""+Locale.getDefault().getLanguage());
        return setLanguage(context, getPersistedData(context, Locale.getDefault().getLanguage()));
    }

    public static Context onAttach(Context context, String str) {
        return setLanguage(context, getPersistedData(context, str));
    }

    public static String getLanguage(Context context) {
        return getPersistedData(context, Locale.getDefault().getLanguage());
    }

    public static Context setLanguage(Context context, String str) {
        persist(context, str);
        if (VERSION.SDK_INT >= 24) {
            return updateResources(context, str);
        }
        return updateResourcesLegacy(context, str);
    }

    public static String getPersistedData(Context context, String str) {
        return context.getSharedPreferences("Application", 0).getString(SELECTED_LANGUAGE, str);
    }

    private static void persist(Context context, String str) {
        Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putString(SELECTED_LANGUAGE, str);
        edit.apply();
    }

    @TargetApi(24)
    private static Context updateResources(Context context, String str) {
        Locale locale = new Locale(str);
        Configuration configuration = context.getResources().getConfiguration();
        LocaleList localeList = new LocaleList(new Locale[]{locale});
        LocaleList.setDefault(localeList);
        configuration.setLocales(localeList);
        return context.createConfigurationContext(configuration);
    }

    private static Context updateResourcesLegacy(Context context, String str) {
        Locale locale = new Locale(str);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context;
    }
}
