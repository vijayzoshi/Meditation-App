package com.kmit.musicapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextPaint;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eightbitlab.bottomnavigationbar.BottomBarItem;
import com.eightbitlab.bottomnavigationbar.BottomNavigationBar;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.github.islamkhsh.CardSliderViewPager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.iammert.library.readablebottombar.ReadableBottomBar;
import com.kmit.musicapp.Model.Banner_Slide;
import com.kmit.musicapp.Model.Category;
import com.kmit.musicapp.Model.Home_order;
import com.kmit.musicapp.Model.Own_Play_List;
import com.kmit.musicapp.Model.Played;
import com.kmit.musicapp.Model.Popular_Albums;
import com.kmit.musicapp.MusicDNAApplication;
import com.kmit.musicapp.R;
import com.kmit.musicapp.Util.Constant;
import com.kmit.musicapp.Util.LanguageHelper;
import com.kmit.musicapp.Util.Methods;
import com.kmit.musicapp.Util.util;
import com.kmit.musicapp.adapters.horizontalrecycleradapters.AllCategoryAdepter;
import com.kmit.musicapp.adapters.horizontalrecycleradapters.LocalTracksHorizontalAdapter;
import com.kmit.musicapp.adapters.horizontalrecycleradapters.Most_play_adepter;
import com.kmit.musicapp.adapters.horizontalrecycleradapters.Resent_adepter;
import com.kmit.musicapp.adapters.horizontalrecycleradapters.StreamTracksHorizontalAdapter;
import com.kmit.musicapp.adapters.horizontalrecycleradapters.Top_play_adepter;
import com.kmit.musicapp.adapters.playlistdialogadapter.AddToPlaylistAdapter;
import com.kmit.musicapp.asyncTask.Asy_Banner_Slide;
import com.kmit.musicapp.asyncTask.Asy_Category;
import com.kmit.musicapp.asyncTask.Asy_Home_com;
import com.kmit.musicapp.asyncTask.Asy_Like_Unlile_Addplaylist;
import com.kmit.musicapp.asyncTask.Asy_Played;
import com.kmit.musicapp.asyncTask.Asy_Search;
import com.kmit.musicapp.asyncTask.Asy_User_Playlist;
import com.kmit.musicapp.asyncTask.Asy_token;
import com.kmit.musicapp.fragments.O_EqualizerFragment.EqualizerFragment;
import com.kmit.musicapp.fragments.O_Favorit.FavoriteFragment;
import com.kmit.musicapp.fragments.O_PlayerFragment.PlayerFragment;
import com.kmit.musicapp.fragments.O_PlaylistDetail.PlaylistDetailFragment;
import com.kmit.musicapp.fragments.O_Profile.ViewProfile;
import com.kmit.musicapp.fragments.O_Profile.ViewProfileEdite;
import com.kmit.musicapp.fragments.O_QueueFragment.QueueFragment;
import com.kmit.musicapp.fragments.O_RecentsFragment.RecentsFragment;
import com.kmit.musicapp.fragments.O_SettingsFragment.SettingsFragment;
import com.kmit.musicapp.fragments.O_View_Category.ViewCategoryFragment;
import com.kmit.musicapp.headsethandler.HeadSetReceiver;
import com.kmit.musicapp.imageLoader.ImageLoader;
import com.kmit.musicapp.interfaces.ServiceCallbacks;
import com.kmit.musicapp.models.Album;
import com.kmit.musicapp.models.AllMusicFolders;
import com.kmit.musicapp.models.AllPlaylists;
import com.kmit.musicapp.models.AllSavedDNA;
import com.kmit.musicapp.models.Artist;
import com.kmit.musicapp.models.EqualizerModel;
import com.kmit.musicapp.models.Favourite;
import com.kmit.musicapp.models.LocalTrack;
import com.kmit.musicapp.models.MusicFolder;
import com.kmit.musicapp.models.Playlist;
import com.kmit.musicapp.models.Queue;
import com.kmit.musicapp.models.RecentlyPlayed;
import com.kmit.musicapp.models.SavedDNA;
import com.kmit.musicapp.models.Settings;
import com.kmit.musicapp.models.Track;
import com.kmit.musicapp.models.UnifiedTrack;
import com.kmit.musicapp.notificationmanager.Constants;
import com.kmit.musicapp.notificationmanager.MediaPlayerService;
import com.kmit.musicapp.utilities.CommonUtils;
import com.kmit.musicapp.utilities.comparators.AlbumComparator;
import com.kmit.musicapp.utilities.comparators.ArtistComparator;
import com.kmit.musicapp.utilities.comparators.LocalMusicComparator;
import com.kmit.musicapp.visualizers.VisualizerView;
import com.squareup.leakcanary.RefWatcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.AnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

//import retrofit2.Call;
//import retrofit2.Call;

