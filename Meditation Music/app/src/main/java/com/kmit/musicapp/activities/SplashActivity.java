package com.kmit.musicapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.kmit.musicapp.R;
import com.kmit.musicapp.Util.Constant;
import com.kmit.musicapp.Util.Methods;
import com.kmit.musicapp.asyncTask.Asy_setting;
import com.kmit.musicapp.fragments.O_LoginRegistration.Login;

public class SplashActivity extends AppCompatActivity {

    int PERMISSIONS_REQUEST_CODE = 1;

    VideoView vide;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        vide = findViewById(R.id.vide);
        String video_url = "android.resource://" + getPackageName() + "/" + R.raw.splash_video1;
        Uri videoUri = Uri.parse(video_url);
        vide.setVideoURI(videoUri);
        vide.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                vide.requestFocus();
                vide.start();
            }
        });


        Constant.get_profile(SplashActivity.this);
        Methods methods = new Methods(this);
        new Asy_setting(this).execute();
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions();
        } else {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(Constant.get_profile_id(SplashActivity.this).equals("0")){
                        Intent i = new Intent(SplashActivity.this, LoginRegisrationActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            },3000);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean allGranted = true;

        if (grantResults.length > 0) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
        }
        if (allGranted) {
            startHomeActivity();
        } else {
            Toast.makeText(this, "Please grant the requested permissions.", Toast.LENGTH_SHORT).show();
        }
    }

    public void startHomeActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(i);
            }
        },3000);
    }

    public void requestPermissions() {
        String[] permissions = {
                Manifest.permission.INTERNET,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_NETWORK_STATE
        };
        ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_REQUEST_CODE);
    }
}