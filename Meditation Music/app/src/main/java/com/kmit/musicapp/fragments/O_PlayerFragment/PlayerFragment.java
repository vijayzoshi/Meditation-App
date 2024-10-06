package com.kmit.musicapp.fragments.O_PlayerFragment;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.PresetReverb;
import android.media.audiofx.Visualizer;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.kmit.musicapp.Config;
import com.kmit.musicapp.Model.Banner_Slide;
import com.kmit.musicapp.MusicDNAApplication;
import com.kmit.musicapp.R;
import com.kmit.musicapp.Util.Constant;
import com.kmit.musicapp.activities.HomeActivity;
import com.kmit.musicapp.asyncTask.Asy_Like_Unlile_Addplaylist;
import com.kmit.musicapp.asyncTask.Asy_Singal_Music;
import com.kmit.musicapp.asyncTask.Asy_download;
import com.kmit.musicapp.clickitemtouchlistener.ClickItemTouchListener;
import com.kmit.musicapp.customviews.CustomProgressBar;
import com.kmit.musicapp.imageLoader.ImageLoader;
import com.kmit.musicapp.lyrics.Lyrics;
import com.kmit.musicapp.models.LocalTrack;
import com.kmit.musicapp.models.SavedDNA;
import com.kmit.musicapp.models.Track;
import com.kmit.musicapp.models.UnifiedTrack;
import com.kmit.musicapp.notificationmanager.AudioPlayerBroadcastReceiver;
import com.kmit.musicapp.snappyrecyclerview.CustomAdapter;
import com.kmit.musicapp.snappyrecyclerview.SnappyRecyclerView;
import com.kmit.musicapp.utilities.DownloadThread;
import com.kmit.musicapp.visualizers.VisualizerView;
import com.squareup.leakcanary.RefWatcher;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class PlayerFragment extends Fragment implements
        AudioPlayerBroadcastReceiver.onCallbackListener, Lyrics.Callback {

    public SnappyRecyclerView snappyRecyclerView;
    public CustomAdapter customAdapter;

    public static VisualizerView mVisualizerView;
    public static MediaPlayer mMediaPlayer;
    public static Visualizer mVisualizer;
    public static Equalizer mEqualizer;
    public static BassBoost bassBoost;
    public static PresetReverb presetReverb;


    public static boolean isReplayIconVisible = false;

    public static boolean isPrepared = false;

    AVLoadingIndicatorView bufferingIndicator;

    public static CustomProgressBar cpb;

    Pair<String, String> temp;

    public ArrayList<Integer> arrayList = new ArrayList<>();

    TextView currTime, totalTime;

    public ImageView repeatController;
    public ImageView shuffleController;

    public ImageView equalizerIcon;
    public ImageView mainTrackController;
    public ImageView nextTrackController;
    public ImageView previousTrackController;
    public ImageView favouriteIcon;
    public ImageView queueIcon;

    public ImageView saveDNAToggle;

    boolean isFav = false;

    public RelativeLayout bottomContainer;
    public RelativeLayout seekBarContainer;
    public RelativeLayout toggleContainer;

    public ImageView selected_track_image;
    public TextView selected_track_title;
    public TextView selected_track_artist;
    public ImageView player_controller;

    public RelativeLayout smallPlayer;

    ImageView favControllerSp, nextControllerSp;

    ImageLoader imgLoader;

    public SeekBar progressBar;

    public static int durationInMilliSec;
    static boolean completed = false;
    public boolean pauseClicked = false;
    boolean isTracking = false;

    public static boolean localIsPlaying = false;

    public Timer timer;

    public static Track track;
    public static LocalTrack localTrack;

    static boolean isRefreshed = false;

    public PlayerFragmentCallbackListener mCallback;
    public onPlayPauseListener mCallback7;
    private int normal;
    private int downloadId;

    @Override
    public void onLyricsDownloaded(Lyrics lyrics) {
        if (lyrics.getTrack().equals(selected_track_title.getText().toString()) && lyrics.getArtist().equals(selected_track_artist.getText().toString())) {
            currentLyrics = lyrics;
            lyricsLoadingIndicator.setVisibility(View.GONE);
            if (currentLyrics.getFlag() == Lyrics.POSITIVE_RESULT) {
                lyricsContent.setText(Html.fromHtml(currentLyrics.getText()));
                lyricsStatus.setVisibility(View.GONE);
            } else {
                lyricsStatus.setText("No Lyrics Found!");
                lyricsStatus.setVisibility(View.VISIBLE);
            }
        }
    }

    public interface PlayerFragmentCallbackListener {
        void onComplete();

        void onPreviousTrack();

        void onEqualizerClicked();

        void onQueueClicked();

        void onPrepared();

        void onFullScreen();

        void onSettingsClicked();

        void onShuffleEnabled();

        void onShuffleDisabled();

        void onSmallPlayerTouched();

        void addCurrentSongtoPlaylist(UnifiedTrack ut);
    }

    public interface onPlayPauseListener {
        void onPlayPause();
    }

    HomeActivity homeActivity;
    public static Context ctx;

    public RelativeLayout spToolbar;
    ImageView overflowMenuAB;
    ImageView spImgAB;
    TextView spTitleAB;
    TextView spArtistAB;

    TextView lyricsStatus;
    AVLoadingIndicatorView lyricsLoadingIndicator;
    public DownloadThread downloadThread;
    public RelativeLayout lyricsContainer;
    public TextView lyricsContent;
    public boolean isLyricsVisisble = false;
    public Lyrics currentLyrics = null;

    public boolean isStart = true;

//    ShowcaseView showCase;

    long startTrack;
    long endTrack;

    public PlayerFragment() {
    }

    public void setupVisualizerFxAndUI() {

        try {
            mVisualizer = new Visualizer(mMediaPlayer.getAudioSessionId());
            mEqualizer = new Equalizer(0, mMediaPlayer.getAudioSessionId());
            mEqualizer.setEnabled(true);

            try {
                bassBoost = new BassBoost(0, mMediaPlayer.getAudioSessionId());
                bassBoost.setEnabled(false);
                BassBoost.Settings bassBoostSettingTemp = bassBoost.getProperties();
                BassBoost.Settings bassBoostSetting = new BassBoost.Settings(bassBoostSettingTemp.toString());
                bassBoostSetting.strength = (1000 / 19);
                bassBoost.setProperties(bassBoostSetting);
                mMediaPlayer.setAuxEffectSendLevel(1.0f);

                presetReverb = new PresetReverb(0, mMediaPlayer.getAudioSessionId());
                presetReverb.setPreset(PresetReverb.PRESET_NONE);
                presetReverb.setEnabled(false);
                mMediaPlayer.setAuxEffectSendLevel(1.0f);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (homeActivity.isEqualizerEnabled) {
            try {
                bassBoost.setEnabled(true);
                BassBoost.Settings bassBoostSettingTemp = bassBoost.getProperties();
                BassBoost.Settings bassBoostSetting = new BassBoost.Settings(bassBoostSettingTemp.toString());
                if (homeActivity.bassStrength == -1) {
                    bassBoostSetting.strength = (1000 / 19);
                } else {
                    bassBoostSetting.strength = homeActivity.bassStrength;
                }
                bassBoost.setProperties(bassBoostSetting);
                mMediaPlayer.setAuxEffectSendLevel(1.0f);

                if (homeActivity.reverbPreset == -1) {
                    presetReverb.setPreset(PresetReverb.PRESET_NONE);
                } else {
                    presetReverb.setPreset(homeActivity.reverbPreset);
                }
                presetReverb.setEnabled(true);
                mMediaPlayer.setAuxEffectSendLevel(1.0f);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (homeActivity.isEqualizerEnabled && homeActivity.isEqualizerReloaded) {
            try {
                homeActivity.isEqualizerEnabled = true;
                int pos = homeActivity.presetPos;
                if (pos != 0) {
                    mEqualizer.usePreset((short) (pos - 1));
                } else {
                    for (short i = 0; i < 5; i++) {
                        mEqualizer.setBandLevel(i, (short) homeActivity.seekbarpos[i]);
                    }
                }
                if (homeActivity.bassStrength != -1 && homeActivity.reverbPreset != -1) {
                    bassBoost.setEnabled(true);
                    bassBoost.setStrength(homeActivity.bassStrength);
                    presetReverb.setEnabled(true);
                    presetReverb.setPreset(homeActivity.reverbPreset);
                }
                mMediaPlayer.setAuxEffectSendLevel(1.0f);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        try {
            mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
            mVisualizer.setDataCaptureListener(
                    new Visualizer.OnDataCaptureListener() {
                        public void onWaveFormDataCapture(Visualizer visualizer,
                                                          byte[] bytes, int samplingRate) {
                        }

                        public void onFftDataCapture(Visualizer visualizer,
                                                     byte[] bytes, int samplingRate) {
                            homeActivity.updateVisualizer(bytes);
                        }
                    }, Visualizer.getMaxCaptureRate() / 2, false, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void togglePlayPause() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            if (homeActivity.isPlayerVisible) {
                mainTrackController.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                player_controller.setImageResource(R.drawable.ic_queue_music_white_48dp);
                isReplayIconVisible = false;
            } else {
                player_controller.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                mainTrackController.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                isReplayIconVisible = false;
            }
            if (mVisualizer != null)
                mVisualizer.setEnabled(false);
            if (!isStart && mCallback != null)
                mCallback7.onPlayPause();
        } else {
            if (!completed) {
                setupVisualizerFxAndUI();
                if (mVisualizer != null)
                    mVisualizer.setEnabled(true);
                if (homeActivity.isPlayerVisible) {
                    mainTrackController.setImageResource(R.drawable.ic_pause_white_48dp);
                    player_controller.setImageResource(R.drawable.ic_queue_music_white_48dp);
                    isReplayIconVisible = false;
                } else {
                    mainTrackController.setImageResource(R.drawable.ic_pause_white_48dp);
                    player_controller.setImageResource(R.drawable.ic_pause_white_48dp);
                    isReplayIconVisible = false;
                }
                mMediaPlayer.start();
                if (!isStart && mCallback != null)
                    mCallback7.onPlayPause();
            } else {
                mMediaPlayer.seekTo(0);
                setupVisualizerFxAndUI();
                if (mVisualizer != null)
                    mVisualizer.setEnabled(true);
                mMediaPlayer.start();
                completed = false;
                if (homeActivity.isPlayerVisible) {
                    mainTrackController.setImageResource(R.drawable.ic_pause_white_48dp);
                    player_controller.setImageResource(R.drawable.ic_pause_white_48dp);
                    isReplayIconVisible = false;
                } else {
                    mainTrackController.setImageResource(R.drawable.ic_pause_white_48dp);
                    player_controller.setImageResource(R.drawable.ic_pause_white_48dp);
                    isReplayIconVisible = false;
                }
            }
        }
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) context;
        ctx = context;
        try {
            mCallback = (PlayerFragmentCallbackListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                currentLyrics = null;
                if (downloadThread != null) {
                    downloadThread.interrupt();
                }
                if (isLyricsVisisble) {
                    isLyricsVisisble = false;
                    lyricsContent.setText("");
                    lyricsContainer.setVisibility(View.GONE);
                    mVisualizerView.setVisibility(View.VISIBLE);
                }
                completed = false;
                pauseClicked = false;
                isPrepared = true;
                mCallback.onPrepared();
                if (homeActivity.isPlayerVisible) {
                    player_controller.setVisibility(View.VISIBLE);
                    player_controller.setImageResource(R.drawable.ic_queue_music_white_48dp);
                } else {
                    player_controller.setVisibility(View.VISIBLE);
                    player_controller.setImageResource(R.drawable.ic_pause_white_48dp);
                }
                togglePlayPause();
                togglePlayPause();
                togglePlayPause();
                bufferingIndicator.setVisibility(View.GONE);
                mainTrackController.setVisibility(View.VISIBLE);
                equalizerIcon.setVisibility(View.VISIBLE);

                snappyRecyclerView.setCurrentPosition(HomeActivity.queueCurrentIndex);
                snappyRecyclerView.setTransparency();

                new HomeActivity.SaveQueue().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                new HomeActivity.SaveRecents().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            }
        });

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                completed = true;
                if (homeActivity.isPlayerVisible) {
                    mainTrackController.setImageResource(R.drawable.ic_replay_white_48dp);
                    player_controller.setImageResource(R.drawable.ic_replay_white_48dp);
                    isReplayIconVisible = true;
                } else {
                    player_controller.setImageResource(R.drawable.ic_replay_white_48dp);
                    mainTrackController.setImageResource(R.drawable.ic_replay_white_48dp);
                    isReplayIconVisible = true;
                }
                new SaveDNA().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                double ratio = percent / 100.0;
                double bufferingLevel = (int) (mp.getDuration() * ratio);
                if (progressBar != null) {
                    progressBar.setSecondaryProgress((int) bufferingLevel);
                }
            }
        });

        mMediaPlayer.setOnErrorListener(
                new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        return true;
                    }
                }
        );

        mMediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                switch (what) {
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        bufferingIndicator.setVisibility(View.VISIBLE);
                        mainTrackController.setVisibility(View.GONE);
                        isPrepared = false;
                        break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        bufferingIndicator.setVisibility(View.GONE);
                        mainTrackController.setVisibility(View.VISIBLE);
                        isPrepared = true;
                        break;
                }
                return true;
            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_player, container, false);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (snappyRecyclerView.getCurrentPosition() != HomeActivity.queueCurrentIndex) {
            snappyRecyclerView.scrollToPosition(HomeActivity.queueCurrentIndex);
            snappyRecyclerView.setCurrentPosition(HomeActivity.queueCurrentIndex);
            customAdapter.notifyDataSetChanged();
        }
        snappyRecyclerView.setTransparency();
    }

    @Override
    public void onCallbackCalled(int i) {
        switch (i) {
            case 6:
                mCallback.onPrepared();
                break;
            case 3:
                if (homeActivity.queueCurrentIndex != 0) {
                    mMediaPlayer.pause();
                    mCallback.onPreviousTrack();
                }
                break;
            case 2:
                mMediaPlayer.pause();
                homeActivity.nextControllerClicked = true;
                mCallback.onComplete();
                break;
        }
    }

    @Override
    public void togglePLayPauseCallback() {
        togglePlayPause();
    }

    @Override
    public boolean getPauseClicked() {
        return pauseClicked;
    }

    @Override
    public void setPauseClicked(boolean bool) {
        pauseClicked = bool;
    }

    @Override
    public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    private String fileName;
    private String folder;
    private AVLoadingIndicatorView circularProgressIndicator;

    void download() {
        folder = Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name) + "/";
        File file = new File(folder);
        if (!file.exists()) {
            file.mkdirs();
        }
        fileName = track.getStreamURL().substring(track.getStreamURL().lastIndexOf('/') + 1, track.getStreamURL().length());
        if (circularProgressIndicator.getVisibility() != VISIBLE) {
            circularProgressIndicator.setVisibility(VISIBLE);
        }
        if (saveDNAToggle.getVisibility() != GONE) {
            saveDNAToggle.setVisibility(GONE);
        }

        Log.e("sdfdsf", "" + Constant.image + track.getStreamURL());

        downloadId = PRDownloader.download(track.getStreamURL(), folder, fileName)
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {
                        arrayList.add(track.getID());
                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {

                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {
                        Log.e("sdfdsf", "" + progress.currentBytes);
                        Log.e("sdfdsf", "" + progress.totalBytes);
                        long progressPercent = progress.currentBytes * 100 / progress.totalBytes;
                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        if (circularProgressIndicator.getVisibility() != GONE) {
                            circularProgressIndicator.setVisibility(GONE);
                        }
                        saveDNAToggle.setImageDrawable(getResources().getDrawable(R.drawable.ic_true));
                        Toast.makeText(ctx, "Location :" + folder, Toast.LENGTH_SHORT).show();

                        if (saveDNAToggle.getVisibility() != VISIBLE) {
                            saveDNAToggle.setVisibility(VISIBLE);
                        }
                        saveDNAToggle.setEnabled(false);
                    }

                    @Override
                    public void onError(Error error) {
                        Log.e("sdfdsf", "" + error.getServerErrorMessage());
                        Toast.makeText(ctx, "some thing Wrong. not Downloaded", Toast.LENGTH_SHORT).show();
                        if (circularProgressIndicator.getVisibility() != GONE) {
                            circularProgressIndicator.setVisibility(GONE);
                        }
                        if (saveDNAToggle.getVisibility() != VISIBLE) {
                            saveDNAToggle.setVisibility(VISIBLE);
                        }
                    }
                });
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = ctx.getTheme();
        theme.resolveAttribute(R.attr.textcolor_f, typedValue, true);
        normal = typedValue.data;
        RelativeLayout adView = (RelativeLayout) view.findViewById(R.id.adView);