public class HomeActivity extends AppCompatActivity
        implements

        SearchView.OnQueryTextListener,
        PlayerFragment.PlayerFragmentCallbackListener,
        PlayerFragment.onPlayPauseListener,
        QueueFragment.queueCallbackListener,
        EqualizerFragment.onCheckChangedListener,
        HeadSetReceiver.onHeadsetEventListener,
        ServiceCallbacks {
    private MovieAdapter movieAdapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.onAttach(newBase));
    }

    public static List<LocalTrack> localTrackList = new ArrayList<>();
    public static List<LocalTrack> finalLocalSearchResultList = new ArrayList<>();
    public static List<LocalTrack> finalSelectedTracks = new ArrayList<>();
    public static List<LocalTrack> recentlyAddedTrackList = new ArrayList<>();
    public static List<LocalTrack> finalRecentlyAddedTrackSearchResultList = new ArrayList<>();
    public static List<Track> streamingTrackList = new ArrayList<>();
    public static List<Album> albums = new ArrayList<>();
    public static List<Album> finalAlbums = new ArrayList<>();
    public static List<Artist> artists = new ArrayList<>();
    public static List<Artist> finalArtists = new ArrayList<>();
    public static List<UnifiedTrack> continuePlayingList = new ArrayList<>();
    public String versionName;
    public int versionCode;
    public int prevVersionCode = -1;
    public static Canvas cacheCanvas;
    public static Album tempAlbum;
    public static Artist tempArtist;
    public static float ratio;
    public static float ratio2;
    public Toolbar spHome;
    public ImageView playerControllerHome;
    public FrameLayout bottomToolbar;
    public CircleImageView spImgHome;
    public TextView spTitleHome;
    public Bitmap selectedImage;
    public SharedPreferences mPrefs;
    public static SharedPreferences.Editor prefsEditor;
    public static Gson gson;
    public ImageLoader imgLoader;
    public ConnectivityManager connManager;
    public NetworkInfo mWifi;
    public static RecentlyPlayed recentlyPlayed;
    public static Favourite favouriteTracks;
    public static Settings settings;
    public static Queue queue;
    public static Queue originalQueue;
    public static Playlist tempPlaylist;
    public static int tempPlaylistNumber;
    public static int renamePlaylistNumber;
    public static AllPlaylists allPlaylists;
    public static AllMusicFolders allMusicFolders;
    public static AllSavedDNA savedDNAs;
    public static SavedDNA tempSavedDNA;
    public static EqualizerModel equalizerModel;
    public static List<LocalTrack> tempFolderContent;
    public static MusicFolder tempMusicFolder;
    public PlayerFragment playerFragment;
    public static boolean shuffleEnabled = false;
    public static boolean repeatEnabled = false;
    public static boolean repeatOnceEnabled = false;
    public static boolean nextControllerClicked = false;
    public static boolean isFavourite = false;
    public static boolean isReloaded = true;
    public static int queueCurrentIndex = 0;
    public int originalQueueIndex = 0;
    public static boolean isSaveDNAEnabled = false;
    public Context ctx;
    public static boolean queueCall = false;
    boolean wasMediaPlayerPlaying = false;
    public static NotificationManager notificationManager;
    public PhoneStateListener phoneStateListener;
    public LocalTracksHorizontalAdapter adapter;
    public StreamTracksHorizontalAdapter sAdapter;
    SearchView searchView;
    MenuItem searchItem;
    RecyclerView soundcloudRecyclerView;
    LinearLayout streamRecyclerContainer;
    RelativeLayout localBanner;
    //    ImageView navImageView;
    public static int screen_width;
    public static int screen_height;
    Toolbar toolbar;
    TextView recentsViewAll, playlistsViewAll;
    public static int themeColor = Color.parseColor("#6F62E7");
    public static float minAudioStrength = 0.40f;
    public static TextPaint tp;
    public Activity main;
    public static float seekBarColor;
    public byte[] mBytes;
    public HeadSetReceiver headSetReceiver;
    ShowcaseView showCase;
    View playerContainer;
    ServiceConnection serviceConnection;
    private MediaPlayerService myService;
    private boolean bound = false;
    public FragmentManager fragMan;
    public static boolean isPlayerVisible = false;
    public static boolean isQueueVisible = false;
    public static boolean isEqualizerVisible = false;
    public static boolean isProfileVisible = false;
    public static boolean isOwnPlaylist = false;
    public static boolean isCategoryVisible = false;
    public static boolean isFullScreenEnabled = false;
    public static boolean isSettingsVisible = false;
    public static boolean isFavoritepro = false;
    public static boolean isRecentVisible = false;
    public static boolean isProfiledetail = false;
    public static boolean isCategorydetail = false;
    public boolean isPlayerTransitioning = false;
    static boolean isSaveRecentsRunning = false;
    static boolean isSaveFavouritesRunning = false;
    static boolean isSaveSettingsRunning = false;
    static boolean isSaveDNAsRunning = false;
    static boolean isSaveQueueRunning = false;
    static boolean isSavePLaylistsRunning = false;
    static boolean isSaveEqualizerRunning = false;
    public static boolean hasQueueEnded = false;
    public static boolean isEqualizerEnabled = false;
    public static boolean isEqualizerReloaded = false;
    public static int[] seekbarpos = new int[5];
    public static int presetPos;
    public static short reverbPreset = -1;
    public static short bassStrength = -1;
    boolean isNotificationVisible = false;
    public static LocalTrack localSelectedTrack;
    public static Track selectedTrack;
    public static LocalTrack editSong;
    public static boolean localSelected = false;
    public static boolean streamSelected = false;
    Button mEndButton;
    public static int statusBarHeightinDp;
    public static int navBarHeightSizeinDp;
    public static boolean hasSoftNavbar = false;
    public static RelativeLayout.LayoutParams lps;
    boolean isSleepTimerEnabled = false;
    boolean isSleepTimerTimeout = false;
    List<String> minuteList;
    Handler sleepHandler;
    private BottomNavigationBar rb_bottom;
    private CardSliderViewPager viewPager;
    public static Methods methods;
    private ArrayList<Banner_Slide> arrayList_banner;
    private Most_play_adepter adapter_recent;
    public static ArrayList<Played> arrayList_mostPlaye;
    private RelativeLayout mostPlaye_avi;
    private RecyclerView mostPlayeMusicList_home;
    private Top_play_adepter adapter_top;
    public static ArrayList<Played> arrayList_topPlaye;
    private RelativeLayout topPlaye_avi;
    private RecyclerView topPlayeMusicList_home;
    private Top_play_adepter adapter_sleep;
    public static ArrayList<Played> arrayList_sleepPlaye;
    private RelativeLayout sleepPlaye_avi;
    private RecyclerView sleepPlayeMusicList_home;
    private Resent_adepter adapter_resent;
    public static ArrayList<Played> arrayList_resent;
    private RelativeLayout resent_avis;
    private RecyclerView resentMusicList_home;
    private AllCategoryAdepter adapter_category;
    public static ArrayList<Category> arrayList_category_list;
    private RelativeLayout populer_category_avi;
    private RecyclerView populer_categoryMusicList_home;
    public static List<Category> all_arrayList_category = new ArrayList<>();
    public String title;
    public String url;
    public String requestBody;
    private ArrayList<Own_Play_List> user_playlist;
    private RelativeLayout search_music_avi;
    private RelativeLayout rl_main_search_music;
    public static ArrayList<Played> search_music_list;
    Most_play_adepter getAdapter_recent;
    private RecyclerView trackList_home;
    int belowid;
    RelativeLayout rl_slider;
    RelativeLayout populer_categoryRecyclerContainer;
    RelativeLayout topPlayedRecyclerContainer;
    RelativeLayout mostPlayedRecyclerContainer;
    RelativeLayout resentRecyclerContainer;
    RelativeLayout sleepPlayedRecyclerContainer;

    private boolean serch_data_load = false;

    private void setAdapterTomost_play() {

        if (arrayList_mostPlaye.size() != 0) {
            if (adapter_recent == null) {
                adapter_recent = new Most_play_adepter(ctx, 0, arrayList_mostPlaye, new Most_play_adepter.RecycleviewClickLisetenerview() {
                    @Override
                    public void onitem(Track played, int position, List<Track> list) {
                        streamingTrackList = list;
                        Track track = streamingTrackList.get(position);
                        queue.getQueue().clear();
                        for (int i = 0; i < streamingTrackList.size(); i++) {
                            Track example = streamingTrackList.get(i);
                            HomeActivity.queue.getQueue().add(new UnifiedTrack(false, null, example));
                        }
                        HomeActivity.queueCurrentIndex = position;
                        selectedTrack = track;
                        streamSelected = true;
                        localSelected = false;
                        queueCall = false;
                        isReloaded = false;
                        onTrackSelected(position, list);
                    }
                });
                AnimationAdapter adapterAnim_portrait = new SlideInRightAnimationAdapter(adapter_recent);
                adapterAnim_portrait.setFirstOnly(true);
                adapterAnim_portrait.setDuration(500);
                adapterAnim_portrait.setInterpolator(new OvershootInterpolator(.9f));
                mostPlayeMusicList_home.setAdapter(adapterAnim_portrait);
            } else {
                adapter_recent.notifyDataSetChanged();

            }
        }

    }

    private void setAdapterTotop_play() {

        if (arrayList_topPlaye.size() != 0) {
            if (adapter_top == null) {
                adapter_top = new Top_play_adepter(ctx, 0, arrayList_topPlaye, new Top_play_adepter.RecycleviewClickLisetenerview() {
                    @Override
                    public void onitem(Track played, int position, List<Track> list) {
                        streamingTrackList = list;
                        Track track = streamingTrackList.get(position);
                        queue.getQueue().clear();
                        for (int i = 0; i < streamingTrackList.size(); i++) {
                            Track example = streamingTrackList.get(i);
                            HomeActivity.queue.getQueue().add(new UnifiedTrack(false, null, example));
                        }
                        HomeActivity.queueCurrentIndex = position;
                        selectedTrack = track;
                        streamSelected = true;
                        localSelected = false;
                        queueCall = false;
                        isReloaded = false;
                        onTrackSelected(position, list);
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

    private void setAdapterTosleep_play() {

        if (arrayList_sleepPlaye.size() != 0) {
            if (adapter_sleep == null) {
                adapter_sleep = new Top_play_adepter(ctx, 0, arrayList_sleepPlaye, new Top_play_adepter.RecycleviewClickLisetenerview() {
                    @Override
                    public void onitem(Track played, int position, List<Track> list) {
                        streamingTrackList = list;
                        Track track = streamingTrackList.get(position);
                        queue.getQueue().clear();
                        for (int i = 0; i < streamingTrackList.size(); i++) {
                            Track example = streamingTrackList.get(i);
                            HomeActivity.queue.getQueue().add(new UnifiedTrack(false, null, example));
                        }
                        HomeActivity.queueCurrentIndex = position;
                        selectedTrack = track;
                        streamSelected = true;
                        localSelected = false;
                        queueCall = false;
                        isReloaded = false;
                        onTrackSelected(position, list);
                    }
                });
                AnimationAdapter adapterAnim_portrait = new SlideInRightAnimationAdapter(adapter_sleep);
                adapterAnim_portrait.setFirstOnly(true);
                adapterAnim_portrait.setDuration(500);
                adapterAnim_portrait.setInterpolator(new OvershootInterpolator(.9f));
                sleepPlayeMusicList_home.setAdapter(adapterAnim_portrait);
            } else {
                adapter_sleep.notifyDataSetChanged();

            }
        }

    }

    private void setAdapterresent() {

        if (arrayList_resent.size() != 0) {
            if (adapter_resent == null) {
                adapter_resent = new Resent_adepter(ctx, arrayList_resent, new Resent_adepter.RecycleviewClickLisetenerview() {
                    @Override
                    public void onitem(Track played, int position, List<Track> list) {
                        streamingTrackList = list;
                        Track track = streamingTrackList.get(position);
                        queue.getQueue().clear();
                        for (int i = 0; i < streamingTrackList.size(); i++) {
                            Track example = streamingTrackList.get(i);
                            queue.getQueue().add(new UnifiedTrack(false, null, example));
                        }
                        HomeActivity.queueCurrentIndex = position;
                        selectedTrack = track;
                        streamSelected = true;
                        localSelected = false;
                        queueCall = false;
                        isReloaded = false;
                        onTrackSelected(position, list);
                    }

                    @Override
                    public void chack() {
                        Intent i = new Intent(HomeActivity.this, LoginRegisrationActivity.class);
                        startActivity(i);
                        finish();
                    }

                });
                AnimationAdapter adapterAnim_portrait = new SlideInRightAnimationAdapter(adapter_resent);
                adapterAnim_portrait.setFirstOnly(true);
                adapterAnim_portrait.setDuration(500);
                adapterAnim_portrait.setInterpolator(new OvershootInterpolator(.9f));
                resentMusicList_home.setAdapter(adapterAnim_portrait);
            } else {
                adapter_resent.notifyDataSetChanged();
            }
        }

    }

    private void setAdapterCategory() {

        if (arrayList_category_list.size() != 0) {
            if (adapter_category == null) {
                adapter_category = new AllCategoryAdepter(ctx, 0, arrayList_category_list, new AllCategoryAdepter.RecycleviewClickLisetenerview() {
                    @Override
                    public void onitem(Category played) {
                        Constant.category_id = played.getCategory_id();
                        Constant.category_name = played.getCategory_name();
                        Constant.category_image = played.getCategory_image();
                        showFragment("categorydetail");
                    }
                });
                AnimationAdapter adapterAnim_portrait = new SlideInRightAnimationAdapter(adapter_category);
                adapterAnim_portrait.setFirstOnly(true);
                adapterAnim_portrait.setDuration(500);
                adapterAnim_portrait.setInterpolator(new OvershootInterpolator(.9f));
                populer_categoryMusicList_home.setAdapter(adapterAnim_portrait);
            } else {
                adapter_category.notifyDataSetChanged();
            }
        }

    }

    private void setSearchdata() {
        if (serch_data_load) {

            if (search_music_list.size() != 0) {
                if (getAdapter_recent == null) {
                    getAdapter_recent = new Most_play_adepter(ctx, 1, search_music_list, new Most_play_adepter.RecycleviewClickLisetenerview() {
                        @Override
                        public void onitem(Track played, int position, List<Track> list) {
                            streamingTrackList = list;
                            Track track = streamingTrackList.get(position);
                            queue.getQueue().clear();
                            for (int i = 0; i < streamingTrackList.size(); i++) {
                                Track example = streamingTrackList.get(i);
                                HomeActivity.queue.getQueue().add(new UnifiedTrack(false, null, example));
                            }
                            HomeActivity.queueCurrentIndex = position;
                            selectedTrack = track;
                            streamSelected = true;
                            localSelected = false;
                            queueCall = false;
                            isReloaded = false;
                            onTrackSelected(position, list);
                        }
                    });
                    AnimationAdapter adapterAnim_portrait = new SlideInRightAnimationAdapter(getAdapter_recent);
                    adapterAnim_portrait.setFirstOnly(true);
                    adapterAnim_portrait.setDuration(500);
                    adapterAnim_portrait.setInterpolator(new OvershootInterpolator(.9f));
                    trackList_home.setAdapter(adapterAnim_portrait);
                } else {

                }
            } else {
                if (rl_main_search_music.getVisibility() != GONE) {
                    rl_main_search_music.setVisibility(GONE);
                }
            }

        }

    }

    private void setSearchdata(String searchdata) {
        if (streamRecyclerContainer.getVisibility() != View.VISIBLE) {
            streamRecyclerContainer.setVisibility(VISIBLE);
        }

        if (serch_data_load) {

            if (search_music_list.size() != 0) {
                if (getAdapter_recent == null) {
                    getAdapter_recent = new Most_play_adepter(ctx, 1, search_music_list, new Most_play_adepter.RecycleviewClickLisetenerview() {
                        @Override
                        public void onitem(Track played, int position, List<Track> list) {
                            streamingTrackList = list;
                            Track track = streamingTrackList.get(position);
                            queue.getQueue().clear();
                            for (int i = 0; i < streamingTrackList.size(); i++) {
                                Track example = streamingTrackList.get(i);
                                HomeActivity.queue.getQueue().add(new UnifiedTrack(false, null, example));
                            }
                            HomeActivity.queueCurrentIndex = position;
                            selectedTrack = track;
                            streamSelected = true;
                            localSelected = false;
                            queueCall = false;
                            isReloaded = false;
                            onTrackSelected(position, list);
                        }
                    });
                    AnimationAdapter adapterAnim_portrait = new SlideInRightAnimationAdapter(getAdapter_recent);
                    adapterAnim_portrait.setFirstOnly(true);
                    adapterAnim_portrait.setDuration(500);
                    adapterAnim_portrait.setInterpolator(new OvershootInterpolator(.9f));
                    trackList_home.setAdapter(adapterAnim_portrait);
                } else {
                    getAdapter_recent.getFilter().filter((CharSequence) searchdata);
                }
            } else {
                if (rl_main_search_music.getVisibility() != GONE) {
                    rl_main_search_music.setVisibility(GONE);
                }
            }

        }
    }

    @SuppressLint({"ResourceType", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(util.getTheme(getApplicationContext()));


        methods = new Methods(this);


        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        screen_width = display.getWidth();
        screen_height = display.getHeight();

        ratio = (float) screen_height / (float) 1920;
        ratio2 = (float) screen_width / (float) 1080;
        ratio = Math.min(ratio, ratio2);

        setContentView(R.layout.activity_home);
        ctx = this;

        final SharedPreferences prefsdfvs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefsdfvs.getBoolean("firstTime", false)) {
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w("error", "getInstanceId failed", task.getException());
                                return;
                            }

                            // Get new Instance ID token
                            String token = task.getResult().getToken();


                            // Log and toast
                            String msg = getString(R.string.msg_token_fmt, token);
                            Log.d("error", msg);
                            new Asy_token(methods.getAPIRequesttoken(token), new Asy_token.Tokensss() {
                                @Override
                                public void onEnd(String success) {

                                    if (success.equals("1")) {
                                        SharedPreferences.Editor editor = prefsdfvs.edit();
                                        editor.putBoolean("firstTime", true);
                                        editor.commit();
                                    }
                                }
                            }).execute();
                        }
                    });

        }
        if (!Constant.Notification) {
            try {
                FirebaseInstanceId.getInstance().deleteInstanceId();
                SharedPreferences.Editor editor = prefsdfvs.edit();
                editor.putBoolean("firstTime", false);
                editor.commit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        playerContainer = findViewById(R.id.player_frag_container);
        mostPlayeMusicList_home = findViewById(R.id.mostPlayeMusicList_home);
        populer_categoryMusicList_home = findViewById(R.id.populer_categoryMusicList_home);
        resentMusicList_home = findViewById(R.id.resentMusicList_home);

        topPlayeMusicList_home = findViewById(R.id.topPlayeMusicList_home);
        sleepPlayeMusicList_home = findViewById(R.id.sleepPlayeMusicList_home);

        trackList_home = findViewById(R.id.trackList_home);

        viewPager = findViewById(R.id.viewPager);
        streamRecyclerContainer = (LinearLayout) findViewById(R.id.streamRecyclerContainer);
        sleepPlaye_avi = findViewById(R.id.sleepPlaye_avi);
        topPlaye_avi = findViewById(R.id.topPlaye_avi);
        mostPlaye_avi = findViewById(R.id.mostPlaye_avi);
        resent_avis = findViewById(R.id.resent_avis);
        populer_category_avi = findViewById(R.id.populer_category_avi);
        rb_bottom = (BottomNavigationBar) findViewById(R.id.rb_bottom);

        rb_bottom.addTab(new BottomBarItem(R.drawable.ic_home, R.string.home));
        rb_bottom.addTab(new BottomBarItem(R.drawable.ic_recent, R.string.resen));
        rb_bottom.addTab(new BottomBarItem(R.drawable.ic_fav_menu, R.string.favoritess));
        rb_bottom.addTab(new BottomBarItem(R.drawable.ic_category, R.string.journeys));
        rb_bottom.addTab(new BottomBarItem(R.drawable.ic_profile, R.string.profile));

        fl_main_screen = (FrameLayout) findViewById(R.id.fl_main_screen);

        search_music_avi = findViewById(R.id.search_music_avi);

        rl_main_search_music = findViewById(R.id.rl_main_search_music);

        arrayList_sleepPlaye = new ArrayList<>();
        arrayList_topPlaye = new ArrayList<>();
        arrayList_banner = new ArrayList<>();
        arrayList_mostPlaye = new ArrayList<>();
        arrayList_category_list = new ArrayList<>();
        arrayList_resent = new ArrayList<>();

        search_music_list = new ArrayList<>();

        fragMan = getSupportFragmentManager();

        headSetReceiver = new HeadSetReceiver(this);
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(headSetReceiver, filter);

        PackageInfo pInfo;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = pInfo.versionName;
            versionCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        mEndButton = new Button(this);
        mEndButton.setBackgroundColor(themeColor);
        mEndButton.setTextColor(Color.WHITE);

        tp = new TextPaint();
        tp.setColor(themeColor);
        tp.setTextSize(65 * ratio);
        tp.setFakeBoldText(true);

        mostPlayeMusicList_home.setHasFixedSize(true);
        LinearLayoutManager llm5 = new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false);
        mostPlayeMusicList_home.setLayoutManager(llm5);

        topPlayeMusicList_home.setHasFixedSize(true);
        LinearLayoutManager llsfd = new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false);
        topPlayeMusicList_home.setLayoutManager(llsfd);

        sleepPlayeMusicList_home.setHasFixedSize(true);
        LinearLayoutManager llsfddv = new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false);
        sleepPlayeMusicList_home.setLayoutManager(llsfddv);

        populer_categoryMusicList_home.setHasFixedSize(true);
        LinearLayoutManager llscs = new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false);
        populer_categoryMusicList_home.setLayoutManager(llscs);

        resentMusicList_home.setHasFixedSize(true);
        LinearLayoutManager ll4 = new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false);
        resentMusicList_home.setLayoutManager(ll4);


        trackList_home.setHasFixedSize(true);
        trackList_home.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false));

        belowid = R.id.rv_tops;
        rl_slider = findViewById(R.id.rl_slider);
        topPlayedRecyclerContainer = findViewById(R.id.topPlayedRecyclerContainer);
        mostPlayedRecyclerContainer = findViewById(R.id.mostPlayedRecyclerContainer);


        populer_categoryRecyclerContainer = findViewById(R.id.populer_categoryRecyclerContainer);
        resentRecyclerContainer = findViewById(R.id.resentRecyclerContainer);
        sleepPlayedRecyclerContainer = findViewById(R.id.sleepPlayedRecyclerContainer);


        if (Constant.get_profile_id(ctx).equals("0")) {
            Intent i = new Intent(HomeActivity.this, LoginRegisrationActivity.class);
            startActivity(i);
            finish();

        }

        new Asy_Home_com(methods.getAPIRequest(Constant.METHOD_HOME_COMPONENT_LISt, Constant.METHOD_HOME_COMPONENT_LISt, Constant.get_profile_id(ctx), "", "", ""),
                new Asy_Home_com.Listener_Home_com() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onEnd(String success, ArrayList<Home_order> arrayListFeatured) {
                        Collections.sort(arrayListFeatured, new Comparator<Home_order>() {
                            @Override
                            public int compare(Home_order lhs, Home_order rhs) {
                                return lhs.getHome_components_order().compareTo(rhs.getHome_components_order());
                            }
                        });
                        for (int i = 0; i < arrayListFeatured.size(); i++) {
                            switch (arrayListFeatured.get(i).getHome_components_name()) {
                                case Constant.METHOD_HOME_Banner_Slider:
                                    if (arrayListFeatured.get(i).getHome_components_status().equals("ENABLE")) {
                                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        params.addRule(RelativeLayout.BELOW, belowid);
                                        rl_slider.setLayoutParams(params);
                                        belowid = rl_slider.getId();
                                    } else {
                                        rl_slider.setVisibility(GONE);
                                    }
                                    break;
                                case Constant.METHOD_HOME_CETEGORY:
                                    if (arrayListFeatured.get(i).getHome_components_status().equals("ENABLE")) {
                                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        params.addRule(RelativeLayout.BELOW, belowid);
                                        populer_categoryRecyclerContainer.setLayoutParams(params);
                                        belowid = populer_categoryRecyclerContainer.getId();
                                    } else {
                                        populer_categoryRecyclerContainer.setVisibility(GONE);
                                    }
                                    break;

                                case Constant.METHOD_HOME_Top_Sounds:
                                    if (arrayListFeatured.get(i).getHome_components_status().equals("ENABLE")) {
                                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        params.addRule(RelativeLayout.BELOW, belowid);
                                        topPlayedRecyclerContainer.setLayoutParams(params);
                                        belowid = topPlayedRecyclerContainer.getId();
                                    } else {
                                        topPlayedRecyclerContainer.setVisibility(GONE);
                                    }
                                    break;

                                case Constant.METHOD_HOME_Popular_Sounds:
                                    if (arrayListFeatured.get(i).getHome_components_status().equals("ENABLE")) {
                                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        params.addRule(RelativeLayout.BELOW, belowid);
                                        mostPlayedRecyclerContainer.setLayoutParams(params);
                                        belowid = mostPlayedRecyclerContainer.getId();
                                    } else {
                                        mostPlayedRecyclerContainer.setVisibility(GONE);
                                    }
                                    break;


                                case Constant.METHOD_HOME_Recently_Played:
                                    if (arrayListFeatured.get(i).getHome_components_status().equals("ENABLE")) {
                                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        params.addRule(RelativeLayout.BELOW, belowid);
                                        resentRecyclerContainer.setLayoutParams(params);
                                        belowid = resentRecyclerContainer.getId();
                                    } else {
                                        resentRecyclerContainer.setVisibility(GONE);
                                    }
                                    break;


                                case Constant.METHOD_HOME_Sleep_Stories:
                                    if (arrayListFeatured.get(i).getHome_components_status().equals("ENABLE")) {
                                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        params.addRule(RelativeLayout.BELOW, belowid);
                                        sleepPlayedRecyclerContainer.setLayoutParams(params);
                                        belowid = sleepPlayedRecyclerContainer.getId();
                                    } else {
                                        sleepPlayedRecyclerContainer.setVisibility(GONE);
                                    }
                                    break;


                            }
                        }

                    }
                }).execute();


        new Asy_Banner_Slide(methods.getAPIRequest(Constant.METHOD_HOME_Banner_Slider, Constant.METHOD_HOME_Banner_Slider, Constant.get_profile_id(ctx), Constant.is_myProfile, "", ""),
                new Asy_Banner_Slide.Listener_Banner_Slide() {
                    @Override
                    public void onStart() {
                        arrayList_banner.clear();

                    }

                    @Override
                    public void onEnd(String success, ArrayList<Banner_Slide> arrayListFeatured) {
                        (findViewById(R.id.banner_avi)).setVisibility(GONE);

                        arrayList_banner = arrayListFeatured;
                        if (arrayList_banner.size() != 0) {
                            if (movieAdapter == null) {
                                movieAdapter = new MovieAdapter(HomeActivity.this, arrayList_banner);
                                viewPager.setAdapter(movieAdapter);
                            } else {
                                movieAdapter.notifyDataSetChanged();
                            }
                        }


                    }
                }).execute();


        new Asy_Played(methods.getAPIRequest(Constant.METHOD_HOME_Popular_Sounds, Constant.METHOD_HOME_Popular_Sounds, Constant.get_profile_id(ctx), Constant.is_myProfile, "", ""),
                Constant.Home,
                new Asy_Played.Listener_Played() {
                    @Override
                    public void onStart() {
                        if (mostPlaye_avi.getVisibility() != VISIBLE) {
                            mostPlaye_avi.setVisibility(VISIBLE);
                        }
                        arrayList_mostPlaye.clear();
                    }

                    @Override
                    public void onEnd(String success, ArrayList<Played> playeds) {
                        if (mostPlaye_avi.getVisibility() != GONE) {
                            mostPlaye_avi.setVisibility(GONE);
                        }
                        arrayList_mostPlaye = playeds;
                        setAdapterTomost_play();
                    }
                }).execute();

        new Asy_Played(methods.getAPIRequest(Constant.METHOD_HOME_Top_Sounds, Constant.METHOD_HOME_Top_Sounds, Constant.get_profile_id(ctx), Constant.is_myProfile, "", ""),
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

        new Asy_Played(methods.getAPIRequest(Constant.METHOD_HOME_Sleep_Stories, Constant.METHOD_HOME_Sleep_Stories, Constant.get_profile_id(ctx), Constant.is_myProfile, "", ""),
                Constant.Home,
                new Asy_Played.Listener_Played() {
                    @Override
                    public void onStart() {
                        if (sleepPlaye_avi.getVisibility() != VISIBLE) {
                            sleepPlaye_avi.setVisibility(VISIBLE);
                        }
                        arrayList_sleepPlaye.clear();
                    }

                    @Override
                    public void onEnd(String success, ArrayList<Played> playeds) {
                        if (sleepPlaye_avi.getVisibility() != GONE) {
                            sleepPlaye_avi.setVisibility(GONE);
                        }
                        arrayList_sleepPlaye = playeds;
                        setAdapterTosleep_play();
                    }
                }).execute();

        new Asy_Played(methods.getAPIRequest(Constant.METHOD_HOME_Recently_Played, Constant.METHOD_HOME_Recently_Played, Constant.get_profile_id(ctx), Constant.is_myProfile, "", ""),
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

        new Asy_Category(methods.getAPIRequest(Constant.METHOD_HOME_CETEGORY, Constant.METHOD_HOME_CETEGORY, Constant.get_profile_id(HomeActivity.this), "", "", ""),
                Constant.Home,
                new Asy_Category.Listener_Recommended_Album() {
                    @Override
                    public void onStart() {
                        if (populer_category_avi.getVisibility() != VISIBLE) {
                            populer_category_avi.setVisibility(VISIBLE);
                        }
                        arrayList_category_list.clear();
                    }

                    @Override
                    public void onEnd(String success, ArrayList<Category> recommended_albums) {
                        if (populer_category_avi.getVisibility() != GONE) {
                            populer_category_avi.setVisibility(GONE);
                        }
                        arrayList_category_list = recommended_albums;
                        setAdapterCategory();
                    }

                }).execute();


        new Asy_Search(methods.getAPIRequest(Constant.METHOD_Search, "  ", "", "", "", ""),
                new Asy_Search.Listener_Recommended_Album() {
                    @Override
                    public void onStart() {
                        if (search_music_avi.getVisibility() != VISIBLE) {
                            search_music_avi.setVisibility(VISIBLE);
                        }
                    }

                    @Override
                    public void onEnd(String success,
                                      ArrayList<Played> search_musics
                    ) {
                        if (search_music_avi.getVisibility() != GONE) {
                            search_music_avi.setVisibility(GONE);
                        }
                        serch_data_load = true;

                        search_music_list = search_musics;

                        setSearchdata();

                    }
                }).execute();


        findViewById(R.id.mostPlaye_view_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = getResources().getString(R.string.topplay);
                url = Constant.Home;
                requestBody = Constant.METHOD_HOME_Popular_Sounds;
                showFragment("recent");
            }
        });

        findViewById(R.id.topPlaye_view_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = getResources().getString(R.string.mostplay);
                url = Constant.Home;
                requestBody = Constant.METHOD_HOME_Top_Sounds;
                showFragment("recent");
            }
        });

        findViewById(R.id.resent_view_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = getResources().getString(R.string.resent);
                url = Constant.Home;
                requestBody = Constant.METHOD_HOME_Recently_Played;
                showFragment("recent");
            }
        });

        findViewById(R.id.sleepPlaye_view_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = getResources().getString(R.string.sleepplay);
                url = Constant.Home;
                requestBody = Constant.METHOD_HOME_Recently_Played;
                showFragment("recent");
            }
        });


        findViewById(R.id.populer_category_view_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment("category");
            }
        });


        imgLoader = new ImageLoader(this);


        hasSoftNavbar = CommonUtils.hasNavBar(this);
        statusBarHeightinDp = CommonUtils.getStatusBarHeight(this);
        navBarHeightSizeinDp = hasSoftNavbar ? CommonUtils.getNavBarHeight(this) : 0;

        serviceConnection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName className, IBinder service) {
                // cast the IBinder and get MyService instance
                MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;
                myService = binder.getService();
                bound = true;
                myService.setCallbacks(HomeActivity.this); // register
            }

            @Override
            public void onServiceDisconnected(ComponentName arg0) {
                bound = false;
            }
        };

        minuteList = new ArrayList<>();
        for (int i = 1; i < 25; i++) {
            minuteList.add(String.valueOf(i * 5));
        }

        sleepHandler = new Handler();

        lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
        lps.setMargins(margin, margin, margin, navBarHeightSizeinDp + ((Number) (getResources().getDisplayMetrics().density * 5)).intValue());


        rb_bottom.setOnSelectListener(new BottomNavigationBar.OnSelectListener() {
            @Override
            public void onSelect(int index) {
                switch (index) {
                    case 0:
                        hideAllFrags();
                        if (isPlayerVisible && !isPlayerTransitioning && playerFragment.smallPlayer != null) {
                            hidePlayer();
                            isPlayerVisible = false;
                        }
                        break;
                    case 1:
                        if (isPlayerVisible && !isPlayerTransitioning && playerFragment.smallPlayer != null) {
                            hidePlayer();
                            isPlayerVisible = false;
                        }
                        title = getResources().getString(R.string.resent);
                        url = Constant.Get_All_Musics;
                        requestBody = Constant.METHOD_HOME_Recently_Played;
                        showFragment("recent");
                        break;
                    case 2:
                        if (isPlayerVisible && !isPlayerTransitioning && playerFragment.smallPlayer != null) {
                            hidePlayer();
                            isPlayerVisible = false;
                        }
                        if (Constant.get_profile_id(ctx).equals("0")) {
                            rb_bottom.selectTab(0, true);
                            Intent i = new Intent(HomeActivity.this, LoginRegisrationActivity.class);
                            startActivity(i);
                            finish();

                        } else {
                            showFragment("favorite");
                        }
                        break;
                    case 3:
                        if (isPlayerVisible && !isPlayerTransitioning && playerFragment.smallPlayer != null) {
                            hidePlayer();
                            isPlayerVisible = false;
                        }
                        title = getResources().getString(R.string.journeys);
                        showFragment("category");
                        break;
                    case 4:
                        if (isPlayerVisible && !isPlayerTransitioning && playerFragment.smallPlayer != null) {
                            hidePlayer();
                            isPlayerVisible = false;
                        }
                        if (Constant.get_profile_id(ctx).equals("0")) {
                            rb_bottom.selectTab(0, true);
                            Intent i = new Intent(HomeActivity.this, LoginRegisrationActivity.class);
                            startActivity(i);
                            finish();

                        } else {
                            showFragment("viewprofile");
                        }
                        break;
                }

            }

        });
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");




        connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {

                PlayerFragment pFrag = playerFragment;

                if (playerFragment != null) {
                    if (state == TelephonyManager.CALL_STATE_RINGING) {
                        //Incoming call: Pause music
                        if (pFrag.mMediaPlayer != null && pFrag.mMediaPlayer.isPlaying()) {
                            wasMediaPlayerPlaying = true;
                            pFrag.togglePlayPause();
                        } else {
                            wasMediaPlayerPlaying = false;
                        }
                    } else if (state == TelephonyManager.CALL_STATE_IDLE) {
                        //Not in call: Play music
                        if (pFrag.mMediaPlayer != null && !pFrag.mMediaPlayer.isPlaying() && wasMediaPlayerPlaying) {
                            pFrag.togglePlayPause();
                        }
                    } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                        //A call is dialing, active or on hold
                        if (playerFragment.mMediaPlayer != null && pFrag.mMediaPlayer.isPlaying()) {
                            wasMediaPlayerPlaying = true;
                            pFrag.togglePlayPause();
                        } else {
                            wasMediaPlayerPlaying = false;
                        }
                    }
                }
                super.onCallStateChanged(state, incomingNumber);
            }
        };
        TelephonyManager mgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (mgr != null) {
            mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }

        mPrefs = getPreferences(MODE_PRIVATE);
        prefsEditor = mPrefs.edit();
        gson = new Gson();

        main = this;


