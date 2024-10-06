package com.kmit.musicapp.fragments.O_Category;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kmit.musicapp.Model.Played;
import com.kmit.musicapp.MusicDNAApplication;
import com.kmit.musicapp.R;
import com.kmit.musicapp.Util.Constant;
import com.kmit.musicapp.Util.RoundedImageView;
import com.kmit.musicapp.activities.HomeActivity;
import com.kmit.musicapp.adapters.horizontalrecycleradapters.Recomend_Music_Adepter;
import com.kmit.musicapp.asyncTask.Asy_Category_detail;
import com.kmit.musicapp.models.Track;
import com.kmit.musicapp.models.UnifiedTrack;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Category extends Fragment {

    RecyclerView recentRecycler;
    public Recomend_Music_Adepter rtAdpater;
    ArrayList<Played> recommended_musics;
    LinearLayoutManager mLayoutManager2;
    String is_likes = "0";
    RelativeLayout mostPlaye_avi;
    View bottomMarginLayout;
    TextView recents_fragment_title;
    ImageView backBtn;
    private ImageView fav_report;
    private boolean getdata = false;
    Context ctx;
    HomeActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ctx = context;
        activity = (HomeActivity) context;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_category, container, false);
        return view;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recentRecycler = (RecyclerView) view.findViewById(R.id.view_recent_recycler);
        fav_report = (ImageView) view.findViewById(R.id.fav_report);
        fav_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recommended_musics != null){
                    if(recommended_musics.size() != 0){
                        if(getdata){
                            List<Track> recentslyPlayed1 = new ArrayList<>();
                            HomeActivity.queue.getQueue().clear();
                            for(int ia = 0; ia<recommended_musics.size(); ia++){
                                Played example = recommended_musics.get(ia);
                                String time = example.getMusic_duration(); //mm:ss
                                String[] units = time.split(":"); //will break the string up into an array
                                int minutes = Integer.parseInt(units[0]); //first element
                                int seconds = Integer.parseInt(units[1]); //second element
                                int duration = (60 * minutes + seconds)*1000; //add up our values
                                Track track =new Track(example.getMusic_title()
                                        ,Integer.parseInt(example.getMusic_id())
                                        ,duration
                                        , Constant.image+example.getMusic_file()
                                        ,Constant.image+example.getMusic_image()
                                );
                                track.setCategory_id(example.getCategory_id());
                                track.setPlayCount(example.getPlayCount());
                                track.setCategory_name(example.getCategory_name());
                                track.setIs_liked(example.getIs_liked());
                                track.setIs_in_playlist(example.getIs_in_playlist());
                                track.setLike_count(example.getLike_count());
                                recentslyPlayed1.add(track);
                                HomeActivity.queue.getQueue().add(new UnifiedTrack(false, null, track));
                            }
                            HomeActivity.streamingTrackList = recentslyPlayed1;
                            Track track = HomeActivity.streamingTrackList.get(0);

                            HomeActivity.queueCurrentIndex=0;
                            HomeActivity.selectedTrack = track;
                            HomeActivity.streamSelected = true;
                            HomeActivity.localSelected = false;
                            HomeActivity.queueCall = false;
                            HomeActivity.isReloaded = false;
                            activity.onTrackSelected(0, recentslyPlayed1);
                        }
                    }
                }
            }
        });
        recommended_musics = new ArrayList<>();
        backBtn = (ImageView) view.findViewById(R.id.recents_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        bottomMarginLayout = view.findViewById(R.id.bottom_margin_layout);
        mostPlaye_avi = (RelativeLayout) view.findViewById(R.id.mostPlaye_avi);
        recents_fragment_title = (TextView) view.findViewById(R.id.recents_fragment_title);
        recents_fragment_title.setText(Constant.category_name);

        new Asy_Category_detail(HomeActivity.methods.getAPIRequest(Constant.METHOD_HOME_CETEGORY_ditail, Constant.category_id, Constant.get_profile_id(ctx), "", "", ""),
                new Asy_Category_detail.Listener_Recommended_Album() {
                    @Override
                    public void onStart() {
                        if (mostPlaye_avi.getVisibility() != VISIBLE) {
                            mostPlaye_avi.setVisibility(VISIBLE);
                        }
                    }

                    @Override
                    public void onEnd(String success, ArrayList<Played> rdsecommended_musics, String artist_id, String album_name, String album_image, String album_status, String created_date, String like_count, String is_liked) {
                        if (mostPlaye_avi.getVisibility() != GONE) {
                            mostPlaye_avi.setVisibility(GONE);
                        }
                        is_likes = is_liked;
                        if (success.equals("1")) {
                            getdata = true;
                        }

                        recommended_musics = rdsecommended_musics;
                        setAdapterresent();

                    }
                }).execute();


        mLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recentRecycler.setLayoutManager(mLayoutManager2);
        recentRecycler.setItemAnimator(new DefaultItemAnimator());
        recentRecycler.setAdapter(rtAdpater);


    }


    private void setAdapterresent() {

            rtAdpater = new Recomend_Music_Adepter(getContext(), recommended_musics, new Recomend_Music_Adepter.RecycleviewClickLisetenerview() {
                @Override
                public void onitem(Track played, int position, List<Track> list) {
                    HomeActivity.streamingTrackList = list;
                    Track track = HomeActivity.streamingTrackList.get(position);
                    HomeActivity.queue.getQueue().clear();
                    for(int i = 0; i<HomeActivity.streamingTrackList.size(); i++){
                        Track example = HomeActivity.streamingTrackList.get(i);
                        HomeActivity.queue.getQueue().add(new UnifiedTrack(false, null, example));
                    }
                    HomeActivity.queueCurrentIndex=position;
                    HomeActivity.selectedTrack = track;
                    HomeActivity.streamSelected = true;
                    HomeActivity.localSelected = false;
                    HomeActivity.queueCall = false;
                    HomeActivity.isReloaded = false;
                    activity.onTrackSelected(position,list);

                }

                @Override
                public void chack() {

                    activity.mainFragment("login");
                }
            });
            AnimationAdapter adapterAnim_portrait = new SlideInRightAnimationAdapter(rtAdpater);
            adapterAnim_portrait.setFirstOnly(true);
            adapterAnim_portrait.setDuration(500);
            adapterAnim_portrait.setInterpolator(new OvershootInterpolator(.9f));
            recentRecycler.setAdapter(adapterAnim_portrait);
    }

    @Override
    public void onResume() {
        super.onResume();
        mLayoutManager2.scrollToPositionWithOffset(0, 0);

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
