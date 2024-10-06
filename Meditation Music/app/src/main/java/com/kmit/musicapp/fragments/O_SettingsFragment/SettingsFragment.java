package com.kmit.musicapp.fragments.O_SettingsFragment;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.kmit.musicapp.MusicDNAApplication;
import com.kmit.musicapp.R;
import com.kmit.musicapp.activities.HomeActivity;
import com.kmit.musicapp.activities.LanguageActivity;
import com.kmit.musicapp.activities.ThemeActivity;
import com.kmit.musicapp.notificationmanager.MediaPlayerService;
import com.squareup.leakcanary.RefWatcher;

public class SettingsFragment extends Fragment {


    HomeActivity homeActivity;


    ImageView backBtn;
    private LinearLayout rv_theme;
    private LinearLayout rv_language;
    private LinearLayout rv_share;
    private LinearLayout rv_rate;
    private LinearLayout rv_more;
    private LinearLayout rv_privecy;

    public SettingsFragment() {
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        backBtn = (ImageView) view.findViewById(R.id.iv_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        rv_theme = (LinearLayout) view.findViewById(R.id.rv_theme);
        rv_theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentStartervice = new Intent(homeActivity, MediaPlayerService.class);
                homeActivity.stopService(intentStartervice);
                startActivity(new Intent(getActivity(), ThemeActivity.class));
                homeActivity.finish();

            }
        });

        rv_language = (LinearLayout) view.findViewById(R.id.rv_language);
        rv_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentStartervice = new Intent(homeActivity, MediaPlayerService.class);
                homeActivity.stopService(intentStartervice);
                startActivity(new Intent(getActivity(), LanguageActivity.class));
                homeActivity.finish();
            }
        });

        rv_share = (LinearLayout) view.findViewById(R.id.rv_share);
        rv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ishare = new Intent(Intent.ACTION_SEND);
                ishare.setType("text/plain");
                ishare.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_name) + " - http://play.google.com/store/apps/details?id=" + homeActivity.getPackageName());
                startActivity(ishare);
            }
        });

        rv_rate = (LinearLayout) view.findViewById(R.id.rv_rate);
        rv_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appName = homeActivity.getPackageName();//your application package name i.e play store application url
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id="
                                    + appName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id="
                                    + appName)));
                }
            }
        });
        rv_more = (LinearLayout) view.findViewById(R.id.rv_more);
        rv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openApplication();
            }
        });
        rv_privecy = (LinearLayout) view.findViewById(R.id.rv_privecy);
        rv_privecy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String url = "https://www.google.co.in/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });

    }


    public void openApplication() {
            try {
                homeActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + homeActivity.getPackageName())));
            }
            catch (android.content.ActivityNotFoundException anfe) {
                homeActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + homeActivity.getPackageName())));
            }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RefWatcher refWatcher = MusicDNAApplication.getRefWatcher(getContext());
        refWatcher.watch(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = MusicDNAApplication.getRefWatcher(getContext());
        refWatcher.watch(this);
    }


}