/*        localBannerPlayAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queue.getQueue().clear();
                for (int i = 0; i < localTrackList.size(); i++) {
                    UnifiedTrack ut = new UnifiedTrack(true, localTrackList.get(i), null);
                    queue.getQueue().add(ut);
                }
                if (queue.getQueue().size() > 0) {
                    Random r = new Random();
                    int tmp = r.nextInt(queue.getQueue().size());
                    queueCurrentIndex = tmp;
                    LocalTrack track = localTrackList.get(tmp);
                    localSelectedTrack = track;
                    streamSelected = false;
                    localSelected = true;
                    queueCall = false;
                    isReloaded = false;
                    onLocalTrackSelected(-1);
                }
            }
        });*/

        bottomToolbar = (FrameLayout) findViewById(R.id.bottomMargin);
        spHome = (Toolbar) findViewById(R.id.smallPlayer_home);
        playerControllerHome = (ImageView) findViewById(R.id.player_control_sp_home);
        spImgHome = (CircleImageView) findViewById(R.id.selected_track_image_sp_home);
        spTitleHome = (TextView) findViewById(R.id.selected_track_title_sp_home);

        playerControllerHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (queue != null && queue.getQueue().size() > 0) {
                    onQueueItemClicked(queueCurrentIndex);
                    bottomToolbar.setVisibility(View.INVISIBLE);
                }
            }
        });

        playerControllerHome.setImageResource(R.drawable.ic_play_arrow_white_48dp);

        spHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (queue != null && queue.getQueue().size() > 0) {
                    onQueueItemClicked(queueCurrentIndex);
                    bottomToolbar.setVisibility(View.INVISIBLE);
                }
            }
        });


        showCase = new ShowcaseView.Builder(this)
                .blockAllTouches()
                .singleShot(0)
                .setStyle(R.style.CustomShowcaseTheme)
                .useDecorViewAsParent()
                .replaceEndButton(mEndButton)
                .setContentTitlePaint(tp)
                .setContentTitle("Recents and Playlists")
                .setContentText("Here all you recent songs and playlists will be listed." +
                        "Long press the cards or playlists for more options \n" +
                        "\n" +
                        "(Press Next to continue / Press back to Hide)")
                .build();
        showCase.setButtonText("Next");
        showCase.setButtonPosition(lps);
        showCase.overrideButtonClick(new View.OnClickListener() {
            int count1 = 0;

            @Override
            public void onClick(View v) {
                count1++;
                switch (count1) {
                    case 1:
//                        showCase.setTarget(new ViewTarget(R.id.local_banner_alt_showcase, (Activity) ctx));
//                        showCase.setContentTitle("Local Songs");
//                        showCase.setContentText("See all songs available locally, classified on basis of Artist and Album");
//                        showCase.setButtonPosition(lps);
//                        showCase.setButtonText("Next");
                        break;
                    case 2:
                        showCase.setTarget(new ViewTarget(searchView.getId(), (Activity) ctx));
                        showCase.setContentTitle("Search");
                        showCase.setContentText("Search for songs from local library and SoundCloud™");
                        showCase.setButtonPosition(lps);
                        showCase.setButtonText("Done");
                        break;
                    case 3:
                        showCase.hide();
                        break;
                }
            }

        });
        if (queue == null) {
            queue = new Queue();
        }
        new loadSavedData().execute();


    }


    public void onTrackSelected(int position) {

        isReloaded = false;
        HideBottomFakeToolbar();

        if (!queueCall) {
            CommonUtils.hideKeyboard(this);

//            searchView.setQuery("", false);
//            searchView.setIconified(true);
            new Thread(new CancelCall()).start();

            isPlayerVisible = true;

            PlayerFragment frag = playerFragment;
            FragmentManager fm = getSupportFragmentManager();
            PlayerFragment newFragment = new PlayerFragment();
            if (frag == null) {
                playerFragment = newFragment;
                int flag = 0;
                for (int i = 0; i < favouriteTracks.getFavourite().size(); i++) {
                    UnifiedTrack ut = favouriteTracks.getFavourite().get(i);
                    if (!ut.getType() && ut.getStreamTrack().getTitle().equals(selectedTrack.getTitle())) {
                        flag = 1;
                        isFavourite = true;
                        break;
                    }
                }
                if (flag == 0) {
                    isFavourite = false;
                }
                playerFragment.localIsPlaying = false;
                playerFragment.track = selectedTrack;
                fm.beginTransaction()
                        .setCustomAnimations(R.anim.slide_up,
                                R.anim.slide_down,
                                R.anim.slide_up,
                                R.anim.slide_down)
                        .add(R.id.player_frag_container, newFragment, "player")
                        .show(newFragment)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
            } else {
                if (playerFragment.track != null && !playerFragment.localIsPlaying && selectedTrack.getTitle() == playerFragment.track.getTitle()) {

                } else {
                    int flag = 0;
                    for (int i = 0; i < favouriteTracks.getFavourite().size(); i++) {
                        UnifiedTrack ut = favouriteTracks.getFavourite().get(i);
                        if (!ut.getType() && ut.getStreamTrack().getTitle().equals(selectedTrack.getTitle())) {
                            flag = 1;
                            isFavourite = true;
                            break;
                        }
                    }
                    if (flag == 0) {
                        isFavourite = false;
                    }
                    playerFragment.localIsPlaying = false;
                    playerFragment.track = selectedTrack;
                    frag.refresh();
                }
            }
            if (!isQueueVisible)
                showPlayer();
        } else {
            PlayerFragment frag = playerFragment;
            playerFragment.localIsPlaying = false;
            playerFragment.track = selectedTrack;
            int flag = 0;
            for (int i = 0; i < favouriteTracks.getFavourite().size(); i++) {
                UnifiedTrack ut = favouriteTracks.getFavourite().get(i);
                if (!ut.getType() && ut.getStreamTrack().getTitle().equals(selectedTrack.getTitle())) {
                    flag = 1;
                    isFavourite = true;
                    break;
                }
            }
            if (flag == 0) {
                isFavourite = false;
            }
            frag.refresh();
        }

        if (playerFragment != null && playerFragment.snappyRecyclerView != null) {
            playerFragment.snappyRecyclerView.getAdapter().notifyDataSetChanged();
            playerFragment.snappyRecyclerView.setTransparency();
        }

        QueueFragment qFrag = (QueueFragment) fragMan.findFragmentByTag("queue");
        if (qFrag != null) {
            qFrag.updateQueueAdapter();
        }

        UnifiedTrack track = new UnifiedTrack(false, null, playerFragment.track);
        for (int i = 0; i < recentlyPlayed.getRecentlyPlayed().size(); i++) {
            if (!recentlyPlayed.getRecentlyPlayed().get(i).getType() && recentlyPlayed.getRecentlyPlayed().get(i).getStreamTrack().getTitle().equals(track.getStreamTrack().getTitle())) {
                recentlyPlayed.getRecentlyPlayed().remove(i);
//                rAdapter.notifyItemRemoved(i);
                break;
            }
        }
        recentlyPlayed.getRecentlyPlayed().add(0, track);
        if (recentlyPlayed.getRecentlyPlayed().size() > 50) {
            recentlyPlayed.getRecentlyPlayed().remove(50);
        }
        continuePlayingList.clear();
        for (int i = 0; i < Math.min(10, recentlyPlayed.getRecentlyPlayed().size()); i++) {
            continuePlayingList.add(recentlyPlayed.getRecentlyPlayed().get(i));
        }

        RecentsFragment rFrag = (RecentsFragment) fragMan.findFragmentByTag("recent");
        if (rFrag != null && rFrag.rtAdpater != null) {
            rFrag.rtAdpater.notifyDataSetChanged();
        }
    }

    public void onTrackSelected(int position, List<Track> tracks) {

        isReloaded = false;
        HideBottomFakeToolbar();

        if (!queueCall) {
            CommonUtils.hideKeyboard(this);

//            searchView.setQuery("", false);
//            searchView.setIconified(true);
            new Thread(new CancelCall()).start();

            isPlayerVisible = true;

            PlayerFragment frag = playerFragment;
            FragmentManager fm = getSupportFragmentManager();
            PlayerFragment newFragment = new PlayerFragment();
            if (frag == null) {
                playerFragment = newFragment;
                int flag = 0;
                for (int i = 0; i < tracks.size(); i++) {
                    UnifiedTrack ut = new UnifiedTrack(false, null, tracks.get(i));
                    if (!ut.getType() && ut.getStreamTrack().getTitle().equals(selectedTrack.getTitle())) {
                        flag = 1;
                        isFavourite = true;
                        break;
                    }
                }
                if (flag == 0) {
                    isFavourite = false;
                }
                playerFragment.localIsPlaying = false;
                playerFragment.track = selectedTrack;
                fm.beginTransaction()
                        .setCustomAnimations(R.anim.slide_up,
                                R.anim.slide_down,
                                R.anim.slide_up,
                                R.anim.slide_down)
                        .add(R.id.player_frag_container, newFragment, "player")
                        .show(newFragment)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
            } else {
                if (playerFragment.track != null && !playerFragment.localIsPlaying && selectedTrack.getTitle() == playerFragment.track.getTitle()) {

                } else {
                    int flag = 0;
                    for (int i = 0; i < favouriteTracks.getFavourite().size(); i++) {
                        UnifiedTrack ut = favouriteTracks.getFavourite().get(i);
                        if (!ut.getType() && ut.getStreamTrack().getTitle().equals(selectedTrack.getTitle())) {
                            flag = 1;
                            isFavourite = true;
                            break;
                        }
                    }
                    if (flag == 0) {
                        isFavourite = false;
                    }
                    playerFragment.localIsPlaying = false;
                    playerFragment.track = selectedTrack;
                    frag.refresh();
                }
            }
            if (!isQueueVisible)
                showPlayer();
        } else {
            PlayerFragment frag = playerFragment;
            playerFragment.localIsPlaying = false;
            playerFragment.track = selectedTrack;
            int flag = 0;
            for (int i = 0; i < favouriteTracks.getFavourite().size(); i++) {
                UnifiedTrack ut = favouriteTracks.getFavourite().get(i);
                if (!ut.getType() && ut.getStreamTrack().getTitle().equals(selectedTrack.getTitle())) {
                    flag = 1;
                    isFavourite = true;
                    break;
                }
            }
            if (flag == 0) {
                isFavourite = false;
            }
            frag.refresh();
        }

        if (playerFragment != null && playerFragment.snappyRecyclerView != null) {
            playerFragment.snappyRecyclerView.getAdapter().notifyDataSetChanged();
            playerFragment.snappyRecyclerView.setCurrentPosition(position);
            playerFragment.snappyRecyclerView.setTransparency();
        }

        QueueFragment qFrag = (QueueFragment) fragMan.findFragmentByTag("queue");
        if (qFrag != null) {
            qFrag.updateQueueAdapter();
        }

/*        UnifiedTrack track = new UnifiedTrack(false, null, playerFragment.track);
        recentsRecycler.setVisibility(View.VISIBLE);
        recentsNothingText.setVisibility(View.INVISIBLE);
        continuePlayingList.clear();
        for (int i = 0; i < Math.min(10, recentlyPlayed.getRecentlyPlayed().size()); i++) {
            continuePlayingList.add(recentlyPlayed.getRecentlyPlayed().get(i));
        }
        rAdapter.notifyDataSetChanged();

        RecentsFragment rFrag = (RecentsFragment) fragMan.findFragmentByTag("recent");
        if (rFrag != null && rFrag.rtAdpater != null) {
            rFrag.rtAdpater.notifyDataSetChanged();
        }*/
    }

    public class loadSavedData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            getSavedData();
            return "done";
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            main.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (settings == null) {
                        settings = new Settings();
                    }

                    themeColor = settings.getThemeColor();
                    minAudioStrength = settings.getMinAudioStrength();


