package com.kmit.musicapp.fragments.O_Profile;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.kmit.musicapp.Model.User;
import com.kmit.musicapp.MusicDNAApplication;
import com.kmit.musicapp.R;
import com.kmit.musicapp.Util.Constant;
import com.kmit.musicapp.activities.HomeActivity;
import com.kmit.musicapp.asyncTask.Asy_Edite_profile;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;
import com.squareup.leakcanary.RefWatcher;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewProfileEdite extends Fragment {

    private EditText editText_name;

    private TextView editText_email;
    private TextView album_fragment_title;
    private CircleImageView circleImageView;
    private ImageView iv_edite;
    private String profileId;
    private int REQUEST_GALLERY_PICKER = 100;
    private ArrayList<Image> galleryImages;
    private String image_profile;
    private boolean is_profile;
    private InputMethodManager imm;
    final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 101;
    private ProgressDialog progressDialog;

    private Context ctx;
    private HomeActivity activity;
    private String ba1="";

    public ViewProfileEdite() {
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

        return inflater.inflate(R.layout.fragment_view_profile_edite, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




        iv_edite = view.findViewById(R.id.iv_edite);
        circleImageView = view.findViewById(R.id.imageView_user_editPro);
        editText_name = view.findViewById(R.id.editText_name_edit_profile);
        editText_email = view.findViewById(R.id.editText_email_edit_profile);
        album_fragment_title = view.findViewById(R.id.album_fragment_title);
        album_fragment_title.setText(Constant.user_name);
        progressDialog = new ProgressDialog(getContext());


        Glide.with(getContext())
                .load(Constant.image + Constant.user_profile_pic)
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .into(circleImageView);



        editText_name.setText(Constant.user_name);
        editText_email.setText(Constant.user_email);



        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    chooseGalleryImage();
            }
        });

        iv_edite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText_name.getText().toString();

                editText_name.clearFocus();
                editText_name.setError(null);

                if (name.equals("") || name.isEmpty()) {
                    editText_name.requestFocus();
                    editText_name.setError(getResources().getString(R.string.please_enter_name));
                } else {
                    if (activity.methods.isNetworkAvailable()) {
                        profileUpdate(Constant.get_profile_id(ctx), name,  image_profile);
                    } else {
                        activity.methods.alertBox(getResources().getString(R.string.internet_connection));
                    }
                }

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

    private void chooseGalleryImage() {
        try {
            ImagePicker.with(this)
                    .setFolderMode(true)
                    .setFolderTitle("Album")
                    .setImageTitle(getResources().getString(R.string.app_name))
                    .setStatusBarColor("#F78705")
                    .setToolbarColor("#F78705")
                    .setProgressBarColor("#F78705")
                    .setMultipleMode(true)
                    .setMaxSize(1)
                    .setShowCamera(false)
                    .start();
        } catch (Exception e) {
            Log.e("error", e.toString());
        }

    }

    public void profileUpdate(String id, String sendName, String profile_image) {
        Log.e("dotfgub",""+ id);
        Log.e("dotfgub",""+ sendName);
        Log.e("dotfgub",""+ profile_image);
        if(is_profile){
            Bitmap bitmapOrg = BitmapFactory.decodeFile(profile_image);
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 100, bao);
            byte [] ba = bao.toByteArray();

            ba1=Base64.encodeToString(ba,Base64.DEFAULT);
        }
        Log.e("dotfgub",""+ ba1);

        new Asy_Edite_profile(activity.methods.getAPIRequest(Constant.Edite_profile, id, sendName, ba1, "", ""),
                new Asy_Edite_profile.Listener_Artist() {
                    @Override
                    public void onStart() {
                        progressDialog.show();
                        progressDialog.setMessage(getResources().getString(R.string.loading));
                        progressDialog.setCancelable(false);

                    }

                    @Override
                    public void onEnd(String success, User user_id) {


                        progressDialog.dismiss();
                        if(user_id.getUser_id().equals("0")){
                            Toast.makeText(getContext(), "Something Went Wrong Please Try Again", Toast.LENGTH_SHORT).show();
                        }else{

                            Constant.user_id=user_id.getUser_id();
                            Constant.user_name=user_id.getUser_name();
                            Constant.user_email=user_id.getUser_email();
                            Constant.user_profile_pic=user_id.getUser_profile_pic();

                            Constant.edite_profile(getContext(),user_id);

                            activity.onBackPressed();
                        }
                    }
                }).execute();


    }

    //alert message box

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY_PICKER) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                is_profile = true;
                galleryImages = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
                image_profile = galleryImages.get(0).getPath();
                Uri uri = Uri.fromFile(new File(galleryImages.get(0).getPath()));
                Picasso.get().load(uri).into(circleImageView);
            }
        }
    }

}