/*
        if (Constant.Ads) {
            if (Constant.IS_DEBUG) {
                adView.loadAd(new AdRequest.Builder().addTestDevice("B3EEABB8EE11C2BE770B684D95219ECB").addTestDevice(homeActivity.methods.getDeviceID(homeActivity)).build());
            } else {
                adView.loadAd(new AdRequest.Builder().build());
            }
        }
*/
/*        MobileAds.initialize(this,@string/appid);


        MobileAds.initialize(homeActivity, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        AdRequest  adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);*/


        if (Constant.Ads) {

            LinearLayout layout = new LinearLayout(homeActivity);
            layout.setOrientation(LinearLayout.VERTICAL);

//Additionally to adjust the position to Bottom
            layout.setGravity(Gravity.BOTTOM);

// Create a banner ad
            AdView mAdView = new AdView(homeActivity);
            mAdView.setAdSize(AdSize.SMART_BANNER);
            mAdView.setAdUnitId(Constant.bannerads);

// Create an ad request.
            AdRequest.Builder adRequestBuilder = new AdRequest.Builder();

// Optionally populate the ad request builder.
            adRequestBuilder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);

// Add the AdView to the view hierarchy.
            layout.addView(mAdView);

// Start loading the ad.
            mAdView.loadAd(adRequestBuilder.build());

            adView.addView(layout);

        }
        imgLoader = new ImageLoader(getContext());

        smallPlayer = (RelativeLayout) view.findViewById(R.id.smallPlayer);

        snappyRecyclerView = (SnappyRecyclerView) view.findViewById(R.id.visualizer_recycler);

        bufferingIndicator = (AVLoadingIndicatorView) view.findViewById(R.id.bufferingIndicator);
        currTime = (TextView) view.findViewById(R.id.currTime);
        totalTime = (TextView) view.findViewById(R.id.totalTime);

        repeatController = (ImageView) view.findViewById(R.id.repeat_controller);
        shuffleController = (ImageView) view.findViewById(R.id.shuffle_controller);

        spToolbar = (RelativeLayout) view.findViewById(R.id.smallPlayer_AB);

        overflowMenuAB = (ImageView) view.findViewById(R.id.menuIcon);

        lyricsContainer = (RelativeLayout) view.findViewById(R.id.lyrics_container);
        lyricsContent = (TextView) view.findViewById(R.id.lyrics_content);
        lyricsLoadingIndicator = (AVLoadingIndicatorView) view.findViewById(R.id.lyrics_loading_indicator);
        lyricsStatus = (TextView) view.findViewById(R.id.lyrics_status_text);


        spImgAB = (ImageView) view.findViewById(R.id.selected_track_image_sp_AB);
        spImgAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!homeActivity.isPlayerTransitioning && smallPlayer != null) {
                    homeActivity.hidePlayer();
                    homeActivity.isPlayerVisible = false;
                }
            }
        });
        spTitleAB = (TextView) view.findViewById(R.id.selected_track_title_sp_AB);
        spTitleAB.setSelected(true);

        spArtistAB = (TextView) view.findViewById(R.id.selected_track_artist_sp_AB);

        if (homeActivity.shuffleEnabled) {
            shuffleController.setColorFilter(ContextCompat.getColor(ctx, R.color.prim), android.graphics.PorterDuff.Mode.MULTIPLY);
            shuffleController.setImageResource(R.drawable.ic_shuffle_filled);
        } else {
            shuffleController.setColorFilter(normal);
            shuffleController.setImageResource(R.drawable.ic_shuffle_filled);
        }

        if (homeActivity.repeatEnabled) {
            repeatController.setColorFilter(ContextCompat.getColor(ctx, R.color.prim), android.graphics.PorterDuff.Mode.MULTIPLY);
            repeatController.setImageResource(R.drawable.ic_repeat_filled);
        } else if (homeActivity.repeatOnceEnabled) {
            repeatController.setColorFilter(normal);
            repeatController.setImageResource(R.drawable.ic_repeat_once);
        } else {
            repeatController.setColorFilter(normal);
            repeatController.setImageResource(R.drawable.ic_repeat_filled);
        }

        repeatController.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (homeActivity.repeatOnceEnabled) {
                    homeActivity.repeatOnceEnabled = false;
                    repeatController.setColorFilter(ContextCompat.getColor(ctx, R.color.prim), android.graphics.PorterDuff.Mode.MULTIPLY);
                    repeatController.setImageResource(R.drawable.ic_repeat_filled);
                } else if (homeActivity.repeatEnabled) {
                    homeActivity.repeatEnabled = false;
                    homeActivity.repeatOnceEnabled = true;
                    repeatController.setColorFilter(normal);
                    repeatController.setImageResource(R.drawable.ic_repeat_once);
                } else {
                    homeActivity.repeatEnabled = true;
                    repeatController.setColorFilter(normal);
                    repeatController.setImageResource(R.drawable.ic_repeat_filled);
                }
            }
        });

        shuffleController.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (homeActivity.shuffleEnabled) {
                    homeActivity.shuffleEnabled = false;
                    shuffleController.setImageResource(R.drawable.ic_shuffle_alpha);
                    shuffleController.setColorFilter(normal);
                    mCallback.onShuffleDisabled();
                } else {
                    homeActivity.shuffleEnabled = true;
                    shuffleController.setImageResource(R.drawable.ic_shuffle_filled);
                    shuffleController.setColorFilter(ContextCompat.getColor(ctx, R.color.prim), android.graphics.PorterDuff.Mode.MULTIPLY);
                    mCallback.onShuffleEnabled();
                }
                snappyRecyclerView.scrollToPosition(HomeActivity.queueCurrentIndex);
                snappyRecyclerView.setCurrentPosition(HomeActivity.queueCurrentIndex);
                customAdapter.notifyDataSetChanged();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        snappyRecyclerView.setTransparency();
                    }
                }, 400);
            }
        });

        equalizerIcon = (ImageView) view.findViewById(R.id.equalizer_icon);
        equalizerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onEqualizerClicked();
            }
        });
        equalizerIcon.setVisibility(View.INVISIBLE);

        saveDNAToggle = (ImageView) view.findViewById(R.id.toggleSaveDNA);
        try {
            for (int i = 0; i <= arrayList.size(); i++) {
                if (track.getID() == arrayList.get(i)) {
                    if (circularProgressIndicator.getVisibility() != GONE) {
                        circularProgressIndicator.setVisibility(GONE);
                    }
                    if (saveDNAToggle.getVisibility() != VISIBLE) {
                        saveDNAToggle.setVisibility(VISIBLE);
                    }

                    saveDNAToggle.setImageResource(R.drawable.ic_true);
                    saveDNAToggle.setEnabled(false);
                    break;
                } else {
                    if (circularProgressIndicator.getVisibility() != GONE) {
                        circularProgressIndicator.setVisibility(GONE);
                    }
                    if (saveDNAToggle.getVisibility() != VISIBLE) {
                        saveDNAToggle.setVisibility(VISIBLE);
                    }

                    saveDNAToggle.setImageResource(R.drawable.ic_save_filled);
                    saveDNAToggle.setEnabled(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        circularProgressIndicator = view.findViewById(R.id.circular_progress);

        saveDNAToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constant.get_profile_id(ctx).equals("0")) {
                    homeActivity.mainFragment("login");
                } else {
                    download();
                }
            }
        });

        mainTrackController = (ImageView) view.findViewById(R.id.controller);
        nextTrackController = (ImageView) view.findViewById(R.id.next);
        previousTrackController = (ImageView) view.findViewById(R.id.previous);

        isFav = false;

        favouriteIcon = (ImageView) view.findViewById(R.id.fav_icon);
        favControllerSp = (ImageView) view.findViewById(R.id.fav_controller_sp);


        favouriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constant.get_profile_id(ctx).equals("0")) {
                    homeActivity.mainFragment("login");

                } else {

                    if (!isFav) {
                        new Asy_Like_Unlile_Addplaylist(HomeActivity.methods.getAPIRequest(Constant.METHOD_Like, Constant.get_profile_id(ctx), Constant.like_Music, String.valueOf(track.getID()), "", "")
                                , Constant.Like
                                , new Asy_Like_Unlile_Addplaylist.Listener_Artist() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onEnd(String success) {
                                if (success.equals("1")) {
                                    favouriteIcon.setImageResource(R.drawable.ic_heart_filled_1);
                                    favControllerSp.setImageResource(R.drawable.ic_heart_filled_1);
                                    isFav = true;
                                }
                            }
                        }).execute();
                    } else {
                        new Asy_Like_Unlile_Addplaylist(HomeActivity.methods.getAPIRequest(Constant.METHOD_Like, Constant.get_profile_id(ctx), Constant.like_Music, String.valueOf(track.getID()), "", "")
                                , Constant.Unlike
                                , new Asy_Like_Unlile_Addplaylist.Listener_Artist() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onEnd(String success) {
                                if (success.equals("1")) {
                                    favouriteIcon.setImageResource(R.drawable.ic_heart_alpha);
                                    favControllerSp.setImageResource(R.drawable.ic_heart_alpha);
                                    isFav = false;
                                }
                            }
                        }).execute();
                    }

                }

            }
        });

        favControllerSp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constant.get_profile_id(ctx).equals("0")) {
                    homeActivity.mainFragment("login");

                } else {

                    if (!isFav) {
                        new Asy_Like_Unlile_Addplaylist(HomeActivity.methods.getAPIRequest(Constant.METHOD_Like, Constant.get_profile_id(ctx), Constant.like_Music, String.valueOf(track.getID()), "", "")
                                , Constant.Like
                                , new Asy_Like_Unlile_Addplaylist.Listener_Artist() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onEnd(String success) {
                                if (success.equals("1")) {
                                    favouriteIcon.setImageResource(R.drawable.ic_heart_filled_1);
                                    favControllerSp.setImageResource(R.drawable.ic_heart_filled_1);
                                    isFav = true;
                                }
                            }
                        }).execute();
                    } else {
                        new Asy_Like_Unlile_Addplaylist(HomeActivity.methods.getAPIRequest(Constant.METHOD_Like, Constant.get_profile_id(ctx), Constant.like_Music, String.valueOf(track.getID()), "", "")
                                , Constant.Unlike
                                , new Asy_Like_Unlile_Addplaylist.Listener_Artist() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onEnd(String success) {
                                if (success.equals("1")) {
                                    favouriteIcon.setImageResource(R.drawable.ic_heart_alpha);
                                    favControllerSp.setImageResource(R.drawable.ic_heart_alpha);
                                    isFav = false;
                                }
                            }
                        }).execute();
                    }

                }
            }
        });

        queueIcon = (ImageView) view.findViewById(R.id.queue_icon);
        queueIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onQueueClicked();
            }
        });

        selected_track_image = (ImageView) view.findViewById(R.id.selected_track_image_sp);
        selected_track_title = (TextView) view.findViewById(R.id.selected_track_title_sp);
        selected_track_artist = (TextView) view.findViewById(R.id.selected_track_artist_sp);
        player_controller = (ImageView) view.findViewById(R.id.player_control_sp);

        nextControllerSp = (ImageView) view.findViewById(R.id.next_controller_sp);

        nextControllerSp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.pause();
                homeActivity.nextControllerClicked = true;
                mCallback.onComplete();
            }
        });

        bottomContainer = (RelativeLayout) view.findViewById(R.id.mainControllerContainer);
        seekBarContainer = (RelativeLayout) view.findViewById(R.id.seekBarContainer);
        toggleContainer = (RelativeLayout) view.findViewById(R.id.toggleContainer);

        mVisualizerView = (VisualizerView) view.findViewById(R.id.myvisualizerview);

        VisualizerView.w = HomeActivity.screen_width;
        VisualizerView.h = HomeActivity.screen_height;
        VisualizerView.conf = Bitmap.Config.ARGB_8888;
        VisualizerView.bmp = Bitmap.createBitmap(VisualizerView.w, VisualizerView.h, VisualizerView.conf);
        HomeActivity.cacheCanvas = new Canvas(VisualizerView.bmp);

        if (HomeActivity.queue != null) {
            customAdapter = new CustomAdapter(getContext(), snappyRecyclerView, HomeActivity.queue.getQueue());
        } else {
            customAdapter = new CustomAdapter(getContext(), snappyRecyclerView, new ArrayList<UnifiedTrack>());
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        snappyRecyclerView.setLayoutManager(linearLayoutManager);
        snappyRecyclerView.setItemAnimator(new DefaultItemAnimator());
        snappyRecyclerView.setAdapter(customAdapter);
        snappyRecyclerView.setActivity(homeActivity);

        snappyRecyclerView.addOnItemTouchListener(new ClickItemTouchListener(snappyRecyclerView) {
            @Override
            public boolean onClick(RecyclerView parent, View view, int position, long id) {
                return false;
            }

            @Override
            public boolean onLongClick(RecyclerView parent, View view, int position, long id) {
                if (homeActivity.isFullScreenEnabled) {
                    homeActivity.isFullScreenEnabled = false;
                    bottomContainer.setVisibility(View.VISIBLE);
                    seekBarContainer.setVisibility(View.VISIBLE);
                    toggleContainer.setVisibility(View.VISIBLE);
                    spToolbar.setVisibility(View.VISIBLE);
                    mCallback.onFullScreen();
                } else {
                    homeActivity.isFullScreenEnabled = true;
                    bottomContainer.setVisibility(View.GONE);
                    seekBarContainer.setVisibility(View.GONE);
                    toggleContainer.setVisibility(View.GONE);
                    spToolbar.setVisibility(View.INVISIBLE);
                    mCallback.onFullScreen();
                }
                return true;
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });


        cpb = (CustomProgressBar) view.findViewById(R.id.customProgress);

        smallPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onSmallPlayerTouched();
            }
        });


        track = homeActivity.selectedTrack;
        localTrack = homeActivity.localSelectedTrack;

        if (homeActivity.streamSelected) {


            new Asy_Singal_Music(homeActivity.methods.getAPIRequest(Constant.Single_Music_Screen, String.valueOf(track.getID()), Constant.get_profile_id(ctx),"", "", ""),
                    new Asy_Singal_Music.Listener_Played() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onEnd(String success) {
                        }
                    }).execute();


            try {
                durationInMilliSec = track.getDuration();
            } catch (Exception e) {

            }
            if (track.getArtworkURL() != null) {
                Glide.with(getActivity()).load(track.getArtworkURL()).override(100, 100).into(selected_track_image);
            } else {
                selected_track_image.setImageResource(R.drawable.ic_default);
            }
            try {
                spTitleAB.setText(track.getTitle());
                selected_track_title.setText(track.getTitle());
                try {
                    selected_track_artist.setText(track.getArtists().get(0).getArtist_name());
                    spArtistAB.setText(track.getArtists().get(0).getArtist_name());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
            }
            try {

                if (track.getIs_liked().equals("1")) {
                    favouriteIcon.setImageResource(R.drawable.ic_heart_filled_1);
                    favControllerSp.setImageResource(R.drawable.ic_heart_filled_1);
                    isFav = true;
                } else {
                    favouriteIcon.setImageResource(R.drawable.ic_heart_alpha);
                    favControllerSp.setImageResource(R.drawable.ic_heart_alpha);
                    isFav = false;
                }
            } catch (Exception e) {
            }

        } else {
            try {
                durationInMilliSec = (int) localTrack.getDuration();
            } catch (Exception e) {

            }
            try {
                imgLoader.DisplayImage(localTrack.getPath(), selected_track_image);
            } catch (Exception e) {

            }
            try {
                spTitleAB.setText(localTrack.getTitle());
                selected_track_title.setText(localTrack.getTitle());
                selected_track_artist.setText(localTrack.getArtist());
                spArtistAB.setText(localTrack.getArtist());
            } catch (Exception e) {

            }

        }

        temp = getTime(durationInMilliSec);
        totalTime.setText(temp.first + ":" + temp.second);

        homeActivity.mWifi = homeActivity.connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        try {
            if (homeActivity.streamSelected) {
                if ((homeActivity.settings.isStreamOnlyOnWifiEnabled() && homeActivity.mWifi.isConnected()) || !homeActivity.settings.isStreamOnlyOnWifiEnabled()) {
                    isPrepared = false;
                    mMediaPlayer.reset();
                    mMediaPlayer.setDataSource(track.getStreamURL() + "?client_id=" + Config.CLIENT_ID);
                    mMediaPlayer.prepareAsync();
                } else {
                    mMediaPlayer.pause();
                    homeActivity.nextControllerClicked = true;
                    mCallback.onComplete();
                }
            } else {
                isPrepared = false;
                mMediaPlayer.reset();
                File f = new File(localTrack.getPath());
                if (f.exists()) {
                    mMediaPlayer.setDataSource(localTrack.getPath());
                    mMediaPlayer.prepareAsync();
                } else {
                    Toast.makeText(getContext(), "Error playing " + localTrack.getTitle() + ". Skipping to next track", Toast.LENGTH_SHORT).show();
                    mMediaPlayer.pause();
                    homeActivity.nextControllerClicked = true;
                    mCallback.onComplete();
                }
            }
            bufferingIndicator.setVisibility(View.VISIBLE);
            mainTrackController.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        overflowMenuAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifiedTrack ut = HomeActivity.queue.getQueue().get(HomeActivity.queueCurrentIndex);
                mCallback.addCurrentSongtoPlaylist(ut);
            }
        });


        player_controller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isReplayIconVisible) {
                    homeActivity.hasQueueEnded = true;
                    mCallback.onComplete();
                } else {
                    if (!pauseClicked) {
                        pauseClicked = true;
                    }
                    togglePlayPause();
                }
            }
        });

        mainTrackController.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isReplayIconVisible) {
                    homeActivity.hasQueueEnded = true;
                    mCallback.onComplete();
                } else {
                    if (!pauseClicked) {
                        pauseClicked = true;
                    }
                    togglePlayPause();
                }
            }
        });

        nextTrackController.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.pause();
                homeActivity.nextControllerClicked = true;
                mCallback.onComplete();
            }
        });

        previousTrackController.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (homeActivity.queueCurrentIndex != 0) {
                    mMediaPlayer.pause();
                    mCallback.onPreviousTrack();
                }
            }
        });

        progressBar = (SeekBar) view.findViewById(R.id.progressBar);
        progressBar.setMax(durationInMilliSec);

        timer = new Timer();
        timer.scheduleAtFixedRate(
                new TimerTask() {
                    public void run() {
                        if (isPrepared && !isTracking && getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    float[] hsv = new float[3];
                                    hsv[0] = homeActivity.seekBarColor;
                                    hsv[1] = (float) 0.8;
                                    hsv[2] = (float) 0.5;
                                    progressBar.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(Color.HSVToColor(hsv), PorterDuff.Mode.SRC_IN));
                                    cpb.update();
                                }
                            });
                            try {
                                temp = getTime(mMediaPlayer.getCurrentPosition());
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        currTime.setText(temp.first + ":" + temp.second);
                                    }
                                });
                                progressBar.setProgress(mMediaPlayer.getCurrentPosition());
                            } catch (Exception e) {
                                Log.e("MEDIA", e.getMessage() + ":");
                            }
                        }
                    }
                }, 0, 50);

        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                temp = getTime(progress);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        currTime.setText(temp.first + ":" + temp.second);
                    }
                });
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isTracking = true;
            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {
                mMediaPlayer.seekTo(seekBar.getProgress());

                if (mMediaPlayer.isPlaying())
                    mMediaPlayer.start();
                isTracking = false;
            }

        });

        final Button mEndButton = new Button(getContext());
        mEndButton.setBackgroundColor(homeActivity.themeColor);
        mEndButton.setTextColor(Color.WHITE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = MusicDNAApplication.getRefWatcher(getContext());
        refWatcher.watch(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        super.onDestroy();
        RefWatcher refWatcher = MusicDNAApplication.getRefWatcher(getContext());
        refWatcher.watch(this);
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        if (mVisualizer != null) {
            mVisualizer.release();
        }
    }

    public void refresh() {

        isRefreshed = true;

        pauseClicked = false;
        completed = false;
        isTracking = false;

        if (snappyRecyclerView.getCurrentPosition() != HomeActivity.queueCurrentIndex) {
            snappyRecyclerView.scrollToPosition(HomeActivity.queueCurrentIndex);
            snappyRecyclerView.setCurrentPosition(HomeActivity.queueCurrentIndex);
            customAdapter.notifyDataSetChanged();
        }

        VisualizerView.w = HomeActivity.screen_width;
        VisualizerView.h = HomeActivity.screen_height;
        VisualizerView.conf = Bitmap.Config.ARGB_8888;
        VisualizerView.bmp = Bitmap.createBitmap(VisualizerView.w, VisualizerView.h, VisualizerView.conf);
        HomeActivity.cacheCanvas = new Canvas(VisualizerView.bmp);

        if (homeActivity.isPlayerVisible) {
            player_controller.setVisibility(View.VISIBLE);
            player_controller.setImageResource(R.drawable.ic_queue_music_white_48dp);
        } else {
            player_controller.setVisibility(View.VISIBLE);
            player_controller.setImageResource(R.drawable.ic_pause_white_48dp);
        }

        isFav = false;


        if (homeActivity.shuffleEnabled) {
            shuffleController.setColorFilter(ContextCompat.getColor(ctx, R.color.prim), android.graphics.PorterDuff.Mode.MULTIPLY);
            shuffleController.setImageResource(R.drawable.ic_shuffle_filled);
        } else {
            shuffleController.setImageResource(R.drawable.ic_shuffle_alpha);
            shuffleController.setColorFilter(normal);
        }

        if (homeActivity.repeatEnabled) {
            repeatController.setColorFilter(ContextCompat.getColor(ctx, R.color.prim), android.graphics.PorterDuff.Mode.MULTIPLY);
            repeatController.setImageResource(R.drawable.ic_repeat_filled);
        } else if (homeActivity.repeatOnceEnabled) {
            repeatController.setColorFilter(normal);
            repeatController.setImageResource(R.drawable.ic_repeat_once);
        } else {
            repeatController.setColorFilter(normal);
            repeatController.setImageResource(R.drawable.ic_repeat_filled);
        }


        try {
            for (int i = 0; i <= arrayList.size(); i++) {
                if (track.getID() == arrayList.get(i)) {
                    if (circularProgressIndicator.getVisibility() != GONE) {
                        circularProgressIndicator.setVisibility(GONE);
                    }
                    if (saveDNAToggle.getVisibility() != VISIBLE) {
                        saveDNAToggle.setVisibility(VISIBLE);
                    }

                    saveDNAToggle.setImageResource(R.drawable.ic_true);
                    saveDNAToggle.setEnabled(false);
                    break;
                } else {
                    if (circularProgressIndicator.getVisibility() != GONE) {
                        circularProgressIndicator.setVisibility(GONE);
                    }
                    if (saveDNAToggle.getVisibility() != VISIBLE) {
                        saveDNAToggle.setVisibility(VISIBLE);
                    }

                    saveDNAToggle.setImageResource(R.drawable.ic_save_filled);
                    saveDNAToggle.setEnabled(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        equalizerIcon.setVisibility(View.INVISIBLE);

        track = homeActivity.selectedTrack;
        localTrack = homeActivity.localSelectedTrack;

        if (homeActivity.streamSelected) {
            new Asy_Singal_Music(homeActivity.methods.getAPIRequest(Constant.Single_Music_Screen, String.valueOf(track.getID()), Constant.get_profile_id(ctx),"", "", ""),
                    new Asy_Singal_Music.Listener_Played() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onEnd(String success) {
                        }
                    }).execute();

            durationInMilliSec = track.getDuration();
            if (track.getArtworkURL() != null) {
                Glide.with(getActivity()).load(track.getArtworkURL()).override(100, 100).into(selected_track_image);
            } else {
                selected_track_image.setImageResource(R.drawable.ic_default);
            }
            try {
                spTitleAB.setText(track.getTitle());
                selected_track_title.setText(track.getTitle());
                try {
                    selected_track_artist.setText(track.getArtists().get(0).getArtist_name());
                    spArtistAB.setText(track.getArtists().get(0).getArtist_name());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
            }
            try {

                if (track.getIs_liked().equals("1")) {
                    favouriteIcon.setImageResource(R.drawable.ic_heart_filled_1);
                    favControllerSp.setImageResource(R.drawable.ic_heart_filled_1);
                    isFav = true;
                } else {
                    favouriteIcon.setImageResource(R.drawable.ic_heart_alpha);
                    favControllerSp.setImageResource(R.drawable.ic_heart_alpha);
                    isFav = false;
                }
            } catch (Exception e) {
            }

        } else {
            try {
                durationInMilliSec = (int) localTrack.getDuration();
            } catch (Exception e) {

            }
            try {
                imgLoader.DisplayImage(localTrack.getPath(), selected_track_image);
            } catch (Exception e) {

            }
            try {
                spTitleAB.setText(localTrack.getTitle());
                selected_track_title.setText(localTrack.getTitle());
                selected_track_artist.setText(localTrack.getArtist());
                spArtistAB.setText(localTrack.getArtist());
            } catch (Exception e) {

            }
        }

        temp = getTime(durationInMilliSec);
        totalTime.setText(temp.first + ":" + temp.second);

        homeActivity.mWifi = homeActivity.connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        try {
            if (homeActivity.streamSelected) {
                if ((homeActivity.settings.isStreamOnlyOnWifiEnabled() && homeActivity.mWifi.isConnected()) || !homeActivity.settings.isStreamOnlyOnWifiEnabled()) {
                    isPrepared = false;
                    mMediaPlayer.reset();
                    progressBar.setProgress(0);
                    progressBar.setSecondaryProgress(0);
                    mMediaPlayer.setDataSource(track.getStreamURL() + "?client_id=" + Config.CLIENT_ID);
                    mMediaPlayer.prepareAsync();
                } else {
                    mMediaPlayer.pause();
                    homeActivity.nextControllerClicked = true;
                    mCallback.onComplete();
                }
            } else {
                isPrepared = false;
                mMediaPlayer.reset();
                progressBar.setProgress(0);
                progressBar.setSecondaryProgress(0);
                File f = new File(localTrack.getPath());
                if (f.exists()) {
                    mMediaPlayer.setDataSource(localTrack.getPath());
                    mMediaPlayer.prepareAsync();
                } else {
                    Toast.makeText(getContext(), "Error playing " + localTrack.getTitle() + ". Skipping to next track", Toast.LENGTH_SHORT).show();
                    mMediaPlayer.pause();
                    homeActivity.nextControllerClicked = true;
                    mCallback.onComplete();
                }
            }
            bufferingIndicator.setVisibility(View.VISIBLE);
            mainTrackController.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        progressBar.setMax(durationInMilliSec);

        timer.cancel();
        timer = new Timer();
        timer.scheduleAtFixedRate(
                new TimerTask() {
                    public void run() {
                        if (isPrepared && !isTracking && getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    float[] hsv = new float[3];
                                    hsv[0] = homeActivity.seekBarColor;
                                    hsv[1] = (float) 0.8;
                                    hsv[2] = (float) 0.5;
                                    progressBar.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(Color.HSVToColor(hsv), PorterDuff.Mode.SRC_IN));
                                    cpb.update();
                                }
                            });
                            try {
                                temp = getTime(mMediaPlayer.getCurrentPosition());
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        currTime.setText(temp.first + ":" + temp.second);
                                    }
                                });
                                progressBar.setProgress(mMediaPlayer.getCurrentPosition());
                            } catch (Exception e) {
                                Log.e("MEDIA", e.getMessage() + ":");
                            }
                        }
                    }
                }, 0, 50);

        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                temp = getTime(progress);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        currTime.setText(temp.first + ":" + temp.second);
                    }
                });
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                startTrack = System.currentTimeMillis();
                isTracking = true;
            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {
                mMediaPlayer.seekTo(seekBar.getProgress());
                endTrack = System.currentTimeMillis();
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.start();
                }
                isTracking = false;
            }

        });
    }

    public Pair<String, String> getTime(int millsec) {
        int min, sec;
        sec = millsec / 1000;
        min = sec / 60;
        sec = sec % 60;
        String minS, secS;
        minS = String.valueOf(min);
        secS = String.valueOf(sec);
        if (sec < 10) {
            secS = "0" + secS;
        }
        Pair<String, String> pair = Pair.create(minS, secS);
        return pair;
    }

    public class SaveDNA extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            if (homeActivity.isSaveDNAEnabled) {
                Bitmap bmp = mVisualizerView.bmp;

                // Generate base64 encoded bitmap
                String base64encoded = getBase64encodedBitmap(bmp);

                // Create SavedDNA object with relevant values.
                SavedDNA savedDNA;
                if (localIsPlaying) {
                    savedDNA = new SavedDNA(localTrack.getTitle(), true, localTrack.getPath(), null, localTrack.getArtist(), base64encoded);
                } else {
                    savedDNA = new SavedDNA(track.getTitle(), false, null, track.getArtworkURL(), "", base64encoded);
                }

                // Check if a DNA exists for the current song, and remove it.
                for (int i = 0; i < homeActivity.savedDNAs.getSavedDNAs().size(); i++) {
                    if (homeActivity.savedDNAs.getSavedDNAs().get(i).getName().equals(savedDNA.getName())) {
                        homeActivity.savedDNAs.getSavedDNAs().remove(i);
                        break;
                    }
                }

                // Add the DNA to the first position.
                homeActivity.savedDNAs.getSavedDNAs().add(0, savedDNA);

                // If number of DNAs get more than 10, then remove the 11th DNA to keep size as 10.
                // Caution : 0-indexed arrays.
                if (homeActivity.savedDNAs.getSavedDNAs().size() > 10) {
                    homeActivity.savedDNAs.getSavedDNAs().remove(10);
                }

                // Display Toast.
                homeActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ctx, "DNA Saved!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            // Set flags to initial values.
            completed = false;
            isPrepared = false;

            // Pause the MediaPlayer and call onComplete().
            mMediaPlayer.pause();
            mCallback.onComplete();
        }
    }

/*    public boolean isShowcaseVisible() {
        return (showCase != null && showCase.isShowing());
    }

    public void hideShowcase() {
        showCase.hide();
    }*/

    public void toggleAlbumArtBackground() {
        snappyRecyclerView.setTransparency();
    }

    public String getBase64encodedBitmap(Bitmap image) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

}