//                    navigationView.setItemIconTintList(ColorStateList.valueOf(themeColor));

                    try {
                        if (Build.VERSION.SDK_INT >= 21) {
                            Window window = ((Activity) ctx).getWindow();
                            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                            window.setStatusBarColor(CommonUtils.getDarkColor(themeColor));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (prevVersionCode == -1 || prevVersionCode <= 30) {
                        savedDNAs = null;
                    }

                    if (allPlaylists == null) {
                        allPlaylists = new AllPlaylists();
                    }

                    if (tempPlaylist == null) {
                        tempPlaylist = new Playlist(null, null);
                    }

                    if (queue == null) {
                        queue = new Queue();
                    }

                    if (favouriteTracks == null) {
                        favouriteTracks = new Favourite();
                    }

                    if (recentlyPlayed == null) {
                        recentlyPlayed = new RecentlyPlayed();
                    }
                    if (allMusicFolders == null) {
                        allMusicFolders = new AllMusicFolders();
                    }
                    if (savedDNAs == null) {
                        savedDNAs = new AllSavedDNA();
                    }

                    if (equalizerModel == null) {
                        equalizerModel = new EqualizerModel();
                        isEqualizerEnabled = true;
                        isEqualizerReloaded = false;
                    } else {
                        isEqualizerEnabled = equalizerModel.isEqualizerEnabled();
                        isEqualizerReloaded = true;
                        seekbarpos = equalizerModel.getSeekbarpos();
                        presetPos = equalizerModel.getPresetPos();
                        reverbPreset = equalizerModel.getReverbPreset();
                        bassStrength = equalizerModel.getBassStrength();
                    }

                    new SaveVersionCode().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    getLocalSongs();

                    if (queue != null && queue.getQueue().size() != 0) {
                        UnifiedTrack utHome = queue.getQueue().get(queueCurrentIndex);
                        if (utHome.getType()) {
                            imgLoader.DisplayImage(utHome.getLocalTrack().getPath(), spImgHome);
                            spTitleHome.setText(utHome.getLocalTrack().getTitle());
                        } else {
                            imgLoader.DisplayImage(utHome.getStreamTrack().getArtworkURL(), spImgHome);
                            spTitleHome.setText(utHome.getStreamTrack().getTitle());
                        }
                    } else {
                        bottomToolbar.setVisibility(GONE);
                    }

                    for (int i = 0; i < Math.min(10, recentlyPlayed.getRecentlyPlayed().size()); i++) {
                        continuePlayingList.add(recentlyPlayed.getRecentlyPlayed().get(i));
                    }


                    adapter = new LocalTracksHorizontalAdapter(finalLocalSearchResultList, ctx);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false);
                    AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
                    alphaAdapter.setFirstOnly(false);


                }
            });
        }
    }

    private void getSavedData() {
        try {
            Gson gson = new Gson();
            Log.d("TIME", "start");
            String json2 = mPrefs.getString("allPlaylists", "");
            allPlaylists = gson.fromJson(json2, AllPlaylists.class);
            Log.d("TIME", "allPlaylists");
            String json3 = mPrefs.getString("queue", "");
            queue = gson.fromJson(json3, Queue.class);
            Log.d("TIME", "queue");
            String json4 = mPrefs.getString("recentlyPlayed", "");
            recentlyPlayed = gson.fromJson(json4, RecentlyPlayed.class);
            Log.d("TIME", "recents");
            String json5 = mPrefs.getString("favouriteTracks", "");
            favouriteTracks = gson.fromJson(json5, Favourite.class);
            Log.d("TIME", "fav");
            String json6 = mPrefs.getString("queueCurrentIndex", "");
            queueCurrentIndex = gson.fromJson(json6, Integer.class);
            Log.d("TIME", "queueCurrentindex");
            String json8 = mPrefs.getString("settings", "");
            settings = gson.fromJson(json8, Settings.class);
            Log.d("TIME", "settings");
            String json9 = mPrefs.getString("equalizer", "");
            equalizerModel = gson.fromJson(json9, EqualizerModel.class);
            Log.d("TIME", "equalizer");
            String json = mPrefs.getString("savedDNAs", "");
            savedDNAs = gson.fromJson(json, AllSavedDNA.class);
            Log.d("TIME", "savedDNAs");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String json7 = mPrefs.getString("versionCode", "");
            prevVersionCode = gson.fromJson(json7, Integer.class);
            Log.d("TIME", "VersionCode : " + prevVersionCode + " : " + versionCode);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getLocalSongs() {

        localTrackList.clear();
        recentlyAddedTrackList.clear();
        finalLocalSearchResultList.clear();
        finalRecentlyAddedTrackSearchResultList.clear();
        albums.clear();
        finalAlbums.clear();
        artists.clear();
        finalArtists.clear();

        ContentResolver musicResolver = this.getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, MediaStore.MediaColumns.DATE_ADDED + " DESC");

        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            int albumColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM);
            int pathColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.DATA);
            int durationColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.DURATION);

            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                String thisAlbum = musicCursor.getString(albumColumn);
                String path = musicCursor.getString(pathColumn);
                long duration = musicCursor.getLong(durationColumn);
                if (duration > 10000) {
                    LocalTrack lt = new LocalTrack(thisId, thisTitle, thisArtist, thisAlbum, path, duration);
                    localTrackList.add(lt);
                    finalLocalSearchResultList.add(lt);
                    if (recentlyAddedTrackList.size() <= 50) {
                        recentlyAddedTrackList.add(lt);
                        finalRecentlyAddedTrackSearchResultList.add(lt);
                    }

                    int pos;
                    if (thisAlbum != null) {
                        pos = checkAlbum(thisAlbum);
                        if (pos != -1) {
                            albums.get(pos).getAlbumSongs().add(lt);
                        } else {
                            List<LocalTrack> llt = new ArrayList<>();
                            llt.add(lt);
                            Album ab = new Album(thisAlbum, llt);
                            albums.add(ab);
                        }
                        if (pos != -1) {
                            finalAlbums.get(pos).getAlbumSongs().add(lt);
                        } else {
                            List<LocalTrack> llt = new ArrayList<>();
                            llt.add(lt);
                            Album ab = new Album(thisAlbum, llt);
                            finalAlbums.add(ab);
                        }
                    }

                    if (thisArtist != null) {
                        pos = checkArtist(thisArtist);
                        if (pos != -1) {
                            artists.get(pos).getArtistSongs().add(lt);
                        } else {
                            List<LocalTrack> llt = new ArrayList<>();
                            llt.add(lt);
                            Artist ab = new Artist(thisArtist, llt);
                            artists.add(ab);
                        }
                        if (pos != -1) {
                            finalArtists.get(pos).getArtistSongs().add(lt);
                        } else {
                            List<LocalTrack> llt = new ArrayList<>();
                            llt.add(lt);
                            Artist ab = new Artist(thisArtist, llt);
                            finalArtists.add(ab);
                        }
                    }

                    File f = new File(path);
                    String dirName = f.getParentFile().getName();
                    if (getFolder(dirName) == null) {
                        MusicFolder mf = new MusicFolder(dirName);
                        mf.getLocalTracks().add(lt);
                        allMusicFolders.getMusicFolders().add(mf);
                    } else {
                        getFolder(dirName).getLocalTracks().add(lt);
                    }
                }

            }
            while (musicCursor.moveToNext());
        }

        if (musicCursor != null)
            musicCursor.close();

        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
        try {
            if (localTrackList.size() > 0) {
                Collections.sort(localTrackList, new LocalMusicComparator());
                Collections.sort(finalLocalSearchResultList, new LocalMusicComparator());
            }
            if (albums.size() > 0) {
                Collections.sort(albums, new AlbumComparator());
                Collections.sort(finalAlbums, new AlbumComparator());
            }
            if (artists.size() > 0) {
                Collections.sort(artists, new ArtistComparator());
                Collections.sort(finalArtists, new ArtistComparator());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<UnifiedTrack> tmp = new ArrayList<>();
        boolean queueCurrentIndexCollision = false;
        int indexCorrection = 0;
        for (int i = 0; i < queue.getQueue().size(); i++) {
            UnifiedTrack ut = queue.getQueue().get(i);
            if (ut.getType()) {
                if (!checkTrack(ut.getLocalTrack())) {
                    if (i == queueCurrentIndex) {
                        queueCurrentIndexCollision = true;
                    } else if (i < queueCurrentIndex) {
                        indexCorrection++;
                    }
                    tmp.add(ut);
                }
            }
        }
        for (int i = 0; i < tmp.size(); i++) {
            queue.getQueue().remove(tmp.get(i));
        }
        if (queueCurrentIndexCollision) {
            if (queue.getQueue().size() > 0) {
                queueCurrentIndex = 0;
            } else {
                queue = new Queue();
            }
        } else {
            queueCurrentIndex -= indexCorrection;
        }

        tmp.clear();

        for (int i = 0; i < recentlyPlayed.getRecentlyPlayed().size(); i++) {
            UnifiedTrack ut = recentlyPlayed.getRecentlyPlayed().get(i);
            if (ut.getType()) {
                if (!checkTrack(ut.getLocalTrack())) {
                    tmp.add(ut);
                }
            }
        }
        for (int i = 0; i < tmp.size(); i++) {
            recentlyPlayed.getRecentlyPlayed().remove(tmp.get(i));
        }

        List<UnifiedTrack> temp = new ArrayList<>();
        List<Playlist> tmpPL = new ArrayList<>();

        for (int i = 0; i < allPlaylists.getPlaylists().size(); i++) {
            Playlist pl = allPlaylists.getPlaylists().get(i);
            for (int j = 0; j < pl.getSongList().size(); j++) {
                UnifiedTrack ut = pl.getSongList().get(j);
                if (ut.getType()) {
                    if (!checkTrack(ut.getLocalTrack())) {
                        temp.add(ut);
                    }
                }
            }
            for (int j = 0; j < temp.size(); j++) {
                pl.getSongList().remove(temp.get(j));
            }
            temp.clear();
            if (pl.getSongList().size() == 0) {
                tmpPL.add(pl);
            }
        }
        for (int i = 0; i < tmpPL.size(); i++) {
            allPlaylists.getPlaylists().remove(tmpPL.get(i));
        }
        tmpPL.clear();
    }

    public boolean checkTrack(LocalTrack lt) {
        for (int i = 0; i < localTrackList.size(); i++) {
            LocalTrack localTrack = localTrackList.get(i);
            if (localTrack.getTitle().equals(lt.getTitle())) {
                return true;
            }
        }
        return false;
    }

    public int checkAlbum(String album) {
        for (int i = 0; i < albums.size(); i++) {
            Album ab = albums.get(i);
            if (ab.getName().equals(album)) {
                return i;
            }
        }
        return -1;
    }

    public int checkArtist(String artist) {
        for (int i = 0; i < artists.size(); i++) {
            Artist at = artists.get(i);
            if (at.getName().equals(artist)) {
                return i;
            }
        }
        return -1;
    }

    public MusicFolder getFolder(String folderName) {
        MusicFolder mf = null;
        for (int i = 0; i < allMusicFolders.getMusicFolders().size(); i++) {
            MusicFolder mf1 = allMusicFolders.getMusicFolders().get(i);
            if (mf1.getFolderName().equals(folderName)) {
                mf = mf1;
                break;
            }
        }
        return mf;
    }

    @Override
    public void onBackPressed() {
        PlayerFragment plFrag = playerFragment;
        QueueFragment qFrag = (QueueFragment) fragMan.findFragmentByTag("queue");
        EqualizerFragment eqFrag = (EqualizerFragment) fragMan.findFragmentByTag("equalizer");


        if (showCase != null && showCase.isShowing()) {
            showCase.hide();
        } else if (qFrag != null && qFrag.isShowcaseVisible()) {
            qFrag.hideShowcase();
        } else if (eqFrag != null && eqFrag.isShowcaseVisible()) {
            eqFrag.hideShowcase();
        } else if (isFullScreenEnabled) {
            isFullScreenEnabled = false;
            plFrag.bottomContainer.setVisibility(View.VISIBLE);
            plFrag.seekBarContainer.setVisibility(View.VISIBLE);
            plFrag.toggleContainer.setVisibility(View.VISIBLE);
            plFrag.spToolbar.setVisibility(View.VISIBLE);
            onFullScreen();
        } else if (isEqualizerVisible) {
            showPlayer2();
        } else if (isQueueVisible) {
            showPlayer3();
        } else if (isPlayerVisible && !isPlayerTransitioning && playerFragment.smallPlayer != null) {
            hidePlayer();
            isPlayerVisible = false;
        } else if (!searchView.isIconified()) {
            searchView.setQuery("", true);
            searchView.setIconified(true);
            if (streamRecyclerContainer.getVisibility() != GONE) {
                streamRecyclerContainer.setVisibility(GONE);
            }
            new Thread(new CancelCall()).start();
        } else if (streamRecyclerContainer.getVisibility() == View.VISIBLE) {
            streamRecyclerContainer.setVisibility(GONE);
        } else {
            if (isCategorydetail) {
                hideFragment("categorydetail");
            } else if (isProfiledetail) {
                hideFragment("Profiledetail");
            } else if (isOwnPlaylist) {
                hideFragment("own_play_list");
            } else {
                rb_bottom.selectTab(0,true);
                if (isQueueVisible) {
                    hideFragment("queue");
                    setTitle("Music");
                } else if (isEqualizerVisible) {
                    finalSelectedTracks.clear();
                    hideFragment("equalizer");
                    setTitle("Music");
                } else if (isRecentVisible) {
                    hideFragment("recent");
                    setTitle("Music");
                } else if (isFavoritepro) {
                    hideFragment("favorite");
                    setTitle("Music");
                } else if (isCategoryVisible) {
                    hideFragment("category");
                    setTitle("Music");
                } else if (isProfileVisible) {
                    hideFragment("viewprofile");
                    setTitle("Music");
                } else if (isSettingsVisible) {
                    hideFragment("settings");
                    new SaveSettings().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    setTitle("Music");
                } else if (!isPlayerTransitioning) {
                    startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        searchItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.findViewById(R.id.search_close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setQuery("", false);
                searchView.setIconified(true);
                searchView.clearFocus();
                if (streamRecyclerContainer.getVisibility() != GONE) {
                    streamRecyclerContainer.setVisibility(GONE);
                }
            }
        });
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showFragment("settings");
            return true;
        }
        if (id == R.id.menu_search) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                try {
                    final Uri imageUri = data.getData();
                    String path = imageUri.getPath();
                    Toast.makeText(this, path + "", Toast.LENGTH_SHORT).show();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    selectedImage = BitmapFactory.decodeStream(imageStream);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        new SaveVersionCode().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new SaveData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new SaveSettings().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new SaveQueue().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        try {
            prefsEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(headSetReceiver);
        RefWatcher refWatcher = MusicDNAApplication.getRefWatcher(this);
        refWatcher.watch(this);
        TelephonyManager mgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (mgr != null) {
            mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        }
        if (bound) {
            myService.setCallbacks(null); // unregister
            unbindService(serviceConnection);
            bound = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        CommonUtils.hideKeyboard(this);
        setSearchdata(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        setSearchdata(newText);
        return true;
    }


    public void hidePlayer() {

        if (playerFragment != null && playerFragment.mVisualizerView != null) {
            playerFragment.mVisualizerView.setVisibility(View.GONE);
            playerFragment.lyricsContainer.setVisibility(View.GONE);
        }

//        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = ((Activity) (ctx)).getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(CommonUtils.getDarkColor(themeColor));
        }

        isPlayerVisible = false;

        if (playerFragment != null && playerFragment.cpb != null) {
            playerFragment.cpb.setAlpha(0.0f);
            playerFragment.cpb.setVisibility(View.VISIBLE);
            playerFragment.cpb.animate()
                    .alpha(1.0f);
        }
        if (playerFragment != null && playerFragment.smallPlayer != null) {
            playerFragment.smallPlayer.setAlpha(0.0f);
            playerFragment.smallPlayer.setVisibility(View.VISIBLE);
            playerFragment.smallPlayer.animate()
                    .alpha(1.0f);
        }

        if (playerFragment != null && playerFragment.spToolbar != null) {
            playerFragment.spToolbar.animate()
                    .alpha(0.0f)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            playerFragment.spToolbar.setVisibility(GONE);
                        }
                    });
        }

        playerContainer.setVisibility(View.VISIBLE);
        if (playerFragment != null) {
            playerContainer.animate()
                    .translationY(playerContainer.getHeight() - playerFragment.smallPlayer.getHeight())
                    .setDuration(300);
        } else {
            playerContainer.animate()
                    .translationY(playerContainer.getHeight() - playerFragment.smallPlayer.getHeight())
                    .setDuration(300)
                    .setStartDelay(500);
        }

        if (playerFragment != null) {

            playerFragment.player_controller.setAlpha(0.0f);
            playerFragment.player_controller.setImageDrawable(playerFragment.mainTrackController.getDrawable());

            playerFragment.player_controller.animate()
                    .alpha(1.0f);

            playerFragment.snappyRecyclerView.animate()
                    .alpha(0.0f)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            playerFragment.snappyRecyclerView.setVisibility(GONE);
                        }
                    });
        }
    }

    public void showPlayer() {

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = ((Activity) (ctx)).getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = getTheme();
            theme.resolveAttribute(R.attr.background_zero, typedValue, true);
            int normal = typedValue.data;

            window.setStatusBarColor(normal);
        }

