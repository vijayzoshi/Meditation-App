package com.kmit.musicapp.fragments.O_Favorit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.kmit.musicapp.Model.Own_Play_List;
import com.kmit.musicapp.Model.Played;
import com.kmit.musicapp.Model.Popular_Albums;
import com.kmit.musicapp.Model.Recommended;
import com.kmit.musicapp.Model.User;
import com.kmit.musicapp.MusicDNAApplication;
import com.kmit.musicapp.R;
import com.kmit.musicapp.Util.Constant;
import com.kmit.musicapp.Util.RoundedImageView;
import com.kmit.musicapp.activities.HomeActivity;
import com.kmit.musicapp.adapters.horizontalrecycleradapters.Fav_reco_adepter;
import com.kmit.musicapp.adapters.horizontalrecycleradapters.Own_play_adepter;
import com.kmit.musicapp.adapters.horizontalrecycleradapters.Resent_adepter;
import com.kmit.musicapp.adapters.horizontalrecycleradapters.Top_play_adepter;
import com.kmit.musicapp.asyncTask.Asy_Like_Unlile_Addplaylist;
import com.kmit.musicapp.asyncTask.Asy_Played;
import com.kmit.musicapp.asyncTask.Asy_User_Playlist;
import com.kmit.musicapp.models.Track;
import com.kmit.musicapp.models.UnifiedTrack;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class FavoriteFragment extends Fragment {

    private Own_play_adepter adapter_recent;
    public static ArrayList<Own_Play_List> arrayList_mostPlaye;
    private RelativeLayout mostPlaye_avi;
    private RecyclerView mostPlayeMusicList_home;

    private Resent_adepter adapter_resent;
    public static ArrayList<Played> arrayList_resent;
    private RelativeLayout resent_avis;
    private RecyclerView resentMusicList_home;
    private ImageView iv_edite;


    private Top_play_adepter adapter_top;
    public static ArrayList<Played> arrayList_topPlaye;
    private RelativeLayout topPlaye_avi;
    private RelativeLayout mostPlaye_nodata;
    private RecyclerView topPlayeMusicList_home;
    private ImageView recents_back_btn;

    private void setAdapterTomost_play() {

        if (arrayList_mostPlaye.size() != 0) {
            adapter_recent = new Own_play_adepter(ctx, arrayList_mostPlaye, new Own_play_adepter.RecycleviewClickLisetenerview() {
                @Override
                public void onitem_press(Own_Play_List played) {
                    Constant.own_playlist_id = played.getUser_playlist_id();
                    Constant.own_playlist_name= played.getUser_playlist_name();

                    try {
                        Constant.own_playlist_image= played.getImages().get(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Constant.own_playlist_music_count= played.getMusic_count();
                    activity.showFragment("own_play_list");

                }

                @Override
                public void onitem_longpress(Own_Play_List played) {
                    AlertDialog diaBox = AskOption(played);
                    diaBox.show();
                }

            });
            AnimationAdapter adapterAnim_portrait = new SlideInRightAnimationAdapter(adapter_recent);
            adapterAnim_portrait.setFirstOnly(true);
            adapterAnim_portrait.setDuration(500);
            adapterAnim_portrait.setInterpolator(new OvershootInterpolator(.9f));
            mostPlayeMusicList_home.setAdapter(adapterAnim_portrait);
        }else{
            mostPlaye_nodata.setVisibility(VISIBLE);
        }


    }


    private void setAdapterTotop_play() {

        if (arrayList_topPlaye.size() != 0) {
            if (adapter_top == null) {
                adapter_top = new Top_play_adepter(ctx, 0, arrayList_topPlaye, new Top_play_adepter.RecycleviewClickLisetenerview() {
                    @Override
                    public void onitem(Track played, int position, List<Track> list) {
                        HomeActivity.streamingTrackList = list;
                        Track track = HomeActivity.streamingTrackList.get(position);
                        HomeActivity.queue.getQueue().clear();
                        for (int i = 0; i < HomeActivity.streamingTrackList.size(); i++) {
                            Track example = HomeActivity.streamingTrackList.get(i);
                            HomeActivity.queue.getQueue().add(new UnifiedTrack(false, null, example));
                        }
                        HomeActivity.queueCurrentIndex = position;
                        HomeActivity.selectedTrack = track;
                        HomeActivity.streamSelected = true;
                        HomeActivity.localSelected = false;
                        HomeActivity.queueCall = false;
                        HomeActivity.isReloaded = false;
                        activity.onTrackSelected(position, list);
                    }
                });
                AnimationAdapter adapterAnim_portrait = new SlideInRightAnimationAdapter(adapter_top);
                adapterAnim_portrait.setFirstOnly(true);
                adapterAnim_portrait.setDuration(500);
                adapterAnim_portrait.setInterpolator(new OvershootInterpolator(.9f));
                topPlayeMusicList_home.setAdapter(adapterAnim_portrait);
            } else {
                adapter_top.notifyDataSetChanged();

            }
        }

    }

    private AlertDialog AskOption(final Own_Play_List played) {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(activity)
                // set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete This Playlist")
                .setIcon(R.drawable.delete)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(final DialogInterface dialog, int whichButton) {
                        new Asy_Like_Unlile_Addplaylist
                                (HomeActivity.methods.getAPIRequest
                                        (Constant.DeleteList
                                                , Constant.get_profile_id(ctx)
                                                , played.getUser_playlist_id()
                                                , ""
                                                , ""
                                                , ""
                                        )
                                        , Constant.DeletePlayList
                                        , new Asy_Like_Unlile_Addplaylist.Listener_Artist() {
                                    @Override
                                    public void onStart() {
                                    }

                                    @Override
                                    public void onEnd(String success) {
                                        dialog.dismiss();
                                    }
                                }).execute();

                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();

        return myQuittingDialogBox;
    }

    private void setAdapterresent() {

        if (arrayList_resent.size() != 0) {
            adapter_resent = new Resent_adepter(ctx, arrayList_resent, new Resent_adepter.RecycleviewClickLisetenerview() {
                @Override
                public void onitem(Track played, int position, List<Track> list) {
                    HomeActivity.streamingTrackList = list;
                    Track track = HomeActivity.streamingTrackList.get(position);
                    HomeActivity.queue.getQueue().clear();
                    for (int i = 0; i < HomeActivity.streamingTrackList.size(); i++) {
                        Track example = HomeActivity.streamingTrackList.get(i);
                        HomeActivity.queue.getQueue().add(new UnifiedTrack(false, null, example));
                    }
                    HomeActivity.queueCurrentIndex = position;
                    HomeActivity.selectedTrack = track;
                    HomeActivity.streamSelected = true;
                    HomeActivity.localSelected = false;
                    HomeActivity.queueCall = false;
                    HomeActivity.isReloaded = false;
                    activity.onTrackSelected(position, HomeActivity.streamingTrackList);

                }

                @Override
                public void chack() {
                    activity.mainFragment("login");
                }
            });
            AnimationAdapter adapterAnim_portrait = new SlideInRightAnimationAdapter(adapter_resent);
            adapterAnim_portrait.setFirstOnly(true);
            adapterAnim_portrait.setDuration(500);
            adapterAnim_portrait.setInterpolator(new OvershootInterpolator(.9f));
            resentMusicList_home.setAdapter(adapterAnim_portrait);
        }

    }

    public FavoriteFragment() {
    }

    private Context ctx;
    private HomeActivity activity;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ctx = context;
        activity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        setHasOptionsMenu(false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mostPlayeMusicList_home = view.findViewById(R.id.mostPlayeMusicList_home);
        resent_avis = view.findViewById(R.id.resent_avis);
        mostPlaye_avi = view.findViewById(R.id.mostPlaye_avi);
        resentMusicList_home = view.findViewById(R.id.resentMusicList_home);
        topPlaye_avi = view.findViewById(R.id.topPlaye_avi);
        mostPlaye_nodata = view.findViewById(R.id.mostPlaye_nodata);
        topPlayeMusicList_home = view.findViewById(R.id.topPlayeMusicList_home);
        recents_back_btn = view.findViewById(R.id.recents_back_btn);
        recents_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        arrayList_topPlaye = new ArrayList<>();

        topPlayeMusicList_home.setHasFixedSize(true);
        LinearLayoutManager llsfd = new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false);
        topPlayeMusicList_home.setLayoutManager(llsfd);




        arrayList_mostPlaye = new ArrayList<>();
        arrayList_resent = new ArrayList<>();

        mostPlayeMusicList_home.setHasFixedSize(true);
        LinearLayoutManager llm5 = new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false);
        mostPlayeMusicList_home.setLayoutManager(llm5);


        resentMusicList_home.setHasFixedSize(true);
        LinearLayoutManager ll4 = new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false);
        resentMusicList_home.setLayoutManager(ll4);


        new Asy_User_Playlist(activity.methods.getAPIRequest(Constant.METHOD_HOME_Popular_Sounds, Constant.METHOD_HOME_Popular_Sounds, Constant.get_profile_id(ctx), Constant.is_myProfile_fav, "", ""),
                new Asy_User_Playlist.Listener_Played() {
                    @Override
                    public void onStart() {
                        if (mostPlaye_avi.getVisibility() != VISIBLE) {
                            mostPlaye_avi.setVisibility(VISIBLE);
                        }
                        arrayList_mostPlaye.clear();
                    }

                    @Override
                    public void onEnd(String success, ArrayList<Own_Play_List> playeds) {
                        if (mostPlaye_avi.getVisibility() != GONE) {
                            mostPlaye_avi.setVisibility(GONE);
                        }
                        arrayList_mostPlaye = playeds;
                        setAdapterTomost_play();
                    }
                }).execute();



        new Asy_Played(activity.methods.getAPIRequest(Constant.METHOD_HOME_Recently_Played, Constant.METHOD_HOME_Recently_Played, Constant.get_profile_id(ctx), Constant.is_myProfile_fav, "", ""),
                Constant.Home,
                new Asy_Played.Listener_Played() {
                    @Override
                    public void onStart() {
                        if (resent_avis.getVisibility() != VISIBLE) {
                            resent_avis.setVisibility(VISIBLE);
                        }
                        arrayList_resent.clear();
                    }

                    @Override
                    public void onEnd(String success, ArrayList<Played> popular_albums) {
                        if (resent_avis.getVisibility() != GONE) {
                            resent_avis.setVisibility(GONE);
                        }
                        arrayList_resent = popular_albums;
                        setAdapterresent();
                    }
                }).execute();


        new Asy_Played(activity.methods.getAPIRequest(Constant.METHOD_HOME_Top_Sounds, Constant.METHOD_HOME_Top_Sounds, Constant.get_profile_id(ctx), Constant.is_myProfile_fav, "", ""),
                Constant.Home,
                new Asy_Played.Listener_Played() {
                    @Override
                    public void onStart() {
                        if (topPlaye_avi.getVisibility() != VISIBLE) {
                            topPlaye_avi.setVisibility(VISIBLE);
                        }
                        arrayList_topPlaye.clear();
                    }

                    @Override
                    public void onEnd(String success, ArrayList<Played> playeds) {
                        if (topPlaye_avi.getVisibility() != GONE) {
                            topPlaye_avi.setVisibility(GONE);
                        }
                        arrayList_topPlaye = playeds;
                        setAdapterTotop_play();
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

