package com.kmit.musicapp.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kmit.musicapp.R;
import com.kmit.musicapp.Util.LanguageHelper;
import com.kmit.musicapp.Util.util;


public class ThemeActivity extends AppCompatActivity {
    private LinearLayout center2;
    private OnClickListener onClickListener = new OnClickListener() {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.dark :

                    util.setTheme(getApplicationContext(), "dark");
                    recreate();
                    return;
                case R.id.light :
                    util.setTheme(getApplicationContext(), "light");
                    recreate();
                    return;
                default:
                    Toast.makeText(ThemeActivity.this, "Cant get This Theme only for pro", Toast.LENGTH_SHORT).show();
                    return;
            }
        }
    };

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.onAttach(newBase));
    }

    public void onBackPressed() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }


    @SuppressLint("ResourceType")
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setTheme(util.getTheme(getApplicationContext()));
        setContentView((int) R.layout.activity_theme);
        this.center2 = (LinearLayout) findViewById(R.id.center2);
        for (int i = 0; i < this.center2.getChildCount(); i++) {
            LinearLayout linearLayout = (LinearLayout) this.center2.getChildAt(i);
            for (int i2 = 0; i2 < linearLayout.getChildCount(); i2++) {
                linearLayout.getChildAt(i2).setOnClickListener(this.onClickListener);
            }
        }

        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
