package com.kmit.musicapp.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.kmit.musicapp.R;
import com.kmit.musicapp.Util.LanguageHelper;
import com.kmit.musicapp.Util.util;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity implements OnClickListener {

    public TextView Ar;
    public TextView Ch;
    public TextView En;
    public TextView Fr;
    public TextView Hi;
    public TextView Ja;
    public TextView Ko;
    public TextView Pt;
    public TextView Ru;
    public TextView sat;
    public TextView bn;
    public TextView or;

    public String Lang = "en";
    private ImageView button;
    private ImageView iv_back;
    private int normal;
    private Context ctx;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.onAttach(newBase));
    }


    private void change_color() {
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            Ar.setBackgroundDrawable(ContextCompat.getDrawable(ctx, R.drawable.image_regular));
            Ch.setBackgroundDrawable(ContextCompat.getDrawable(ctx, R.drawable.image_regular));
            Fr.setBackgroundDrawable(ContextCompat.getDrawable(ctx, R.drawable.image_regular));
            Ja.setBackgroundDrawable(ContextCompat.getDrawable(ctx, R.drawable.image_regular));
            Ko.setBackgroundDrawable(ContextCompat.getDrawable(ctx, R.drawable.image_regular));
            Pt.setBackgroundDrawable(ContextCompat.getDrawable(ctx, R.drawable.image_regular));
            Ru.setBackgroundDrawable(ContextCompat.getDrawable(ctx, R.drawable.image_regular));
            En.setBackgroundDrawable(ContextCompat.getDrawable(ctx, R.drawable.image_regular));
            Hi.setBackgroundDrawable(ContextCompat.getDrawable(ctx, R.drawable.image_regular));
            sat.setBackgroundDrawable(ContextCompat.getDrawable(ctx, R.drawable.image_regular));
            bn.setBackgroundDrawable(ContextCompat.getDrawable(ctx, R.drawable.image_regular));
            or.setBackgroundDrawable(ContextCompat.getDrawable(ctx, R.drawable.image_regular));
        } else {
            sat.setBackground(ContextCompat.getDrawable(ctx, R.drawable.image_regular));
            bn.setBackground(ContextCompat.getDrawable(ctx, R.drawable.image_regular));
            or.setBackground(ContextCompat.getDrawable(ctx, R.drawable.image_regular));
            Ar.setBackground(ContextCompat.getDrawable(ctx, R.drawable.image_regular));
            Ch.setBackground(ContextCompat.getDrawable(ctx, R.drawable.image_regular));
            En.setBackground(ContextCompat.getDrawable(ctx, R.drawable.image_regular));
            Fr.setBackground(ContextCompat.getDrawable(ctx, R.drawable.image_regular));
            Hi.setBackground(ContextCompat.getDrawable(ctx, R.drawable.image_regular));
            Ja.setBackground(ContextCompat.getDrawable(ctx, R.drawable.image_regular));
            Ko.setBackground(ContextCompat.getDrawable(ctx, R.drawable.image_regular));
            Pt.setBackground(ContextCompat.getDrawable(ctx, R.drawable.image_regular));
            Ru.setBackground(ContextCompat.getDrawable(ctx, R.drawable.image_regular));
        }
        sat.setTextColor(normal);
        bn.setTextColor(normal);
        or.setTextColor(normal);
        Ar.setTextColor(normal);
        Ch.setTextColor(normal);
        En.setTextColor(normal);
        Fr.setTextColor(normal);
        Hi.setTextColor(normal);
        Ja.setTextColor(normal);
        Ko.setTextColor(normal);
        Pt.setTextColor(normal);
        Ru.setTextColor(normal);
    }

    Locale laLocale;

    public void onClick(View view) {
        change_color();
        switch (view.getId()) {
            case R.id.ar:
                laLocale  = new Locale("ar","MA");
                Lang = "ar";
                Ar.setBackground(ContextCompat.getDrawable(ctx, R.drawable.image_pressed));
                Ar.setTextColor(Color.WHITE);
                return;
            case R.id.ch:
                laLocale  = new Locale("zh","CH");
                Lang = "zh";
                Ch.setBackground(ContextCompat.getDrawable(ctx, R.drawable.image_pressed));
                Ch.setTextColor(Color.WHITE);
                return;
            case R.id.en:
                laLocale  = new Locale("en");
                Lang = "en";
                En.setBackground(ContextCompat.getDrawable(ctx, R.drawable.image_pressed));
                En.setTextColor(Color.WHITE);
                return;
            case R.id.fr:
                laLocale  = new Locale("fr","FR");
                Lang = "fr";
                Fr.setBackground(ContextCompat.getDrawable(ctx, R.drawable.image_pressed));
                Fr.setTextColor(Color.WHITE);
                return;
            case R.id.hi:
                laLocale  = new Locale("hi");
                Lang = "hi";
                Hi.setBackground(ContextCompat.getDrawable(ctx, R.drawable.image_pressed));
                Hi.setTextColor(Color.WHITE);
                return;
            case R.id.ja:
                laLocale  = new Locale("ja");
                Lang = "ja";
                Ja.setBackground(ContextCompat.getDrawable(ctx, R.drawable.image_pressed));
                Ja.setTextColor(Color.WHITE);
                return;
            case R.id.ko:
                laLocale  = new Locale("ko");
                Lang = "ko";
                Ko.setBackground(ContextCompat.getDrawable(ctx, R.drawable.image_pressed));
                Ko.setTextColor(Color.WHITE);
                return;
            case R.id.pt:
                laLocale  = new Locale("pt");
                Lang = "pt";
                Pt.setBackground(ContextCompat.getDrawable(ctx, R.drawable.image_pressed));
                Pt.setTextColor(Color.WHITE);
                return;
            case R.id.ru:
                laLocale  = new Locale("ru");
                Lang = "ru";
                Ru.setBackground(ContextCompat.getDrawable(ctx, R.drawable.image_pressed));
                Ru.setTextColor(Color.WHITE);
                return;

            case R.id.bn:
                laLocale = new Locale("bn");
                Lang = "bn";
                bn.setBackground(ContextCompat.getDrawable(ctx, R.drawable.image_pressed));
                bn.setTextColor(Color.WHITE);
                return;
            case R.id.or:
                laLocale = new Locale("or");
                Lang = "or";
                or.setBackground(ContextCompat.getDrawable(ctx, R.drawable.image_pressed));
                or.setTextColor(Color.WHITE);
                return;
            case R.id.sat:
                laLocale = new Locale("sat");
                Lang = "sat";
                sat.setBackground(ContextCompat.getDrawable(ctx, R.drawable.image_pressed));
                sat.setTextColor(Color.WHITE);
                return;
            default:
                return;
        }
    }

    @SuppressLint("ResourceType")
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setTheme(util.getTheme(getApplicationContext()));
        setContentView((int) R.layout.activity_language);
        ctx = LanguageActivity.this;
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        button = (ImageView) findViewById(R.id.ask);
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getTheme();
        theme.resolveAttribute(R.attr.background_zero, typedValue, true);
        normal = typedValue.data;
        Ar = (TextView) findViewById(R.id.ar);
        Fr = (TextView) findViewById(R.id.fr);
        En = (TextView) findViewById(R.id.en);
        Hi = (TextView) findViewById(R.id.hi);
        Ja = (TextView) findViewById(R.id.ja);
        Pt = (TextView) findViewById(R.id.pt);
        Ch = (TextView) findViewById(R.id.ch);
        Ko = (TextView) findViewById(R.id.ko);
        Ru = (TextView) findViewById(R.id.ru);
        sat = (TextView) findViewById(R.id.sat);
        bn = (TextView) findViewById(R.id.bn);
        or = (TextView) findViewById(R.id.or);
        Ar.setOnClickListener(this);
        Fr.setOnClickListener(this);
        En.setOnClickListener(this);
        Hi.setOnClickListener(this);
        Ja.setOnClickListener(this);
        Pt.setOnClickListener(this);
        Ch.setOnClickListener(this);
        Ko.setOnClickListener(this);
        Ru.setOnClickListener(this);

        sat.setOnClickListener(this);
        bn.setOnClickListener(this);
        or.setOnClickListener(this);
        change_color();
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Log.e("dzfghgght", "" + Lang);
                Set_language(Lang);
                Log.e("vlbmn", "" + Lang);

                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                android.content.res.Configuration conf = res.getConfiguration();
                conf.setLocale(new Locale(Lang));
                res.updateConfiguration(conf, dm);

                Intent launchIntentForPackage = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                launchIntentForPackage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(launchIntentForPackage);
                if (Build.VERSION.SDK_INT >= 29) {
                    System.exit(0);
                }
            }
        });
    }


    public void Set_language(String str) {
        SharedPreferences.Editor edit = getSharedPreferences("Application", 0).edit();
        edit.putString("language", str);
        edit.commit();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
