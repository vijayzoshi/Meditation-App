package com.kmit.musicapp.fragments.O_View_Category;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kmit.musicapp.Model.Category;
import com.kmit.musicapp.MusicDNAApplication;
import com.kmit.musicapp.R;
import com.kmit.musicapp.Util.Constant;
import com.kmit.musicapp.activities.HomeActivity;
import com.kmit.musicapp.adapters.horizontalrecycleradapters.CategoryAdepter;
import com.kmit.musicapp.asyncTask.Asy_Category;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.AnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ViewCategoryFragment extends Fragment {


    RecyclerView rv;
    ImageView backBtn;
    Context ctx;
    TextView fragTitle;
    HomeActivity activity;
    private CategoryAdepter adapter_movie;
    private RelativeLayout populer_artist_avi;

    public void onAttach(Context context) {
        super.onAttach(context);
        ctx = context;
        activity = (HomeActivity) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_category, container, false);
        return view;
    }

    private void setAdapterArtists() {

        if (HomeActivity.all_arrayList_category.size() != 0) {
            adapter_movie = new CategoryAdepter(getContext(),1, HomeActivity.all_arrayList_category, new CategoryAdepter.RecycleviewClickLisetenerview() {
                @Override
                public void onitem(Category played) {
                    Constant.category_id = played.getCategory_id();
                    Constant.category_name = played.getCategory_name();
                    Constant.category_image = played.getCategory_image();
                    activity.showFragment("categorydetail");

                }
            });
            AnimationAdapter adapterAnim_portrait = new SlideInRightAnimationAdapter(adapter_movie);
            adapterAnim_portrait.setFirstOnly(true);
            adapterAnim_portrait.setDuration(500);
            adapterAnim_portrait.setInterpolator(new OvershootInterpolator(.9f));
            rv.setAdapter(adapterAnim_portrait);
        }

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        populer_artist_avi = (RelativeLayout) view.findViewById(R.id.populer_artist_avi);
        backBtn = (ImageView) view.findViewById(R.id.view_album_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        fragTitle = (TextView) view.findViewById(R.id.album_fragment_title);
        rv = (RecyclerView) view.findViewById(R.id.album_songs_recycler);
        rv.setHasFixedSize(true);
        LinearLayoutManager ll4 = new LinearLayoutManager(getContext());
        rv.setLayoutManager(ll4);

        new Asy_Category(HomeActivity.methods.getAPIRequest(Constant.METHOD_HOME_CETEGORY, Constant.METHOD_HOME_CETEGORY, Constant.get_profile_id(getContext()), "", "", ""), Constant.Home,
                new Asy_Category.Listener_Recommended_Album() {
                    @Override
                    public void onStart() {
                        if (populer_artist_avi.getVisibility() != VISIBLE) {
                            populer_artist_avi.setVisibility(VISIBLE);
                        }
                        HomeActivity.all_arrayList_category.clear();
                    }

                    @Override
                    public void onEnd(String success, ArrayList<Category> artists) {
                        if (populer_artist_avi.getVisibility() != GONE) {
                            populer_artist_avi.setVisibility(GONE);
                        }
                        HomeActivity.all_arrayList_category = artists;
                        setAdapterArtists();
                    }
                }).execute();

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

}
