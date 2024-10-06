package com.kmit.musicapp.fragments.O_Profile;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.kmit.musicapp.Model.User;
import com.kmit.musicapp.MusicDNAApplication;
import com.kmit.musicapp.R;
import com.kmit.musicapp.Util.Constant;
import com.kmit.musicapp.activities.HomeActivity;
import com.kmit.musicapp.activities.LoginRegisrationActivity;
import com.squareup.leakcanary.RefWatcher;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewProfile extends Fragment {

    private TextView textViewName, textViewEmail;
    private TextView album_fragment_title;
    private CircleImageView circleImageView;
    private ImageView iv_edite;

    private Context ctx;
    private HomeActivity activity;
    private ImageView ic_logout;

    public ViewProfile() {
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ctx = context;
        activity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_view_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        circleImageView = view.findViewById(R.id.imageView_profile);
        iv_edite = view.findViewById(R.id.iv_edite);
        ic_logout = view.findViewById(R.id.iv_logout);
        textViewName = view.findViewById(R.id.textView_name_profile);
        textViewEmail = view.findViewById(R.id.textView_email_profile);
        album_fragment_title = view.findViewById(R.id.album_fragment_title);
        album_fragment_title.setText(Constant.user_name);

        ic_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.user_id = "1";
                Constant.user_name = "";
                Constant.user_email = "";
                Constant.user_profile_pic = "";

                Constant.edite_profile(getContext(), new User("0", "", "", ""));
                activity.hideAllFrags();
                activity.hideFragment("allPlaylists");
                startActivity(new Intent(activity, LoginRegisrationActivity.class));

            }
        });

        textViewName.setText(Constant.user_name);
        textViewEmail.setText(Constant.user_email);

        Glide.with(getContext())
                .load(Constant.image + Constant.user_profile_pic)
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .into(circleImageView);
        iv_edite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.showFragment("Profiledetail");
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
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

    @Override
    public boolean getUserVisibleHint() {
         return super.getUserVisibleHint();
    }
}
