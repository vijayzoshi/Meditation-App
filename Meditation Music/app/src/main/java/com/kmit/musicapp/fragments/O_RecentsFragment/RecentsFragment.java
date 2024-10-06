package com.kmit.musicapp.fragments.O_RecentsFragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kmit.musicapp.Model.Played;
import com.kmit.musicapp.MusicDNAApplication;
import com.kmit.musicapp.R;
import com.kmit.musicapp.Util.Constant;
import com.kmit.musicapp.Util.EndlessRecyclerViewScrollListener;
import com.kmit.musicapp.activities.HomeActivity;
import com.kmit.musicapp.adapters.horizontalrecycleradapters.Resent_adepter;
import com.kmit.musicapp.asyncTask.Asy_Played;
import com.kmit.musicapp.models.Track;
import com.kmit.musicapp.models.UnifiedTrack;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class RecentsFragment extends Fragment{

    RecyclerView recentRecycler;
    public Resent_adepter rtAdpater;
    LinearLayoutManager mLayoutManager2;
    Context ctx;
    HomeActivity activity;
    LinearLayout noContent;
    TextView fragTitle;
    ImageView backBtn;
    private RelativeLayout banner_avi;
    private ArrayList<Played> arrayList_resent;
    String title;
    String url;
    private Boolean isOver = false, isScroll = false;
    public RecentsFragment(String title,String url,String requestBody) {

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
        return inflater.inflate(R.layout.fragment_recents, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        arrayList_resent =new ArrayList<>();
        recentRecycler = (RecyclerView) view.findViewById(R.id.view_recent_recycler);
        noContent = (LinearLayout) view.findViewById(R.id.no_recents_content);
        backBtn = (ImageView) view.findViewById(R.id.recents_back_btn);
        banner_avi = (RelativeLayout) view.findViewById(R.id.banner_avi);
        fragTitle = (TextView) view.findViewById(R.id.recents_fragment_title);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        fragTitle.setText(activity.title);


        mLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recentRecycler.setHasFixedSize(true);
        recentRecycler.setLayoutManager(mLayoutManager2);
        recentRecycler.setNestedScrollingEnabled(false);

        recentRecycler.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager2) {
            @Override
            public void onLoadMore(int p, int totalItemsCount) {
                if (!isOver) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isOver = true;
                            isScroll = true;
                            getmusic();
                        }
                    }, 0);
                }
            }
        });

        start = 0;
        end = 10;
        isOver = false;
        isScroll = false;
        if (rtAdpater != null) {
            rtAdpater = null;
        }
        getmusic();

    }


    int start = 0;
    int end = 10;
    private void getmusic(){
        new Asy_Played(activity.methods.getAPIRequest(activity.requestBody, activity.requestBody, Constant.get_profile_id(ctx), Constant.is_myProfile, ""+start,""+ end),
                activity.url,
                new Asy_Played.Listener_Played() {
                    @Override
                    public void onStart() {
                        if (banner_avi.getVisibility() != VISIBLE) {
                            banner_avi.setVisibility(VISIBLE);
                        }
                    }

                    @Override
                    public void onEnd(String success, ArrayList<Played> popular_albums) {
                        if (banner_avi.getVisibility() != GONE) {
                            banner_avi.setVisibility(GONE);
                        }
                        if (success.equals("1")) {
                            if (popular_albums.size() != 0) {
                                isOver = false;

                                start=start+10;
                                end=end+10;
                                arrayList_resent.addAll(popular_albums);
                                setAdapterresent();
                            }else{
                                isOver = true;
                            }
                        }
                    }
                }).execute();
    }
    private void setAdapterresent() {

        if (arrayList_resent.size() != 0) {
            if (rtAdpater == null) {
                rtAdpater = new Resent_adepter(activity, arrayList_resent, new Resent_adepter.RecycleviewClickLisetenerview() {
                    @Override
                    public void onitem(Track played, int position, List<Track> list) {
                        HomeActivity.streamingTrackList = list;
                        Track track = HomeActivity.streamingTrackList.get(position);
                        HomeActivity.queue.getQueue().clear();
                        for(int i = 0; i<HomeActivity.streamingTrackList.size(); i++){
                            Track example = HomeActivity.streamingTrackList.get(i);
                            HomeActivity.queue.getQueue().add(new UnifiedTrack(false, null, example));
                        }
                        HomeActivity.queueCurrentIndex = position;
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
            } else {
                rtAdpater.recentslyPlayed =arrayList_resent;
                rtAdpater.notifyDataSetChanged();
            }

        }

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