//        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//        searchView.setQuery("", false);
//        searchView.setIconified(true);
        new Thread(new CancelCall()).start();

        isPlayerVisible = true;
        isEqualizerVisible = false;
        isQueueVisible = false;

        playerContainer.setVisibility(View.VISIBLE);
        if (playerFragment != null && playerFragment.mVisualizerView != null)
            playerFragment.mVisualizerView.setVisibility(View.INVISIBLE);

        if (playerFragment != null && playerFragment.player_controller != null) {
            playerFragment.player_controller.setAlpha(1.0f);
            playerFragment.player_controller.animate()
                    .setDuration(300)
                    .alpha(0.0f);
        }

        if (playerFragment != null && playerFragment.cpb != null) {
            playerFragment.cpb.animate()
                    .alpha(0.0f);
        }
        if (playerFragment != null && playerFragment.smallPlayer != null) {
            playerFragment.smallPlayer.animate()
                    .alpha(0.0f);
        }

        if (playerFragment != null && playerFragment.spToolbar != null) {
            playerFragment.spToolbar.setVisibility(View.VISIBLE);
            playerFragment.spToolbar.animate().alpha(1.0f);
        }

        isPlayerTransitioning = true;

        playerContainer.animate()
                .setDuration(300)
                .translationY(0)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        isPlayerTransitioning = false;
                        if (playerFragment != null && playerFragment.snappyRecyclerView != null) {
                            playerFragment.snappyRecyclerView.setTransparency();
                        }
                    }
                });


        if (playerFragment != null && playerFragment.snappyRecyclerView != null) {
            playerFragment.snappyRecyclerView.setVisibility(View.VISIBLE);
            playerFragment.snappyRecyclerView.animate()
                    .alpha(1.0f)
                    .setDuration(300);
        }

        final Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (playerFragment != null && playerFragment.mVisualizerView != null) {
                    if (playerFragment.isLyricsVisisble) {
                        playerFragment.mVisualizerView.setVisibility(GONE);
                        playerFragment.lyricsContainer.setVisibility(View.VISIBLE);
                    } else {
                        playerFragment.mVisualizerView.setVisibility(View.VISIBLE);
                    }
                }
            }
        }, 400);

    }

    public void hidePlayer2() {

        isEqualizerVisible = true;

        if (playerFragment.mVisualizerView != null)
            playerFragment.mVisualizerView.setVisibility(View.GONE);

        final Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                playerContainer.animate()
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .translationX(playerContainer.getWidth());
            }
        }, 50);

        playerContainer.setVisibility(View.VISIBLE);

    }

    public void showPlayer2() {

        isPlayerVisible = true;
        isEqualizerVisible = false;
        isQueueVisible = false;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideFragment("equalizer");
            }
        }, 350);

        playerContainer.setVisibility(View.VISIBLE);
        if (playerFragment.mVisualizerView != null)
            playerFragment.mVisualizerView.setVisibility(View.INVISIBLE);

        isPlayerTransitioning = true;

        playerContainer.animate()
                .setDuration(300)
                .translationX(0)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        isPlayerTransitioning = false;
                        if (playerFragment != null && playerFragment.snappyRecyclerView != null) {
                            playerFragment.snappyRecyclerView.setTransparency();
                        }
                        if (!playerFragment.isLyricsVisisble) {
                            playerFragment.mVisualizerView.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    public void hidePlayer3() {

        isQueueVisible = true;

        if (playerFragment.mVisualizerView != null)
            playerFragment.mVisualizerView.setVisibility(View.INVISIBLE);

        final Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                playerContainer.animate()
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .translationX(-1 * playerContainer.getWidth());
            }
        }, 50);

        playerContainer.setVisibility(View.VISIBLE);

    }

    public void showPlayer3() {

        isPlayerVisible = true;
        isEqualizerVisible = false;
        isQueueVisible = false;

        if (playerFragment.mVisualizerView != null)
            playerFragment.mVisualizerView.setVisibility(View.INVISIBLE);

        isPlayerTransitioning = true;

        playerContainer.animate()
                .setDuration(300)
                .translationX(0)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        isPlayerTransitioning = false;
                        if (playerFragment != null && playerFragment.snappyRecyclerView != null) {
                            playerFragment.snappyRecyclerView.setTransparency();
                        }
                        if (!playerFragment.isLyricsVisisble) {
                            playerFragment.mVisualizerView.setVisibility(View.VISIBLE);
                        }
                    }
                });

        final Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideFragment("queue");
            }
        }, 400);
    }

    public void updateVisualizer(byte[] bytes) {
        mBytes = bytes;
        try {
            new MyAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            updatePoints();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            main.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    playerFragment.mVisualizerView.updateVisualizer(mBytes);
                    if (playerFragment.mVisualizerView.bmp != null) {
//                        if (navImageView != null) {
//                            try {
//                                Bitmap croppedBmp = Bitmap.createBitmap(playerFragment.mVisualizerView.bmp, 0, (int) (75 * ratio), screen_width, screen_width);
//                                navImageView.setImageBitmap(croppedBmp);
//                            } catch (Exception | OutOfMemoryError e) {
//                                e.printStackTrace();
//                            }
//                        }
                    }
                }
            });
        }
    }

    public void updatePoints() {

        try {
            playerFragment.mVisualizerView.outerRadius = (float) (Math.min(playerFragment.mVisualizerView.width, playerFragment.mVisualizerView.height) * 0.42);
            playerFragment.mVisualizerView.normalizedPosition = ((float) (playerFragment.mMediaPlayer.getCurrentPosition()) / (float) (playerFragment.durationInMilliSec));
            if (mBytes == null) {
                return;
            }
            playerFragment.mVisualizerView.angle = (float) (Math.PI - playerFragment.mVisualizerView.normalizedPosition * playerFragment.mVisualizerView.TAU);
            playerFragment.mVisualizerView.color = 0;
            playerFragment.mVisualizerView.lnDataDistance = 0;
            playerFragment.mVisualizerView.distance = 0;
            playerFragment.mVisualizerView.size = 0;
            playerFragment.mVisualizerView.volume = 0;
            playerFragment.mVisualizerView.power = 0;
        } catch (Exception e) {

        }

        float x, y;

        int midx = (int) (playerFragment.mVisualizerView.width / 2);
        int midy = (int) (playerFragment.mVisualizerView.height / 2);

        // calculate min and max amplitude for current byte array
        float max = Integer.MIN_VALUE, min = Integer.MAX_VALUE;
        for (int a = 16; a < (mBytes.length / 2); a++) {
            float amp = mBytes[(a * 2)] * mBytes[(a * 2)] + mBytes[(a * 2) + 1] * mBytes[(a * 2) + 1];
            if (amp > max) {
                max = amp;
            }
            if (amp < min) {
                min = amp;
            }
        }

        /**
         * Number Fishing is all that is used here to get the best looking DNA
         * Number fishing is HOW YOU WIN AT LIFE. -- paullewis :)
         * **/

        for (int a = 16; a < (mBytes.length / 2); a++) {

            if (max <= 10.0) {
                break;
            }

            // scale the amplitude to the range [0,1]
            float amp = mBytes[(a * 2) + 0] * mBytes[(a * 2) + 0] + mBytes[(a * 2) + 1] * mBytes[(a * 2) + 1];
            if (max != min)
                amp = (amp - min) / (max - min);
            else {
                amp = 0;
            }

            playerFragment.mVisualizerView.volume = (amp);

            // converting polar to cartesian (distance calculated afterwards acts as radius for polar co-ords)
            x = (float) Math.sin(playerFragment.mVisualizerView.angle);
            y = (float) Math.cos(playerFragment.mVisualizerView.angle);

            // filtering low amplitude
            if (playerFragment.mVisualizerView.volume < minAudioStrength) {
                continue;
            }

            // color ( value of hue inn HSV ) calculated based on current progress of the song or audio clip
            playerFragment.mVisualizerView.color = (float) (playerFragment.mVisualizerView.normalizedPosition - 0.12 + Math.random() * 0.24);
            playerFragment.mVisualizerView.color = Math.round(playerFragment.mVisualizerView.color * 360);
            seekBarColor = (float) (playerFragment.mVisualizerView.normalizedPosition);
            seekBarColor = Math.round(seekBarColor * 360);

            // calculating distance from center ( 'r' in polar coordinates)
            playerFragment.mVisualizerView.lnDataDistance = (float) ((Math.log(a - 4) / playerFragment.mVisualizerView.LOG_MAX) - playerFragment.mVisualizerView.BASE);
            playerFragment.mVisualizerView.distance = playerFragment.mVisualizerView.lnDataDistance * playerFragment.mVisualizerView.outerRadius;


            // size of the circle to be rendered at the calculated position
            playerFragment.mVisualizerView.size = ratio * ((float) (4.5 * playerFragment.mVisualizerView.volume * playerFragment.mVisualizerView.MAX_DOT_SIZE + Math.random() * 2));

            // alpha also based on volume ( amplitude )
            playerFragment.mVisualizerView.alpha = (float) (playerFragment.mVisualizerView.volume * 0.09);

            // final cartesian coordinates for drawing on canvas
            x = x * playerFragment.mVisualizerView.distance;
            y = y * playerFragment.mVisualizerView.distance;


            float[] hsv = new float[3];
            hsv[0] = playerFragment.mVisualizerView.color;
            hsv[1] = (float) 0.9;
            hsv[2] = (float) 0.9;

            // setting color of the Paint
            playerFragment.mVisualizerView.mForePaint.setColor(Color.HSVToColor(hsv));

            if (playerFragment.mVisualizerView.size >= 8.0 && playerFragment.mVisualizerView.size < 29.0) {
                playerFragment.mVisualizerView.mForePaint.setAlpha(17);
            } else if (playerFragment.mVisualizerView.size >= 29.0 && playerFragment.mVisualizerView.size <= 60.0) {
                playerFragment.mVisualizerView.mForePaint.setAlpha(9);
            } else if (playerFragment.mVisualizerView.size > 60.0) {
                playerFragment.mVisualizerView.mForePaint.setAlpha(3);
            } else {
                playerFragment.mVisualizerView.mForePaint.setAlpha((int) (playerFragment.mVisualizerView.alpha * 1000));
            }

            // Draw to the *temp* canvas, this is done to deal with gaps in rendering, when canvas is out of focus
            cacheCanvas.drawCircle(midx + x, midy + y, playerFragment.mVisualizerView.size, playerFragment.mVisualizerView.mForePaint);

        }
    }

    @Override
    public void onComplete() {

        // Check for sleep timer and whether it has timed out
        if (isSleepTimerEnabled && isSleepTimerTimeout) {
            Toast.makeText(ctx, "Sleep timer timed out, closing app", Toast.LENGTH_SHORT).show();

            if (playerFragment != null && playerFragment.timer != null)
                playerFragment.timer.cancel();

            // Remove the notification
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(1);

            // Finish the activity
            finish();
            return;
        }

        // Save the DNA if saving is enabled
        if (isSaveDNAEnabled) {
            new SaveTheDNAs().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

        QueueFragment qFrag = (QueueFragment) fragMan.findFragmentByTag("queue");
        queueCall = true;

        PlayerFragment plFrag = playerFragment;

        if (repeatOnceEnabled && !nextControllerClicked) {

            /*
             * Executed if repeat once is enabled and user did not click the next button from player.
             */

            // Set Progress bar to 0
            plFrag.progressBar.setProgress(0);
            plFrag.progressBar.setSecondaryProgress(0);

            // Get Visualizer and Visualizer View to initial state
            plFrag.mVisualizer.setEnabled(true);
            VisualizerView.w = screen_width;
            VisualizerView.h = screen_height;
            VisualizerView.conf = Bitmap.Config.ARGB_8888;
            VisualizerView.bmp = Bitmap.createBitmap(VisualizerView.w, VisualizerView.h, VisualizerView.conf);
            cacheCanvas = new Canvas(VisualizerView.bmp);

            // Play the song again by seeking media player to 0
            plFrag.mMediaPlayer.seekTo(0);

            // Setup the icons
            plFrag.mainTrackController.setImageResource(R.drawable.ic_pause_white_48dp);
            plFrag.isReplayIconVisible = false;
            plFrag.player_controller.setImageResource(R.drawable.ic_pause_white_48dp);

            // Resume the MediaPlayer
            plFrag.isPrepared = true;
            plFrag.mMediaPlayer.start();
        } else {

            /*
             * Executed if repeat once is disabled.
             * Execution depends on the current position in queue and whether the next button was clicked or not.
             * If current position is at the end of the queue, then number of elements in the queue are checked.
             */

            if (queueCurrentIndex < queue.getQueue().size() - 1) {
                queueCurrentIndex++;
                nextControllerClicked = false;
                hasQueueEnded = false;
                if (qFrag != null) {
                    qFrag.updateQueueAdapter();
                }
                selectedTrack = queue.getQueue().get(queueCurrentIndex).getStreamTrack();
                streamSelected = true;
                localSelected = false;
                onTrackSelected(-1);
            } else {
                if ((repeatEnabled || repeatOnceEnabled) && (queue.getQueue().size() > 1)) {
                    nextControllerClicked = false;
                    hasQueueEnded = false;
                    queueCurrentIndex = 0;
                    if (qFrag != null) {
                        qFrag.updateQueueAdapter();
                    }
                    onQueueItemClicked(0);
                } else if ((repeatEnabled || repeatOnceEnabled) && (queue.getQueue().size() == 1)) {
                    nextControllerClicked = false;
                    hasQueueEnded = false;
                    plFrag.progressBar.setProgress(0);
                    plFrag.progressBar.setSecondaryProgress(0);
                    plFrag.mVisualizer.setEnabled(true);
                    plFrag.mMediaPlayer.seekTo(0);
                    plFrag.mainTrackController.setImageResource(R.drawable.ic_pause_white_48dp);
                    plFrag.isReplayIconVisible = false;
                    plFrag.player_controller.setImageResource(R.drawable.ic_pause_white_48dp);
                    plFrag.isPrepared = true;
                    plFrag.mMediaPlayer.start();
                } else {
                    if ((nextControllerClicked || hasQueueEnded) && (queue.getQueue().size() > 1)) {
                        nextControllerClicked = false;
                        hasQueueEnded = false;
                        queueCurrentIndex = 0;
                        if (qFrag != null) {
                            qFrag.updateQueueAdapter();
                        }
                        onQueueItemClicked(0);
                    } else if ((nextControllerClicked || hasQueueEnded) && (queue.getQueue().size() == 1)) {
                        nextControllerClicked = false;
                        hasQueueEnded = false;
                        plFrag.progressBar.setProgress(0);
                        plFrag.progressBar.setSecondaryProgress(0);
                        if (plFrag.mVisualizer != null)
                            plFrag.mVisualizer.setEnabled(true);
                        plFrag.mMediaPlayer.seekTo(0);
                        plFrag.mainTrackController.setImageResource(R.drawable.ic_pause_white_48dp);
                        plFrag.isReplayIconVisible = false;
                        plFrag.player_controller.setImageResource(R.drawable.ic_pause_white_48dp);
                        plFrag.isPrepared = true;
                        plFrag.mMediaPlayer.start();
                    } else {
                        // keep queue at last track or you are doomed
                    }
                }
            }
        }

    }

    @Override
    public void onPreviousTrack() {

        QueueFragment qFrag = (QueueFragment) fragMan.findFragmentByTag("queue");

        /*
         * Execution depends on the current position in the queue
         */

        if (queueCurrentIndex > 0) {
            queueCall = true;
            queueCurrentIndex--;
            if (qFrag != null) {
                qFrag.updateQueueAdapter();
            }
            selectedTrack = queue.getQueue().get(queueCurrentIndex).getStreamTrack();
            streamSelected = true;
            localSelected = false;
            onTrackSelected(-1);
        } else {
            // keep queue at 0
        }
    }

    @Override
    public void onEqualizerClicked() {
        hideAllFrags();
        hidePlayer2();
        showFragment("equalizer");
    }

    @Override
    public void onQueueClicked() {
        hideAllFrags();
        hidePlayer3();
        showFragment("queue");
    }

    @Override
    public void onPrepared() {
        showNotification();
    }

    @Override
    public void onFullScreen() {

        //Adds Haptic Feedback(Vibration) on entering and exiting full screen mode of video player
        View view = findViewById(R.id.root_view);
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);


        if (isFullScreenEnabled) {
            Toast.makeText(this, "Long Press to Exit", Toast.LENGTH_SHORT).show();
            ActionBar actionBar = getSupportActionBar();
            actionBar.hide();
        } else {
            ActionBar actionBar = getSupportActionBar();
            actionBar.show();
        }
    }

    @Override
    public void onSettingsClicked() {
        if (playerFragment.smallPlayer != null) {
            hidePlayer();
            isPlayerVisible = false;
            showFragment("settings");
        }
    }


    @Override
    public void onShuffleEnabled() {
        originalQueue = new Queue();
        for (UnifiedTrack ut : queue.getQueue()) {
            originalQueue.addToQueue(ut);
        }
        originalQueueIndex = queueCurrentIndex;
        UnifiedTrack ut = queue.getQueue().get(queueCurrentIndex);
        Collections.shuffle(queue.getQueue());
        for (int i = 0; i < queue.getQueue().size(); i++) {
            if (ut.equals(queue.getQueue().get(i))) {
                queue.getQueue().remove(i);
                break;
            }
        }
        queue.getQueue().add(0, ut);
        queueCurrentIndex = 0;

        new SaveQueue().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onShuffleDisabled() {
        UnifiedTrack ut1 = queue.getQueue().get(queueCurrentIndex);
        for (int i = 0; i < queue.getQueue().size(); i++) {
            UnifiedTrack ut = queue.getQueue().get(i);
            if (!originalQueue.getQueue().contains(ut)) {
                originalQueue.getQueue().add(ut);
            }
        }
        queue.getQueue().clear();
        for (UnifiedTrack ut : originalQueue.getQueue()) {
            queue.addToQueue(ut);
        }
        for (int i = 0; i < queue.getQueue().size(); i++) {
            if (ut1.equals(queue.getQueue().get(i))) {
                queueCurrentIndex = i;
                break;
            }
        }

        new SaveQueue().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onSmallPlayerTouched() {
        if (!isPlayerVisible) {
            isPlayerVisible = true;
            showPlayer();
        } else {
            isPlayerVisible = false;
            hidePlayer();
        }
    }

    @Override
    public void addCurrentSongtoPlaylist(UnifiedTrack ut) {
        if (Constant.get_profile_id(ctx).equals("0")) {
            Intent i = new Intent(HomeActivity.this, LoginRegisrationActivity.class);
            startActivity(i);
            finish();

        } else {

            showAddToPlaylistDialog(ut);
        }
    }

    @Override
    public void onPlayPause() {
        showNotification();
    }

    @Override
    public void onQueueItemClicked(final int position) {

        if (isPlayerVisible && isQueueVisible)
            showPlayer3();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                queueCurrentIndex = position;
                UnifiedTrack ut = queue.getQueue().get(position);
                Track track = ut.getStreamTrack();
                selectedTrack = track;
                streamSelected = true;
                localSelected = false;
                queueCall = false;
                isReloaded = false;
                onTrackSelected(position);
            }
        }, 500);
    }

    @Override
    public void onQueueSave() {
        showSaveQueueDialog();
    }

    @Override
    public void onQueueClear() {
        clearQueue();
        new SaveQueue().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onHeadsetRemoved() {
        PlayerFragment pFrag = getPlayerFragment();
        if (pFrag != null) {
            if (pFrag.mMediaPlayer != null && pFrag.mMediaPlayer.isPlaying()) {
                if (pFrag.isReplayIconVisible) {

                } else {
                    if (!pFrag.pauseClicked) {
                        pFrag.pauseClicked = true;
                    }
                    pFrag.togglePlayPause();
                }
            }
        }
    }

    @Override
    public void onHeadsetNextClicked() {
        PlayerFragment pFrag = getPlayerFragment();
        if (pFrag != null && !isReloaded) {
            pFrag.nextTrackController.performClick();
        }
    }

    @Override
    public void onHeadsetPreviousClicked() {
        PlayerFragment pFrag = getPlayerFragment();
        if (pFrag != null && !isReloaded) {
            pFrag.previousTrackController.performClick();
        }
    }

    @Override
    public void onHeadsetPlayPauseClicked() {
        PlayerFragment pFrag = getPlayerFragment();
        if (pFrag != null && !isReloaded) {
            if (pFrag.mMediaPlayer != null && pFrag.mMediaPlayer.isPlaying()) {
                if (pFrag.isReplayIconVisible) {
                    hasQueueEnded = true;
                    onComplete();
                } else {
                    if (!pFrag.pauseClicked) {
                        pFrag.pauseClicked = true;
                    }
                    pFrag.togglePlayPause();
                }
            }
        }
    }


    @Override
    public void onCheckChanged(boolean isChecked) {
        EqualizerFragment eqFrag = (EqualizerFragment) fragMan.findFragmentByTag("equalizer");
        if (isChecked) {
            try {
                isEqualizerEnabled = true;
                int pos = presetPos;
                if (pos != 0) {
                    playerFragment.mEqualizer.usePreset((short) (pos - 1));
                } else {
                    for (short i = 0; i < 5; i++) {
                        playerFragment.mEqualizer.setBandLevel(i, (short) seekbarpos[i]);
                    }
                }
                if (bassStrength != -1 && reverbPreset != -1) {
                    playerFragment.bassBoost.setEnabled(true);
                    playerFragment.bassBoost.setStrength(bassStrength);
                    playerFragment.presetReverb.setEnabled(true);
                    playerFragment.presetReverb.setPreset(reverbPreset);
                }
                playerFragment.mMediaPlayer.setAuxEffectSendLevel(1.0f);
                if (eqFrag != null)
                    eqFrag.setBlockerVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                isEqualizerEnabled = false;
                playerFragment.mEqualizer.usePreset((short) 0);
                playerFragment.bassBoost.setStrength((short) (((float) 1000 / 19) * (1)));
                playerFragment.presetReverb.setPreset((short) 0);
                if (eqFrag != null)
                    eqFrag.setBlockerVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        equalizerModel.isEqualizerEnabled = isChecked;
    }

    @Override
    public PlayerFragment getPlayerFragment() {
        return playerFragment;
    }

    class CancelCall implements Runnable {

        @Override
        public void run() {
//            if (call != null)
//                call.cancel();
        }
    }

    public void onQueueItemClicked2(int position) {
        if (position <= (queue.getQueue().size() - 1)) {
            queueCurrentIndex = position;
            UnifiedTrack ut = queue.getQueue().get(position);
            Track track = ut.getStreamTrack();
            selectedTrack = track;
            streamSelected = true;
            localSelected = false;
            queueCall = true;
            isReloaded = false;
            onTrackSelected(position);
        }
    }

    public void showAddToPlaylistDialog(final UnifiedTrack track) {
        final Dialog dialog = new Dialog(ctx);
        dialog.setContentView(R.layout.add_to_playlist_dialog);
        dialog.setTitle("Add to Playlist");

        final ListView lv = (ListView) dialog.findViewById(R.id.playlist_list);

        new Asy_User_Playlist(methods.getAPIRequest(Constant.Download, Constant.get_profile_id(HomeActivity.this), "1", Constant.is_myProfile_fav, "", ""),
                new Asy_User_Playlist.Listener_Played() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onEnd(String success, ArrayList<Own_Play_List> playeds) {
                        user_playlist = playeds;
                        AddToPlaylistAdapter adapter;
                        Log.e("item_and_click", "" + Constant.get_profile_id(ctx));
                        if (user_playlist != null && user_playlist.size() != 0) {
                            adapter = new AddToPlaylistAdapter(user_playlist, ctx);
                            lv.setAdapter(adapter);
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Own_Play_List own_play_list = user_playlist.get(position);
                                    Track track1 = track.getStreamTrack();

                                    Log.e("item_and_click", "" + Constant.get_profile_id(ctx));
                                    Log.e("item_and_click", "" + position);
                                    Log.e("item_and_click", "" + String.valueOf(track1.getID()));

                                    new Asy_Like_Unlile_Addplaylist
                                            (HomeActivity.methods.getAPIRequest
                                                    (Constant.MY_add_music_playlis
                                                            , Constant.get_profile_id(ctx)
                                                            , own_play_list.getUser_playlist_id()
                                                            , String.valueOf(track1.getID())
                                                            , ""
                                                            , ""
                                                    )
                                                    , Constant.AddInPlaylistMusic
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
                            });
                        } else {
                            lv.setVisibility(GONE);
                        }
                    }
                }).execute();


        // set the custom dialog components - text, image and button
        final EditText text = (EditText) dialog.findViewById(R.id.new_playlist_name);
        ImageView image = (ImageView) dialog.findViewById(R.id.confirm_button);
        // if button is clicked, close the custom dialog
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isNameRepeat = false;
                if (text.getText().toString().trim().equals("")) {
                    text.setError("Enter Playlist Name!");
                } else {
                    new Asy_Like_Unlile_Addplaylist
                            (HomeActivity.methods.getAPIRequest
                                    (Constant.CreatePlayList
                                            , Constant.get_profile_id(ctx)
                                            , text.getText().toString()
                                            , ""
                                            , ""
                                            , ""
                                    )
                                    , Constant.CreateNewPlaylist
                                    , new Asy_Like_Unlile_Addplaylist.Listener_Artist() {
                                @Override
                                public void onStart() {
                                }

                                @Override
                                public void onEnd(String success) {
                                    dialog.dismiss();
                                }
                            }).execute();

/*                    for (int i = 0; i < allPlaylists.getPlaylists().size(); i++) {
                        if (text.getText().toString().equals(allPlaylists.getPlaylists().get(i).getPlaylistName())) {
                            isNameRepeat = true;
                            text.setError("Playlist with same name exists!");
                            break;
                        }
                    }
                    if (!isNameRepeat) {
                        List<UnifiedTrack> l = new ArrayList<UnifiedTrack>();
                        l.add(track);
                        Playlist pl = new Playlist(l, text.getText().toString().trim());
                        allPlaylists.addPlaylist(pl);
                        dialog.dismiss();
                        new SavePlaylists().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }*/
                }
            }
        });

        dialog.show();
    }

    public void showSaveQueueDialog() {
        final Dialog dialog = new Dialog(ctx);
        dialog.setContentView(R.layout.add_to_playlist_dialog);
        dialog.setTitle("Save Queue");

        ListView lv = (ListView) dialog.findViewById(R.id.playlist_list);
        lv.setVisibility(GONE);

        // set the custom dialog components - text, image and button
        final EditText text = (EditText) dialog.findViewById(R.id.new_playlist_name);
        ImageView image = (ImageView) dialog.findViewById(R.id.confirm_button);
        // if button is clicked, close the custom dialog
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isNameRepeat = false;
                if (text.getText().toString().trim().equals("")) {
                    text.setError("Enter Playlist Name!");
                } else {
                    new Asy_Like_Unlile_Addplaylist
                            (HomeActivity.methods.getAPIRequest
                                    (Constant.CreatePlayList
                                            , Constant.get_profile_id(ctx)
                                            , text.getText().toString()
                                            , ""
                                            , ""
                                            , ""
                                    )
                                    , Constant.CreateNewPlaylist
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
            }
        });
        dialog.show();
    }

    FrameLayout fl_main_screen;

    public void mainFragment(String type) {
        startActivity(new Intent(this, LoginRegisrationActivity.class));
        if (fl_main_screen.getVisibility() != VISIBLE) {
            fl_main_screen.setVisibility(VISIBLE);
        }


    }


    public void showFragment(String type) {

        if (
                !type.equals("own_play_list") &&
                        !type.equals("categorydetail") &&
                        !type.equals("Profiledetail")
        ) {
            hideAllFrags();
        }

        if (!searchView.isIconified()) {
            searchView.setQuery("", true);
            searchView.setIconified(true);
            streamRecyclerContainer.setVisibility(GONE);
            new Thread(new CancelCall()).start();
        }

        if (type.equals("queue") && !isQueueVisible) {
            hideAllFrags();
            isQueueVisible = true;
            FragmentManager fm = getSupportFragmentManager();
            QueueFragment newFragment = (QueueFragment) fm.findFragmentByTag("queue");
            if (newFragment == null) {
                newFragment = new QueueFragment();
            }
            fm.beginTransaction()
                    .add(R.id.equalizer_queue_frag_container, newFragment, "queue")
                    .show(newFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        } else if (type.equals("equalizer") && !isEqualizerVisible) {
            isEqualizerVisible = true;
            FragmentManager fm = getSupportFragmentManager();
            EqualizerFragment newFragment = (EqualizerFragment) fm.findFragmentByTag("equalizer");
            if (newFragment == null) {
                newFragment = new EqualizerFragment();
            }
            fm.beginTransaction()
                    .add(R.id.equalizer_queue_frag_container, newFragment, "equalizer")
                    .show(newFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        } else if (type.equals("viewprofile") && !isProfileVisible) {
            isProfileVisible = true;
            FragmentManager fm = getSupportFragmentManager();
            ViewProfile newFragment = (ViewProfile) fm.findFragmentByTag("viewprofile");
            if (newFragment == null) {
                newFragment = new ViewProfile();
            }
            fm.beginTransaction()
                    .setCustomAnimations(R.anim.slide_left,
                            R.anim.slide_right,
                            R.anim.slide_left,
                            R.anim.slide_right)
                    .add(R.id.fragContainer, newFragment, "viewprofile")
                    .show(newFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        } else if (type.equals("own_play_list") && !isOwnPlaylist) {
            isOwnPlaylist = true;
            FragmentManager fm = getSupportFragmentManager();
            PlaylistDetailFragment newFragment = (PlaylistDetailFragment) fm.findFragmentByTag("own_play_list");
            if (newFragment == null) {
                newFragment = new PlaylistDetailFragment();
            }
            fm.beginTransaction()
                    .setCustomAnimations(R.anim.slide_left,
                            R.anim.slide_right,
                            R.anim.slide_left,
                            R.anim.slide_right)
                    .add(R.id.fragContainer, newFragment, "own_play_list")
                    .show(newFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        } else if (type.equals("favorite") && !isFavoritepro) {

            isFavoritepro = true;
            FragmentManager fm = getSupportFragmentManager();
            FavoriteFragment newFragment = (FavoriteFragment) fm.findFragmentByTag("favorite");
            if (newFragment == null) {
                newFragment = new FavoriteFragment();
            }
            fm.beginTransaction()
                    .setCustomAnimations(R.anim.slide_left,
                            R.anim.slide_right,
                            R.anim.slide_left,
                            R.anim.slide_right)
                    .add(R.id.fragContainer, newFragment, "favorite")
                    .show(newFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        } else if (type.equals("categorydetail")) {
            isCategorydetail = true;
            FragmentManager fm = getSupportFragmentManager();
            com.kmit.musicapp.fragments.O_Category.Category newFragment = (com.kmit.musicapp.fragments.O_Category.Category) fm.findFragmentByTag("categorydetail");
            if (newFragment == null) {
                newFragment = new com.kmit.musicapp.fragments.O_Category.Category();
            }
            fm.beginTransaction()
                    .setCustomAnimations(R.anim.slide_left,
                            R.anim.slide_right,
                            R.anim.slide_left,
                            R.anim.slide_right)
                    .add(R.id.fragContainer, newFragment, "categorydetail")
                    .show(newFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        } else if (type.equals("Profiledetail")) {
            isProfiledetail = true;
            FragmentManager fm = getSupportFragmentManager();
            ViewProfileEdite newFragment = (ViewProfileEdite) fm.findFragmentByTag("Profiledetail");
            if (newFragment == null) {
                newFragment = new ViewProfileEdite();
            }
            fm.beginTransaction()
                    .setCustomAnimations(R.anim.slide_left,
                            R.anim.slide_right,
                            R.anim.slide_left,
                            R.anim.slide_right)
                    .add(R.id.fragContainer, newFragment, "Profiledetail")
                    .show(newFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        } else if (type.equals("category") && !isCategoryVisible) {
            isCategoryVisible = true;
            FragmentManager fm = getSupportFragmentManager();
            ViewCategoryFragment newFragment = (ViewCategoryFragment) fm.findFragmentByTag("category");
            if (newFragment == null) {
                newFragment = new ViewCategoryFragment();
            }
            fm.beginTransaction()
                    .setCustomAnimations(R.anim.slide_left,
                            R.anim.slide_right,
                            R.anim.slide_left,
                            R.anim.slide_right)
                    .add(R.id.fragContainer, newFragment, "category")
                    .show(newFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        } else if (type.equals("recent") && !isRecentVisible) {
            isRecentVisible = true;
            FragmentManager fm = getSupportFragmentManager();
            RecentsFragment newFragment = (RecentsFragment) fm.findFragmentByTag("recent");
            if (newFragment == null) {
                newFragment = new RecentsFragment(title, url, requestBody);
            }
            fm.beginTransaction()
                    .setCustomAnimations(R.anim.slide_left,
                            R.anim.slide_right,
                            R.anim.slide_left,
                            R.anim.slide_right)
                    .add(R.id.fragContainer, newFragment, "recent")
                    .show(newFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        } else if (type.equals("settings") && !isSettingsVisible) {
            isSettingsVisible = true;
            FragmentManager fm = getSupportFragmentManager();
            SettingsFragment newFragment = (SettingsFragment) fm.findFragmentByTag("settings");
            if (newFragment == null) {
                newFragment = new SettingsFragment();
            }
            fm.beginTransaction()
                    .setCustomAnimations(R.anim.slide_left,
                            R.anim.slide_right,
                            R.anim.slide_left,
                            R.anim.slide_right)
                    .add(R.id.fragContainer, newFragment, "settings")
                    .show(newFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        }
    }

    public void hideFragment(String type) {

        if (type.equals("queue")) {
            isQueueVisible = false;
//            navigationView.setCheckedItem(R.id.nav_home);
            FragmentManager fm = getSupportFragmentManager();
            Fragment frag = fm.findFragmentByTag("queue");
            if (frag != null) {
                fm.beginTransaction()
                        .remove(frag)
                        .commitAllowingStateLoss();
            }
        } else if (type.equals("equalizer")) {
            isEqualizerVisible = false;
            new SaveEqualizer().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            FragmentManager fm = getSupportFragmentManager();
            Fragment frag = fm.findFragmentByTag("equalizer");
            if (frag != null) {
                fm.beginTransaction()
                        .remove(frag)
                        .commitAllowingStateLoss();
            }
        } else if (type.equals("categorydetail")) {
            isCategorydetail = false;
            FragmentManager fm = getSupportFragmentManager();
            Fragment frag = fm.findFragmentByTag("categorydetail");
            if (frag != null) {
                fm.beginTransaction()
                        .remove(frag)
                        .commitAllowingStateLoss();
            }
        } else if (type.equals("Profiledetail")) {
            isProfiledetail = false;
            FragmentManager fm = getSupportFragmentManager();
            Fragment frag = fm.findFragmentByTag("Profiledetail");
            if (frag != null) {
                fm.beginTransaction()
                        .remove(frag)
                        .commitAllowingStateLoss();
            }
        } else if (type.equals("viewprofile")) {
            isProfileVisible = false;
            FragmentManager fm = getSupportFragmentManager();
            Fragment frag = fm.findFragmentByTag("viewprofile");
            if (frag != null) {
                fm.beginTransaction()
                        .remove(frag)
                        .commitAllowingStateLoss();
            }
        } else if (type.equals("own_play_list")) {
            isOwnPlaylist = false;
            FragmentManager fm = getSupportFragmentManager();
            Fragment frag = fm.findFragmentByTag("own_play_list");
            if (frag != null) {
                fm.beginTransaction()
                        .remove(frag)
                        .commitAllowingStateLoss();
            }
        } else if (type.equals("favorite")) {
            isFavoritepro = false;
            FragmentManager fm = getSupportFragmentManager();
            Fragment frag = fm.findFragmentByTag("favorite");
            if (frag != null) {
                fm.beginTransaction()
                        .remove(frag)
                        .commitAllowingStateLoss();
            }
        } else if (type.equals("category")) {
            isCategoryVisible = false;
            FragmentManager fm = getSupportFragmentManager();
            Fragment frag = fm.findFragmentByTag("category");
            if (frag != null) {
                fm.beginTransaction()
                        .remove(frag)
                        .commitAllowingStateLoss();
            }
        } else if (type.equals("recent")) {
            isRecentVisible = false;
            setTitle("Music");
//            navigationView.setCheckedItem(R.id.nav_home);
            FragmentManager fm = getSupportFragmentManager();
            Fragment frag = fm.findFragmentByTag("recent");
            if (frag != null) {
                fm.beginTransaction()
                        .remove(frag)
                        .commitAllowingStateLoss();
            }
        } else if (type.equals("settings")) {
            isSettingsVisible = false;
            setTitle("Music");
//            navigationView.setCheckedItem(R.id.nav_home);
            FragmentManager fm = getSupportFragmentManager();
            Fragment frag = fm.findFragmentByTag("settings");
            if (frag != null) {
                fm.beginTransaction()
                        .remove(frag)
                        .commitAllowingStateLoss();
            }
        }
    }

    public void hideAllFrags() {
        hideFragment("queue");
        hideFragment("equalizer");
        hideFragment("categorydetail");
        hideFragment("Profiledetail");
        hideFragment("viewprofile");
        hideFragment("favorite");
        hideFragment("own_play_list");
        hideFragment("category");
        hideFragment("recent");
        hideFragment("settings");

        setTitle("Music");

    }

    public void showNotification() {
        if (!isNotificationVisible) {
            Intent intent = new Intent(this, MediaPlayerService.class);
            intent.setAction(Constants.ACTION_PLAY);
            startService(intent);
            isNotificationVisible = true;
        }
    }

    public void HideBottomFakeToolbar() {
        bottomToolbar.setVisibility(View.INVISIBLE);
    }

    public static void addToFavourites(UnifiedTrack ut) {
        boolean isRepeat = false;
        for (int i = 0; i < favouriteTracks.getFavourite().size(); i++) {
            UnifiedTrack ut1 = favouriteTracks.getFavourite().get(i);
            if (ut.getType() && ut1.getType()) {
                if (ut.getLocalTrack().getTitle().equals(ut1.getLocalTrack().getTitle())) {
                    isRepeat = true;
                    break;
                }
            } else if (!ut.getType() && !ut1.getType()) {
                if (ut.getStreamTrack().getTitle().equals(ut1.getStreamTrack().getTitle())) {
                    isRepeat = true;
                    break;
                }
            }
        }

        if (!isRepeat)
            favouriteTracks.getFavourite().add(ut);

    }

    public class SaveData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try {
                String json6 = gson.toJson(queueCurrentIndex);
                prefsEditor.putString("queueCurrentIndex", json6);
            } catch (Exception e) {

            }
            return null;
        }
    }

    public class SaveVersionCode extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try {
                String json7 = gson.toJson(versionCode);
                prefsEditor.putString("versionCode", json7);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static class SaveRecents extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            if (!isSaveRecentsRunning) {
                isSaveRecentsRunning = true;
                try {
                    String json4 = gson.toJson(recentlyPlayed);
                    prefsEditor.putString("recentlyPlayed", json4);
                } catch (Exception e) {

                }
                isSaveRecentsRunning = false;
            }
            return null;
        }
    }

    public static class SaveFavourites extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            if (!isSaveFavouritesRunning) {
                isSaveFavouritesRunning = true;
                try {
                    String json5 = gson.toJson(favouriteTracks);
                    prefsEditor.putString("favouriteTracks", json5);
                } catch (Exception e) {

                }
                isSaveFavouritesRunning = false;
            }
            return null;
        }
    }

    public static class SaveSettings extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            if (!isSaveSettingsRunning) {
                isSaveSettingsRunning = true;
                try {
                    String json8 = gson.toJson(settings);
                    prefsEditor.putString("settings", json8);
                } catch (Exception e) {

                }
                isSaveSettingsRunning = false;
            }
            return null;
        }
    }

    public static class SaveTheDNAs extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            if (!isSaveDNAsRunning) {
                isSaveDNAsRunning = true;
                try {
                    String json = gson.toJson(savedDNAs);
                    prefsEditor.putString("savedDNAs", json);
                } catch (Exception e) {

                }
                isSaveDNAsRunning = false;
            }
            return null;
        }
    }

    public static class SaveQueue extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            if (!isSaveQueueRunning) {
                isSaveQueueRunning = true;
                try {
                    String json3 = gson.toJson(queue);
                    prefsEditor.putString("queue", json3);
                    String json6 = gson.toJson(queueCurrentIndex);
                    prefsEditor.putString("queueCurrentIndex", json6);
                } catch (Exception e) {

                }
                isSaveQueueRunning = false;
            }
            return null;
        }
    }

    public static class SavePlaylists extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            if (!isSavePLaylistsRunning) {
                isSavePLaylistsRunning = true;
                try {
                    String json2 = gson.toJson(allPlaylists);
                } catch (Exception e) {

                }
                isSavePLaylistsRunning = false;
            }
            return null;
        }
    }

    public static class SaveEqualizer extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            if (!isSaveEqualizerRunning) {
                isSaveEqualizerRunning = true;
                try {
                    String json2 = gson.toJson(equalizerModel);
                    prefsEditor.putString("equalizer", json2);
                } catch (Exception e) {

                }
                isSaveEqualizerRunning = false;
            }
            return null;
        }
    }

    public void clearQueue() {
        QueueFragment qFrag = (QueueFragment) fragMan.findFragmentByTag("queue");
        for (int i = 0; i < queue.getQueue().size(); i++) {
            if (i < queueCurrentIndex) {
                queue.getQueue().remove(i);
                queueCurrentIndex--;
                if (qFrag != null) {
                    qFrag.notifyAdapterItemRemoved(i);
                }
                i--;
            } else if (i > queueCurrentIndex) {
                queue.getQueue().remove(i);
                if (qFrag != null) {
                    qFrag.notifyAdapterItemRemoved(i);
                }
                i--;
            }
        }
        if (playerFragment != null && playerFragment.snappyRecyclerView != null) {
            playerFragment.snappyRecyclerView.getAdapter().notifyDataSetChanged();
            playerFragment.snappyRecyclerView.setTransparency();
        }
    }


    public void refreshAlbumAndArtists() {

        albums.clear();
        finalAlbums.clear();
        artists.clear();
        finalArtists.clear();

        for (int i = 0; i < localTrackList.size(); i++) {
            LocalTrack lt = localTrackList.get(i);

            String thisAlbum = lt.getAlbum();

            int pos = checkAlbum(thisAlbum);

            if (pos != -1) {
                albums.get(pos).getAlbumSongs().add(lt);
            } else {
                List<LocalTrack> llt = new ArrayList<>();
                llt.add(lt);
                Album ab = new Album(thisAlbum, llt);
                albums.add(ab);
            }

            if (pos != -1) {
                finalAlbums.get(pos).getAlbumSongs().add(lt);
            } else {
                List<LocalTrack> llt = new ArrayList<>();
                llt.add(lt);
                Album ab = new Album(thisAlbum, llt);
                finalAlbums.add(ab);
            }

            String thisArtist = lt.getArtist();

            pos = checkArtist(thisArtist);

            if (pos != -1) {
                artists.get(pos).getArtistSongs().add(lt);
            } else {
                List<LocalTrack> llt = new ArrayList<>();
                llt.add(lt);
                Artist ab = new Artist(thisArtist, llt);
                artists.add(ab);
            }

            if (pos != -1) {
                finalArtists.get(pos).getArtistSongs().add(lt);
            } else {
                List<LocalTrack> llt = new ArrayList<>();
                llt.add(lt);
                Artist ab = new Artist(thisArtist, llt);
                finalArtists.add(ab);
            }

        }

        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
        try {
            if (localTrackList.size() > 0) {
                Collections.sort(localTrackList, new LocalMusicComparator());
                Collections.sort(finalLocalSearchResultList, new LocalMusicComparator());
            }
            if (albums.size() > 0) {
                Collections.sort(albums, new AlbumComparator());
                Collections.sort(finalAlbums, new AlbumComparator());
            }
            if (artists.size() > 0) {
                Collections.sort(artists, new ArtistComparator());
                Collections.sort(finalArtists, new ArtistComparator());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updateVisualizerRecycler() {
        if (playerFragment != null && playerFragment.snappyRecyclerView != null) {
            playerFragment.snappyRecyclerView.getAdapter().notifyDataSetChanged();
            playerFragment.snappyRecyclerView.scrollToPosition(queueCurrentIndex);
            playerFragment.snappyRecyclerView.setTransparency();
        }
    }


}
